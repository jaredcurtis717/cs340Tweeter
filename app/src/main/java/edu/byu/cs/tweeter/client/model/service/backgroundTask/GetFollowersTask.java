package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {
    private static final String URL_PATH = "/getfollowers";

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected void runTask() throws Exception {
        FollowingRequest request;
        if (getLastItem() == null){
            request = new FollowingRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), null);
        }
        else{
            request = new FollowingRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), getLastItem().getAlias());
        }
        FollowingResponse response = getServerFacade().getFollowees(request, URL_PATH);

        if (response.isSuccess()){
            items = response.getFollowees();
            hasMorePages = response.getHasMorePages();
            sendSuccessMessage();
        }
        else{
            sendFailedMessage(response.getMessage());
        }
    }


}
