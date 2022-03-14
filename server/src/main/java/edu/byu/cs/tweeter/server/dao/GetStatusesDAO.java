package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class GetStatusesDAO {
    /**
     * Gets the statuses in a users story. Uses
     * information in the request object to limit the number of statuses returned and to return the
     * next set of statuses after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose statuses are to be returned and any
     *                other information required to satisfy the request.
     * @return the statuses.
     */
    public StatusesResponse getStory(PagedRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getAlias() != null;

        List<Status> allStatuses = getDummyStatuses();
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int followeesIndex = getStatusesStartingIndex(request.getLastItem(), allStatuses);

                for(int limitCounter = 0; followeesIndex < allStatuses.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allStatuses.size();
            }
        }

        return new StatusesResponse(responseStatuses, hasMorePages);
    }

    /**
     * Gets the statuses in a users feed. Uses
     * information in the request object to limit the number of statuses returned and to return the
     * next set of statuses after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose statuses are to be returned and any
     *                other information required to satisfy the request.
     * @return the statuses.
     */
    public StatusesResponse getFeed(PagedRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getAlias() != null;

        List<Status> allStatuses = getDummyStatuses();
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int followeesIndex = getStatusesStartingIndex(request.getLastItem(), allStatuses);

                for(int limitCounter = 0; followeesIndex < allStatuses.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allStatuses.size();
            }
        }

        return new StatusesResponse(responseStatuses, hasMorePages);
    }

    private int getStatusesStartingIndex(String lastStatus, List<Status> allStatuses) {

        int followeesIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i).getPost())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<Status> getDummyStatuses() {
        return getFakeData().getFakeStatuses();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy data.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }
}
