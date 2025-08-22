## ✨ Your first microservice: `donation-service`

The `donation-service` is the entry point through which users (or later, a web frontend) can make donations or request refunds. You implement the microservice in a network of existing microservices. The two services you will be interacting with are the `user-service` and the `donation-transaction-service`.

### `user-service`
- Maintains a record of users registered on the donation platform.
- Each user has a balance indicating the amount of money (in Euro) they currently own.
- The service allows updating of the current balance.
- For a complete overview of the API see [api-user-service.http](api-user-service.http)

### `donation-transaction-service`
- Records the donation transactions that have occurred.
- Supports checking past transactions, adding new ones, or deleting a transaction.
- For a complete overview of the API see [api-donation-transaction-service.http](api-donation-transaction-service.http)

Two users, `alex` and `lisa`, are already created with some transaction history. Balances and transaction histories may be modified by colleagues, as this is a public API. Create your own test users if necessary.

### Task 0: Bootstrap your Spring Boot application
- Use the Spring Initializr in IntelliJ to create a new Spring Boot Application
- Add `Spring Web` as Dependency
- In this exercise we won't need to persist anything (no Spring Data ;))
- Before you start with the below tasks, play around with the http-request files to understand both microservices you need to interact with.

### Task 1: Implement the `/total-donations/<donor>` Endpoint

**Objective:**
Allow users to view the total amount they have donated.

**HTTP Method:** `GET`

**Upstream APIs:** `/transactions/donor/alex`

**Functionality:**
Call the `donation-transaction-service` and return the sum of all donations made by the donor.

**Example Response (JSON):**
```json
{
  "donor": "alex",
  "amount": 60
}
```

### Task 2: Implement the `/donations` Endpoint

**Objective:**
Enable users to donate a specific amount to another user.

**HTTP Method:** `POST`

**Upstream APIs:** Investigate the available endpoints of both `user-service` and `donation-transaction-service`

**Example Payload (JSON):**
```json
{
  "donor": "alex",
  "recipient": "lisa",
  "amount": 200
}
```

**Required Actions:**
- Retrieve current balances of donor and recipient (via `user-service`)
- Update balances of both parties (according to request)
- Record the donation transaction in the `donation-transaction-service`
- Return the donation transaction entry as the response.

**Example Response (JSON):**
```json
{
  "id": 4,
  "donor": "alex",
  "recipient": "lisa",
  "amount": 50
}
```

### Task 3: Implement the `/donations/undo/<id>` Endpoint

**Objective:**
Provide users the ability to reverse a donation by specifying the transaction ID.

**HTTP Method:** `POST`

**Required Actions:**
- Find the donation transaction entry by the id provided (via `donation-transaction-service`)
- Retrieve donor and recipient user details  (via `user-service`)
- Reverse the transaction (update balances and delete transaction entry) via `user-service`/`donation-transaction-service`
- Return the user entity of the donor with the updated balance

**Example Response (JSON):**
```json
{
  "name": "lisa",
  "balance": 307
}
```

### Task 4: Error Scenarios

**Objective:**
Identify potential error scenarios and handle them with appropriate HTTP status codes and error messages. This also includes handling errors from upstream APIs.

### Task 5: Distributed Transactions Research

**Objective:**
Explore what distributed transactions are and identify what could go wrong in the previous scenarios given the current setup.


# Hints
- Try to encapsulate all REST request logic in services (`@Service`). Usually developers create a service per microservice they have to integrate with and call suffix it with "Client".
- You will need a lot of DTOs to make this all work, here is some suggested folder structure:
```plaintext
.
`-- com
    `-- example
        `-- microservicedonation
            |-- DonationController.java
            |-- MicroserviceDonationApplication.java
            |-- RestTemplateConfig.java
            |-- dtos
            |   |-- BalanceChange.java
            |   |-- DonationRequest.java
            |   |-- DonationTransaction.java
            |   |-- DonationTransactionDonorCollection.java
            |   |-- TotalDonationsReponse.java
            |   `-- User.java
            `-- service
                |-- DonationTransactionServiceClient.java
                `-- UserServiceClient.java
```