package com.mycompany.greenbasket;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

@WebServlet("/FarmerLoginServlet")
public class FarmerLoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse res)
            throws ServletException,IOException {

        Farmer f = new FarmerDAO().loginFarmer(
                req.getParameter("email"),
                req.getParameter("password"));

        if (f != null && f.isSuspended()) {
            req.setAttribute("errorMessage",
                    "This farmer account has been suspended by GreenBasket Admin. Please contact support.");
            req.getRequestDispatcher("/FarmerLogin.jsp").forward(req,res);
            return;
        }

        if(f != null){
            req.getSession().setAttribute("loggedFarmer",f);
            res.sendRedirect(req.getContextPath()+"/welcomeFarmer.jsp");
        } else {
            req.setAttribute("errorMessage","Invalid email or password.");
            req.getRequestDispatcher("/FarmerLogin.jsp").forward(req,res);
        }
    }
}
