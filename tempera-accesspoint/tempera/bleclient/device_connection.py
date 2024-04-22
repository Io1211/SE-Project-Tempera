import asyncio
import logging
from typing import List

import requests
from bleak import BLEDevice, BleakScanner, BleakClient
from requests import Response

from bleclient.device_notification import detection_callback
from utils.config_utils import init_config

logger = logging.getLogger(f"tempera.{__name__}")


CONFIG = init_config()
REQUIRED_SERVICES = ["180a", "183f", "181a"]
REQUIRED_CHARACTERISTICS = ["2a29", "2a25", "2bf2", "2a6e", "2a77", "2a6f", "2bd3"]
SCANNING_TIMEOUT = 5


async def get_tempera_stations() -> List[BLEDevice]:
    logger.info("Scanning for BLE devices...")
    scanner = BleakScanner(detection_callback)
    devices = await scanner.discover(timeout=SCANNING_TIMEOUT)
    logger.info(f"Found devices: {devices}")

    tempera_stations = []
    for device in devices:
        if "G4T1" in device.name:
            logger.info(
                f"Found tempera station: Device[name:{device.name};address:{device.address}]"
            )
            tempera_stations.append(device)

    if not tempera_stations:
        logger.warning(
            "No devices found with 'G4T1' in their name.\n"
            "Make sure 'G4T1' is part of the tempera station's name you are trying to connect."
        )
        # TODO: send log to back end
        return []

    # TODO: send log to back end
    return tempera_stations


async def validate_stations(tempera_stations: List[BLEDevice]) -> BLEDevice | None:
    """
    Returns the first valid tempera station. Valid means that its ID corresponds to one stored in the webapp back end.

    :param tempera_stations:
    :return:
    """
    logger.info(f"Trying to validate stations: {tempera_stations}")

    try:
        server_address = CONFIG["webserver_address"]
        access_point_id = CONFIG["access_point_id"]
    except KeyError as e:
        logger.critical(f"Failed to read parameter from the config file: {e}")
        raise KeyError

    response = requests.get(
        f"{server_address}/rasp/api/valid_devices",
        params={"device_id": access_point_id},
        hooks={"response": validate_access_point},
    )

    response = response.json()
    allowed_stations = response["stations_allowed"]

    for station in tempera_stations:
        # Just return the first valid station (multi-station support not required for this project)
        async with BleakClient(station.address, services=REQUIRED_SERVICES) as client:

            async with asyncio.TaskGroup() as tg:
                id_ok = tg.create_task(validate_id(client, allowed_stations)).result()
                missing_characteristics = tg.create_task(
                    validate_characteristics(client)
                ).result()

        if id_ok and not missing_characteristics:
            logger.info(
                f"Connecting to station (Station[name: {station.name}; address: {station.address}]) "
                f"as it meets all requirements."
            )
            return station
        elif id_ok and missing_characteristics is not None:
            logger.info(
                f"Station (Station[name: {station.name}; address: {station.address}]) meets ID requirement but lacks"
                f" the following characteristics: {missing_characteristics}"
            )
        else:
            logger.info(
                f"Station (Station[name: {station.name}; address: {station.address}]) doesn't meet ID requirement! "
                f"Make sure you have registered this station's ID in the web app server if you want to connect to it."
            )

    raise RuntimeError("No valid tempera stations found.")


async def validate_id(client: BleakClient, valid_ids: List[str]):
    station_id = "Not Found"

    for service in client.services:
        if "180a" in service.uuid:
            for characteristic in service.characteristics:
                if "2a25" in characteristic.uuid:
                    station_id = await client.read_gatt_char(characteristic.uuid)
                    station_id = station_id.decode()

    logger.info(f"Checking station ID '{station_id}' against web app server data.")

    return station_id in valid_ids


async def validate_characteristics(client: BleakClient) -> List[str]:
    missing_characteristics = REQUIRED_CHARACTERISTICS
    for service in client.services:
        for characteristic in service.characteristics:
            for required_characteristic in REQUIRED_CHARACTERISTICS:
                if required_characteristic in characteristic.uuid:
                    missing_characteristics.remove(required_characteristic)

    return missing_characteristics


# Keep scanning for stations every 60 seconds until one is found.
# Usually a stop after n attempts would probably be better, but with headless raspberry pi
# this strategy might be preferable
# @retry(wait=wait_fixed(60))
async def discovery_loop() -> BLEDevice:
    tempera_stations = await get_tempera_stations()
    if not tempera_stations:
        raise ValueError("No tempera stations found.")

    tempera_station = await validate_stations(tempera_stations)

    if not tempera_station:
        raise ValueError("No valid tempera station found.")

    return tempera_station


def validate_access_point(response: Response, *args, **kwargs) -> None:
    code = response.status_code
    response = response.json()

    if code != 200:
        logger.debug(f"{code}, {response}")
        raise RuntimeError("An error occurred in calling the API")
    elif not response["access_point_allowed"]:
        logger.debug(f"{response.status_code}, {response}")
        raise RuntimeError("This access point is not registered in the web app server.")


def get_scan_order() -> bool:
    try:
        server_address = CONFIG["webserver_address"]
    except KeyError as e:
        logger.critical(f"Failed to read parameter from the config file: {e}")
        raise KeyError

    response = requests.get(f"{server_address}/rasp/api/scan_order")
    if response.status_code != 200:
        logger.debug(f"{response.status_code}, {response.json()}")
        raise RuntimeError("An error occurred in calling the API")

    return response.json()["scan"]
