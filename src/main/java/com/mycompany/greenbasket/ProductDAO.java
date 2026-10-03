package com.mycompany.greenbasket;

import java.sql.*;
import java.util.*;

public class ProductDAO {

    public ProductDAO(){
        ensureSchema();
        DemoDataSeeder.seedIfNeeded();
    }

    private void ensureSchema(){
        String[] sql={
            "CREATE TABLE IF NOT EXISTS categories(id INT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(100) NOT NULL UNIQUE,icon VARCHAR(20) DEFAULT '🌿')",
            "CREATE TABLE IF NOT EXISTS products(id INT AUTO_INCREMENT PRIMARY KEY,farmer_id INT NOT NULL,category_id INT NOT NULL,name VARCHAR(140) NOT NULL,description TEXT,price DECIMAL(10,2) NOT NULL,stock INT NOT NULL DEFAULT 0,unit VARCHAR(30) NOT NULL DEFAULT 'kg',image_url VARCHAR(500),active BOOLEAN NOT NULL DEFAULT TRUE,created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,INDEX(farmer_id),INDEX(category_id))",
            "CREATE TABLE IF NOT EXISTS farmer_verification(farmer_id INT PRIMARY KEY,certificate_no VARCHAR(120),note VARCHAR(500),status VARCHAR(20) NOT NULL DEFAULT 'PENDING',submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)",
            "CREATE TABLE IF NOT EXISTS orders(id INT AUTO_INCREMENT PRIMARY KEY,user_id INT NOT NULL,total_amount DECIMAL(10,2) NOT NULL,status VARCHAR(30) NOT NULL DEFAULT 'PLACED',delivery_address VARCHAR(500) NOT NULL,created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,INDEX(user_id))",
            "CREATE TABLE IF NOT EXISTS order_items(id INT AUTO_INCREMENT PRIMARY KEY,order_id INT NOT NULL,product_id INT NOT NULL,product_name VARCHAR(140) NOT NULL,quantity INT NOT NULL,price DECIMAL(10,2) NOT NULL)",
            "CREATE TABLE IF NOT EXISTS feedback(id INT AUTO_INCREMENT PRIMARY KEY,user_id INT NOT NULL,order_id INT NOT NULL,order_item_id INT NOT NULL,product_id INT NOT NULL,rating INT NOT NULL,feedback_type VARCHAR(20) NOT NULL DEFAULT 'REVIEW',comment VARCHAR(1000) NOT NULL,created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,UNIQUE KEY uq_feedback_user_item(user_id,order_item_id),INDEX(product_id),INDEX(order_id),INDEX(user_id))"
        };

        String[] names={
            "Vegetables","Fruits","Grains & Rice","Pulses & Beans","Spices & Herbs",
            "Dairy & Eggs","Nuts & Seeds","Oils & Ghee","Honey & Natural Sweeteners"
        };
        String[] icons={"🥬","🍎","🌾","🫘","🌿","🥛","🥜","🫙","🍯"};

        try(Connection c=DBConnection.getConnection(); Statement s=c.createStatement()){
            for(String q:sql) s.execute(q);

            ensureFarmerStatusColumn(c);

            try(PreparedStatement p=c.prepareStatement("INSERT IGNORE INTO categories(name,icon) VALUES(?,?)")){
                for(int i=0;i<names.length;i++){
                    p.setString(1,names[i]);
                    p.setString(2,icons[i]);
                    p.addBatch();
                }
                p.executeBatch();
            }
        }catch(Exception e){ e.printStackTrace(); }
    }

    private void ensureFarmerStatusColumn(Connection c) {
        try {
            boolean exists = false;
            DatabaseMetaData meta = c.getMetaData();
            try(ResultSet rs = meta.getColumns(c.getCatalog(), null, "farmers", "status")) {
                exists = rs.next();
            }
            if(!exists) {
                try(Statement st = c.createStatement()) {
                    st.executeUpdate("ALTER TABLE farmers ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'");
                }
            }
        } catch(Exception e) {
            System.err.println("Could not ensure farmers.status column: " + e.getMessage());
        }
    }

