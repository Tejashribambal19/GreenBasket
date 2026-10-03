package com.mycompany.greenbasket;
import java.io.*;import jakarta.servlet.annotation.*;import jakarta.servlet.http.*;
@WebServlet("/LogoutServlet") public class LogoutServlet extends HttpServlet{protected void doGet(HttpServletRequest r,HttpServletResponse s)throws IOException{r.getSession().invalidate();s.sendRedirect(r.getContextPath()+"/home.jsp");}}
