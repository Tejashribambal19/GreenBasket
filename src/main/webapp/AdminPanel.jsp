<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*,com.mycompany.greenbasket.*" %>
<%
if(session.getAttribute("admin")==null){
    response.sendRedirect("AdminLogin.jsp");
    return;
}
UserRegDAO aud=new UserRegDAO();
FarmerDAO afd=new FarmerDAO();
ProductDAO apd=new ProductDAO();
OrderDAO aod=new OrderDAO();
FeedbackDAO afb=new FeedbackDAO();

List<UserReg> users=aud.getAllUsers();
List<Farmer> farmers=afd.getAllFarmers();
List<String[]> verifications=apd.verificationRequests();
List<String[]> allOrders=aod.getAllOrders();
List<Feedback> allFeedback=afb.getAllFeedback();
%>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Admin Panel | GreenBasket</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/greenbasket.css">
</head>
<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="admin-shell">
    <section class="admin-top">
        <div>
            <h1>GreenBasket Control Room</h1>
            <p>Marketplace health, farmer verification and customer activity in one view.</p>
        </div>
        <a class="btn btn-gold" href="LogoutServlet">Sign out</a>
    </section>

    <div class="admin-stats">
        <div class="admin-stat"><strong><%=users.size()%></strong><span>Registered buyers</span></div>
        <div class="admin-stat"><strong><%=farmers.size()%></strong><span>Registered farmers</span></div>
        <div class="admin-stat"><strong><%=apd.countProducts()%></strong><span>Product listings</span></div>
        <div class="admin-stat"><strong><%=allOrders.size()%></strong><span>Orders</span></div>
        <div class="admin-stat"><strong><%=allFeedback.size()%></strong><span>Feedback</span></div>
    </div>

    <section class="panel">
        <div class="panel-head"><h2>Farmer verification</h2></div>
        <div class="table-wrap">
            <table class="data-table">
                <thead>
                    <tr><th>Farmer</th><th>Farm</th><th>Certificate</th><th>Note</th><th>Status</th><th>Review</th></tr>
                </thead>
                <tbody>
                <% if(verifications.isEmpty()){ %>
                    <tr><td colspan="6">No verification submissions yet.</td></tr>
                <% } %>
                <% for(String[] v:verifications){ %>
                    <tr>
                        <td><%=v[1]%></td>
                        <td><%=v[2]%></td>
                        <td><%=v[3]%></td>
                        <td><%=v[4]%></td>
                        <td><span class="status <%=v[5]%>"><%=v[5]%></span></td>
                        <td>
                            <div class="small-actions">
                                <a class="btn btn-soft" href="AdminVerificationServlet?farmerId=<%=v[0]%>&status=APPROVED">Approve</a>
                                <a class="btn btn-danger" href="AdminVerificationServlet?farmerId=<%=v[0]%>&status=REJECTED">Reject</a>
                            </div>
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </section>

    <section class="panel">
        <div class="panel-head">
            <div>
                <h2>Registered buyers</h2>
                <p class="panel-subtitle">Buyer accounts are view-only from the admin dashboard.</p>
            </div>
        </div>
        <div class="table-wrap">
            <table class="data-table">
                <thead>
                    <tr><th>ID</th><th>Name</th><th>Email</th><th>Mobile</th><th>City</th><th>State</th></tr>
                </thead>
                <tbody>
                <% for(UserReg x:users){ %>
                    <tr>
                        <td><%=x.getId()%></td>
                        <td><%=x.getName()%></td>
                        <td><%=x.getEmail()%></td>
                        <td><%=x.getMob()%></td>
                        <td><%=x.getCity()%></td>
                        <td><%=x.getState()%></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </section>

    <section class="panel" id="farmers">
        <div class="panel-head">
            <div>
                <h2>Registered farmers</h2>
                <p class="panel-subtitle">Suspend a farmer to block login and hide that farmer's products from the marketplace.</p>
            </div>
        </div>
        <div class="table-wrap">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th><th>Farmer</th><th>Farm</th><th>Email</th><th>Mobile</th>
                        <th>City</th><th>State</th><th>Status</th><th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <% for(Farmer x:farmers){ %>
                    <tr>
                        <td><%=x.getId()%></td>
                        <td><%=x.getName()%></td>
                        <td><%=x.getFarmName()%></td>
                        <td><%=x.getEmail()%></td>
                        <td><%=x.getMob()%></td>
                        <td><%=x.getCity()%></td>
                        <td><%=x.getState()%></td>
                        <td><span class="status <%=x.getStatus()%>"><%=x.getStatus()%></span></td>
                        <td>
                            <% if(x.isSuspended()){ %>
                                <form method="post" action="FarmerStatusServlet" class="inline-form">
                                    <input type="hidden" name="farmerId" value="<%=x.getId()%>">
                                    <input type="hidden" name="status" value="ACTIVE">
                                    <button type="submit" class="btn btn-soft">Activate</button>
                                </form>
                            <% } else { %>
                                <form method="post" action="FarmerStatusServlet" class="inline-form"
                                      onsubmit="return confirm('Suspend <%=x.getName().replace("'", "\\'")%>? Their products will be hidden and they will not be able to log in.');">
                                    <input type="hidden" name="farmerId" value="<%=x.getId()%>">
                                    <input type="hidden" name="status" value="SUSPENDED">
                                    <button type="submit" class="btn btn-warning">Suspend</button>
                                </form>
                            <% } %>
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </section>

    <section class="panel" id="feedback">
        <div class="panel-head">
            <div><h2>Buyer feedback & complaints</h2><p class="panel-subtitle">Reviews help shoppers; complaints are visible here for admin follow-up.</p></div>
        </div>
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>Buyer</th><th>Order</th><th>Product</th><th>Farm</th><th>Type</th><th>Rating</th><th>Comment</th><th>Date</th></tr></thead>
                <tbody>
                <% if(allFeedback.isEmpty()){ %><tr><td colspan="8">No feedback submitted yet.</td></tr><% } %>
                <% for(Feedback fb:allFeedback){ %>
                    <tr>
                        <td><%=fb.getUserName()%></td><td>#<%=fb.getOrderId()%></td><td><%=fb.getProductName()%></td>
                        <td><%=fb.getFarmName()==null?"—":fb.getFarmName()%></td>
                        <td><span class="feedback-type <%=fb.getFeedbackType()%>"><%=fb.getFeedbackType()%></span></td>
                        <td><span class="rating-stars"><%="★".repeat(Math.max(0,Math.min(5,fb.getRating())))%></span> <%=fb.getRating()%>/5</td>
                        <td class="feedback-comment-cell"><%=fb.getComment()%></td><td><%=fb.getCreatedAt()%></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </section>

    <section class="panel">
        <div class="panel-head"><h2>Recent orders</h2></div>
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>Order</th><th>Buyer</th><th>Total</th><th>Status</th><th>Date</th></tr></thead>
                <tbody>
                <% if(allOrders.isEmpty()){ %>
                    <tr><td colspan="5">No orders yet.</td></tr>
                <% } %>
                <% for(String[] o:allOrders){ %>
                    <tr>
                        <td>#<%=o[0]%></td><td><%=o[1]%></td><td>₹<%=o[2]%></td>
                        <td><span class="status APPROVED"><%=o[3]%></span></td><td><%=o[4]%></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </section>
</main>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>
