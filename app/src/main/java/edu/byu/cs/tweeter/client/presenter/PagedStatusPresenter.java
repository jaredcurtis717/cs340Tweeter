package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;

public abstract class PagedStatusPresenter extends PagedPresenter<Status> {
    protected final StatusService statusService;

    public PagedStatusPresenter(View view) {
        super(view);
        statusService = new StatusService();
    }
}
