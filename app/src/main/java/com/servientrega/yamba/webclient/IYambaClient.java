package com.servientrega.yamba.webclient;

import com.servientrega.yamba.model.Status;

import java.util.List;

/**
 * Created by marlonramos on 5/26/15.
 */
public interface IYambaClient {
    void postStatus(String status) throws YambaClientException;

    void postStatus(String status, double latitude, double longitude) throws YambaClientException;

    List<Status> getTimeline(int maxPosts) throws YambaClientException;
}
