# Tender Management System 🏗️

## 📌 Overview
The **Tender Management System** is a Spring Boot–based REST API used to manage tender bidding operations and approval workflows.  
It supports **JWT-based authentication and authorization** with two roles:

- **BIDDER**
- **APPROVER**

The system allows bidders to place bids and approvers to review, approve, or delete them based on defined access rules.

---

## 🛠️ Tech Stack
- Java 8
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- H2 Database
- Maven
- Swagger (OpenAPI)

---

## 🧩 Data Models

### 1️⃣ RoleModel
| Field Name | Type | Description |
|----------|------|------------|
| id | Integer | Primary Key (Auto Increment) |
| rolename | String | Unique (`BIDDER`, `APPROVER`) |

---

### 2️⃣ UserModel
| Field Name | Type | Description |
|----------|------|------------|
| id | Integer | Primary Key |
| username | String | User name |
| companyName | String | Company name |
| email | String | Unique |
| password | String | Login password |
| role | Integer | Foreign key → RoleModel |

---

### 3️⃣ BiddingModel
| Field Name | Type | Description |
|----------|------|------------|
| id | Integer | Primary Key |
| biddingId | Integer | Unique |
| projectName | String | Fixed value: **"Metro Phase V 2024"** |
| bidAmount | Double | Bid amount |
| yearsToComplete | Double | Duration |
| dateOfBidding | String | Format: `dd/MM/yyyy` |
| status | String | Default: `pending` |
| bidderId | Integer | Foreign key → User |

---

## 🗃️ Preloaded Database Data

### Roles
| id | rolename |
|----|----------|
| 1 | BIDDER |
| 2 | APPROVER |

### Users
| username | companyName | password | email | role |
|--------|------------|----------|-------|------|
| bidder1 | companyOne | bidder123$ | bidderemail@gmail.com | BIDDER |
| bidder2 | companyTwo | bidder789$ | bidderemail2@gmail.com | BIDDER |
| approver | defaultCompany | approver123$ | approveremail@gmail.com | APPROVER |

---

## 🔐 Security & Authentication
- JWT-based authentication
- Token must be passed in request header:
