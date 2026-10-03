package com.mycompany.greenbasket;
import java.io.*;import jakarta.servlet.annotation.*;import jakarta.servlet.http.*;
@WebServlet("/DeleteProductServlet") public class DeleteProductServlet extends HttpServlet{protected void doGet(HttpServletRequest r,HttpServletResponse s)throws IOException{Farmer f=(Farmer)r.getSession().getAttribute("loggedFarmer");if(f!=null){try{new ProductDAO().delete(Integer.parseInt(r.getParameter("id")),f.getId());}catch(Exception ignored){}}s.sendRedirect(r.getContextPath()+"/welcomeFarmer.jsp");}}
