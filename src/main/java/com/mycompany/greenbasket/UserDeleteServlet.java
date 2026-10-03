package com.mycompany.greenbasket;
import java.io.*;import jakarta.servlet.annotation.*;import jakarta.servlet.http.*;
@WebServlet("/UserDeleteServlet") public class UserDeleteServlet extends HttpServlet{protected void doGet(HttpServletRequest r,HttpServletResponse s)throws IOException{if(r.getSession().getAttribute("admin")==null){s.sendRedirect("AdminLogin.jsp");return;}try{new UserRegDAO().deleteUser(Integer.parseInt(r.getParameter("id")));}catch(Exception ignored){}s.sendRedirect(r.getContextPath()+"/AdminPanel.jsp");}}
