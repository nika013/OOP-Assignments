<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %><%--
  Created by IntelliJ IDEA.
  User: nika
  Date: 13.06.23
  Time: 14:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        String id = request.getParameter("id");
        Connection connection = (Connection) request.getServletContext().getAttribute("dbConnection");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM products p WHERE p.productid = '" + id + "';");
        ResultSet resultSet = statement.executeQuery();
        String name = null;
        String imageFile = null;
        int price = 0;
        while (resultSet.next()) {
            name = resultSet.getString("name");
            imageFile = resultSet.getString("imagefile");
            price = resultSet.getInt("price");
        }
    %>
    <h1><%=name%></h1>
    <img src="store-images/<%=imageFile%>"  alt="<%=name%>">
    <form action="CartServlet" method="post">
        <p>$<%=Integer.toString(price)%></p>
        <input name="productID" type="hidden" value="<%=id%>"/>
        <input type="submit" value="Add to Cart" name="Add to Cart">
    </form>

</body>
</html>
