# 📚 Book Management API - Backend Spring Boot

API Java Spring Boot pour la gestion d'une collection de livres, avec authentification sécurisée via JWT et base de données PostgreSQL.

---

## 🚀 Installation et démarrage

### Prérequis
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Étapes

1. **Installer PostgreSQL**

```bash
# Ubuntu/Debian
sudo apt install postgresql postgresql-contrib

# macOS
brew install postgresql
brew services start postgresql

# Windows
# Télécharger depuis https://www.postgresql.org/download/windows/

sudo -u postgres psql

CREATE DATABASE book_management;
CREATE USER bookuser WITH PASSWORD 'passer';
GRANT ALL PRIVILEGES ON DATABASE book_management TO bookuser;

\q


spring.datasource.url=jdbc:postgresql://localhost:5432/book_management
spring.datasource.username=postgres
spring.datasource.password=passer

mvn clean install
mvn spring-boot:run

L'API sera disponible sur :
📍 http://localhost:8080/api

