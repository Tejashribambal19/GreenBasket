# GreenBasket

GreenBasket is a Java-based full-stack agricultural marketplace web application that connects farmers directly with buyers.

The platform allows farmers to list and manage agricultural products, buyers to browse and purchase fresh produce, and administrators to manage users, farmers, products, reviews, complaints, and marketplace activity.

\---

## Features

### Buyer Features

* Buyer registration and login
* Browse available agricultural products
* View product details
* Place orders
* View previous orders
* Rate purchased products
* Submit product reviews
* Submit complaints and feedback
* View product ratings and review count

### Farmer Features

* Farmer registration and login
* Add agricultural products
* Update product information
* Manage listed products
* View marketplace activity
* Manage product availability

### Admin Features

* Admin login
* View registered buyers
* View registered farmers
* Activate or suspend farmer accounts
* Manage users
* Manage marketplace activity
* View complaints and feedback
* Monitor products and farmer activity

\---

## Technology Stack

|Category|Technology|
|-|-|
|Programming Language|Java|
|Backend|Jakarta Servlets|
|Frontend|JSP, HTML, CSS, JavaScript|
|Database|MySQL|
|Build Tool|Maven|
|Web Server|Apache Tomcat|
|Version Control|Git \& GitHub|
|IDE|NetBeans / Eclipse / IntelliJ IDEA|

\---

## Project Structure

```text
GreenBasket/
|
|-- src/
|   `-- main/
|       |-- java/
|       `-- webapp/
|
|-- sql/
|-- target/
|-- pom.xml
|-- README.md
`-- .gitignore
```

\---

## Main Modules

GreenBasket consists of three major user modules.

### Buyer Module

The buyer module allows customers to interact with the marketplace.

Buyers can:

* Register an account
* Login securely
* Browse products
* View product information
* Purchase products
* View order history
* Rate products
* Submit reviews and feedback

### Farmer Module

The farmer module allows farmers to manage their products in the marketplace.

Farmers can:

* Create farmer accounts
* Login to their dashboard
* Add new products
* Update product details
* Manage listed products
* View product activity

### Admin Module

The admin module provides control over the GreenBasket platform.

Administrators can:

* Manage buyers
* Manage farmers
* Activate farmer accounts
* Suspend farmer accounts
* Monitor listed products
* View feedback and complaints
* Manage marketplace activity

When a farmer account is suspended, products associated with that farmer can be restricted from appearing to buyers.

\---

## Product Rating and Feedback System

GreenBasket includes a rating and feedback feature.

After purchasing a product, buyers can provide feedback through their account.

A buyer can provide:

* Product rating
* Written review
* Feedback
* Complaint or issue

Product ratings can be displayed using:

* Average rating
* Total review count

This helps buyers understand the experience of previous customers before purchasing a product.

\---

## Prerequisites

Before running GreenBasket locally, make sure the following software is installed:

* Java JDK
* Apache Tomcat
* MySQL Server
* Maven
* Git
* NetBeans, Eclipse, or IntelliJ IDEA

Recommended versions:

```text
Java: 17+
Apache Tomcat: 10+
MySQL: 8+
Maven: 3+
```

\---

## Database Setup

### Step 1: Start MySQL

Make sure your MySQL server is running.

### Step 2: Create the database

Open MySQL and execute:

```sql
CREATE DATABASE greenbasket;
USE greenbasket;
```

\---

## SQL Files

Database-related SQL scripts are available inside the:

```text
sql/
```

folder.

Import or execute the required SQL scripts before starting the application.

\---

## Database Configuration

Configure the application with your local MySQL connection details.

Example configuration:

```text
Database URL:
jdbc:mysql://localhost:3306/greenbasket

Username:
your\_mysql\_username

Password:
your\_mysql\_password
```

Do not store real production passwords or sensitive credentials in the GitHub repository.

\---

## Installation

### 1\. Clone the repository

Open Command Prompt, PowerShell, or Git Bash and run:

```bash
git clone https://github.com/Tejashribambal19/GreenBasket.git
cd GreenBasket
```

\---

## Build the Project

Run:

```bash
mvn clean package
```

Maven will compile the project and generate the application package.

The generated file will normally be available inside:

```text
target/
```

\---

## Deploy Using Apache Tomcat

GreenBasket is designed to run using Apache Tomcat.

### Step 1

Build the project:

```bash
mvn clean package
```

### Step 2

Locate the generated `.war` file inside:

```text
target/
```

### Step 3

Copy the WAR file into the Tomcat:

```text
webapps/
```

directory.

Example:

```text
apache-tomcat/
`-- webapps/
    `-- GreenBasket.war
```

### Step 4

Start Apache Tomcat.

On Windows:

```text
apache-tomcat/bin/startup.bat
```

Or start Tomcat directly from your IDE.

\---

## Run the Application

After starting Apache Tomcat, open your browser.

The application will usually be available at:

```text
http://localhost:8080/GreenBasket/
```

The exact URL may depend on the generated WAR file name and Tomcat configuration.

\---

## Application Workflow

```text
User
 |
 |-- Buyer
 |   |-- Register / Login
 |   |-- Browse Products
 |   |-- Purchase Product
 |   |-- View Orders
 |   `-- Rating / Feedback
 |
 |-- Farmer
 |   |-- Register / Login
 |   |-- Add Products
 |   `-- Manage Products
 |
 `-- Admin
     |-- Manage Buyers
     |-- Manage Farmers
     |-- Activate / Suspend Farmers
     `-- View Feedback
```

\---

## Application Architecture

GreenBasket follows a traditional Java web application architecture.

```text
Browser
   |
   v
JSP / HTML / CSS / JavaScript
   |
   v
Jakarta Servlets
   |
   v
Java Application Logic
   |
   v
JDBC
   |
   v
MySQL Database
```

Apache Tomcat is used as the web application server.

\---

## Security Notes

For security, avoid committing sensitive information such as:

* MySQL passwords
* Database credentials
* API keys
* Secret tokens
* Production usernames and passwords

Use placeholder values in public repositories.

Example:

```text
DB\_USERNAME=your\_username
DB\_PASSWORD=your\_password
```

\---

## Future Enhancements

* Online payment integration
* Shopping cart improvements
* Product search
* Product category filters
* Price filtering
* Order tracking
* Email notifications
* Farmer analytics dashboard
* Buyer wishlist
* Product recommendation system
* REST API implementation
* Spring Boot migration
* Responsive mobile interface
* Cloud database deployment
* Cloud application deployment

\---

## Learning Outcomes

This project demonstrates practical knowledge of:

* Java Web Development
* Object-Oriented Programming
* Jakarta Servlets
* JSP
* JDBC
* MySQL
* Maven
* Apache Tomcat
* CRUD Operations
* Authentication
* Role-based application functionality
* Database integration
* Git
* GitHub
* Full-stack web application development

\---

## Repository

GitHub Repository:

https://github.com/Tejashribambal19/GreenBasket

\---

## Author

**Tejashri Bambal**

GitHub:

https://github.com/Tejashribambal19

\---

## About the Project

GreenBasket was developed as a full-stack Java web application to demonstrate the implementation of an online agricultural marketplace using Java web technologies.

The project focuses on connecting buyers and farmers through a centralized marketplace while providing administrative control for managing users and platform activity.

It demonstrates practical implementation of Java, JSP, Servlets, JDBC, MySQL, Maven, Apache Tomcat, and full-stack web application development.

