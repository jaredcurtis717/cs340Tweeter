package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.CountNotificationObserver;

public class CountNotificationHandler extends BackgroundTaskHandler<CountNotificationObserver> {

    public CountNotificationHandler(CountNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(CountNotificationObserver observer, Bundle data) {
        observer.handleSuccess(data.getInt(GetCountTask.COUNT_KEY));
    }
}
