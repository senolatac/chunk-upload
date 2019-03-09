# chunk-upload Spring Boot, Angular 7, MySQL, Docker, Hibernate

The application structure is as follows.
- **file-upload-server** - Microservice implemented using Spring boot. [More info](file-upload-server/README.md)
- **file-upload-client** - A NodeJs application implemented using Angular 7. This consumes services hosted by file-upload-server.  [More info](file-upload-client/README.md)
- **docker-compose.yml** - Docker compose file to run file-upload-server in container.

### Build

#### 1) Build file-upload-server

```
$ cd file-upload-server
$ gradlew clean build
```

#### 2) Build Docker images and run it in containers using docker-compose from parent directory.
   This also create container for Mysql and run it.
   
```
$ docker-compose up
```

NOTE: To run without docker container follow [steps](file-upload-server/README.md) in file-upload-server project.

#### 3) Build and run file-upload-client application

```
$ cd file-upload-client
$ docker build -t file-upload-client .
$ docker run -d -p 4200:80 file-upload-client
```

### Access application using following URL

```
http://localhost:4200
```

