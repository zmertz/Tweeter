package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.server.dao.interfaces.IStatusDAO;
import edu.byu.cs.tweeter.server.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.CreateStatusRequest;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.CreateStatusResponse;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;


public class StatusDAO implements IStatusDAO {

    private Table storyTable;
    private Table feedTable;

    public void connectToTable(String table) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);
        switch (table) {
            case "story":
                storyTable = dynamoDB.getTable("story");
                break;
            case "feed":
                feedTable = dynamoDB.getTable("feed");
                break;
        }
    }

    @Override
    public CreateStatusResponse createStatus(CreateStatusRequest request, String date) {
        connectToTable("story");
        System.out.println(date);
        Item item = new Item()
                .withPrimaryKey("sender_alias", request.getUser().getAlias())
                .withString("date_time", date)
                .withString("message", request.getNewStatus());
        storyTable.putItem(item);

        return new CreateStatusResponse(true, null);
    }

    @Override
    public FeedResponse getFeed(FeedRequest request) {
        connectToTable("feed");
        System.out.println("Get user returns"+ request.getTarget());
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#receiver", "receiver_alias");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":receiver_alias", request.getTarget());

        List<Status> feed = new ArrayList<>();
        boolean hasMorePages = false;

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#receiver = :receiver_alias").withNameMap(nameMap)
                .withValueMap(valueMap).withScanIndexForward(true);

            ItemCollection<QueryOutcome> items = null;
            Iterator<Item> iterator = null;
            Item item = null;
            try {
                items = feedTable.query(querySpec);
                iterator = items.iterator();
                while (iterator.hasNext()) {
                    item = iterator.next();
                    List<String> urls = new ArrayList<>();
                    String time = item.get("date_time").toString();
                    DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
                    Date prettyTime = new Date(Long.parseLong(time));
                    GetUserResponse creator = new DAOFactory().getUserDAO().getUser(new GetUserRequest(item.get("sender_alias").toString()));
                    Status status = new Status(item.get("message").toString(), creator.getUser(), obj.format(prettyTime),
                            urls, urls);

                    feed.add(status);
                    status = null;
                }
            } catch (Exception e) {
                System.err.println("Unable to query feed table.");
                System.err.println(e.getMessage());
            }

        return new FeedResponse(feed, hasMorePages);
    }

    public StoryResponse getStory(StoryRequest request) {
        connectToTable("story");
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#sender", "sender_alias");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":sender_alias", request.getUser());
        List<Status> story = new ArrayList<>();
        boolean hasMorePages = false;

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#sender = :sender_alias").withNameMap(nameMap)
                .withValueMap(valueMap).withScanIndexForward(true);

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;
        GetUserResponse creator = new DAOFactory().getUserDAO().getUser(new GetUserRequest(request.getUser()));
        try {
            items = storyTable.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                List<String> urls = new ArrayList<>();
                String time = item.get("date_time").toString();
                DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
                Date prettyTime = new Date(Long.parseLong(time));
                //String time = "1 min ago";
                Status status = new Status(item.get("message").toString(), creator.getUser(), obj.format(prettyTime),
                        urls, urls);
                story.add(status);
                status = null;
            }
        }
        catch (Exception e) {
            System.err.println("Unable to query story table.");
            System.err.println(e.getMessage());
        }

        return new StoryResponse(story, hasMorePages);
    }

    // This works with processStatusBatch() [StatusService:80]
    @Override
    public void addBatchToFeed(List<String> followerAlias, StatusService.StatusBatchMessage statusBatchMessage) {
        TableWriteItems items = new TableWriteItems("feed");

        for (String alias: followerAlias) {
            Item item = new Item()
                    .withPrimaryKey("receiver_alias", alias)
                    .withString("date_time", statusBatchMessage.getStatus().getDate())
                    .withString("message", statusBatchMessage.getStatus().getPost())
                    .withString("sender_alias", statusBatchMessage.getStatus().getUser().getAlias());
            items.addItemToPut(item);
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems("feed");
            }
        }

        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    private void loopBatchWrite(TableWriteItems items) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);

        // Check if anymore need to be written to the table
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
        }
    }


}
