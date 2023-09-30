package LoginSystem.src.main.java.org.example.Login;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        super.doGet(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        ServletContext servletContext = httpServletRequest.getServletContext();
        AccountManager accountManager = (AccountManager) servletContext.getAttribute("accountManager");
        String userName = (String) httpServletRequest.getParameter("user-name");
        String password = (String) httpServletRequest.getParameter("password");
        if (accountManager.isCorrect(userName, password)) {
            RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher("welcome.jsp");
            dispatcher.forward(httpServletRequest, httpServletResponse);
        } else {
            String againPage = respondAgain(servletContext.getRealPath("/homepage.html"));
            httpServletResponse.setContentType("text/html");
            PrintWriter out = httpServletResponse.getWriter();
            out.println(againPage);
        }
    }

    private String respondAgain(String htmlFilePath) {
        // Read the HTML file
        StringBuilder htmlContent;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(htmlFilePath));
            htmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String modifiedHtml = htmlContent.toString();
        modifiedHtml = modifiedHtml.replace("<h1>Welcome to Homework 5</h1>",
                "<h1>Please Try Again</h1>");
        modifiedHtml = modifiedHtml.replace("<p>Please log in</p>",
                "<p>Either your user-name or password was incorrect. Please Try Again.</p>");
        return modifiedHtml;
    }
}
