# Transportation Management System (TMS) — Backend

## Project overview

Proof-of-concept backend for a Transportation Management System (TMS) built with Spring Boot and Spring GraphQL.

Features
- GraphQL API to manage shipments
- Role-based access control (RBAC)
- CRUD operations for admins
- Query-only access for employees
- Pagination, filtering and sorting

Tech stack
- Backend: Spring Boot, Spring GraphQL, Spring Security
- Database: in-memory H2 (default)
- Authentication: HTTP Basic
- API: GraphQL

## User credentials
| Role     | Username | Password |
|----------|----------|----------|
| Admin    | admin    | admin    |
| Employee | emp      | emp      |

## Run locally

Prerequisites
- Java 17+
- Maven 3.x

Quick start
```bash
git clone <YOUR_REPO_URL>
cd tms-backend
mvn clean install
```

Run (dev profile enables GraphiQL):

```bash
# run with the 'dev' profile (GraphiQL enabled)
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

The backend will start at: http://localhost:8080

## GraphQL endpoint

POST http://localhost:8080/graphql

If GraphiQL is enabled (dev profile) the UI is available at:

http://localhost:8080/graphiql

### Authentication (HTTP Basic)
Set the Authorization header in your client. Example headers:

Admin:

```json
{ "Authorization": "Basic YWRtaW46YWRtaW4=" }
```

Employee:

```json
{ "Authorization": "Basic ZW1wOmVtcA==" }
```

## Examples

Note: the running schema uses input objects `ShipmentFilter` and `SortInput` for filtering and sorting. Use the `filter` and `sort` arguments as shown below.

1) List shipments (with filter & sort)

```graphql
query Shipments($page: Int!, $size: Int!, $filter: ShipmentFilter, $sort: SortInput) {
  shipments(page: $page, size: $size, filter: $filter, sort: $sort) {
    totalCount
    data {
      id
      shipmentNumber
      origin
      destination
      status
      carrier
      weight
    }
  }
}
```

Variables example:

```json
{
  "page": 0,
  "size": 5,
  "filter": { "status": "IN_TRANSIT", "origin": "Delhi" },
  "sort": { "field": "shipmentNumber", "direction": "ASC" }
}
```

2) Single shipment

```graphql
query {
  shipment(id: 1) {
    id
    shipmentNumber
    origin
    destination
    status
    carrier
    weight
  }
}
```

3) Add shipment (admin only)

```graphql
mutation AddShipment($inputShipmentNumber: String!, $origin: String!, $destination: String!, $status: String!, $carrier: String!, $weight: Float!) {
  addShipment(
    shipmentNumber: $inputShipmentNumber,
    origin: $origin,
    destination: $destination,
    status: $status,
    carrier: $carrier,
    weight: $weight
  ) {
    id
    createdAt
  }
}
```

Variables example:

```json
{
  "inputShipmentNumber": "SHP-001",
  "origin": "Delhi",
  "destination": "Mumbai",
  "status": "IN_TRANSIT",
  "carrier": "DHL",
  "weight": 120
}
```

4) Update shipment (admin only)

```graphql
mutation UpdateShipment($id: ID!, $status: String, $carrier: String) {
  updateShipment(id: $id, status: $status, carrier: $carrier) {
    id
    shipmentNumber
    status
    carrier
  }
}
```

Variables example:

```json
{ "id": 1, "status": "DELIVERED", "carrier": "DHL" }
```

5) Delete shipment (admin only)

```graphql
mutation { deleteShipment(id: 1) }
```

## Role-based access control

| Role  | Allowed actions                 |
|-------|---------------------------------|
| Admin | query, add, update, delete      |
| Emp   | query only                      |

Unauthorized mutation attempts as an employee will return `Access is denied`.

## Troubleshooting

- If you see "Unable to locate a Java Runtime" — install a JDK (Adoptium/Temurin) and set `JAVA_HOME`.
- If GraphiQL returns `invalid credentials`, ensure your client sends the HTTP Basic Authorization header (see above) or use `curl -u admin:admin` for testing.
- If the server complains about missing entities or repositories, try a clean build:

```bash
./mvnw clean package
```