package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter extends Presenter<PagedPresenter.View>{

    public PagedPresenter(View view) {
        super(view);
    }

    public interface View<T> extends Presenter.View {

        void setLoadingStatus(boolean value);

        void clickedUser(User user);

        void addItems(List<T> items);
    }
}
