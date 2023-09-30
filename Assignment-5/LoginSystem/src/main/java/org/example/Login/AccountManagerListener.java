package LoginSystem.src.main.java.org.example.Login;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
public class AccountManagerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        AccountManager accountManager = new AccountManager();
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("accountManager", accountManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
