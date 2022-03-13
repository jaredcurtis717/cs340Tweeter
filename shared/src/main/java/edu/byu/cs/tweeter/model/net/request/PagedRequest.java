package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class PagedRequest {

    private AuthToken authToken;
    private String alias;
    private int limit;
    private String lastItem;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private PagedRequest() {}

    /**
     * Creates an instance.
     *
     * @param alias the alias of the user whose followees are to be returned.
     * @param limit the maximum number of followees to return.
     * @param lastItem the alias of the last followee that was returned in the previous request (null if
     *                     there was no previous request or if no followees were returned in the
     *                     previous request).
     */
    public PagedRequest(AuthToken authToken, String alias, int limit, String lastItem) {
        this.authToken = authToken;
        this.alias = alias;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    /**
     * Returns the auth token of the user who is making the request.
     *Au
     * @return the auth token.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken the auth token.
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns the follower whose followees are to be returned by this request.
     *
     * @return the follower.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the follower.
     *
     * @param alias the follower.
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the last followee that was returned in the previous request or null if there was no
     * previous request or if no followees were returned in the previous request.
     *
     * @return the last followee.
     */
    public String getLastItem() {
        return lastItem;
    }

    /**
     * Sets the last followee.
     *
     * @param lastItem the last followee.
     */
    public void setLastItem(String lastItem) {
        this.lastItem = lastItem;
    }
}
