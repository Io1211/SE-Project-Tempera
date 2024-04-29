import logging
from getpass import getpass
from pathlib import Path

import yaml

logging.getLogger("configGenerator")

welcome_art = """
#######################################################
#                                                     #
#   ____ ___  _   _ _____ ___ ____ _   _ ____  _____  #
#  / ___/ _ \| \ | |  ___|_ _/ ___| | | |  _ \| ____| #
# | |  | | | |  \| | |_   | | |  _| | | | |_) |  _|   #
# | |__| |_| | |\  |  _|  | | |_| | |_| |  _ <| |___  #
#  \____\___/|_| \_|_|   |___\____|\___/|_| \_\_____| #
# |_   _| ____|  \/  |  _ \| ____|  _ \    / \        #
#   | | |  _| | |\/| | |_) |  _| | |_) |  / _ \       #
#   | | | |___| |  | |  __/| |___|  _ <  / ___ \      #
#   |_| |_____|_|  |_|_|   |_____|_| \_\/_/   \_\     #
#                                                     #
#######################################################
"""


def prompt(message: str, parse_float: bool = False) -> str | float:
    param = None
    while not param:
        param = input(message)
        if not param:
            print("---\n❌ No input provided ❌\n➡️  Please provide an input.\n---")
        elif parse_float:
            try:
                return float(param)
            except ValueError:
                print(
                    "---\n❌ The sending interval must be a number (int or float) ❌\n➡️  Please try again.\n---"
                )
                param = None  # or the loop wouldn't continue
    return param


def main():
    print(welcome_art)

    config = {}
    config_file = Path(__file__).parent.resolve() / "conf.yaml"

    with open(config_file, "w") as cf:
        config["access_point_id"] = prompt("Set the ID of this access point >> ")
        config["webserver_address"] = "http://" + prompt(
            "Set the IP address and port of the web server in the following format: <IP-address>:<port>\n"
            "http://"
        )
        config["user_name"] = prompt(
            "Set the user name for the web server api authentication >> "
        )
        config["password"] = ""
        while config["password"] == "":
            config["password"] = getpass()

        config["sending_interval"] = prompt(
            "Set a data transfer interval in seconds "
            "between access point and web server >> ",
            parse_float=True,
        )
        yaml.dump(config, cf)

    print("\nSetup done! ✨🚀✨\nGood bye. ")
    import time

    time.sleep(0.5)


if __name__ == "__main__":
    main()