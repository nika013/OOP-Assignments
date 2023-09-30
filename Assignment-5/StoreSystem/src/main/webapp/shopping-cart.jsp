<%@ page import="org.example.ShoppingCart" %>
<%@ page import="org.example.Product" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: nika
  Date: 13.06.23
  Time: 15:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        int total = cart.getTotal();
    %>
    <h1>
        Shopping Cart
    </h1>
    <form action="UpdateServlet" method="post">
        <ul>
            <%
                Map<String, Product> products = cart.getProducts();
                for (String id:
                        products.keySet()) {
                    Product product = products.get(id);
                    String name = product.getName();
                    String price = Integer.toString(product.getPrice());
                    String productId = product.getProductId();
                    String quantity = Integer.toString(product.getQuantity());
            %>
            <li>
                <input type="text" name="<%=productId%>" id="<%=productId%>" value="<%=quantity%>">
                <%=name%>, <%=price%>
            </li>
            <%
                }
            %>
        </ul>
        Total Amount: <%=Integer.toString(total)%>
        <input type="submit" value="Update Cart">
    </form>
    <a href="homepage.jsp">Continue Shopping</a>

</body>
</html>
