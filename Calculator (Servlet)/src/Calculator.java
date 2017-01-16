import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Calculator
 */
@WebServlet("/Calculator")
public class Calculator extends HttpServlet {
    private static final long serialVersionUID = 1L;
    DecimalFormat df = new DecimalFormat("###,##0.00");
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();
        boolean valid = true;
        String strX = request.getParameter("X");
        String strY = request.getParameter("Y");
        String strOp = request.getParameter("Op");
        double x = 0;
        double y = 0;
        
        out.println("<!doctype html>");
        out.println("<html>");
        out.println("  <head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>Calculator</title>");
        out.println("  <style>");
        out.println("      body {background-color: palegreen;}");
        out.println("      table, th, td {border: 1px solid black;}");
        out.println("      button {font-size: 14px;}");
        out.println("  </style>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("      <h1 style=\"color:white;\">Calculator</h1>");
        
        if (strX == null && strY == null && strOp == null) {
            valid = false;
        } else {
            if (strX == null) {
                out.println("<h2>Input X is missing!</h2>");
                valid = false;
            } else if (strX.length() == 0){
                out.println("<h2>Input X is empty!</h2>");
                valid = false;
            }
            if (strY == null) {
                out.println("<h2>Input Y is missing!</h2>");
                valid = false;
            } else if (strY.length() == 0){
                out.println("<h2>Input Y is empty!</h2>");
                valid = false;
            }
            if (strOp == null) {
                out.println("<h2>Missing or invalid operator!</h2>");
                valid = false;
            } else if (!(strOp.equals("+")||strOp.equals("-")||strOp.equals("*")||strOp.equals("/"))) {
                out.println("<h2>Invalid operator!</h2>");
                valid = false;
            }
        }
        
        // Check if X and Y are number
        if (valid) {
            try {
                x = Double.parseDouble(strX);
            } catch (NumberFormatException e) {
                out.println("<h2>X is not a number! </h2>");
                valid = false;
            }
            
            try {
                y = Double.parseDouble(strY);
            } catch (NumberFormatException e) {
                out.println("<h2>Y is not a number! </h2>");
                valid = false;
            }
        }
        
        // Do the math
        if (valid) {
            if (strOp.equals("+")) {
                out.println("<h2>" + plus(x, y) + "</h2>");
            } else if (strOp.equals("-")) {
                out.println("<h2>" + minus(x, y) + "</h2>");
            } else if (strOp.equals("*")) {
                out.println("<h2>" + multiply(x, y) + "</h2>");
            } else {
                out.println("<h2>" + divide(x, y) + "</h2>");
            }
        }
        
        out.println("      <table>");
        out.println("      <form>");
        out.println("        <tr>");
        out.println("          <td>X:</td>");
        if (strX == null) {
            out.println("          <td><input type=\"text\" name=\"X\"></td>");
        } else {
            out.println("          <td><input type=\"text\" name=\"X\" value=\"" + strX + "\"></td>");
        }
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>Y:</td>");
        if (strY == null) {
            out.println("          <td><input type=\"text\" name=\"Y\"></td>");
        } else {
            out.println("          <td><input type=\"text\" name=\"Y\" value=\"" + strY + "\"></td>");
        }
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td colspan=\"2\" align=\"center\">");
        out.println("            <input type=\"submit\" name=\"Op\" value=\"+\" id=\"plus\">");
        out.println("            <input type=\"submit\" name=\"Op\" value=\"-\" id=\"minus\">");
        out.println("            <input type=\"submit\" name=\"Op\" value=\"*\" id=\"mul\">");
        out.println("            <input type=\"submit\" name=\"Op\" value=\"/\" id=\"div\">");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("      </form>");
        out.println("      </table>");
        out.println("  </body>");
        out.println("</html>");
    }
    
    private String plus(double x, double y) {
        return (df.format(x) + " + " + df.format(y) + " = " + df.format(x+y));
    }
    
    private String minus(double x, double y) {
        return (df.format(x) + " - " + df.format(y) + " = " + df.format(x-y));
    }
    
    private String multiply(double x, double y) {
        return (df.format(x) + " * " + df.format(y) + " = " + df.format(x*y));
    }
    
    private String divide(double x, double y) {
        if (y == 0) {
            return "Cannot divide by zero";
        }
        return (df.format(x) + " / " + df.format(y) + " = " + df.format(x/y));
    }
}
