package com.mycompany.greenbasket;
import java.math.BigDecimal;
public class Product {
    private int id, farmerId, categoryId, stock;
    private String categoryName, farmerName, farmName, name, description, unit, imageUrl;
    private BigDecimal price;
    private boolean active;
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getFarmerId(){return farmerId;} public void setFarmerId(int v){farmerId=v;}
    public int getCategoryId(){return categoryId;} public void setCategoryId(int v){categoryId=v;}
    public int getStock(){return stock;} public void setStock(int v){stock=v;}
    public String getCategoryName(){return categoryName;} public void setCategoryName(String v){categoryName=v;}
    public String getFarmerName(){return farmerName;} public void setFarmerName(String v){farmerName=v;}
    public String getFarmName(){return farmName;} public void setFarmName(String v){farmName=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getUnit(){return unit;} public void setUnit(String v){unit=v;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
