package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.IFollowDAO;


/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDAO implements IFollowDAO {

    public Table connect() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("follows");

        return table;
    }

    @Override
    public FollowingResponse getFollowing(FollowingRequest request) {
        Table table = connect();
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#follower", "follower_handle");
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":follower_handle", request.getFollowerAlias());
        boolean hasMorePages = false;

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#follower = :follower_handle").withNameMap(nameMap)
                .withValueMap(valueMap).withScanIndexForward(true);

        List<User> allFollowees = new ArrayList<>();
        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;
        try {
            items = table.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                User user = new User(item.get("followee_firstName").toString(), item.get("followee_lastName").toString(),
                        item.get("followee_handle").toString(), item.get("followee_imageUrl").toString());
                allFollowees.add(user);
                user = null;
            }
        }
        catch (Exception e) {
            System.err.println("Error querying followees");
            System.err.println(e.getMessage());
        }
        return new FollowingResponse(allFollowees, hasMorePages);
    }

    @Override
    public FollowingResponse getFollowers(FollowingRequest request) {
        Table table = connect();
        Index index = table.getIndex("follows_index");
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#followee", "followee_handle");
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":followee_handle", request.getFollowerAlias());
        boolean hasMorePages = false;

        QuerySpec querySpec = new QuerySpec().withScanIndexForward(true).withKeyConditionExpression("#followee = :followee_handle")
                .withNameMap(nameMap).withValueMap(valueMap);

        List<User> allFollowers = new ArrayList<>();
        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;
        try {
            items = index.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                User user = new User();
                user.setFirstName(item.get("follower_firstName").toString());
                user.setLastName(item.get("follower_lastName").toString());
                user.setAlias(item.get("follower_handle").toString());
                user.setImageUrl(item.get("follower_imageUrl").toString());
                allFollowers.add(user);
                user = null;
            }
        }
        catch (Exception e) {
            System.err.println("Error querying followers");
            System.err.println(e.getMessage());
        }

        return new FollowingResponse(allFollowers, hasMorePages);
    }

    @Override
    public FollowResponse follow(FollowRequest request) {
        Table table = connect();

        Item item = new Item()
                .withPrimaryKey("follower_handle", request.getcurrUser().getAlias(), "followee_handle", request.getFollowee().getAlias())
                .withString("followee_firstName", request.getFollowee().getFirstName())
                .withString("followee_lastName", request.getFollowee().getLastName())
                .withString("followee_imageUrl", request.getFollowee().getImageUrl())
                .withString("follower_firstName", request.getcurrUser().getFirstName())
                .withString("follower_lastName", request.getcurrUser().getLastName())
                .withString("follower_imageUrl", request.getcurrUser().getImageUrl());

        table.putItem(item);
        return new FollowResponse(true, null);
    }

    @Override
    public UnfollowResponse unfollow(UnfollowRequest request) {
        Table table = connect();
        table.deleteItem("follower_handle", request.getUser(), "followee_handle", request.getUser());
        return new UnfollowResponse(true, null);
    }

    @Override
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        Table table = connect();
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#follower", "follower_handle");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":follower_handle", request.getUser());

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#follower = :follower_handle").withNameMap(nameMap)
                .withValueMap(valueMap);

        int count = 0;
        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        try {
            items = table.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
        }
        catch (Exception e) {
            System.err.println("Error counting followees");
            System.err.println(e.getMessage());
        }

        System.out.println(count);
        return new FollowingCountResponse(true, null, count);
    }

    @Override
    public FollowerCountResponse getFollowerCount(FollowerCountRequest request) {

        Table table = connect();
        Index index = table.getIndex("follows_index");
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#followee", "followee_handle");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":followee_handle", request.getUser());

        QuerySpec querySpec = new QuerySpec().withScanIndexForward(false).withKeyConditionExpression("#followee = :followee_handle")
                .withNameMap(nameMap).withValueMap(valueMap);

        int count = 0;
        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        try {
            items = index.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
        }
        catch (Exception e) {
            System.err.println("Error counting followers");
            System.err.println(e.getMessage());
        }
        System.out.println(count);
        return new FollowerCountResponse(true, null, count);
    }

    @Override
    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        Table table = connect();
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#follower", "follower_handle");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":follower_handle", request.getFollower());

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#follower = :follower_handle").withNameMap(nameMap)
                .withValueMap(valueMap);

        boolean isFollower = false;
        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;
        try {
            items = table.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                if (item.get("followee_handle").equals(request.getFollowee())){
                    isFollower = true;
                }
            }
        }
        catch (Exception e) {
            System.err.println("Error verifying if is follower");
            System.err.println(e.getMessage());
        }

        return new IsFollowerResponse(isFollower);
    }

    @Override
    public List<String> getFollowerAliases(String alias) {
        Table table = connect();
        Index index = table.getIndex("follows_index");
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#followee", "followee_handle");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":followee_handle", alias);

        QuerySpec querySpec = new QuerySpec().withScanIndexForward(false).withKeyConditionExpression("#followee = :followee_handle")
                .withNameMap(nameMap).withValueMap(valueMap);

        List<String> followerAliases = new ArrayList<>();
        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;
        try {
            items = index.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                followerAliases.add(item.get("follower_handle").toString());
            }
        }
        catch (Exception e) {
            System.err.println("Error retrieving aliases");
            System.err.println(e.getMessage());
        }

        return followerAliases;
    }


}
