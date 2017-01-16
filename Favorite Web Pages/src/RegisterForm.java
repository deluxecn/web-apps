/**
 * 08-672 HW3
 * @author Luxiao Ding (luxiaod@andrew.cmu.edu)
 * 09/26/2016
 */

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class RegisterForm {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String button;

    public RegisterForm(HttpServletRequest request) {
        email = request.getParameter("email");
        email = email == null ? null : email.trim();
        firstName = request.getParameter("firstName");
        firstName = firstName == null ? null : firstName.trim();
        lastName = request.getParameter("lastName");
        lastName = lastName == null ? null : lastName.trim();
        password = request.getParameter("password");
        password = password == null ? null : password.trim();
        button = request.getParameter("button");
    }

    public String getEmail() {
        return email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getButton() {
        return button;
    }

    public boolean isPresent() {
        return button != null;
    }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (email == null || email.length() == 0)
            errors.add("User Email is required");
        if (firstName == null || firstName.length() == 0)
            errors.add("First name is required");
        if (lastName == null || lastName.length() == 0)
            errors.add("Last name is required");
        if (password == null || password.length() == 0)
            errors.add("Password is required");
        if (button == null)
            errors.add("Button is required");

        if (errors.size() > 0)
            return errors;

        if (!button.equals("Register"))
            errors.add("Invalid button");

        return errors;
    }
}
