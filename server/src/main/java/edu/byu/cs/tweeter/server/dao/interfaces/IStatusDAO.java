package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.CreateStatusRequest;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.CreateStatusResponse;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

public interface IStatusDAO {
    CreateStatusResponse createStatus(CreateStatusRequest request, String date);
    FeedResponse getFeed(FeedRequest request);
    StoryResponse getStory(StoryRequest request);

    void addBatchToFeed(List<String> followerAlias, StatusService.StatusBatchMessage statusBatchMessage);
}
