package edu.byu.cs.tweeter.client.net;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IntResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;


public class ServerFacadeTest {

    private ServerFacade serverFacade;

    @Before
    public void setup(){
        serverFacade = new ServerFacade();
    }

    @Test
    public void testRegister(){
        LoginResponse response = null;
        try{
            response = serverFacade.register(new RegisterRequest("JC", "Cu", "jmc", "password", "image"), "/register");
        }catch (Exception ex){
            Assert.assertNull(ex); //Shouldn't throw an exception, if it does test should fail
        }

        Assert.assertNotNull(response);
        Assert.assertNull(response.getMessage());
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getAuthToken());
        Assert.assertNotNull(response.getUser());
    }

    @Test
    public void testGetFollowers(){
        FollowingResponse response = null;
        try{
            response = serverFacade.getFollowees(new PagedRequest(new AuthToken(), "@j", 5, null), "/getfollowers");
        }catch (Exception ex){
            Assert.assertNull(ex); //Shouldn't throw an exception, if it does test should fail
        }

        Assert.assertNotNull(response);
        Assert.assertNull(response.getMessage());
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getHasMorePages());
        Assert.assertNotNull(response.getFollowees());
        Assert.assertTrue(response.getFollowees().size()<=5);
    }

    @Test
    public void testGetFollowersCount(){
         IntResponse response = null;
        try{
            response = serverFacade.getFollowersCount(new TargetUserRequest(new AuthToken(), "@bob"), "/getfollowercount");
        }catch (Exception ex){
            Assert.assertNull(ex); //Shouldn't throw an exception, if it does test should fail
        }

        Assert.assertNotNull(response);
        Assert.assertNull(response.getMessage());
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotNull(response.getNumber());
    }
}
