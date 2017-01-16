
/**
 * 08-672 HW3
 * @author Luxiao Ding (luxiaod@andrew.cmu.edu)
 * 09/26/2016
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Favorites extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private FavoriteDAO favoriteDAO;
    private UserDAO userDAO;

    public void init() throws ServletException {
        String jdbcDriverName = getInitParameter("jdbcDriver");
        String jdbcURL = getInitParameter("jdbcURL");

        try {
            userDAO = new UserDAO(jdbcDriverName, jdbcURL, "luxiaod_user");
            favoriteDAO = new FavoriteDAO(jdbcDriverName, jdbcURL, "luxiaod_favorite");
        } catch (MyDAOException e) {
            throw new ServletException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            if (request.getParameter("RegisterPage") != null
                    && request.getParameter("RegisterPage").equals("RegisterPage")) {
                register(request, response);
                return;
            }
            if (request.getParameter("button") != null && request.getParameter("button").equals("Register")) {
                register(request, response);
                return;
            }
            login(request, response);
        } else {
            if (request.getParameter("button") != null && request.getParameter("button").equals("Logout")) {
                session.removeAttribute("user");
                LoginForm form = new LoginForm(request);
                outputLoginPage(response, form, null);
                return;
            }
            processAdd(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> errors = new ArrayList<String>();

        LoginForm form = new LoginForm(request);

        if (!form.isPresent()) {
            outputLoginPage(response, form, null);
            return;
        }

        errors.addAll(form.getValidationErrors());
        if (errors.size() != 0) {
            outputLoginPage(response, form, errors);
            return;
        }

        try {
            UserBean user;
            user = userDAO.read(form.getEmail());
            if (user == null) {
                errors.add("No such user");
                outputLoginPage(response, form, errors);
                return;
            }

            if (!form.getPassword().equals(user.getPassword())) {
                errors.add("Incorrect password");
                outputLoginPage(response, form, errors);
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            processAdd(request, response);
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            outputLoginPage(response, form, errors);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<String>();

        RegisterForm form = new RegisterForm(request);

        if (!form.isPresent()) {
            outputRegister(response, form, null);
            return;
        }

        errors.addAll(form.getValidationErrors());
        if (errors.size() != 0) {
            outputRegister(response, form, errors);
            return;
        }

        try {
            if (userDAO.read(form.getEmail()) == null) {
                UserBean user = new UserBean();
                user.setEmail(form.getEmail());
                user.setPassword(form.getPassword());
                user.setFirstName(form.getFirstName());
                user.setLastName(form.getLastName());
                userDAO.create(user);
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                processAdd(request, response);
            } else {
                errors.add("The email has already been used!");
                outputRegister(response, form, errors);
            }
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            outputRegister(response, form, errors);
        }

    }

    private void processAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<String>();

        ItemForm form = new ItemForm(request);

        if (!form.isPresent()) {
            outputSitesList(request, response);
            return;
        }

        errors.addAll(form.getValidationErrors());

        if (errors.size() > 0) {
            outputSitesList(request, response, errors);
            return;
        }

        try {
            FavoriteBean bean = new FavoriteBean();
            bean.setUrl(form.getUrl());
            bean.setComment(form.getComment());
            bean.setCount(0);
            UserBean ub = (UserBean) request.getSession().getAttribute("user");
            bean.setUserId(userDAO.read(ub.getEmail()).getUserId());
            favoriteDAO.create(bean);
            outputSitesList(request, response);
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            outputSitesList(request, response, errors);
        }
    }

    // Methods that generate & output HTML
    private void generateHead(PrintWriter out) {
        out.println("  <head>");
        out.println("    <meta charset=\"utf-8\"/>");
        out.println("    <title>Favorite Websites</title>");
        out.println("  </head>");
    }

    private void outputRegister(HttpServletResponse response, RegisterForm form, List<String> errors)
            throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        generateHead(out);

        out.println("<body>");
        out.println("<h2>Register</h2>");

        // Generate an HTML <form> to get data from the user
        out.println("<form method=\"POST\">");
        out.println("    <table>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">E-mail address:</td>");
        out.println("            <td><input type=\"text\" name=\"email\" /></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">First Name:</td>");
        out.println("            <td><input type=\"text\" name=\"firstName\" /></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">Last Name:</td>");
        out.println("            <td><input type=\"text\" name=\"lastName\" /></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">Password:</td>");
        out.println("            <td><input type=\"password\" name=\"password\" /></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td colspan=\"2\" style=\"text-align: center;\">");
        out.println("                <input type=\"submit\" name=\"button\" value=\"Register\" />");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("    </table>");
        out.println("</form>");
        if (errors != null && errors.size() > 0) {
            for (String error : errors) {
                out.println("<p style=\"font-size: large; color: red\">");
                out.println(error);
                out.println("</p>");
            }
        }
        out.println("</body>");
        out.println("</html>");
    }

    private void outputLoginPage(HttpServletResponse response, LoginForm form, List<String> errors) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        generateHead(out);

        out.println("<body>");
        out.println("<h2>Login</h2>");

        if (errors != null && errors.size() > 0) {
            for (String error : errors) {
                out.println("<p style=\"font-size: large; color: red\">");
                out.println(error);
                out.println("</p>");
            }
        }

        // Generate an HTML <form> to get data from the user
        out.println("<form method=\"POST\">");
        out.println("    <table>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">Email:</td>");
        out.println("            <td><input type=\"text\" name=\"email\"");
        if (form != null && form.getEmail() != null) {
            out.println("            value=\"" + form.getEmail() + "\"");
        }
        out.println("                />");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: x-large\">Password:</td>");
        out.println("            <td><input type=\"password\" name=\"password\" /></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td colspan=\"2\" style=\"text-align: center;\">");
        out.println("                <input type=\"submit\" name=\"button\" value=\"Login\" />");
        out.println(
                "                <a href=\"#\" onclick=\"document.getElementById('RegisterPage').submit();\">Register</a>");
        out.println("                <input type=\"hidden\" name=\"registerlink\" value=\"registerlink\" />");
        out.println("            </td>");
        out.println("        </tr>");
        out.println("    </table>");
        out.println("</form>");
        out.println("<form method=\"POST\" id=\"RegisterPage\">");
        out.println("<input type=\"hidden\" name=\"RegisterPage\" value=\"RegisterPage\" />");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }

    private void outputSitesList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Just call the version that takes a List passing an empty List
        List<String> list = new ArrayList<String>();
        outputSitesList(request, response, list);
    }

    private void outputSitesList(HttpServletRequest request, HttpServletResponse response, List<String> messages)
            throws IOException {
        // Get the list of items to display at the end
        FavoriteBean[] beans = null;
        UserBean u = (UserBean) request.getSession().getAttribute("user");
        String clickedSite = request.getParameter("ClickSite");
        if (clickedSite != null) {
            try {
                favoriteDAO.updateCount(Integer.parseInt(clickedSite));
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MyDAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            u = userDAO.read(u.getEmail());
            beans = favoriteDAO.getFavorites(u.getUserId());
        } catch (MyDAOException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to get favorite sites list!");
            e.printStackTrace();
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        generateHead(out);

        out.println("<body>");
        out.println("<h2>Favorites for " + u.getFirstName() + " " + u.getLastName() + "</h2>");

        // Generate an HTML <form> to get data from the user
        out.println("<form method=\"POST\">");
        out.println("    <table>");
        out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: large\">URL: </td>");
        out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"url\"/></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td style=\"font-size: large\">Comment: </td>");
        out.println("            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"comment\"/></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("            <td></td>");
        out.println("            <td><input type=\"submit\" name=\"button\" value=\"Add Favorite\"/></td>");
        out.println("            <td><input type=\"submit\" name=\"button\" value=\"Logout\"/></td>");
        out.println("        </tr>");
        out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
        out.println("    </table>");
        out.println("</form>");

        for (String message : messages) {
            out.println("<p style=\"font-size: large; color: red\">");
            out.println(message);
            out.println("</p>");
        }
        out.println("<table>");
        for (int i = 0; i < beans.length; i++) {
            out.println("    <tr>");
            out.println("        <td>");
            out.println("            <form method=\"POST\" id=\"" + beans[i].getId() + "\"> ");
            out.println("            <input type=\"hidden\" name=\"ClickSite\" value=\"" + beans[i].getId() + "\"> ");
            out.println("<a href=\"#\" onclick=\"document.getElementById('" + beans[i].getId() + "').submit();\">"
                    + beans[i].getUrl() + "</a>");
            out.println("<br/>" + beans[i].getComment() + "<br/>" + beans[i].getCount() + " Clicks" + "<br><br></td>");
            out.println("    </tr>");
            out.println("</form>");
        }
        out.println("</table>");

        out.println("</body>");
        out.println("</html>");
    }
}
