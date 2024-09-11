# DigitalPlanner

## Overview
DigitalPlanner is a web application designed to manage employee scheduling. The application provides features for managing employee data, planning work schedules using generators, and tracking availability and absences.

## Table of Contents
1. [Features](#features)
2. [Technologies Used](#technologies-used)
3. [Installation](#installation)
4. [Usage](#usage)
5. [Directory Structure](#directory-structure)
6. [Contributing](#contributing)
7. [License](#license)

## Features
- **Employee Management:** Add, edit, and remove employee details.
- **Schedule Management:** Create/Generate and manage work schedules for employees.
- **Availability Management:** Track employee availability and absences.
- **Role-Based Access Control:** Different functionalities for admin and regular users.
- **Profile Management:** Update profile information including profile picture.
- **Password Management:** Change user passwords.

## Technologies Used
- **Java**: Primary programming language.
- **Spring Boot**: Framework for building the backend.
- **Thymeleaf**: Template engine for dynamic web pages.
- **Hibernate**: ORM tool for database interactions.
- **MySQL Database**: Relational database management system.
- **Maven**: Dependency management and build tool.

## Prerequisites
- Java 11 or higher
- Maven 3.6.0 or higher

## Setup and Installation
Follow these steps to set up the project locally:
1. **Clone the repository:**
   ```sh
   git clone https://github.com/SonerDemircan/DigitalPlannerEindwerk.git

2. **Navigate to the project directory:**
   ```sh
   cd DigitalPlannerEindwerk

3. **Install the required dependencies:**
   ```sh
   mvn clean install

4. **Start the application:**
   ```sh
   mvn spring-boot:run

## Usage
1. **Access the application:**
   Once the application is running, navigate to [http//localhost:8080](http://localhost:8080) to access the DigitalPlanner.

2. **User Registration:**
   Register a new account to start using the planner or sign in using your username and password.

3. **Features:**
   - Dashboard: View an overview of the system.
   - Employees: Manage employee information including adding new employees, editing details, and removing employees.
   - Schedules: Create and manage work schedules.
   - Availability: Track employee availability and manage absences.
   - Profile: Update your profile information including uploading a profile picture.
   - Change Password: Update your login password.

## Directory Structure
   Here's an overview of the project's directory structure:

```
DigitalPlannerEindwerk
├─ src/
│  └─ main/
│     ├─ java/
│     │  └─ GroepswerkJava
│     │     └─ DigitalPlanner/
│     │        ├─ controller/
│     │        ├─ dto/
│     │        ├─ exceptions/
│     │        ├─ model/
│     │        ├─ PlanningGenerator/
│     │        ├─ repository/
│     │        ├─ security/
│     │        ├─ service/
│     │        └─ DigitalPlannerApplication.java
│     ├─ resources/
│     │  ├─ static/
│     │  ├─ templates
│     │  └─ application.properties
│     └─ test/
│        └─ java/
│           └─ GroepswerkJava/
│              └─ DigitalPlanner/
│                 ├─ controller/
│                 ├─ repository/
│                 └─ service/
├─ .gitignore
├─ pom.xml
└─ README.md
```

## Dependencies
This project relies on the following dependencies:

- Spring Boot Starter Web: For building web applications, including RESTful applications using Spring MVC.
- Spring Boot Starter Data JPA: For persistence using Spring Data JPA and Hibernate.
- Spring Boot Starter Thymeleaf: For building web applications using Thymeleaf templates.
- Spring Boot Starter Security: For securing the application.
- MySQL Connector: JDBC driver for connecting to MySQL database.
- JUnit 5: For testing the application.

For a complete list of dependencies, refer to the pom.xml file.

## Contributing
1. Fork the repository.
2. Create a new feature branch.
3. Commit your changes.
4. Push the branch to your fork.
5. Create a pull request.

## Disclaimer
This project is intended for educational and demonstration purposes only. While we strive to keep the information up-to-date and correct, we make no representations or warranties of any kind, express or implied, about the completeness, accuracy, reliability, suitability, or availability with respect to the project or the information, products, services, or related graphics contained in the project for any purpose. Any reliance you place on such information is therefore strictly at your own risk.
