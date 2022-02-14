package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserEntryNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class UserPresenter extends Presenter<UserPresenter.View> {

    public UserPresenter(View view) {
        super(view);
    }

    public interface View extends Presenter.View {
        void navigateToUser(User user);
    }

    abstract String getDescription();

    public class UserObserver extends BaseObserver implements UserEntryNotificationObserver {

        public UserObserver() {
            super(view);
        }

        @Override
        public void handleSuccess(User registeredUser, AuthToken authToken) {
            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.navigateToUser(registeredUser);
        }

        @Override
        String getDescription() {
            return UserPresenter.this.getDescription();
        }
    }
}
