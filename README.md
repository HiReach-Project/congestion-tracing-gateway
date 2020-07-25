# Congestion API Gateway
This is a REST API gateway built to aggregate data from multiple [congestion APIs](https://github.com/HiReach-Project/congestion-tracing-standalone).
The congestion API is more accurate when the user base is larger.
This API Gateway is build to make it possible for multiple companies to share congestion data while keeping their data in their own private database.
The more companies who contribute with location data the more accurate the congestion data will be.  
A running instance on a company's private server is referred to as a `node` in the context of this API.
##### How it works:
Multiple companies are using the [congestion tracing API](https://github.com/HiReach-Project/congestion-tracing-standalone)
but they want to keep their users location data in their own private database and only share congestion data.
Users who have access to the API Gateway can request data about congestion in a certain point defined by latitude and longitude.
The gateway then calls every node and aggregates the data from all companies and returns a more accurate response.
## Installation
### Postgres database
Tested with: PostgreSQL 12.2

Create a database:
```bash
sudo -u postgres psql
create database "database_name" owner "database_owner";
```
### Run the API in a docker container
Package the API in a JAR:
```bash
./mvnw package
```
Build docker container:
```bash
docker build -t congestion_tracing_gateway .
```
Run container:
```bash
docker run --network=host congestion_tracing_gateway
```
# API specification
## Authorization
All API requests require the use of an API key.
To authenticate an API request, you should append your API key as a GET parameter.
```http
GET /api/congestion/?key=1234567890
```
## Endpoints
### Get congestion
```http
GET /api/congestion/?key=1234567890&lat=44.348732&lon=26.104334&radius=10
```
*  **URL Params**

   **Required:**   
   `key=[integer]` - Authorization key.  
   `lat=[numeric]` - Latitude. Constraints: `-90.0 < lat < 90.0`  
   `lon=[numeric]` - Longitude. Constraints: `-180.0 < lat < 180.0`  
   `radius=[numeric]` - Distance in meters representing how big the perimeter to be when querying for active devices. Must be a positive number. 
   
    **Optional:**  
    
   `seconds_ago=[integer]` - Devices will post their location ideally every few seconds. This parameter controls how long to go back in time when querying for congestion. Default is 30 seconds.

    **Sample call:**
    ```shell script
    curl --request GET http://localhost:8080/api/congestion/?key=1234567890&lat=44.348732&lon=26.104334&radius=12.5&seconds_ago=45
    ```
   **Success Response:**
        
    **Code:** 200 OK  
    **Content:** `3`  
    The response is an integer representing the total number of devices being inside the perimeter of a circle
    of radius `12.5` meters and the center at `44.348732, 26.104334` which posted their location data (`lat`, `lon`) in the past 45 seconds.

   **Error Response:**
      
    **Code:** 403 FORBIDDEN <br />
    **Content:**   
    ```json
    {
        "timestamp": "1595662694514",
        "status": 403,
        "error": "Forbidden",
        "message": "",
        "path": "/api/congestion"
    }
    ```
    **Code:** 429 TOO MANY REQUESTS <br />
    **Content:**   
    ```json
    {
        "timestamp": 1595665040720,
        "status": 429,
        "error": "Too Many Requests",
        "message": "You have exhausted the API Request Quota.",
        "path": "/api/congestion"
    }
    ```
    **Code:** 400 BAD REQUEST <br />
    **Content:**   
    ```json
    {
        "timestamp": 1595662694514,
        "status": 400,
        "error": "Bad Request",
        "message": "radius should be of type double",
        "path": "/api/congestion"
    }
    ```
    **Code:** 500 INTERNAL SERVER ERROR <br />
    **Content:**   
    ```json
    {
        "timestamp": 1595663467680,
        "status": 500,
        "error": "Internal Server Error",
        "message": "Oops! Something went wrong on our side.",
        "path": "/api/congestion"
    }
   ```
   
### Get nodes
A node is an instance of [congestion tracing API](https://github.com/HiReach-Project/congestion-tracing-standalone)
running on a certain company server. You can either call directly the `/congestion` endpoint which aggregates the congestion data from all nodes
or get all nodes urls and call them individually. You can store the node urls and call them directly in case the API Gateway is down.
```http
GET /api/nodes/?key=1234567890
```
*  **URL Params**

   **Required:**   
   `key=[integer]` - Authorization key.  
   
    **Sample call:**
    ```shell script
    curl --request GET http://localhost:8080/api/nodes/?key=1234567890
    ```
   **Success Response:**
        
    **Code:** 200 OK  
    **Content:**   
    ```json
    [
        {
            "name": "Company1",
            "url": "https://company1.com/api/congestion"
        },
        {
            "name": "Company2",
            "url": "https://company2.com/api/congestion"
        }
    ]
    ```
   **Error Response:**
      
    **Code:** 403 FORBIDDEN <br />
    **Content:**   
    ```json
    {
        "timestamp": "1595662694514",
        "status": 403,
        "error": "Forbidden",
        "message": "",
        "path": "/api/nodes"
    }
    ```
    **Code:** 429 TOO MANY REQUESTS <br />
    **Content:**   
    ```json
    {
        "timestamp": 1595665040720,
        "status": 429,
        "error": "Too Many Requests",
        "message": "You have exhausted the API Request Quota.",
        "path": "/api/nodes"
    }
    ```
    **Code:** 400 BAD REQUEST <br />
    **Content:**   
    ```json
    {
        "timestamp": 1595662694514,
        "status": 400,
        "error": "Bad Request",
        "message": "lon should be of type double",
        "path": "/api/nodes"
    }
    ```
    **Code:** 500 INTERNAL SERVER ERROR <br />
    **Content:**   
    ```json
    {
        "timestamp": 1595663467680,
        "status": 500,
        "error": "Internal Server Error",
        "message": "Oops! Something went wrong on our side.",
        "path": "/api/nodes"
    }
   ```