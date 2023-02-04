package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class IsFollowingRequest {
    private AuthToken authToken;
    private String follower;
    private String followee;

    public IsFollowingRequest(){ }

    public IsFollowingRequest(AuthToken authToken, String follower, String followee) {
        this.authToken = authToken;
        this.follower = follower;
        this.followee = followee;
    }

    public AuthToken getAuthToken() {
        return authToken;
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

    public String getFollowee() {
        return followee;
    }

    public void setFollowee(String followee) {
        this.followee = followee;
    }
}
