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

public class CartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super.doGet(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        ServletContext servletContext = httpServletRequest.getServletContext();
        Connection connection = (Connection) servletContext.getAttribute("dbConnection");
        ShoppingCart cart = (ShoppingCart) httpServletRequest.getSession().getAttribute("cart");
        String productId = (String) httpServletRequest.getParameter("productID");
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM products p WHERE p.productid = '" + productId + "';");
            ResultSet resultSet = statement.executeQuery();
            String name = null;
            String imageFile = null;
            int price = 0;
            while (resultSet.next()) {
                name = resultSet.getString("name");
                imageFile = resultSet.getString("imagefile");
                price = resultSet.getInt("price");
            }
            cart.addToCart(name, productId, price);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/shopping-cart.jsp");
        requestDispatcher.forward(httpServletRequest, httpServletResponse);
    }
}
