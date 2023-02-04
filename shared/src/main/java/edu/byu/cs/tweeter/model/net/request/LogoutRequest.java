package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class LogoutRequest {
    AuthToken authToken;

    public LogoutRequest() {}

    public LogoutRequest(AuthToken authToken) {
        this.authToken = authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }
}
