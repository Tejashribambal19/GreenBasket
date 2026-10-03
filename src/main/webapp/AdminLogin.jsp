
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Admin Login | GreenBasket</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/assets/css/greenbasket.css">
</head>

<body>

<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="page-shell">

    <div class="auth-layout">

        <!-- LEFT SIDE -->
        <div class="auth-story">

            <span class="eyebrow">
                Administration
            </span>

            <h2 style="margin-top: 18px;">
                Marketplace oversight, without the clutter.
            </h2>

            <p>
                Review users, farmers, verification requests,
                products and orders from one dashboard.
            </p>

        </div>


        <!-- LOGIN FORM -->
        <div class="auth-form">

            <h1>Admin Access</h1>

            <p class="sub">
                Authorized GreenBasket administration only.
            </p>


            <% if (request.getAttribute("errorMessage") != null) { %>

                <div class="message error">
                    <%= request.getAttribute("errorMessage") %>
                </div>

            <% } %>


            <form action="<%= request.getContextPath() %>/AdminLoginServlet"
                  method="post">

                <div class="field">
                    <label for="email">Admin Email</label>

                    <input
                        type="email"
                        id="email"
                        name="email"
                        placeholder="admin@greenbasket.com"
                        required>
                </div>


                <div class="field">
                    <label for="password">Password</label>

                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Enter admin password"
                        required>
                </div>


                <button
                    class="btn btn-primary"
                    type="submit">

                    Open Admin Panel →
                </button>

            </form>

        </div>

    </div>

</main>

<%@ include file="/WEB-INF/includes/footer.jspf" %>

</body>
</html>
