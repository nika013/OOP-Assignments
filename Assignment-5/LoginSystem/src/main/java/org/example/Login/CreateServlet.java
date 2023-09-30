package LoginSystem.src.main.java.org.example.Login;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class CreateServlet extends HttpServlet {
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
            String againPage = respondAgain(servletContext.getRealPath("/create-page.html"), userName);
            httpServletResponse.setContentType("text/html");
            PrintWriter out = httpServletResponse.getWriter();
            out.println(againPage);
        } else {
            accountManager.addAccount(userName, password);
            RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher("welcome.jsp");
            dispatcher.forward(httpServletRequest, httpServletResponse);
        }
    }

    private String respondAgain(String htmlFilePath, String name) {
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
        modifiedHtml = modifiedHtml.replace("<h1>Create New Acount</h1>",
                "<h1>The name " + name + " is already in use</h1>");
        return modifiedHtml;
    }
}
