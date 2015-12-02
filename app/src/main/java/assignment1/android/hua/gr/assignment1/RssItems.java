package assignment1.android.hua.gr.assignment1;

import java.io.Serializable;

/**
 * Created by d1 on 1/12/2015.
 */

//class fro rss items
public class RssItems implements Serializable {
    String postTitle ;
    String postDescription;


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


}
