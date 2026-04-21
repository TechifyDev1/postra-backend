# ⚙️ Postra Backend API

The central API for the **Postra** ecosystem, built with **Spring Boot** and **Java 21**. It handles authentication, post management, and integration with third-party services like Cloudinary.

---

## ✨ Key Features

- **🔐 Secure Auth:** JWT-based authentication and Spring Security for protecting API endpoints.
- **💾 Data Management:** Efficient post and user data handling using Spring Data JPA with MySQL.
- **🖼️ Image Uploads:** Direct integration with [Cloudinary](https://cloudinary.com/) for media management.
- **🧹 Content Parsing:** Uses JSoup for robust HTML parsing and sanitization.
- **🐋 Docker Support:** Ready for containerized deployment with a pre-configured Dockerfile.
- **🛠️ Extensible:** A clean, layered architecture designed for easy addition of new features.

---

## 🛠️ Tech Stack

- **Language:** [Java 21](https://www.oracle.com/java/technologies/downloads/)
- **Framework:** [Spring Boot 3.5.3](https://spring.io/projects/spring-boot)
- **Database:** MySQL (via Spring Data JPA)
- **Security:** Spring Security & JJWT
- **Utilities:** JSoup (HTML Parsing), Cloudinary SDK (Image Management)
- **Build Tool:** Maven

---

## 🚀 Getting Started

### Prerequisites

- JDK 21
- Maven
- MySQL Database
- Cloudinary Account (for image management)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/TechifyDev1/postra-backend.git
   cd postra-backend
   ```

2. **Database Setup:**
   - Create a MySQL database named `postra`.
   - Update `src/main/resources/application.properties` with your database credentials and Cloudinary API keys.

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Build the JAR:**
   ```bash
   ./mvnw clean install
   ```

---

## 📁 Project Structure

- `src/main/java/`: Source code including controllers, services, repositories, and models.
- `src/main/resources/`: Configuration files and application properties.
- `Dockerfile`: Containerization setup for the application.

---

## 📄 License

Internal use and development only.
