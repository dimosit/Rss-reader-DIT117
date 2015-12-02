package assignment1.android.hua.gr.assignment1;

import java.io.Serializable;

/**
 * Created by d1 on 1/12/2015.
 */
public class RssItems implements Serializable {
    String postTitle ;
    String postDescription;
    String postDate;

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}
