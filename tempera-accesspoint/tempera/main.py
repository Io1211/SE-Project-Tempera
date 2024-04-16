import asyncio
import logging.config
import sys
from typing import List

from bleak import BleakClient, BleakScanner, BLEDevice

from api.poster import post
from bleclient.device_notify import (
    detection_callback,
    notify,
)
from utils.db_utils import delete_measurements

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("tempera")

data_collection_interval = 10
data_sending_interval = 5


async def main():
    delete_measurements()

    scanner = BleakScanner(detection_callback)
    print("Scanning for BLE devices...")
    devices = await scanner.discover(timeout=5)

    devs = []
    for device in devices:
        if "G4T1" in device.name:
            devs.append(device)

    print(f"Tempera stations found {devs}")
    # TODO: adapt to multiple connected devices
    device_address = devs[0].address  # for now only care about handling 1 device

    async with BleakClient(
        device_address,
        timeout=120,
        # services=list(uuids.values()),
        disconnected_callback=stop_notify,
    ) as client:
        print(f"Connected to device {device_address}.")

        while True:
            for service in client.services:
                print(
                    f"Service[desc:{service.description};handle:{service.handle};uuid:{service.uuid}]"
                )
                for characteristic in service.characteristics:
                    print(
                        f"Characteristics[desc:{characteristic.description};uuid:{characteristic.uuid}]"
                    )

                    await start_notify(characteristic, characteristic.uuid)

            await asyncio.sleep(data_collection_interval)

            for val in uuids.values():
                await post(val)

            await asyncio.sleep(data_sending_interval)

    sys.exit("Failed to connect to the ble server")


if __name__ == "__main__":
    asyncio.run(main())
