# Congestion API Gateway
This is a REST API gateway built to aggregate data from multiple [Congestion APIs](https://github.com/HiReach-Project/congestion-tracing-standalone).
The congestion API is more accurate when the user base is larger.
This API Gateway is build to make it possible for multiple companies to share congestion data while keeping their data in their own private database.
The more companies who contribute with location data the more accurate the congestion data will be.  
A running instance on a company's private server is referred to as a `node` in the context of this API.
##### How it works:
Multiple companies are using the [Congestion API](https://github.com/HiReach-Project/congestion-tracing-standalone)
but they want to keep their users location data in their own private database and only share congestion data.
Users who have access to the API Gateway can request data about congestion in a certain point defined by latitude and longitude.
The gateway then calls every node and aggregates the data from all companies and returns a more accurate response.    
##High-level architecture diagram
![gateway-diagram](https://user-images.githubusercontent.com/34125719/91662544-d6ca0780-eaeb-11ea-9172-f19055a2410d.png)
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
docker run --network=host \
-e SERVER_PORT=8080 \
-e SPRING_DATASOURCE_USERNAME=database_owner \
-e SPRING_DATASOURCE_PASSWORD=database_owner_password \
-e SPRING_DATASOURCE_URL=jdbc:postgresql://your_database_server_url:5432/database_name \
congestion_tracing_gateway
```
# API specification
## Authorization
All API requests require the use of an API key.
To authenticate an API request, you should append your API key as a GET parameter.
```http
GET /api/congestion?key=1234567890
```
**Note**: for security reasons there is NO default api key added into the database. For testing the API, a hashed key must be manually added,
 after running the container, as a SHA3_256 encoded string into the `company` table.  
 The hash can be obtained easily from [here](https://md5calc.com/hash/sha3-256/1234567890).  
 Example adding the SHA3_256 encoded hash of `1234567890` key in the database:
```sql
insert into company values(1, 'your_company_name', '01da8843e976913aa5c15a62d45f1c9267391dcbd0a76ad411919043f374a163');
```
## Endpoints
### Get congestion
```http
GET /api/congestion?key=1234567890&lat=44.348732&lon=26.104334&radius=10
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
    curl --request GET 'http://localhost:8080/api/congestion?key=1234567890&lat=44.348732&lon=26.104334&radius=12.5&seconds_ago=45'
    ```
   **Success Response:**
        
    **Code:** 200 OK  
    **Content:** `3`  
    The response is an integer representing the total number of devices being inside the perimeter of a circle
    of radius `12.5` meters and the center at `44.348732, 26.104334` which posted their location data (`lat`, `lon`) in the past 45 seconds.

   
### Get nodes
A node is an instance of the [Congestion API](https://github.com/HiReach-Project/congestion-tracing-standalone)
running on a certain company server. You can either call directly the `/congestion` endpoint which aggregates the congestion data from all nodes
or get all nodes urls and call them individually. You can store the node urls and call them directly in case the API Gateway is down.
```http
GET /api/nodes?key=1234567890
```
*  **URL Params**

   **Required:**   
   `key=[integer]` - Authorization key.  
   
    **Sample call:**
    ```shell script
    curl --request GET 'http://localhost:8080/api/nodes?key=1234567890'
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
## Error Responses 
**Code:** 400 BAD REQUEST  
**Content:**   
```json
{
    "timestamp": "2020-08-22T13:21:05.045562Z",
    "status": 400,
    "error": "Bad Request",
    "message": "lon should be of type double",
    "path": "/api/nodes"
}
``` 
**Code:** 403 FORBIDDEN  
**Content:**   
```json
{
    "timestamp": "2020-08-22T13:21:05.045562Z",
    "status": 403,
    "error": "Forbidden",
    "message": "",
    "path": "/api/nodes"
}
```
**Code:** 429 TOO MANY REQUESTS  
**Content:**   
```json
{
    "timestamp": "2020-08-22T13:21:05.045562Z",
    "status": 429,
    "error": "Too Many Requests",
    "message": "You have exhausted the API Request Quota.",
    "path": "/api/nodes"
}
```
**Code:** 500 INTERNAL SERVER ERROR  
**Content:**   
```json
{
    "timestamp": "2020-08-22T13:21:05.045562Z",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Oops! Something went wrong on our side.",
    "path": "/api/nodes"
}
```
