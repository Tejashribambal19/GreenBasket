package com.mycompany.greenbasket;
import java.io.*;import jakarta.servlet.*;import jakarta.servlet.annotation.*;import jakarta.servlet.http.*;
@WebServlet("/Userregservlet")
public class Userregservlet extends HttpServlet{
 protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{req.setCharacterEncoding("UTF-8");UserReg u=new UserReg();u.setName(req.getParameter("name"));u.setEmail(req.getParameter("email"));u.setPassword(req.getParameter("password"));u.setMob(req.getParameter("mob"));u.setGender(req.getParameter("gender"));u.setAdd(req.getParameter("add"));u.setCity(req.getParameter("city"));u.setState(req.getParameter("state"));if(new UserRegDAO().saveUserReg(u)){req.getSession().setAttribute("loggedUser",u);res.sendRedirect(req.getContextPath()+"/welcomeUser.jsp");}else{req.setAttribute("errorMessage","Registration failed. Email may already be registered.");req.getRequestDispatcher("/UserRegistration.jsp").forward(req,res);}}
}
