package com.mycompany.greenbasket;
import java.io.*;import jakarta.servlet.*;import jakarta.servlet.annotation.*;import jakarta.servlet.http.*;
@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet{protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{if("admin@greenbasket.com".equals(req.getParameter("email"))&&"admin123".equals(req.getParameter("password"))){req.getSession().setAttribute("admin",req.getParameter("email"));res.sendRedirect(req.getContextPath()+"/AdminPanel.jsp");}else{req.setAttribute("errorMessage","Invalid admin email or password.");req.getRequestDispatcher("/AdminLogin.jsp").forward(req,res);}}}
