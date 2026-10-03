package com.mycompany.greenbasket;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response)
            throws ServletException,IOException {
        UserReg user=(UserReg)request.getSession().getAttribute("loggedUser");
        if(user==null){ response.sendRedirect("UserLogin.jsp"); return; }
        try{
            int orderId=Integer.parseInt(request.getParameter("orderId"));
            int orderItemId=Integer.parseInt(request.getParameter("orderItemId"));
            int rating=Integer.parseInt(request.getParameter("rating"));
            String type=request.getParameter("feedbackType");
            String comment=request.getParameter("comment");

            FeedbackDAO dao=new FeedbackDAO();
            boolean ok=dao.saveFeedback(user.getId(),orderId,orderItemId,rating,type,comment);
            if(ok){
                response.sendRedirect(request.getContextPath()+"/feedback.jsp?orderId="+orderId+"&saved=1");
            }else{
                response.sendRedirect(request.getContextPath()+"/feedback.jsp?orderId="+orderId+"&error=1");
            }
        }catch(Exception e){
            response.sendRedirect(request.getContextPath()+"/orders.jsp?feedbackError=1");
        }
    }
}
