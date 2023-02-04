package edu.byu.cs.tweeter.model.net.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedResponse extends PagedResponse {
    private List<Status> statusList;

    FeedResponse(boolean success, boolean hasMorePages) {
        super(success, hasMorePages);
    }

    FeedResponse(boolean success, String message, boolean hasMorePages) {
        super(success, message, hasMorePages);
    }

    public FeedResponse(List<Status> statusList, boolean hasMorePages) {
        super(true, hasMorePages);
        this.statusList = statusList;
    }

    public List<Status> getStatusList() {
        return statusList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedResponse that = (FeedResponse) o;
        return Objects.equals(statusList, that.statusList) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess();
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusList);
    }
}
