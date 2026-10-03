package com.mycompany.greenbasket;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/FarmerStatusServlet")
public class FarmerStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/AdminLogin.jsp");
            return;
        }

        try {
            int farmerId = Integer.parseInt(request.getParameter("farmerId"));
            String status = request.getParameter("status");
            if (!"ACTIVE".equalsIgnoreCase(status) && !"SUSPENDED".equalsIgnoreCase(status)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid farmer status.");
                return;
            }

            new FarmerDAO().setFarmerStatus(farmerId, status);
            response.sendRedirect(request.getContextPath() + "/AdminPanel.jsp#farmers");
        } catch (NumberFormatException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid farmer ID.");
        }
    }
}
