# Game Management System 🎮

Welcome to the **Game Management System**! This Java-based application manages games, user registrations, bookings, leaderboards, and notifications. It uses MySQL for data storage and provides a console-based interface for user interaction.

---

## Features ✨

- **User Registration & Login**: Secure user authentication.
- **Game Management**: Add, update, and manage games.
- **Booking System**: Book game sessions with specific dates and times.
- **Leaderboard**: Track user scores and achievements.
- **Notifications**: Send notifications to users.
- **Recent Games**: View recently added games.
- **Search Functionality**: Search games by title.
- **Payment Integration**: Simulate payment processing.
- **Data Validation**: Validate email, password, date, and time formats.

---

## Technologies Used 🛠️

- **Java**: Core programming language.
- **MySQL**: Database for storage.
- **JDBC**: For database interaction.
- **Data Structures**: `LinkedList` and `Stack` for managing games and operations.

---

## Prerequisites 📋

1. **JDK 8+**: Install Java Development Kit.
2. **MySQL**: Set up a database named `gamemanagementdb`.
3. **MySQL Connector/J**: Add the JDBC driver to your project.
4. **IDE**: Use IntelliJ, Eclipse, or VS Code.

---

## Database Setup 🗃️

1. Create the database:
   ```sql
   CREATE DATABASE gamemanagementdb;
   USE gamemanagementdb;

## How to Run??
### Step 1 : Clone the Repository to Your Local Machine
### Step 2 : Set Up MySQL:

- Update the DB_URL, USER, and PASS constants in the GameManagementSystem class with your MySQL credentials.

- Run the SQL scripts provided in the Database Setup section to create the necessary tables.

### Step 3 : Compile & Run the java code
```bash
javac GameManagementSystem.java
java GameManagementSystem
```


