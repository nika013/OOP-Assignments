package org.example;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

public class UpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super.doGet(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        ServletContext servletContext = httpServletRequest.getServletContext();
        Connection connection = (Connection) servletContext.getAttribute("dbConnection");
        ShoppingCart cart = (ShoppingCart) httpServletRequest.getSession().getAttribute("cart");

        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = httpServletRequest.getParameter(paramName);
                cart.updateCart(paramName, Integer.valueOf(paramValue));
        }

        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/shopping-cart.jsp");
        requestDispatcher.forward(httpServletRequest, httpServletResponse);
    }
}
