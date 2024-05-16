config = {
    "version": 1,
    "disable_existing_loggers": False,
    "formatters": {
        "simple": {
            "format": "%(asctime)s - %(name)s - %(levelname)s: %(message)s",
            "datefmt": "%Y-%m-%d %H:%M:%S%z",
        },
        "rich": {"format": "%(message)s", "datefmt": "[%X]"},
    },
    "handlers": {
        "stdout": {
            "class": "rich.logging.RichHandler",
            "level": "INFO",
            "formatter": "rich",
        },
        "file": {
            "class": "logging.handlers.RotatingFileHandler",
            "level": "INFO",
            "formatter": "simple",
            "filename": "access_point.log",
            "maxBytes": 10000000,
            "backupCount": 1,
        },
    },
    "loggers": {
        "root": {"level": "NOTSET", "handlers": ["stdout", "file"]},
        "tempera": {"level ": "INFO", "handlers": []},
    },
}
