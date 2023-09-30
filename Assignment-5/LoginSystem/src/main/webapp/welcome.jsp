<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome JSP</title>
</head>
<body>
    <h1>Welcome <%= request.getParameter("user-name") %></h1>
</body>
</html>
