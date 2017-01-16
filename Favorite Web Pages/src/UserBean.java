/**
 * 08-672 HW3
 * @author Luxiao Ding (luxiaod@andrew.cmu.edu)
 * 09/26/2016
 */

public class UserBean {
    private int userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public int getUserId() {
        return userId;
    }
    
    public String getPassword() {
        return password;
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
    
    public void setUserId(int i) {
        userId = i;
    }
    
    public void setEmail(String s) {
        email = s;
    }
    
    public void setPassword(String s) {
        password = s;
    }
    
    public void setFirstName(String s) {
        firstName = s;
    }
    
    public void setLastName(String s) {
        lastName = s;
    }
}
