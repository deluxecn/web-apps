/**
 * 08-672 HW3
 * @author Luxiao Ding (luxiaod@andrew.cmu.edu)
 * 09/26/2016
 */
public class FavoriteBean {
    private int id;
    private int userId;
    private String url;
    private String comment;
    private int count;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUrl() {
        return url;
    }

    public String getComment() {
        return comment;
    }

    public int getCount() {
        return count;
    }

    public void setId(int i) {
        id = i;
    }

    public void setUserId(int i) {
        userId = i;
    }
    
    public void setUrl(String s) {
        url = s;
    }

    public void setComment(String s) {
        comment = s;
    }

    public void setCount(int i) {
        count = i;
    }

}
