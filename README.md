![Logo of the project](https://raw.githubusercontent.com/KateLocate/menu_generator/main/frontend/menu_app/public/logo512.png)

# Menu Generator

> [Menu Generator on Render](https://menu-generator-fa38.onrender.com)

A project designed to make everyday cooking and grocery shopping more fun, random and less annoying.
It is a small web service with API and simple frontend.

## Developing

- This project uses Java 21 and Maven. All Maven dependencies are listed in `pom.xml` file.
- The project also requires `npm` module.
- Docker is used to run the project database locally.
- If you have the above installed, then you can use the Makefile commands.
- Start with cloning this repo:
    ```shell
    git clone https://github.com/KateLocate/menu_generator.git
    cd menu_generator/
    ```

### Building

Commands used for building, deploying and testing are located in the Makefile.

```shell
make build
make run
```

`make list-commands` lists all available commands from the Makefile  
`make build` - builds both backend and frontend, compiles everything and creates JAR package     
`make run` - runs the JAR package locally

### Deploying / Publishing

This app is hosted on Render, which has its own database solution. 
Render also supports synchronization with GitHub repository, so there is no specific infrastructure
dedicated to the deployment process.

There is also a simple CI/CD workflow that runs backend tests for each push or PR into the `main` branch.

## Local deploy

> http://127.0.0.1:8080/ - the app with the frontend can be found here  


## Links

- Project homepage: https://menu-generator-fa38.onrender.com
- Repository: https://github.com/KateLocate/menu_generator
- Issue tracker: https://github.com/KateLocate/menu_generator/issues

## Licensing

The code in this project is licensed under MIT license.
