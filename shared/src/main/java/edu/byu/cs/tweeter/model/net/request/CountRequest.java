package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class CountRequest {
    User user;

    public CountRequest(User user) {
        this.user = user;
    }
    public CountRequest(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
