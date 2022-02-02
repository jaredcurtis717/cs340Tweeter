package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryService {
    public void getStory(AuthToken authToken, User user, int pageSize, Status lastStatus, GetStoryObserver getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new GetStoryHandler(getStoryObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }

    public interface GetStoryObserver {
        void handleSuccess(List<Status> statuses, boolean hasMorePages, Status lastStatus);

        void handleFailure(String message);

        void handleException(Exception exception);

        void removeLoadingFooter();

        void setLoading(boolean value);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends Handler {
        private final GetStoryObserver observer;

        public GetStoryHandler(GetStoryObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            observer.setLoading(false);
            observer.removeLoadingFooter();

            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
                Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

                observer.handleSuccess(statuses, hasMorePages, lastStatus);

            } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
                observer.handleFailure(message);
                //Toast.makeText(getContext(), "Failed to get story: " + message, Toast.LENGTH_LONG).show();
            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
                observer.handleException(ex);
                //Toast.makeText(getContext(), "Failed to get story because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
