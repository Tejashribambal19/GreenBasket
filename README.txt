GREENBASKET PROFESSIONAL v5
===========================

Requirements
- Java 17
- Apache Tomcat 10.1+
- MySQL running locally
- Maven / NetBeans Maven support

Database
- Database: greenbasket
- Username: root
- Password: root

Run
1. Extract this ZIP to a normal user folder (for example C:\Users\ASUS\Downloads or NetBeansProjects).
2. Open this folder as a Maven project in NetBeans.
3. Make sure MySQL is running and your existing greenbasket database is available.
4. Clean and Build.
5. Run on Apache Tomcat 10.1+.
6. Open http://localhost:8080/greenbasket/

Existing data
Your existing userreg, farmers and manually added products are not deleted. GreenBasket extends the existing database and keeps your current records.

Admin login
Email: admin@greenbasket.com
Password: admin123

Demo buyer
Email: buyer@greenbasket.com
Password: buyer123

Demo farmer
Email: farmer@greenbasket.com
Password: farmer123

Admin changes
- Registered Buyers is view-only (no Update/Delete buttons).
- Registered Farmers has Status and Suspend / Activate controls.
- Suspended farmers cannot log in and their products are hidden from the shop.

Feedback flow
Buyer -> My Account -> Orders -> Rate / Feedback
- 1-5 star rating
- Product Review or Complaint / Issue
- Written comment
Admin -> Admin Panel -> Buyer feedback & complaints
Marketplace product cards show average rating and review count when reviews exist.

Product images
- v5 bundles local photorealistic product images inside the project.
- The 36 Indian showcase products no longer depend on LoremFlickr or any external image host.
- Oranges and BeetRoot are also recognized and mapped to local photos.
- Unknown products receive a category-appropriate local image where possible.
- Internet access is NOT required for the bundled catalog images.

See CHANGES-v5.txt for the update details.
