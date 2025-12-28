# Tender Management System – Tender Management API

## Overview
The Tender Management System is a Spring Boot–based RESTful API developed to manage bidding details and approval workflows. The application enables bidders to create bids and approvers to review, approve, and manage those bids using secure JWT-based authentication and authorization.

## Technology Stack
- Java
- Spring Boot
- Spring Security (JWT Authentication)
- JPA / Hibernate
- RESTful Web Services

## Roles & Authorization
The system supports two roles:
- BIDDER
- APPROVER

Roles are identified from the JWT token. Every secured API request must include the JWT token in the request header as shown below:
Authorization: Bearer <JWT_TOKEN>

All endpoints except /login are authenticated and authorized using role-based access control.

## Database Models

### RoleModel
Fields:
- id (Integer, Primary Key, Auto Increment)
- rolename (String, Unique)

### UserModel
Fields:
- id (Integer, Primary Key, Auto Increment)
- username (String)
- companyName (String)
- email (String, Unique)
- password (String, Encrypted)
- role (Integer, Foreign Key)

### BiddingModel
Fields:
- id (Integer, Primary Key, Auto Increment)
- biddingId (Integer, Unique)
- projectName (String, Fixed value: "Metro Phase V 2024")
- bidAmount (Double)
- yearsToComplete (Double)
- dateOfBidding (String, Current date in dd/MM/yyyy format)
- status (String, Default value: "pending")
- bidderId (Integer, Foreign Key)

## Preloaded Database Data

### Roles
- 1: BIDDER
- 2: APPROVER

### Users
- bidder1 | companyOne | bidderemail@gmail.com | bidder123$ | BIDDER
- bidder2 | companyTwo | bidderemail2@gmail.com | bidder789$ | BIDDER
- approver | defaultCompany | approveremail@gmail.com | approver123$ | APPROVER

## API Endpoints

### 1. Login
POST /login  
Authenticates the user and generates a JWT token.

Request Body:
{
  "email": "bidderemail@gmail.com",
  "password": "bidder123$"
}

Success Response (200 OK):
{
  "jwt": "your_jwt_token",
  "status": 200
}

Error Response:
- 400 Bad Request on invalid credentials

### 2. Add Bidding
POST /bidding/add  
Accessible only by BIDDER.

Request Body:
{
  "biddingId": 2608,
  "bidAmount": 14000000.0,
  "yearsToComplete": 2.6
}

Success Response (201 CREATED):
{
  "id": 1,
  "biddingId": 2608,
  "projectName": "Metro Phase V 2024",
  "bidAmount": 14000000.0,
  "yearsToComplete": 2.6,
  "dateOfBidding": "07/07/2023",
  "status": "pending",
  "bidderId": 1
}

Error Response:
- 400 Bad Request on validation failure

### 3. List Biddings
GET /bidding/list?bidAmount=15000000  
Returns all bidding records where the bid amount is greater than the given value.

Success Response:
- 200 OK

Error Response:
- 400 Bad Request with message "no data available"

### 4. Update Bidding Status
PATCH /bidding/update/{id}  
Accessible only by APPROVER.

Request Body:
{
  "status": "approved"
}

Success Response (200 OK):
{
  "id": 1,
  "biddingId": 2608,
  "projectName": "Metro Phase V 2024",
  "bidAmount": 14000000.0,
  "yearsToComplete": 2.6,
  "dateOfBidding": "07/07/2023",
  "status": "approved",
  "bidderId": 1
}

Error Response:
- 400 Bad Request on invalid credentials

### 5. Delete Bidding
DELETE /bidding/delete/{id}  
Accessible by APPROVER or the BIDDER who created the bid.

Success Response:
- 204 No Content with message "deleted successfully"

Error Responses:
- 400 Bad Request if the bidding ID is not found
- 403 Forbidden if the user does not have permission

## Validation & Error Handling
- Any validation failure returns 400 Bad Request
- Unauthorized access returns 403 Forbidden
- All secured endpoints require a valid JWT token

## Conclusion
This API provides a secure, role-based tender and bidding management system using Spring Boot and JWT authentication, ensuring controlled access and proper validation throughout the bidding lifecycle.
