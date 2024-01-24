# Info-Fetcher-Microservice

This Github repository contains a Springboot Microservice with a Postgres Database and a Kong API Gateway that can be dockerized:

## Summary

[Info-Fetcher-Microservice](#info-fetcher-microservice)
* [Summary](#summary)
* [Setup and Pre-requisites](#setup-and-pre-requisites)
* [Running the Microservice](#running-the-microservice)
    * [Verify the containers are running](#verifying-the-containers-are-running)
        * [Microservice](#microservice)
        * [pgAdmin](#pgadmin)
        * [Both databases](#both-databases)
        * [Kong Container](#kong-container)
        * [Prometheus](#prometheus)
        * [Grafana](#grafana)
    * [Cleaning up Exited Containers](#cleaning-up-exited-containers)
* [Kong API Gateway](#kong-api-gateway)
* [Populating the Microservice Database](#populating-the-microservice-database)
* [Testing the Microservice](#testing-the-microservice)
* [Monitoring the Microservice](#monitoring-the-microservice)


## Setup and Pre-requisites

1. If not already installed:

- Install Docker on your device (you can use the following link for a guide: [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/))
- Install the latest version of OpenJDK 17 on your device (The following page has a complete catalogue of OpenJDK downloads: [https://www.openlogic.com/openjdk-downloads](https://www.openlogic.com/openjdk-downloads))

2. Clone this repository or download the .zip file from GitHub (extract the downloaded zip file )

## Running the Microservice

1. Using a Command Line Interface of your choosing, change directory to the downloaded/cloned repository

2. Run the following command to build a .jar application file of the microservice: 

```
<# Linux/MacOs #>
./mvnw clean package -DskipTests

<# Windows #>
.\mvnw clean package -DskipTests
```
3. If the build is a success, it should be indicated in your CLI. If it is successful, run this command to deploy it:

```
docker-compose up -d --build
```

3. 9 docker containers should now be running:
    * `microservice`: where the spring-boot api image, built using a Dockerfile, is containerized
    * `db`: a Postgres database container and used by the API
    * `kong-gateway`: a Kong API gateway container to route the API
    * `kong-db`: a Postgres database container used by the Kong gateway
    * `kong-migration`: a temporary Kong API Gateway that runs the `kong migrations bootstrap` command to initialize the `kong-gateway` container
    * `pgadmin`: a pgAdmin container used to access both Postgres databases we have containerized.
    * `service-init`: where a python script runs to set up our `kong-gateway` container with all the services and routes needed to access our microservice.
    * `prometheus`: a Prometheus monitoring container for our API Microservice.
    * `grafana`: a Grafana metrics visualization dashboard container, integrating seamlessly with the `prometheus` container

4. The microservice is now running, ready for use and is connected to the Kong API Gateway.

#### Verifying the containers are running

##### Microservice

Using a web browser of your choosing, head to <http://localhost:8080/person/1>. You should have accessed a microservice endpoint. Since the database it is accessing has not been populated yet, this endpoint will return the following with a 404 response:
```json
{
    "timestamp": "{local time of response}",
    "message": [
        "The Person Object with id '1' does not exist in our records"
    ]
}
```

##### pgAdmin

Using a web browser of your choosing, head to <http://localhost:5050/>. You should see the pgAdmin login page. To verify both databases, make sure to sign in using the following credentials:

* Email : `admin@admin.com`
* Password : `admin`

You can change these credentials under the following `pgadmin` container environment variables in the "[docker-compose.yaml](https://github.com/mpirotaiswilton-IW/Info-Fetcher-Microservice/blob/main/docker-compose.yaml)" file: 

* `PGADMIN_DEFAULT_EMAIL`
* `PGADMIN_DEFAULT_PASSWORD`

##### Both databases

1. After successfully logging into pgAdmin, click on `Add New Server` on the Dashboard Home Page:
    * In the General tab, name your server as you see fit
    * Navigate to the Connection tab
    * For the `host name/address`, use the name of the Postgres container `db`
    * Make sure the port field is `5432`
    * the `Username` field is defined by the `POSTGRES_USER` environment variable for the `db` container in the `docker-compose.yaml` file
    * the `Password` field is defined by the `POSTGRES_PASSWORD` environment variable for the `db` container in the `docker-compose.yaml` file
    * Click save and, in the Object explorer, under Servers you should see your newly saved server `db`. This is the database the microservice uses.

2. To verify the kong database is up, we will add another server. Navigate to the `Servers (1)` element in the Object explorer. In the Dashboard tab, click on `Add New Server` on the Dashboard Home Page:
    * In the General tab, name your server as you see fit
    * Navigate to the Connection tab
    * For the `host name/address`, use the name of the Postgres container `kong-database`
    * Make sure the port field is `5432`
    * the `Username` field is defined by the `POSTGRES_USER` environment variable for the `kong-database` container in the `docker-compose.yaml` file
    * the `Password` field is defined by the `POSTGRES_PASSWORD` environment variable for the `kong-database` container in the `docker-compose.yaml` file
    * Click save and, in the Object explorer, under Servers you should see your newly saved server `kong-database`. This is the database the API gateway uses.

##### Kong Container

Using a web browser of your choosing, head to <http://localhost:8002/>. You should have accessed the Kong Manager GUI web page.

##### Prometheus

Using a web browser of your choosing, head to <http://localhost:9090/>. You should be directed to a Prometheus web page. If you head to <http://localhost:9090/targets/>, you should see 2 targets listed, each with one job with a state of `UP`:

* `prometheus` 
* `spring-actuator`

##### Grafana

Using a web browser of your choosing, head to <http://localhost:3000/>. You should be directed to a Grafana login page. The default crededntials are as follows:

* Email or Password: `admin`
* Password: `admin`

After logging in, you will be prompted to change the log in password. While this step can be skipped, it is highly recommended to do so for increased security. After this, you will be redirected the Grafana container's home page.

#### Cleaning up Exited Containers

At this point, there are 2 container which are no longer running: `kong-migration` and `service-init`. These containers have only ran to initialize and populate the Kong API gateway. To remove these exited containers, run the following command in a seperate CLI window: 

```
docker container prune -f
```

## Kong API Gateway

While we can access the microservice through the local host on port 8080, we want to access our API through the Kong API Gateway too. Fortunately, the `service-init` container has already populated the service and routes to access our microservice with the Kong Gateway.

Using a web browser of choice, head to <http://localhost:8002/default/services>. You should see a list of services under the `default` workspace and a service called `PeoplePerson-Get`. If you click on that item of the list then select the `Routes` tab, you should see a list of routes connected to the service and a route called `PeoplePerson-GetRoute`.

## Populating the Microservice Database


In order to test our Microservice, we will need to populate the database it uses. Once all containers are up and running, run the following command, replacing `"DATABASE_USERNAME"` with the `POSTGRES_USER` environment variable value for the `db` container in the `docker-compose.yaml` file:
```
docker exec -it db psql -U "DATABASE_USERNAME" -d "DATABASE_USERNAME" -f /tmp/Mock_data.sql
```
This will fill our `db` postgres database container with 1000 Person entries  from the file `/db/Mock_data.sql`.

## Testing the Microservice

1. Import the Postman Collection provided as a .json file in the `postman` folder in this repository

2. Send the request from the endpoint "Get Person by ID"

3. You should get the following response body:

```json
{
    "id": 1,
    "firstName": "Bob",
    "lastName": "Henderson",
    "age": 26,
    "sex": "Female"
}
```

## Monitoring the Microservice

To monitor the microservice, we can use the `grafana` container we deployed. Using the web browser of your choosing, head to <http://localhost:3000/dashboards>. Under the `Microservice` folder, click on `Basic Dashboard`. You should then be directed to a dashboard with 5 panels: On the left there is the microservice Uptime, Start time date and CPU usage. On the right are the total number of requests resulting in 2XX responses and in 4XX responses.

* Using the provided Postman collection, Send a request from the endpoint "Get Person by ID" (`http://localhost:8080/person/{Number between 1 and 1000}`) 5 times.
* In the Grafana Dashboard, you should see an increase of 5 in the "2XX Response Count" graph
* Using the provided Postman collection, Send a request from the endpoint "Get Person by ID", making sure to set the path variable to a number greater than a 1000 (eg. `http://localhost:8080/person/123456`) 4 times.
* In the Grafana Dashboard, you should see an increase of 4 in the "4XX Response Count" graph