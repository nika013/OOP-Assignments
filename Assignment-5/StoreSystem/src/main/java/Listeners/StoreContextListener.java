package Listeners;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
@WebListener
public class StoreContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/Store");
        dataSource.setUsername("nika13");
        dataSource.setPassword("Nikasql123!.");
        Connection connection;
        try {
            connection = dataSource.getConnection();
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM products");
//            while (resultSet.next()) {
//                String productId = resultSet.getString("productid");
//                System.out.println(productId);
//            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (connection != null) {
            ServletContext servletContext = servletContextEvent.getServletContext();
            servletContext.setAttribute("dbConnection", connection);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
