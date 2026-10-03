package com.mycompany.greenbasket;

import java.sql.*;
import java.util.*;

public class FarmerDAO {

    public FarmerDAO() {
        ensureStatusColumn();
    }

    private void ensureStatusColumn() {
        try (Connection c = DBConnection.getConnection()) {
            boolean exists = false;
            DatabaseMetaData meta = c.getMetaData();
            try (ResultSet rs = meta.getColumns(c.getCatalog(), null, "farmers", "status")) {
                exists = rs.next();
            }
            if (!exists) {
                try (Statement st = c.createStatement()) {
                    st.executeUpdate("ALTER TABLE farmers ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'");
                }
            }
        } catch (Exception e) {
            System.err.println("Could not ensure farmers.status column: " + e.getMessage());
        }
    }

    public boolean saveFarmer(Farmer f) {
        String q = "INSERT INTO farmers(name,email,password,mob,gender,address,city,state,farm_name,status) VALUES(?,?,?,?,?,?,?,?,?,'ACTIVE')";
        try (Connection c=DBConnection.getConnection();
             PreparedStatement p=c.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1,f.getName()); p.setString(2,f.getEmail()); p.setString(3,f.getPassword());
            p.setString(4,f.getMob()); p.setString(5,f.getGender()); p.setString(6,f.getAddress());
            p.setString(7,f.getCity()); p.setString(8,f.getState()); p.setString(9,f.getFarmName());
            if(p.executeUpdate()>0){
                try(ResultSet r=p.getGeneratedKeys()){ if(r.next()) f.setId(r.getInt(1)); }
                f.setStatus("ACTIVE");
                return true;
            }
        } catch(Exception e){ e.printStackTrace(); }
        return false;
    }

    public Farmer loginFarmer(String email,String password) {
        try(Connection c=DBConnection.getConnection();
            PreparedStatement p=c.prepareStatement("SELECT * FROM farmers WHERE email=? AND password=? LIMIT 1")) {
            p.setString(1,email); p.setString(2,password);
            try(ResultSet r=p.executeQuery()){ if(r.next()) return map(r); }
        } catch(Exception e){ e.printStackTrace(); }
        return null;
    }

    public List<Farmer> getAllFarmers() {
        List<Farmer> l=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();
            PreparedStatement p=c.prepareStatement("SELECT * FROM farmers ORDER BY id DESC");
            ResultSet r=p.executeQuery()) {
            while(r.next()) l.add(map(r));
        } catch(Exception e){ e.printStackTrace(); }
        return l;
    }

    public Farmer getFarmerById(int id) {
        try(Connection c=DBConnection.getConnection();
            PreparedStatement p=c.prepareStatement("SELECT * FROM farmers WHERE id=?")) {
            p.setInt(1,id);
            try(ResultSet r=p.executeQuery()){ if(r.next()) return map(r); }
        } catch(Exception e){ e.printStackTrace(); }
        return null;
    }

    public boolean updateFarmer(Farmer f) {
        String q="UPDATE farmers SET name=?,email=?,password=?,mob=?,gender=?,address=?,city=?,state=?,farm_name=? WHERE id=?";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)) {
            p.setString(1,f.getName()); p.setString(2,f.getEmail()); p.setString(3,f.getPassword());
            p.setString(4,f.getMob()); p.setString(5,f.getGender()); p.setString(6,f.getAddress());
            p.setString(7,f.getCity()); p.setString(8,f.getState()); p.setString(9,f.getFarmName());
            p.setInt(10,f.getId());
            return p.executeUpdate()>0;
        } catch(Exception e){ e.printStackTrace(); return false; }
    }

    public boolean setFarmerStatus(int farmerId, String status) {
        String normalized = "SUSPENDED".equalsIgnoreCase(status) ? "SUSPENDED" : "ACTIVE";
        try(Connection c=DBConnection.getConnection();
            PreparedStatement p=c.prepareStatement("UPDATE farmers SET status=? WHERE id=?")) {
            p.setString(1, normalized);
            p.setInt(2, farmerId);
            return p.executeUpdate()>0;
        } catch(Exception e){ e.printStackTrace(); return false; }
    }

    private Farmer map(ResultSet r)throws SQLException {
        Farmer f=new Farmer();
        f.setId(r.getInt("id"));
        f.setName(r.getString("name"));
        f.setEmail(r.getString("email"));
        f.setPassword(r.getString("password"));
        f.setMob(r.getString("mob"));
        f.setGender(r.getString("gender"));
        f.setAddress(r.getString("address"));
        f.setCity(r.getString("city"));
        f.setState(r.getString("state"));
        f.setFarmName(r.getString("farm_name"));
        try { f.setStatus(r.getString("status")); } catch(SQLException ignored) { f.setStatus("ACTIVE"); }
        return f;
    }
}
