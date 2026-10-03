package com.mycompany.greenbasket;
import java.io.*;import jakarta.servlet.*;import jakarta.servlet.annotation.*;import jakarta.servlet.http.*;
@WebServlet("/Userloginservlet")
public class Userloginservlet extends HttpServlet{protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{UserReg u=new UserRegDAO().loginUser(req.getParameter("email"),req.getParameter("password"));if(u!=null){req.getSession().setAttribute("loggedUser",u);res.sendRedirect(req.getContextPath()+"/welcomeUser.jsp");}else{req.setAttribute("errorMessage","Invalid email or password.");req.getRequestDispatcher("/UserLogin.jsp").forward(req,res);}}}
