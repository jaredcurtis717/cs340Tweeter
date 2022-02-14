package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetUserNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter<PagedPresenter.View<T>> {
    protected static final int PAGE_SIZE = 10;
    protected boolean isLoading;
    protected boolean hasMorePages;
    protected T lastItem;
    protected User targetUser;

    private UserService userService;


    public PagedPresenter(View view) {
        super(view);
        isLoading = false;
        userService = new UserService();
    }

    private void setHasMorePages(boolean value) {
        hasMorePages = value;
    }

    public boolean getHasMorePages() {
        return hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    abstract String getDescription();

    public void clickItem(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserObserver());
    }


    public interface View<T> extends Presenter.View {

        void setLoadingStatus(boolean value);

        void clickedUser(User user);

        void addItems(List<T> items);
    }

    public void loadItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);

            callTask(user);
        }
    }

    public abstract void callTask(User user);

    protected class GetUserObserver extends BaseObserver implements GetUserNotificationObserver {

        public GetUserObserver() {
            super(view);
        }

        @Override
        public void handleSuccess(User user) {
            view.clickedUser(user);
        }


        @Override
        String getDescription() {
            return PagedPresenter.this.getDescription();
        }
    }

    protected class GetItemsObserver extends BaseObserver implements PagedNotificationObserver<T> {

        public GetItemsObserver() {
            super(view);
        }

        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(items);
        }

        @Override
        String getDescription() {
            return PagedPresenter.this.getDescription();
        }
    }
}
