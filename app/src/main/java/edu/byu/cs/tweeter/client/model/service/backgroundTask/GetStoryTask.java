package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {
    private static String URL_PATH = "/getstory";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected void runTask() throws Exception {
        PagedRequest request;
        if (getLastItem() == null){
            request = new PagedRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), null);
        }
        else{
            request = new PagedRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), getLastItem().getPost());
        }
        StatusesResponse response = getServerFacade().getStatuses(request, URL_PATH);

        if (response.isSuccess()){
            items = response.getStatuses();
            hasMorePages = response.getHasMorePages();
            sendSuccessMessage();
        }
        else{
            sendFailedMessage(response.getMessage());
        }
    }

}
