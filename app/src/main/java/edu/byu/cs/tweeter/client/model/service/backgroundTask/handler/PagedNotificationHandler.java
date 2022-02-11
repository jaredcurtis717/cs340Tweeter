package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class PagedNotificationHandler<T> extends BackgroundTaskHandler<PagedNotificationObserver<T>> {

    public PagedNotificationHandler(PagedNotificationObserver<T> observer) {
        super(observer);
    }


    @Override
    protected void handleSuccessMessage(PagedNotificationObserver<T> observer, Bundle data) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);

        observer.handleSuccess(items, hasMorePages);
    }
}
