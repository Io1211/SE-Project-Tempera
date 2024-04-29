# Running the python components with docker

It is as simple as typing

```bash
# Since the app container depends on the api one, this
# command will start both in the correct order and launch the app.
$ docker compose run app
# Or if you want to rebuild the images before starting the app
# (like after making changes to the code)
$ docker compose run --build app

# If you just want to run the api container.
$ docker compose run api
```

> :warning:
> Set the *hostname* to *api* and the *port* to *80* in the configure script if running the app via *docker compose*.
> This is the only way that the two containers can communicate the http requests with each other.

If you run just the api container, you can connect to the testing api in your browser
by clicking the link that appears in the terminal
when you run the container or navigate to `http://0.0.0.0:80` directly.
The host and port are set in the api `Dockerfile` *CMD* command if you want to change them (you also
have to change the mapping in the `compose.yaml` -> api service -> ports, to match the new one!).

## Running in dev-mode

The app can also be run in *dev-mode* meaning that all code changes will immediately and automatically
be reflected in the containers with the following command, although beware that the interactive mode
is not available in this scenario (i.e., you can't provide the input to the configure script).
This mode is most useful for the api.

```bash
$ docker compose watch <service>
```

### Details

There are 2 Dockerfile, one for each component (ble-app & api).
`compose.yaml` points to the dirs of those 2 Dockerfiles with the *build* command.
When running *docker compose [command]* it automatically executes *docker build -t [name] .*
in the directory specified with build.

API container:
> The api container must expose the api port to the host machine, thus the ports key in the api
> service (port: [host]:[docker]).

APP container:
> The app container takes user input and must therefore be run interactively. This happens with
> *docker compose run [service]* and **not** with the canonical *docker compose up* command.
> Additionally, the app container needs bluetooth to work. The best way to achieve this is to use
> the host machine bluetooth in the container. This happens with the *volumes: /var/...:/var...* command.


If you still want to run the containers individually, you can do it like so. If you use the `compose.yaml`
all the flags are take care of for you.

```bash
# api
$ docker run -p 80:80 api:latest
# app
$ docker run -itv /var/run/dbus:/var/run/dbus access_point:latest
```