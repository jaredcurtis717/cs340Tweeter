package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

public interface IsFollowerNotificationObserver extends ServiceObserver {
    void handleSuccess(boolean isFollower);
}
