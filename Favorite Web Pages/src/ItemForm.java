/**
 * 08-672 HW3
 * @author Luxiao Ding (luxiaod@andrew.cmu.edu)
 * 09/26/2016
 */

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class ItemForm {
    private String url;
    private String comment;

    public ItemForm(HttpServletRequest request) {
        url = request.getParameter("url");
        url = url == null ? null : url.trim();
        comment = request.getParameter("comment");
        comment = comment == null ? null : comment.trim();
    }

    public String getUrl() {
        return url;
    }
    
    public String getComment() {
        return comment;
    }
    
    public boolean isPresent() {
        return url != null;
    }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (url == null || url.length() == 0) {
            errors.add("URL is required");
        }
        
        if (comment == null || comment.length() == 0) {
            errors.add("Comment is required");
        }

        return errors;
    }
}
