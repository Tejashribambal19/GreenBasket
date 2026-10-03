package com.mycompany.greenbasket;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Adds a professional Indian demo catalog without deleting or replacing
 * the user's existing buyer, farmer or product records.
 */
public final class DemoDataSeeder {
    private static final String SEED_KEY = "professional_indian_catalog_v5_local_photos";
    private static volatile boolean checkedThisJvm = false;

    private DemoDataSeeder() {}

    public static synchronized void seedIfNeeded() {
        if (checkedThisJvm) return;

        try (Connection con = DBConnection.getConnection()) {
            createSettingsTable(con);

            if (hasSeedMarker(con)) {
                checkedThisJvm = true;
                return;
            }

            int prakriti = ensureFarmer(con,
                    "Ananya Deshmukh", "farmer@greenbasket.com", "farmer123",
                    "9876543210", "Female", "Baner organic belt", "Pune", "Maharashtra",
                    "Prakriti Organic Farm");

            int sahyadri = ensureFarmer(con,
                    "Rohan Patil", "sahyadri@greenbasket.com", "farmer123",
                    "9876543211", "Male", "Trimbak farm road", "Nashik", "Maharashtra",
                    "Sahyadri Naturals");

            int aaranya = ensureFarmer(con,
                    "Meera Kulkarni", "aaranya@greenbasket.com", "farmer123",
                    "9876543212", "Female", "Katol countryside", "Nagpur", "Maharashtra",
                    "Aaranya Organics");

            ensureBuyer(con);

            ensureVerification(con, prakriti, "GB-DEMO-ORG-001");
            ensureVerification(con, sahyadri, "GB-DEMO-ORG-002");
            ensureVerification(con, aaranya, "GB-DEMO-ORG-003");

            seedProducts(con, prakriti, sahyadri, aaranya);

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO app_settings(setting_key, setting_value) VALUES(?, ?) " +
                    "ON DUPLICATE KEY UPDATE setting_value=VALUES(setting_value)")) {
                ps.setString(1, SEED_KEY);
                ps.setString(2, "seeded");
                ps.executeUpdate();
            }

