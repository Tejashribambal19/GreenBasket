package com.mycompany.greenbasket;
public class Category {
    private int id; private String name; private String icon;
    public Category(){} public Category(int id,String name,String icon){this.id=id;this.name=name;this.icon=icon;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getIcon(){return icon;} public void setIcon(String v){icon=v;}
}
