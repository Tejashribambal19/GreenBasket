package com.mycompany.greenbasket;
import java.io.Serializable;
public class CartItem implements Serializable {
    private int productId, quantity;
    public CartItem(){} public CartItem(int p,int q){productId=p;quantity=q;}
    public int getProductId(){return productId;} public void setProductId(int v){productId=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
}
