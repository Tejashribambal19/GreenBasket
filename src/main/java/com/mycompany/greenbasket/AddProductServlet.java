package com.mycompany.greenbasket;

import java.io.*;
import java.math.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

@WebServlet("/AddProductServlet")
public class AddProductServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest r,HttpServletResponse s)
            throws ServletException,IOException {

        Farmer sessionFarmer=(Farmer)r.getSession().getAttribute("loggedFarmer");
        if(sessionFarmer==null){
            s.sendRedirect(r.getContextPath()+"/FarmerLogin.jsp");
            return;
        }

        Farmer currentFarmer = new FarmerDAO().getFarmerById(sessionFarmer.getId());
        if(currentFarmer == null || currentFarmer.isSuspended()){
            r.getSession().removeAttribute("loggedFarmer");
            r.setAttribute("errorMessage","This farmer account is suspended and cannot add products.");
            r.getRequestDispatcher("/FarmerLogin.jsp").forward(r,s);
            return;
        }

        try{
            Product p=new Product();
            p.setFarmerId(currentFarmer.getId());
            p.setCategoryId(Integer.parseInt(r.getParameter("categoryId")));
            p.setName(r.getParameter("name"));
            p.setDescription(r.getParameter("description"));
            p.setPrice(new BigDecimal(r.getParameter("price")));
            p.setStock(Integer.parseInt(r.getParameter("stock")));
            p.setUnit(r.getParameter("unit"));
            p.setImageUrl(r.getParameter("imageUrl"));

            if(new ProductDAO().save(p)){
                s.sendRedirect(r.getContextPath()+"/welcomeFarmer.jsp?added=1");
            } else {
                throw new Exception();
            }
        }catch(Exception e){
            r.setAttribute("errorMessage","Please check product details and try again.");
            r.getRequestDispatcher("/AddProduct.jsp").forward(r,s);
        }
    }
}
