package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

//ALSO DOES GETSTORY()
public class FeedRequest {
    private AuthToken authToken;
    private String target;
    private int limit;
    private String lastPost;

    public FeedRequest() {
    }

    public FeedRequest(AuthToken authToken, String target, int limit, String lastPost) {
        this.authToken = authToken;
        this.target = target;
        this.limit = limit;
        this.lastPost = lastPost;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLastPost() {
        return lastPost;
    }

    public void setLastPost(String lastPost) {
        this.lastPost = lastPost;
    }
}
