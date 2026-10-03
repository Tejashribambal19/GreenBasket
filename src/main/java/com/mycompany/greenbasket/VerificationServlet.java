package com.mycompany.greenbasket;
import java.io.*;import jakarta.servlet.annotation.*;import jakarta.servlet.http.*;
@WebServlet("/VerificationServlet") public class VerificationServlet extends HttpServlet{protected void doPost(HttpServletRequest r,HttpServletResponse s)throws IOException{Farmer f=(Farmer)r.getSession().getAttribute("loggedFarmer");if(f==null){s.sendRedirect("FarmerLogin.jsp");return;}new ProductDAO().submitVerification(f.getId(),r.getParameter("certificateNo"),r.getParameter("note"));s.sendRedirect(r.getContextPath()+"/welcomeFarmer.jsp?verification=1");}}
