package com.mycompany.greenbasket;
import java.io.Serializable;
public class UserReg implements Serializable {
    private int id; private String name,email,password,mob,gender,add,city,state;
    public int getId(){return id;} public void setId(int id){this.id=id;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getMob(){return mob;} public void setMob(String v){mob=v;}
    public String getGender(){return gender;} public void setGender(String v){gender=v;}
    public String getAdd(){return add;} public void setAdd(String v){add=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public String getState(){return state;} public void setState(String v){state=v;}
}
