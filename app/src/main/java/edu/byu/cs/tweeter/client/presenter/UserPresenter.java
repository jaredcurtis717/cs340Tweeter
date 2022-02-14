package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserEntryNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class UserPresenter extends Presenter<UserPresenter.View>{

    public UserPresenter(View view){
        super(view);
    }

    public interface View extends Presenter.View {
        void displayValidationError(String message);
        void clearValidationError();
        void navigateToUser(User user);
    }

    abstract String getDescription();

    public class UserObserver implements UserEntryNotificationObserver {

        @Override
        public void handleSuccess(User registeredUser, AuthToken authToken) {
            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.navigateToUser(registeredUser);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to " + getDescription() + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to " + getDescription() + " because of exception: " + exception.getMessage());
        }
    }
}
