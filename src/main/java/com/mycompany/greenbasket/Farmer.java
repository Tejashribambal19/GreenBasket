package com.mycompany.greenbasket;

import java.io.Serializable;

public class Farmer implements Serializable {
    private int id;
    private String name;
    private String email;
    private String password;
    private String mob;
    private String gender;
    private String address;
    private String city;
    private String state;
    private String farmName;
    private String status = "ACTIVE";

    public int getId(){ return id; }
    public void setId(int v){ id=v; }

    public String getName(){ return name; }
    public void setName(String v){ name=v; }

    public String getEmail(){ return email; }
    public void setEmail(String v){ email=v; }

    public String getPassword(){ return password; }
    public void setPassword(String v){ password=v; }

    public String getMob(){ return mob; }
    public void setMob(String v){ mob=v; }

    public String getGender(){ return gender; }
    public void setGender(String v){ gender=v; }

    public String getAddress(){ return address; }
    public void setAddress(String v){ address=v; }

    public String getCity(){ return city; }
    public void setCity(String v){ city=v; }

    public String getState(){ return state; }
    public void setState(String v){ state=v; }

    public String getFarmName(){ return farmName; }
    public void setFarmName(String v){ farmName=v; }

    public String getStatus(){ return status; }
    public void setStatus(String v){ status = (v == null || v.isBlank()) ? "ACTIVE" : v; }

    public boolean isSuspended(){ return "SUSPENDED".equalsIgnoreCase(status); }
}
