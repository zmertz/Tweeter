package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest {
    User currUser;
    User followee;

    public FollowRequest() {}

    public FollowRequest(User currUser, User followee) {
        this.currUser = currUser;
        this.followee = followee;
    }

    public void setcurrUser(User currUser) {
        this.currUser = currUser;
    }

    public User getcurrUser() {
        return currUser;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public User getFollowee() {
        return followee;
    }
}
