/**
 * 08-672 HW3
 * @author Luxiao Ding (luxiaod@andrew.cmu.edu)
 * 09/26/2016
 */

public class MyDAOException extends Exception {
    private static final long serialVersionUID = 1L;

    public MyDAOException(Exception e) {
        super(e);
    }

    public MyDAOException(String s) {
        super(s);
    }
}
