package com.mycompany.greenbasket;
import java.io.*;import jakarta.servlet.annotation.*;import jakarta.servlet.http.*;
@WebServlet("/ContactServlet") public class ContactServlet extends HttpServlet{protected void doPost(HttpServletRequest r,HttpServletResponse s)throws IOException{s.sendRedirect(r.getContextPath()+"/contact.jsp?sent=1");}}
