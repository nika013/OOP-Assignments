<%--
  Created by IntelliJ IDEA.
  User: nika
  Date: 13.06.23
  Time: 13:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.dbcp2.BasicDataSource" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="javax.servlet.ServletContext" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.PreparedStatement" %>
<html>
<head>
    <title>Store</title>
</head>
<body>
    <h1>Student Store</h1>
    <p>Items avalaible: </p>
    <ul>
        <%
            ServletContext servletContext = (ServletContext) request.getServletContext();
            Connection connection = (Connection) servletContext.getAttribute("dbConnection");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM products;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String id = resultSet.getString("productid");
        %>
        <li><a href="show-product.jsp?id=<%=id%>" ><%= name %></a></li>
        <%
            }
            resultSet.close();
            statement.close();
        %>
    </ul>
</body>
</html>
