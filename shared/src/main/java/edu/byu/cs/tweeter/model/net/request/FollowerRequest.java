package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowerRequest {
    private AuthToken authToken;
    private String follower;
    private int limit;
    private String lastFollower;

    public FollowerRequest() {
    }

    public FollowerRequest(AuthToken authToken, String follower, int limit, String lastfollower) {
        this.authToken = authToken;
        this.follower = follower;
        this.limit = limit;
        this.lastFollower = lastfollower;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    /*public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }*/

    public String getLastfollower() {
        return lastFollower;
    }

    public void setLastfollower(String lastfollower) {
        this.lastFollower = lastfollower;
    }
}
