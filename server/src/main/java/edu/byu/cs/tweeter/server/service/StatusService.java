package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.interfaces.IDAOFactory;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.CreateStatusRequest;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.CreateStatusResponse;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public class StatusService {
    private final IDAOFactory dao;

    public StatusService(IDAOFactory dao) {
        this.dao = dao;
    }

    public CreateStatusResponse postStatus(CreateStatusRequest request) {

        Long date = Calendar.getInstance().getTimeInMillis();
        Status status = new Status(request.getNewStatus(), request.getUser(), request.getAuthToken().datetime);
        String postStatusURL = "https://sqs.us-west-2.amazonaws.com/784864918410/Status_Queue";
        addObjectToQueue(postStatusURL, new StatusMessage(status));

        return dao.getStatusDAO().createStatus(request, String.valueOf(date));
    }

    private void addObjectToQueue(String queueUrl, Object object) {
        Gson gson = new Gson();
        String messageBody = gson.toJson(object);

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);

        String id = sendMessageResult.getMessageId();
        System.out.println("Message Id: " + id);
    }

    public FeedResponse getFeed(FeedRequest request) {
        return dao.getStatusDAO().getFeed(request);
    }

    public StoryResponse getStory(StoryRequest request) {
        return dao.getStatusDAO().getStory(request);
    }

    private void addBatchToBatchQueue(Status status, List<String> followerAliases) {
        String updateFeedURL = "https://sqs.us-west-2.amazonaws.com/784864918410/Feed_Queue";
        addObjectToQueue(updateFeedURL, new StatusBatchMessage(status, followerAliases));
    }

    // From PostUpdateFeedMessagesHandler
    public void createStatusBatches(String requestBody) {
        Gson gson = new Gson();
        int batchSize = 25;
        StatusMessage statusMessage = gson.fromJson(requestBody, StatusMessage.class);
        List<String> followerAliases = dao.getFollowDAO().getFollowerAliases(statusMessage.status.getUser().getAlias());
        ArrayList<String> batchAliases = new ArrayList<>();
        for (int i = 0; i < followerAliases.size(); i++) {
            batchAliases.add(followerAliases.get(i));
            if (batchAliases.size() >= batchSize || i == (followerAliases.size() - 1)) {
                addBatchToBatchQueue(statusMessage.status, batchAliases);
                batchAliases.clear();
            }
        }
    }

    // From UpdateFeedHandler.java
    public void processStatusBatch(String requestBody){
        Gson gson = new Gson();
        StatusBatchMessage statusBatchMessage = gson.fromJson(requestBody, StatusBatchMessage.class);
        dao.getStatusDAO().addBatchToFeed(statusBatchMessage.getFollowerAlias(), statusBatchMessage);
    }

    public static class StatusMessage{
        Status status;
        public StatusMessage(Status status) {
            this.status = status;
        }
    }

    public static class StatusBatchMessage {
        public Status status;
        public List<String> followerAlias;

        public StatusBatchMessage(Status status, List<String> followerAlias) {
            this.status = status;
            this.followerAlias = followerAlias;
        }

        public Status getStatus() {
            return status;
        }

        public List<String> getFollowerAlias() {
            return followerAlias;
        }

    }

}
