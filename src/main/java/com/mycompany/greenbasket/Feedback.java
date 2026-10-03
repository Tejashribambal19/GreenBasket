package com.mycompany.greenbasket;

public class Feedback {
    private int id;
    private int userId;
    private int orderId;
    private int orderItemId;
    private int productId;
    private String userName;
    private String productName;
    private String farmerName;
    private String farmName;
    private int rating;
    private String feedbackType;
    private String comment;
    private String createdAt;

    public int getId(){ return id; }
    public void setId(int id){ this.id=id; }
    public int getUserId(){ return userId; }
    public void setUserId(int userId){ this.userId=userId; }
    public int getOrderId(){ return orderId; }
    public void setOrderId(int orderId){ this.orderId=orderId; }
    public int getOrderItemId(){ return orderItemId; }
    public void setOrderItemId(int orderItemId){ this.orderItemId=orderItemId; }
    public int getProductId(){ return productId; }
    public void setProductId(int productId){ this.productId=productId; }
    public String getUserName(){ return userName; }
    public void setUserName(String userName){ this.userName=userName; }
    public String getProductName(){ return productName; }
    public void setProductName(String productName){ this.productName=productName; }
    public String getFarmerName(){ return farmerName; }
    public void setFarmerName(String farmerName){ this.farmerName=farmerName; }
    public String getFarmName(){ return farmName; }
    public void setFarmName(String farmName){ this.farmName=farmName; }
    public int getRating(){ return rating; }
    public void setRating(int rating){ this.rating=rating; }
    public String getFeedbackType(){ return feedbackType; }
    public void setFeedbackType(String feedbackType){ this.feedbackType=feedbackType; }
    public String getComment(){ return comment; }
    public void setComment(String comment){ this.comment=comment; }
    public String getCreatedAt(){ return createdAt; }
    public void setCreatedAt(String createdAt){ this.createdAt=createdAt; }
}
