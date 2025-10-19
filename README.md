# 🩸 BloodMate – Full-Stack Blood Donation Management System

**BloodMate** is a full-stack blood donation platform designed to connect donors, recipients, and administrators efficiently.  
It includes an **Android app frontend**, a **Spring Boot backend**, and a **web-based admin panel** built with HTML and CSS.

---

## 🚀 Features

### Android App (Frontend)
- Donor registration & profile management  
- Blood request creation & tracking  
- Notifications for donation requests  
- View upcoming donation camps/events  
- Communicates with backend via REST APIs

### Admin Panel (Frontend)
- Built with HTML and CSS  
- Manage blood requests (create, update, delete)  
- Assign/unassign donors  
- Track donor health information  
- Organize donation camps/events  
- Manage banners/images via **Cloudinary**

### Backend (Spring Boot)
- Handles all CRUD operations for donors, requests, and events  
- Provides REST APIs for Android app integration  
- Serves admin panel pages  
- Ready for future enhancements (authentication, notifications)

---

## 🛠️ Tech Stack

- **Mobile Frontend:** Android Studio, Java, XML  
- **Web Admin Panel:** HTML, CSS, JavaScript  
- **Backend:** Spring Boot  
- **Database:** MySQL  
- **Cloud Services:** Cloudinary (for image uploads)  
- **Tools:** IntelliJ IDEA, Android Studio, Postman, Git/GitHub

---

## 📂 Project Structure
BloodMate/ <br>
┣ android-app/ # Android app frontend<br>
┃ ┣ app/src/main/java/<br>
┃ ┗ app/src/main/res/ # XML layouts<br>
┣ backend/ # Spring Boot backend<br>
┃ ┣ src/main/java/... # Controllers, Services, Repositories, Models<br>
┃ ┣ src/main/resources/static/ # HTML/CSS admin panel<br>
┃ ┗ pom.xml<br>
┣ README.md<br>



---

## ⚙️ Setup & Run

### Backend (Spring Boot)
bash
cd backend
mvn spring-boot:run bash

Configure database credentials in application.properties or application.yml

Backend runs at http://localhost:8080
---
Android App

Open the project in Android Studio

Update the API base URL in Retrofit client (RetrofitClient.java)

Run on emulator or physical device

🧠 Key Highlights

Full-stack solution: Android + Spring Boot + Admin Panel

Clean separation of frontend and backend logic

Responsive HTML/CSS admin panel

REST APIs integrated with Android app

Easy to extend with authentication, notifications, and analytics


📸 Screenshots
-

### Android App

<p align="center" class = "images">
  <img src="assets/screenshots/splash.jpg" alt="Splash Screen" width="220"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="assets/screenshots/login.jpg" alt="Login Screen" width="220"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="assets/screenshots/signup.jpg" alt="Sign Up Screen" width="220"/> 
</p><br>
  <p align="center">
  <img src="assets/screenshots/home.jpg" alt="Home Screen" width="220"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="assets/screenshots/camps.jpg" alt="Camps Screen" width="220"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="assets/screenshots/profile.jpg" alt="Profile Screen" width="220"/> 
</p>

---

🤝 Contributing
-
Fork the repository

Create a new branch (feature/your-feature)

Commit your changes and open a pull request

---
🧑‍💻 Author
-

##### Abhishek Dhawan 
📧 [Email Address](mailto:abhidhawan1303@gmail.com)<br>
🔗 [GitHub](https://www.github.com/Code-Where) <br>
🔗 [LinkedIn](https://www.linkedin.com/in/abhishekdhawan786) <br>

---
📜 License
-
This project is licensed under MIT License.
