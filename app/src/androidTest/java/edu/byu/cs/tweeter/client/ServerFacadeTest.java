package edu.byu.cs.tweeter.client;

import static org.junit.Assert.assertNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.CountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class ServerFacadeTest {

    private ServerFacade facade;

    @Before
    public void setup() {
        facade = new ServerFacade();
    }

    @Test
    public void registerTest() {
        RegisterResponse response = null;
        RegisterRequest request = new RegisterRequest("@name", "pass",
                "first", "last", null);
        try {
            response = facade.register(request, "/register");
        }catch(Exception e) {
            e.printStackTrace();
            Assert.assertNull(e);
        }
        Assert.assertNotNull(response);
        Assert.assertNull(response.getMessage()); //null for no error message
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getAuthToken());
        Assert.assertNotNull(response.getUser());
    }

    @Test
    public void loginTest() {
        LoginResponse response = null;
        LoginRequest request = new LoginRequest("@name", "pass");
        try {
            response = facade.login(request, "/login");
        }catch(Exception e) {
            Assert.assertNull(e);
        }
        Assert.assertNotNull(response);
        Assert.assertNull(response.getMessage()); //null for no error message
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getAuthToken());
        Assert.assertNotNull(response.getUser());
    }

    @Test
    public void getFollowersTest(){
        FollowerResponse response = null;
        try{
            response = facade.getFollowers(new FollowerRequest(new AuthToken(), "@follower", 2, null), "/getfollower");
        }catch (Exception ex){
            Assert.assertNull(ex);
        }
        Assert.assertNotNull(response);
        Assert.assertNull(response.getMessage()); //null for no error message
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getFollowers());
    }

    @Test
    public void followersCountTest(){
        CountResponse response = null;
        User user = new User();
        try{
            response = facade.getFollowingCount(new CountRequest(user), "/getfollowingcount");
        }catch (Exception ex){
            Assert.assertNull(ex);
        }
        Assert.assertNotNull(response);
        Assert.assertNull(response.getMessage()); //null for no error message
        Assert.assertTrue(response.isSuccess());
    }
}
