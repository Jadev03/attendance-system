# Attendance-system
## **Description**
The Attendance-System is an imaginary school system designed to manage student attendance efficiently. This project uses **JWT** and **RESTful APIs**. Instead of a database, it uses resource files (`tokenInfo.json` and `userInfo.json`) located in `server/src/resources`.
- **`tokenInfo.json`**: Maintains JWT tokens.
- **`userInfo.json`**: Stores usernames and their corresponding hashed passwords.  
  The JWT functionality leverages the [java-jwt library](https://github.com/auth0/java-jwt).

---
## **Features**
- Teacher login system.
- Home page for navigation and logout functionality.

---
## **Tech Stack**
- **Frontend**: React.js
- **Backend**: Java (Maven)
- **Version Control**: Git

---
## **Installation**

### **Prerequisites**
1. Install **Node.js** (npm included).
2. Install **Maven** for Java projects.

### **Steps**
1. Clone the repository:
   ```bash
   git clone https://github.com/Jadev03/attendance-system.git
2. Setup Frontend:
- Navigate to client directory
    ```bash
  cd client
- install dependencies
  ```bash
  npm install
3. Setup Backend
- Navigate to the server directory:
    ```bash 
    cd server
- Ensure Maven is properly configured for pom.xml:
    ```bash
  mvn compile
---

## **Running the Application**
1. Start the frontend server
- From the client directory
    ```bash 
  npm start
- Access the client at  http://localhost:3000
2. Start the Backend server
- From the server directory
    ```bash
  mvn exec:java
- Access the server at http://localhost:8080

---
## **Usage**
1. Open the application in your browser at http://localhost:3000
2. Log in with the credentials:
- username :- **`user`**
- Password:-  **`Password&123`**
3. Navigate to the home page after a successful login
4. Use the "Logout" button on the home page to return to the login page

---
## **Project Structure**




  ![Screenshot (798).png](..%2F..%2FPictures%2FScreenshots%2FScreenshot%20%28798%29.png)
  ![Screenshot (799).png](..%2F..%2FPictures%2FScreenshots%2FScreenshot%20%28799%29.png)
  