<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*,com.mycompany.greenbasket.*" %>
<%
UserReg fu=(UserReg)session.getAttribute("loggedUser");
if(fu==null){ response.sendRedirect("UserLogin.jsp"); return; }
int orderId=0;
try{ orderId=Integer.parseInt(request.getParameter("orderId")); }catch(Exception ignored){}
FeedbackDAO fdao=new FeedbackDAO();
if(orderId<=0 || !fdao.orderBelongsToUser(orderId,fu.getId())){
    response.sendRedirect("orders.jsp"); return;
}
List<String[]> items=fdao.getOrderItemsForFeedback(fu.getId(),orderId);
%>
<!doctype html>
<html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Rate & Feedback | GreenBasket</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/greenbasket.css"></head>
<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>
<main class="page-shell"><div class="container feedback-page">
    <div class="section-head">
        <div><span class="eyebrow">Your experience matters</span><h2 style="margin-top:14px">Rate products from order #<%=orderId%>.</h2><p>Share a review for other shoppers or send a complaint directly to the GreenBasket admin team.</p></div>
        <a class="btn btn-outline" href="orders.jsp">← Back to orders</a>
    </div>
    <% if(request.getParameter("saved")!=null){ %><div class="message success">Thank you. Your feedback has been saved.</div><% } %>
    <% if(request.getParameter("error")!=null){ %><div class="message error">Feedback could not be saved. Please choose a rating and write a comment.</div><% } %>

    <% if(items.isEmpty()){ %><div class="empty"><h3>No order items found.</h3></div><% } %>
    <div class="feedback-grid">
    <% for(String[] item:items){
        boolean existing=item[5]!=null;
        String rating=item[6]==null?"5":item[6];
        String type=item[7]==null?"REVIEW":item[7];
        String comment=item[8]==null?"":item[8];
    %>
      <article class="feedback-card">
        <div class="feedback-product-head">
          <div><span class="eyebrow"><%=existing?"Feedback saved":"Purchased product"%></span><h3><%=item[2]%></h3></div>
          <div class="feedback-order-meta"><strong><%=item[3]%></strong> × ₹<%=item[4]%></div>
        </div>
        <form method="post" action="FeedbackServlet">
          <input type="hidden" name="orderId" value="<%=orderId%>">
          <input type="hidden" name="orderItemId" value="<%=item[0]%>">
          <div class="field-grid">
            <div class="field">
              <label>Rating</label>
              <select name="rating" required>
                <% for(int n=5;n>=1;n--){ %><option value="<%=n%>" <%=String.valueOf(n).equals(rating)?"selected":""%>><%=n%> star<%=n==1?"":"s"%> <%=n==5?"— Excellent":n==4?"— Very good":n==3?"— Good":n==2?"— Fair":"— Poor"%></option><% } %>
              </select>
            </div>
            <div class="field">
              <label>Feedback type</label>
              <select name="feedbackType" required>
                <option value="REVIEW" <%=type.equals("REVIEW")?"selected":""%>>Product review</option>
                <option value="COMPLAINT" <%=type.equals("COMPLAINT")?"selected":""%>>Complaint / issue</option>
              </select>
            </div>
          </div>
          <div class="field"><label>Your feedback</label><textarea name="comment" maxlength="1000" required placeholder="Tell us about freshness, quality, packaging, delivery, or any issue..."><%=comment%></textarea></div>
          <div class="feedback-actions"><div class="star-preview" aria-hidden="true">★★★★★</div><button class="btn btn-primary" type="submit"><%=existing?"Update feedback":"Submit feedback"%></button></div>
        </form>
      </article>
    <% } %>
    </div>
</div></main>
<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body></html>
