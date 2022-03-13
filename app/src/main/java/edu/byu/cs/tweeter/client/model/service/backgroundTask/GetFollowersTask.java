package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

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
        PagedRequest request;
        if (getLastItem() == null){
            request = new PagedRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), null);
        }
        else{
            request = new PagedRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), getLastItem().getAlias());
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