    public List<Category> getCategories(){
        List<Category> l=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();
            PreparedStatement p=c.prepareStatement("SELECT * FROM categories ORDER BY id");
            ResultSet r=p.executeQuery()){
            while(r.next()) l.add(new Category(r.getInt("id"),r.getString("name"),r.getString("icon")));
        }catch(Exception e){e.printStackTrace();}
        return l;
    }

    public boolean save(Product x){
        String q="INSERT INTO products(farmer_id,category_id,name,description,price,stock,unit,image_url,active) VALUES(?,?,?,?,?,?,?,?,1)";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){
            p.setInt(1,x.getFarmerId()); p.setInt(2,x.getCategoryId()); p.setString(3,x.getName());
            p.setString(4,x.getDescription()); p.setBigDecimal(5,x.getPrice()); p.setInt(6,x.getStock());
            p.setString(7,x.getUnit()); p.setString(8,x.getImageUrl());
            return p.executeUpdate()>0;
        }catch(Exception e){e.printStackTrace();return false;}
    }

    public List<Product> getAllActive(Integer category,String search){
        StringBuilder q=new StringBuilder(
            "SELECT p.*,c.name category_name,f.name farmer_name,f.farm_name " +
            "FROM products p JOIN categories c ON c.id=p.category_id " +
            "JOIN farmers f ON f.id=p.farmer_id " +
            "WHERE p.active=1 AND COALESCE(f.status,'ACTIVE')='ACTIVE'"
        );
        List<Object>a=new ArrayList<>();

        if(category!=null&&category>0){
            q.append(" AND p.category_id=?");
            a.add(category);
        }
        if(search!=null&&!search.isBlank()){
            q.append(" AND (p.name LIKE ? OR p.description LIKE ? OR f.farm_name LIKE ?)");
            String s="%"+search.trim()+"%";
            a.add(s); a.add(s); a.add(s);
        }
        q.append(" ORDER BY p.id DESC");
        return list(q.toString(),a);
    }

    public List<Product> getByFarmer(int id){
        return list(
            "SELECT p.*,c.name category_name,f.name farmer_name,f.farm_name " +
            "FROM products p JOIN categories c ON c.id=p.category_id JOIN farmers f ON f.id=p.farmer_id " +
            "WHERE p.farmer_id=? ORDER BY p.id DESC",
            List.of(id)
        );
    }

    public Product getById(int id){
        List<Product> l=list(
            "SELECT p.*,c.name category_name,f.name farmer_name,f.farm_name " +
            "FROM products p JOIN categories c ON c.id=p.category_id JOIN farmers f ON f.id=p.farmer_id " +
            "WHERE p.id=?",
            List.of(id)
        );
        return l.isEmpty()?null:l.get(0);
    }

    public boolean delete(int id,int farmerId){
        try(Connection c=DBConnection.getConnection();
            PreparedStatement p=c.prepareStatement("DELETE FROM products WHERE id=? AND farmer_id=?")){
            p.setInt(1,id); p.setInt(2,farmerId);
            return p.executeUpdate()>0;
        }catch(Exception e){e.printStackTrace();return false;}
    }

    public int countProducts(){
        try(Connection c=DBConnection.getConnection();
            Statement s=c.createStatement();
            ResultSet r=s.executeQuery("SELECT COUNT(*) FROM products")){
            return r.next()?r.getInt(1):0;
        }catch(Exception e){return 0;}
    }

    private List<Product> list(String q,List<Object>a){
        List<Product> l=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){
            for(int i=0;i<a.size();i++)p.setObject(i+1,a.get(i));
            try(ResultSet r=p.executeQuery()){
                while(r.next()) l.add(map(r));
            }
        }catch(Exception e){e.printStackTrace();}
        return l;
    }

    private Product map(ResultSet r)throws SQLException{
        Product x=new Product();
        x.setId(r.getInt("id"));
        x.setFarmerId(r.getInt("farmer_id"));
        x.setCategoryId(r.getInt("category_id"));
        x.setCategoryName(r.getString("category_name"));
        x.setFarmerName(r.getString("farmer_name"));
        x.setFarmName(r.getString("farm_name"));
        x.setName(r.getString("name"));
        x.setDescription(r.getString("description"));
        x.setPrice(r.getBigDecimal("price"));
        x.setStock(r.getInt("stock"));
        x.setUnit(r.getString("unit"));
        x.setImageUrl(r.getString("image_url"));
        x.setActive(r.getBoolean("active"));
        return x;
    }

    public String verificationStatus(int farmerId){
        try(Connection c=DBConnection.getConnection();
            PreparedStatement p=c.prepareStatement("SELECT status FROM farmer_verification WHERE farmer_id=?")){
            p.setInt(1,farmerId);
            try(ResultSet r=p.executeQuery()){ return r.next()?r.getString(1):"NOT_SUBMITTED"; }
        }catch(Exception e){ return "NOT_SUBMITTED"; }
    }

    public boolean submitVerification(int farmerId,String cert,String note){
        String q="INSERT INTO farmer_verification(farmer_id,certificate_no,note,status) VALUES(?,?,?,'PENDING') " +
                 "ON DUPLICATE KEY UPDATE certificate_no=VALUES(certificate_no),note=VALUES(note),status='PENDING'";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){
            p.setInt(1,farmerId); p.setString(2,cert); p.setString(3,note);
            return p.executeUpdate()>0;
        }catch(Exception e){e.printStackTrace();return false;}
    }

    public List<String[]> verificationRequests(){
        List<String[]>l=new ArrayList<>();
        String q="SELECT v.farmer_id,f.name,f.farm_name,v.certificate_no,v.note,v.status " +
                 "FROM farmer_verification v JOIN farmers f ON f.id=v.farmer_id ORDER BY v.updated_at DESC";
        try(Connection c=DBConnection.getConnection();Statement s=c.createStatement();ResultSet r=s.executeQuery(q)){
            while(r.next()) l.add(new String[]{
                r.getString(1),r.getString(2),r.getString(3),
                r.getString(4),r.getString(5),r.getString(6)
            });
        }catch(Exception e){e.printStackTrace();}
        return l;
    }

    public boolean setVerification(int farmerId,String status){
        try(Connection c=DBConnection.getConnection();
            PreparedStatement p=c.prepareStatement("UPDATE farmer_verification SET status=? WHERE farmer_id=?")){
            p.setString(1,status); p.setInt(2,farmerId);
            return p.executeUpdate()>0;
        }catch(Exception e){return false;}
    }
}
