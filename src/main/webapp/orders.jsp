<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*,com.mycompany.greenbasket.*" %>
<%
UserReg ou=(UserReg)session.getAttribute("loggedUser");
if(ou==null){ response.sendRedirect("UserLogin.jsp"); return; }
List<String[]> orders=new OrderDAO().getOrdersForUser(ou.getId());
%>
<!doctype html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>My Orders | GreenBasket</title><link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/greenbasket.css"></head><body>
<%@ include file="/WEB-INF/includes/header.jspf" %>
<main class="page-shell"><div class="container">
  <div class="section-head"><div><span class="eyebrow">Order history</span><h2 style="margin-top:14px">Your GreenBasket orders.</h2><p>Track purchases and share a rating, review or complaint for products you ordered.</p></div><a class="btn btn-primary" href="products.jsp">Shop again</a></div>
  <%if(request.getParameter("placed")!=null){%><div class="message success">Order #<%=request.getParameter("placed")%> was placed successfully.</div><%}%>
  <%if(request.getParameter("feedbackError")!=null){%><div class="message error">We could not open that feedback request.</div><%}%>
  <section class="panel">
  <%if(orders.isEmpty()){%><div class="empty"><p>You have not placed any orders yet.</p></div><%}else{%>
    <div class="table-wrap"><table class="data-table"><thead><tr><th>Order</th><th>Total</th><th>Status</th><th>Delivery address</th><th>Placed</th><th>Feedback</th></tr></thead><tbody>
    <%for(String[] o:orders){%><tr><td><strong>#<%=o[0]%></strong></td><td>₹<%=o[1]%></td><td><span class="status APPROVED"><%=o[2]%></span></td><td><%=o[3]%></td><td><%=o[4]%></td><td><a class="btn btn-soft btn-compact" href="feedback.jsp?orderId=<%=o[0]%>">★ Rate / Feedback</a></td></tr><%}%>
    </tbody></table></div>
  <%}%>
  </section>
</div></main>
<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body></html>
