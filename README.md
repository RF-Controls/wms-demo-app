# Warehouse Management Demo Application

This is a simple, yet enterprise grade (built with spring boot and angular), application to demonstrate consuming tag
data from RFC OS and relating the EPC codes to everyday items. On it's own this app can be very handy for testing
and demonstrations. The app allows you to do the following:

- Create items (from real world objects) with names, descriptions, and images
- Associate EPC codes to these items
- Search for the location of items by name (no memorizing EPC codes!)
- Monitor the Item Location in real time

#### Instructional Videos

- [Installation Using Docker](https://youtu.be/Wo96cuTCbOE)
- [How to use the application](https://youtu.be/qX9RwrW_N40)

### Some Notes

- The default landing page of the app is located at this address: http://localhost:9000
- The default username/password for the application is admin/admin
- By default this app assumes it is running on the same machine as a standalone docker instance of RFC OS. If this is not the case you will want to modify
  the application-dev.yml and/or application-prod.yml file(s) with the correct location of RFC OS. You will find the appropriate
  properties near the bottom of the file.

### Known Issues

- The websocket connection to RFC OS does not automatically connect if dropped.
- Fuzzy zone association logic is not complete

## Development

This application was created using [JHipster](https://www.jhipster.tech/), information about the application architecture
can be found on their website.

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```

./mvnw


npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

###If you would rather run inside a docker container...
This maven command will build to the local Docker daemon using Jib and Maven.
This is handy for testing a complete build on a local docker instance

```
mvn clean -Pprod compile jib:dockerBuild
```

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

## Classes of Interest

For your own edification you may want to glance at a few of these classes as they demonstrate how to interact with RFC OS
and process tag data.

- **com.rfcontrols.wms.listener.EndpointListener** - Demonstrates how to connect to the RFC OS websocket server and consume tag data
- **com.rfcontrols.wms.service.impl.ZoneAssociationServiceImpl** - Contains some buisness logic on associating tags to zones
- **com.rfcontrols.wms.bo.TagWindowBO** - Demonstrates how to smooth data coming from RFC OS using clustering algorithms and confidence intervals.
  If you are not happy with the quality of your data you can improve upon the logic in this class. This is still a work in progress so any input is welcome.

## Building for production

### Building a Docker tarball using Jib and Maven

Useful for building a docker container to install on another system

```

mvn clean -Pprod compile jib:buildTar

```

To install the tarball on another system

```

docker load --input wmsdemo-image.tar
docker-compose -f docker/app.yml up -d
```

### Packaging as jar

To build the final jar and optimize the WMS demo application for production, run:

```

./mvnw -Pprod clean verify

```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```

java -jar target/*.jar

```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```

./mvnw -Pprod,war clean verify

```