            checkedThisJvm = true;
        } catch (Exception e) {
            System.err.println("GreenBasket Indian demo catalog seed skipped: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createSettingsTable(Connection con) throws Exception {
        try (Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS app_settings(" +
                    "setting_key VARCHAR(100) PRIMARY KEY," +
                    "setting_value VARCHAR(255)," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
        }
    }

    private static boolean hasSeedMarker(Connection con) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT setting_value FROM app_settings WHERE setting_key=?")) {
            ps.setString(1, SEED_KEY);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static int ensureFarmer(Connection con, String name, String email, String password,
            String mob, String gender, String address, String city, String state, String farmName)
            throws Exception {

        try (PreparedStatement find = con.prepareStatement("SELECT id FROM farmers WHERE email=?")) {
            find.setString(1, email);
            try (ResultSet rs = find.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        String sql = "INSERT INTO farmers(name,email,password,mob,gender,address,city,state,farm_name,status) " +
                "VALUES(?,?,?,?,?,?,?,?,?,'ACTIVE')";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, mob);
            ps.setString(5, gender);
            ps.setString(6, address);
            ps.setString(7, city);
            ps.setString(8, state);
            ps.setString(9, farmName);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new IllegalStateException("Could not create demo farmer: " + email);
    }

    private static void ensureBuyer(Connection con) throws Exception {
        try (PreparedStatement find = con.prepareStatement("SELECT id FROM userreg WHERE email=?")) {
            find.setString(1, "buyer@greenbasket.com");
            try (ResultSet rs = find.executeQuery()) {
                if (rs.next()) return;
            }
        }

        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO userreg(name,email,password,mob,gender,address,city,state) VALUES(?,?,?,?,?,?,?,?)")) {
            ps.setString(1, "Demo Buyer");
            ps.setString(2, "buyer@greenbasket.com");
            ps.setString(3, "buyer123");
            ps.setString(4, "9876500000");
            ps.setString(5, "Female");
            ps.setString(6, "GreenBasket demo address");
            ps.setString(7, "Nagpur");
            ps.setString(8, "Maharashtra");
            ps.executeUpdate();
        }
    }

    private static void ensureVerification(Connection con, int farmerId, String certificate) throws Exception {
        String sql = "INSERT INTO farmer_verification(farmer_id,certificate_no,note,status) " +
                "VALUES(?,?,?,'APPROVED') ON DUPLICATE KEY UPDATE " +
                "certificate_no=VALUES(certificate_no),note=VALUES(note),status='APPROVED'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, farmerId);
            ps.setString(2, certificate);
            ps.setString(3, "Verified GreenBasket demo farmer for the Indian showcase catalog.");
            ps.executeUpdate();
        }
    }

    private static int categoryId(Connection con, String categoryName) throws Exception {
        try (PreparedStatement ps = con.prepareStatement("SELECT id FROM categories WHERE name=?")) {
            ps.setString(1, categoryName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new IllegalStateException("Missing category: " + categoryName);
    }

    private static void seedProducts(Connection con, int prakriti, int sahyadri, int aaranya) throws Exception {
        addProduct(con, prakriti, categoryId(con, "Vegetables"), "Organic Tomatoes",
                "Juicy desi tomatoes, naturally ripened and harvested fresh for curries, salads and everyday cooking.",
                "45.00", 55, "kg", "assets/images/products/tomatoes.jpg");
        addProduct(con, sahyadri, categoryId(con, "Vegetables"), "Organic Potatoes",
                "Clean, firm potatoes grown with natural soil nutrition; ideal for sabzi, parathas and snacks.",
                "40.00", 70, "kg", "assets/images/products/potatoes.jpg");
        addProduct(con, aaranya, categoryId(con, "Vegetables"), "Organic Onions",
                "Fresh red onions with balanced sweetness and pungency, carefully sorted after harvest.",
                "38.00", 65, "kg", "assets/images/products/onions.jpg");
        addProduct(con, prakriti, categoryId(con, "Vegetables"), "Fresh Spinach",
                "Tender pesticide-free palak harvested in the morning and packed for same-day freshness.",
                "30.00", 45, "bunch", "assets/images/products/spinach.jpg");
        addProduct(con, sahyadri, categoryId(con, "Fruits"), "Alphonso Mangoes",
                "Naturally matured Ratnagiri-style Alphonso mangoes with rich aroma and smooth golden pulp.",
                "900.00", 24, "dozen", "assets/images/products/mangoes.jpg");
        addProduct(con, aaranya, categoryId(con, "Fruits"), "Organic Bananas",
                "Naturally ripened bananas with no artificial ripening agents; perfect for breakfast and snacks.",
                "70.00", 40, "dozen", "assets/images/products/bananas.jpg");
        addProduct(con, prakriti, categoryId(con, "Fruits"), "Farm Fresh Guava",
                "Crisp guavas with fragrant pink-white flesh, grown organically and picked at the right maturity.",
                "80.00", 42, "kg", "assets/images/products/guava.jpg");
        addProduct(con, sahyadri, categoryId(con, "Fruits"), "Organic Pomegranate",
                "Ruby-red anar with juicy arils and naturally sweet-tart flavour from carefully maintained orchards.",
                "180.00", 32, "kg", "assets/images/products/pomegranate.jpg");
        addProduct(con, aaranya, categoryId(con, "Grains & Rice"), "Traditional Brown Rice",
                "Whole-grain brown rice, minimally processed to retain its nutty aroma and natural bran.",
                "140.00", 60, "kg", "assets/images/products/brown-rice.jpg");
        addProduct(con, prakriti, categoryId(con, "Grains & Rice"), "Organic Basmati Rice",
                "Long-grain aromatic basmati rice, naturally aged for fluffy biryani, pulao and everyday meals.",
                "180.00", 58, "kg", "assets/images/products/basmati-rice.jpg");
        addProduct(con, sahyadri, categoryId(con, "Grains & Rice"), "Organic Jowar",
                "Cleaned sorghum grain suitable for bhakri, rotis and wholesome millet-based meals.",
                "80.00", 52, "kg", "assets/images/products/jowar.jpg");
        addProduct(con, aaranya, categoryId(con, "Grains & Rice"), "Pearl Millet (Bajra)",
                "Stone-cleaned bajra from low-input farms, ideal for bhakri, khichdi and traditional winter recipes.",
                "70.00", 50, "kg", "assets/images/products/bajra.jpg");
        addProduct(con, prakriti, categoryId(con, "Pulses & Beans"), "Unpolished Toor Dal",
                "Clean, unpolished pigeon pea dal with natural colour and comforting farm-fresh flavour.",
                "170.00", 48, "kg", "assets/images/products/toor-dal.jpg");
        addProduct(con, sahyadri, categoryId(con, "Pulses & Beans"), "Organic Moong Dal",
                "Light, naturally processed yellow moong dal for khichdi, dal tadka and everyday protein-rich meals.",
                "150.00", 46, "kg", "assets/images/products/moong-dal.jpg");
        addProduct(con, aaranya, categoryId(con, "Pulses & Beans"), "Organic Chana Dal",
                "Nutty split Bengal gram, naturally grown and cleaned without artificial polishing.",
                "110.00", 50, "kg", "assets/images/products/chana-dal.jpg");
        addProduct(con, prakriti, categoryId(con, "Pulses & Beans"), "Red Rajma",
                "Premium kidney beans selected for consistent size and creamy texture in rajma masala.",
                "180.00", 35, "kg", "assets/images/products/rajma.jpg");
        addProduct(con, sahyadri, categoryId(con, "Spices & Herbs"), "Organic Turmeric Powder",
                "Deep golden haldi ground in small batches with no artificial colour, fillers or preservatives.",
                "120.00", 30, "250 g", "assets/images/products/turmeric.jpg");
        addProduct(con, aaranya, categoryId(con, "Spices & Herbs"), "Organic Red Chilli Powder",
                "Bold lal mirch powder with balanced heat and colour, stone-ground from naturally dried chillies.",
                "140.00", 28, "250 g", "assets/images/products/red-chilli.jpg");
        addProduct(con, prakriti, categoryId(con, "Spices & Herbs"), "Organic Coriander Powder",
                "Freshly ground dhania powder with warm citrus notes for curries, gravies and masalas.",
                "100.00", 34, "250 g", "assets/images/products/coriander-powder.jpg");
        addProduct(con, sahyadri, categoryId(con, "Spices & Herbs"), "Fresh Coriander",
                "Bright, fragrant dhania harvested fresh for chutneys, curries and garnishing.",
                "25.00", 50, "bunch", "assets/images/products/coriander.jpg");
        addProduct(con, aaranya, categoryId(con, "Dairy & Eggs"), "A2 Farm Milk",
                "Fresh cow milk collected in small batches and chilled quickly for daily household use.",
                "90.00", 30, "litre", "assets/images/products/milk.jpg");
        addProduct(con, prakriti, categoryId(con, "Dairy & Eggs"), "Organic Curd",
                "Thick farm-style dahi prepared from fresh milk with a mild natural tang.",
                "80.00", 24, "500 g", "assets/images/products/curd.jpg");
        addProduct(con, sahyadri, categoryId(con, "Dairy & Eggs"), "Fresh Paneer",
                "Soft handmade paneer prepared in small batches from fresh farm milk.",
                "220.00", 22, "500 g", "assets/images/products/paneer.jpg");
        addProduct(con, aaranya, categoryId(con, "Dairy & Eggs"), "Free-range Eggs",
                "Farm eggs from free-range hens, carefully packed in recyclable trays.",
                "120.00", 36, "dozen", "assets/images/products/eggs.jpg");
        addProduct(con, prakriti, categoryId(con, "Nuts & Seeds"), "Raw Almonds",
                "Crunchy badam packed without added salt, sugar or artificial flavouring.",
                "450.00", 24, "500 g", "assets/images/products/almonds.jpg");
        addProduct(con, sahyadri, categoryId(con, "Nuts & Seeds"), "Whole Cashews",
                "Premium kaju with a naturally creamy taste, ideal for snacking and festive cooking.",
                "520.00", 20, "500 g", "assets/images/products/cashews.jpg");
        addProduct(con, aaranya, categoryId(con, "Nuts & Seeds"), "Flax Seeds",
                "Cleaned alsi seeds with a mild nutty flavour for laddoos, chutneys and breakfast bowls.",
                "120.00", 30, "500 g", "assets/images/products/flax-seeds.jpg");
        addProduct(con, prakriti, categoryId(con, "Nuts & Seeds"), "White Sesame Seeds",
                "Naturally cleaned til seeds for chikki, laddoos, chutneys and traditional recipes.",
                "140.00", 28, "500 g", "assets/images/products/sesame.jpg");
        addProduct(con, sahyadri, categoryId(con, "Oils & Ghee"), "Cold-pressed Groundnut Oil",
                "Wood-pressed peanut oil with warm nutty aroma and no chemical refining.",
                "320.00", 26, "litre", "assets/images/products/groundnut-oil.jpg");
        addProduct(con, aaranya, categoryId(con, "Oils & Ghee"), "Cold-pressed Mustard Oil",
                "Kachi ghani-style mustard oil with robust aroma for Indian cooking and pickles.",
                "280.00", 25, "litre", "assets/images/products/mustard-oil.jpg");
        addProduct(con, prakriti, categoryId(con, "Oils & Ghee"), "Virgin Coconut Oil",
                "Cold-pressed coconut oil with a clean natural aroma for cooking and traditional uses.",
                "350.00", 20, "litre", "assets/images/products/coconut-oil.jpg");
        addProduct(con, sahyadri, categoryId(con, "Oils & Ghee"), "Cultured Cow Ghee",
                "Slow-made golden ghee prepared in traditional small batches for rich aroma and flavour.",
                "700.00", 20, "500 ml", "assets/images/products/ghee.jpg");
        addProduct(con, aaranya, categoryId(con, "Honey & Natural Sweeteners"), "Raw Forest Honey",
                "Unheated raw honey with floral notes, bottled in small batches from seasonal harvests.",
                "350.00", 26, "500 g", "assets/images/products/honey.jpg");
        addProduct(con, prakriti, categoryId(con, "Honey & Natural Sweeteners"), "Natural Jaggery Blocks",
                "Traditional unrefined gur with caramel-like taste, made without artificial colour.",
                "100.00", 40, "kg", "assets/images/products/jaggery.jpg");
        addProduct(con, sahyadri, categoryId(con, "Honey & Natural Sweeteners"), "Natural Date Syrup",
                "Smooth khajur syrup with naturally rich sweetness for milk, desserts and breakfast dishes.",
                "320.00", 22, "500 ml", "assets/images/products/date-syrup.jpg");
        addProduct(con, aaranya, categoryId(con, "Honey & Natural Sweeteners"), "Organic Coconut Sugar",
                "Natural coconut blossom sugar with mild caramel notes for baking and beverages.",
                "280.00", 20, "500 g", "assets/images/products/coconut-sugar.jpg");
    }

    private static void addProduct(Connection con, int farmerId, int categoryId, String name,
            String description, String price, int stock, String unit, String imageUrl) throws Exception {

        String localImageUrl = imageUrl;

        // Idempotent: a v3 showcase row is kept, but its old SVG is upgraded to
        // a real product-photo URL. A same-name product owned by another farmer
        // is treated as user data and is not modified.
        try (PreparedStatement find = con.prepareStatement(
                "SELECT id,farmer_id FROM products WHERE name=? LIMIT 1")) {
            find.setString(1, name);
            try (ResultSet rs = find.executeQuery()) {
                if (rs.next()) {
                    int id=rs.getInt("id");
                    if(rs.getInt("farmer_id")==farmerId){
                        try(PreparedStatement up=con.prepareStatement("UPDATE products SET image_url=? WHERE id=?")){
                            up.setString(1, localImageUrl);
                            up.setInt(2, id);
                            up.executeUpdate();
                        }
                    }
                    return;
                }
            }
        }

        String sql = "INSERT INTO products(farmer_id,category_id,name,description,price,stock,unit,image_url,active) " +
                "VALUES(?,?,?,?,?,?,?,?,1)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, farmerId);
            ps.setInt(2, categoryId);
            ps.setString(3, name);
            ps.setString(4, description);
            ps.setBigDecimal(5, new BigDecimal(price));
            ps.setInt(6, stock);
            ps.setString(7, unit);
            ps.setString(8, localImageUrl);
            ps.executeUpdate();
        }
    }

}
