# Price Comparator Backend

This is a Java Spring Boot application that enables users to compare product prices across different stores, track discounts, receive alerts, and optimize shopping baskets.

---

## Table of Contents

* [Features](#-features)
* [Prerequisites](#-prerequisites)
* [Environment Variables](#-environment-variables)
* [Getting Started](#-getting-started)
* [CSV Data Import](#-csv-data-import)
* [API Endpoints](#-api-endpoints)

  * [1. Basket Optimization](#1-basket-optimization)
  * [2. Price History](#2-price-history)
  * [3. Best Value Recommendations](#3-best-value-recommendations)
  * [4. Price Alerts](#4-price-alerts)
  * [5. Best Discounts](#5-best-discounts)
  * [6. New Discounts](#6-new-discounts)
* [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
* [Assumptions & Simplifications](#assumptions--simplifications)
* [Demo Video](#demo-video)

---

## Features

1. **Basket Optimization** – Identify the cheapest store for each item in a shopping list.
2. **Price History** – Retrieve historical price changes for a specific product at a given store.
3. **Best Value Recommendations** – Compare products by price per unit or by category.
4. **Price Alerts** – Notify users when a product reaches a target price.
5. **Discount Tracking** – List the best available discounts and new discounts added in the last 24 hours.

---

## Prerequisites

* Java 17+
* Maven 3.6+
* PostgreSQL (or any JDBC-compatible database)

---

##  Environment Variables

| Variable         | Description                           | Example               |
| ---------------- | ------------------------------------- | --------------------- |
| `DB_USERNAME`    | Database username                     | `my_db_user`          |
| `DB_PASSWORD`    | Database password                     | `super_secret_pwd`    |
| `EMAIL`          | Email address used for sending alerts | `noreply@example.com` |
| `EMAIL_PASSWORD` | Password for the email account        | `email_secret_pwd`    |

**Windows (Terminal)**

```bash
set DB_USERNAME=your_db_username
set DB_PASSWORD=your_db_password
set EMAIL=your_email@example.com
set EMAIL_PASSWORD=your_email_password
mvn spring-boot:run
```

**VS Code (.env)**

```ini
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
EMAIL=your_email@example.com
EMAIL_PASSWORD=your_email_password
```

or define them in `launch.json`.


## Getting Started

1. Clone the repository:
   
git clone [https://github.com/your-org/price-comparator-backend.git](https://github.com/your-org/price-comparator-backend.git)
cd price-comparator-backend

````
2. Ensure your environment variables are set.
3. Start the application:
mvn spring-boot:run
````

4. The backend will start at: `http://localhost:8081`

---

##  CSV Data Import

On startup, `CsvImportService` automatically loads all `.csv` files from `src/main/resources/data/` via a `@PostConstruct` method.

* **Price files:** `store_YYYY-MM-DD.csv`
* **Discount files:** `store_discount_YYYY-MM-DD.csv`

Each row maps to either a `PriceEntry` or `DiscountEntry` entity. Products and stores are created or reused as needed.

---

## API Endpoints

Base URL: `http://localhost:8081/api`

### 1. Basket Optimization

**POST** `/basket/optimize`

Request Body:

```json
{
  "date": "2025-05-23",
  "productIds": ["X026", "X031", "X043"]
}
```

Returns the cheapest store for each product ID on the specified date.

### 2. Price History

**POST** `/prices/history`

Request Body:

```json
{
  "productId": "X001",
  "store": "kaufland"
}
```

Retrieves historical prices for a product at a given store.

### 3. Best Value Recommendations

**GET** `/prices/best-value?productName=<name>&date=<YYYY-MM-DD>`

Example:

```
GET /prices/best-value?productName=lapte Zuzu&date=2025-05-23
```

**Category recommendations:**

```
GET /prices/recommendations?category=lactate&date=2025-05-23
```

### 4. Price Alerts

#### Create Alert

**POST** `/alerts/create-alert`

Request Body:

```json
{
  "userEmail": "john@example.com",
  "productId": "X028",
  "targetPrice": 7.00
}
```

#### Check Triggered Alerts (manual)

**GET** `/alerts/triggered-alert?date=<YYYY-MM-DD>`

Example:

```
GET /alerts/triggered-alert?date=2025-05-23
```

### 5. Best Discounts

**GET** `/discounts/best`

Retrieves the top discounts available across all stores.

### 6. New Discounts (Last 24h)

**GET** `/discounts/new`

Lists discounts added in the past 24 hours.

---

## Data Transfer Objects (DTOs)

We use Java record classes for clean, immutable DTOs.

Example:

```java
public record BasketOptimizeRequest(LocalDate date, List<String> productIds) {}
```

DTOs ensure type safety and controlled API exposure.

---

## Assumptions & Simplifications

* CSV files are trusted and well-formatted.
* Prices in CSV already reflect discounts.
* Product matching uses consistent `productId` across stores.
