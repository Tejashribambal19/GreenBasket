package com.mycompany.greenbasket;

import java.sql.*;
import java.util.*;

public class FeedbackDAO {

    public FeedbackDAO(){ ensureSchema(); }

    private void ensureSchema(){
        String sql = "CREATE TABLE IF NOT EXISTS feedback(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "order_id INT NOT NULL," +
                "order_item_id INT NOT NULL," +
                "product_id INT NOT NULL," +
                "rating INT NOT NULL," +
                "feedback_type VARCHAR(20) NOT NULL DEFAULT 'REVIEW'," +
                "comment VARCHAR(1000) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE KEY uq_feedback_user_item(user_id,order_item_id)," +
                "INDEX(product_id),INDEX(order_id),INDEX(user_id))";
        try(Connection c=DBConnection.getConnection(); Statement s=c.createStatement()){
            s.execute(sql);
        }catch(Exception e){ e.printStackTrace(); }
    }

    public boolean orderBelongsToUser(int orderId,int userId){
        String q="SELECT 1 FROM orders WHERE id=? AND user_id=?";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(q)){
            p.setInt(1,orderId); p.setInt(2,userId);
            try(ResultSet r=p.executeQuery()){ return r.next(); }
        }catch(Exception e){ return false; }
    }

    /**
     * Returns: orderItemId, productId, productName, quantity, price, existingFeedbackId,
     * rating, type, comment.
     */
    public List<String[]> getOrderItemsForFeedback(int userId,int orderId){
        List<String[]> out=new ArrayList<>();
        String q="SELECT oi.id,oi.product_id,oi.product_name,oi.quantity,oi.price," +
                "fb.id,fb.rating,fb.feedback_type,fb.comment " +
                "FROM order_items oi JOIN orders o ON o.id=oi.order_id " +
                "LEFT JOIN feedback fb ON fb.order_item_id=oi.id AND fb.user_id=o.user_id " +
                "WHERE o.id=? AND o.user_id=? ORDER BY oi.id";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(q)){
            p.setInt(1,orderId); p.setInt(2,userId);
            try(ResultSet r=p.executeQuery()){
                while(r.next()){
                    out.add(new String[]{
                        r.getString(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),
                        r.getString(6),r.getString(7),r.getString(8),r.getString(9)
                    });
                }
            }
        }catch(Exception e){ e.printStackTrace(); }
        return out;
    }

    public boolean saveFeedback(int userId,int orderId,int orderItemId,int rating,String type,String comment){
        if(rating<1 || rating>5) return false;
        if(type==null || !(type.equals("REVIEW") || type.equals("COMPLAINT"))) type="REVIEW";
        if(comment==null || comment.trim().isEmpty()) return false;
        comment=comment.trim();
        if(comment.length()>1000) comment=comment.substring(0,1000);

        String productLookup="SELECT oi.product_id FROM order_items oi JOIN orders o ON o.id=oi.order_id " +
                "WHERE oi.id=? AND o.id=? AND o.user_id=?";
        String upsert="INSERT INTO feedback(user_id,order_id,order_item_id,product_id,rating,feedback_type,comment) " +
                "VALUES(?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE rating=VALUES(rating)," +
                "feedback_type=VALUES(feedback_type),comment=VALUES(comment),created_at=CURRENT_TIMESTAMP";
        try(Connection c=DBConnection.getConnection()){
            int productId;
            try(PreparedStatement p=c.prepareStatement(productLookup)){
                p.setInt(1,orderItemId); p.setInt(2,orderId); p.setInt(3,userId);
                try(ResultSet r=p.executeQuery()){
                    if(!r.next()) return false;
                    productId=r.getInt(1);
                }
            }
            try(PreparedStatement p=c.prepareStatement(upsert)){
                p.setInt(1,userId); p.setInt(2,orderId); p.setInt(3,orderItemId); p.setInt(4,productId);
                p.setInt(5,rating); p.setString(6,type); p.setString(7,comment);
                return p.executeUpdate()>0;
            }
        }catch(Exception e){ e.printStackTrace(); return false; }
    }

    public List<Feedback> getAllFeedback(){
        List<Feedback> out=new ArrayList<>();
        String q="SELECT fb.id,fb.user_id,fb.order_id,fb.order_item_id,fb.product_id," +
                "u.name buyer_name,oi.product_name,f.name farmer_name,f.farm_name," +
                "fb.rating,fb.feedback_type,fb.comment,fb.created_at " +
                "FROM feedback fb " +
                "JOIN userreg u ON u.id=fb.user_id " +
                "JOIN order_items oi ON oi.id=fb.order_item_id " +
                "LEFT JOIN products p ON p.id=fb.product_id " +
                "LEFT JOIN farmers f ON f.id=p.farmer_id " +
                "ORDER BY fb.created_at DESC,fb.id DESC";
        try(Connection c=DBConnection.getConnection(); Statement s=c.createStatement(); ResultSet r=s.executeQuery(q)){
            while(r.next()){
                Feedback x=new Feedback();
                x.setId(r.getInt("id")); x.setUserId(r.getInt("user_id")); x.setOrderId(r.getInt("order_id"));
                x.setOrderItemId(r.getInt("order_item_id")); x.setProductId(r.getInt("product_id"));
                x.setUserName(r.getString("buyer_name")); x.setProductName(r.getString("product_name"));
                x.setFarmerName(r.getString("farmer_name")); x.setFarmName(r.getString("farm_name"));
                x.setRating(r.getInt("rating")); x.setFeedbackType(r.getString("feedback_type"));
                x.setComment(r.getString("comment")); x.setCreatedAt(r.getString("created_at"));
                out.add(x);
            }
        }catch(Exception e){ e.printStackTrace(); }
        return out;
    }

    /** productId -> [averageRating, reviewCount] */
    public Map<Integer,String[]> getRatingSummary(){
        Map<Integer,String[]> out=new HashMap<>();
        String q="SELECT product_id,ROUND(AVG(rating),1),COUNT(*) FROM feedback " +
                "WHERE feedback_type='REVIEW' GROUP BY product_id";
        try(Connection c=DBConnection.getConnection(); Statement s=c.createStatement(); ResultSet r=s.executeQuery(q)){
            while(r.next()) out.put(r.getInt(1),new String[]{r.getString(2),r.getString(3)});
        }catch(Exception e){ e.printStackTrace(); }
        return out;
    }

    public int countFeedback(){
        try(Connection c=DBConnection.getConnection(); Statement s=c.createStatement(); ResultSet r=s.executeQuery("SELECT COUNT(*) FROM feedback")){
            return r.next()?r.getInt(1):0;
        }catch(Exception e){ return 0; }
    }
}
