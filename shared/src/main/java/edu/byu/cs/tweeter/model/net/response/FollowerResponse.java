package edu.byu.cs.tweeter.model.net.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowerResponse extends PagedResponse {
    private List<User> followers;

    public FollowerResponse(boolean success, boolean hasMorePages) {
        super(success, hasMorePages);
    }

    public FollowerResponse(List<User> followers, boolean hasMorePages) {
        super(true, hasMorePages);
        this.followers = followers;
    }

    FollowerResponse(boolean success, String message, boolean hasMorePages) {
        super(success, message, hasMorePages);
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowerResponse that = (FollowerResponse) o;
        return Objects.equals(followers, that.followers) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess();
    }

    @Override
    public int hashCode() {
        return Objects.hash(followers);
    }
}
