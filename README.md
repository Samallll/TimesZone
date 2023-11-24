# TimesZone E-commerce Website

TimesZone is an e-commerce web application for watch shopping. It is built on the Spring Boot framework for the backend, with MySQL as the database. The frontend is developed using HTML, CSS, and JavaScript, with Thymeleaf for templating.

## Features
- Browse and explore a wide range of watches from various brands.
- User-friendly interface for seamless navigation.
- Backed by the robust functionality of Spring Boot.

## Technologies Used
- **Backend:** Spring Boot
- **Database:** MySQL
- **Frontend:** HTML, CSS, JavaScript, Thymeleaf
- **Dependencies:** Thymeleaf, Spring Security, Razorpay

## Getting Started
1. **Clone the repository:**
    ```bash
    git clone https://github.com/Samallll/TimesZone.git
    ```

2. **Database Setup:**
    - Create a MySQL database and update the `application.properties` file with your database configurations.

3. **Run the Application:**
    - Execute the main class: `src/main/java/com/example/TimesZone/TimesZoneApplication.java`

4. **Access the Application:**
    - Open your web browser and go to `http://localhost:8080`

## Configuration
This project relies on an `application.properties` file to manage various settings and configurations. Before running the application, make sure to review and update this file with the appropriate values.

### Database Configuration
1. Create a MySQL database.
2. Open the `src/main/resources/application.properties` file.
3. Update the following properties with your database credentials:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/times
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

### Running the Application
After configuring the `application.properties` file, you're ready to run the application. Follow the steps in the [Getting Started](#getting-started) section to clone the repository and launch the application.

Feel free to reach out if you encounter any issues during the configuration process.

### User-side Features
- **Shop Products:**
    - Explore and shop from a diverse collection of watches.

- **Authentication:**
    - Create an account with email OTP validation or log in using email/password or OTP.

- **Password Management:**
    - Forgot password? Reset it via OTP sent to the registered email ID.
    - Change user details, including password, in the user profile section.

- **Wishlist and Referral:**
    - Add products to the wishlist for future purchases.
    - Share referral codes via email to earn rewards credited to the wallet.

- **Address Management:**
    - View, add, and delete addresses with soft delete functionality.

- **Order Management:**
    - View orders, download invoices, cancel, and return orders based on the order status.
    - Purchase products using available coupons for discounts.
    - Payment Methods: Choose between Cash on Delivery, Razorpay and Wallet for secure and convenient payments.

### Admin-side Features
- **Order Details Report:**
    - Download order details report for a specific time period in CSV and PDF formats.

- **User Management:**
    - Block users for administrative purposes.

- **Product Management:**
    - Add, edit, and remove products.
    - Add product images stored in the MySQL database.

- **Category Management:**
    - Add, edit, and manage categories and sub-categories.

- **Coupon Management:**
    - Add coupons and manage them based on conditions like minimum purchase amount and coupon usage count.

- **Offer Management:**
    - Make product and category offers with priority given to category offers. Offers are applied on specified dates and removed at specified dates (implemented using task scheduler).

### Learnings and Mistakes
This project is part of my learning journey with Spring Boot, and I acknowledge that there might be areas for improvement. Here are some of the lessons learned and identified mistakes:

1. **Database Design:**
    - Reflecting on the project, I realized that the initial database design could be optimized for better performance. Future iterations may include refinements in this area.

2. **Code Structure:**
    - The project's code structure might not adhere to the best practices. I'm actively working on understanding and implementing cleaner and more modular code.

3. **Frontend Styling:**
    - The frontend, built with HTML, CSS, and JavaScript, may lack some modern styling practices. Exploring and incorporating a front-end framework could enhance the visual appeal.

4. **Coding Standards:**
    - Adherence to coding standards is crucial for maintainability and collaboration. I've recognized instances where coding standards might not have been consistently followed, and I'm committed to improving this aspect.
    - ![SonarQube Quality Gate](src/main/resources/static/assets/img/quality_gate_passed_timeszone.svg)

5. **Secrets Handling:**
    - Managing secrets securely, especially during deployment, is paramount. In the current state, there might be room for improvement in how application secrets are handled. I'm exploring best practices and implementing enhanced security measures.

6. **Branching Implementation:**
    - While implementing features, I've learned the importance of effective branching strategies. In retrospect, there might have been more efficient ways to organize and manage branches during feature development. I aim to implement improved branching practices in future updates.

Your feedback and contributions are highly welcome! Feel free to open issues or pull requests to help enhance the overall quality of this project.

---
