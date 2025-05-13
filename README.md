# Courier Tracking Application

The application continuously tracks courier locations in real time, logs their store‐entry events, and accurately computes each courier’s total distance traveled each day. Built with a modern technology stack, it delivers a modular, scalable solution that easily adapts to changing requirements.

## Features

- Store and process courier location data in real time  
- Detect couriers entering within 100 m of Migros stores  
- Filter repeated store entries within a 1-minute interval  
- Calculate total travel distance per courier  
- Geospatial queries with PostgreSQL + PostGIS  
- Asynchronous event handling (event-driven architecture)  

---

## Technologies

- **Java** 21  
- **Spring Boot** 3  
- **Spring Data JPA**  
- **PostgreSQL** with **PostGIS**  
- **Redis**  
- **Maven**  
- **Lombok**  
- **MapStruct**  
- **OpenAPI** / **Swagger**  
- **Docker** & **Docker Compose**  

---

## Installation & Running

### Using Docker

1. Build and start all services:  
   ```bash
   docker-compose up -d --build
   ```  
2. Verify containers are running:  
   ```bash
   docker-compose ps
   ```  


## API Documentation

Once the application is running, open your browser:

```
http://localhost:8080/swagger-ui
```

---

## Core API Endpoints

| Method | Endpoint                               | Description                                   |
| ------ | -------------------------------------- | --------------------------------------------- |
| POST   | `/api/v1/couriers`                     | Create a new courier                          |
| GET    | `/api/v1/couriers`                     | List all couriers                             |
| POST   | `/api/v1/couriers/location`            | Update a courier’s location                   |
| GET    | `/api/v1/couriers/{id}/total-distance` | Get total travel distance for a courier       |

---

## Sample Requests

### 1. Create a Courier

```bash
curl -X POST "http://localhost:8080/api/v1/couriers"   -H "Content-Type: application/json"   -d '{
    "fullName": "Ali Yılmaz"
  }'
```

**Response**  
```json
{
  "id": 1,
  "fullName": "Ahmet Yeşilyurt",
  "createdAt": "2023-11-10T14:00:00Z"
}
```

---

### 2. Update Courier Location

```bash
curl -X POST "http://localhost:8080/api/v1/couriers/location"   -H "Content-Type: application/json"   -d '{
    "time": "2023-11-10T14:30:00Z",
    "courier": 1,
    "lat": 40.9923307,
    "lng": 29.1244229
  }'
```

**Response**  
```json
{
  "locationId": 123,
  "courierId": 1,
  "timestamp": "2023-11-10T14:30:00Z",
  "distanceSinceLast": 0.85
}
```

---

### 3. Retrieve Total Distance

```bash
curl -X GET "http://localhost:8080/api/v1/couriers/1/total-distance"
```

**Response**  
```json
{
  "courierId": 1,
  "totalDistanceKm": 42.37
}
```

