package com.orchestral.findsdk.api.track;

import java.util.Map;

/**
 * Copyright © 2017. Orion Health. All rights reserved.
 */
public class TrackResponse {

    public TrackResponse(Boolean success, String message, String location, Map<String, String> bayes) {
        this.success = success;
        this.message = message;
        this.location = location;
        this.bayes = bayes;
    }

    public final Boolean success;
    public final String message;
    public final String location;
    public final Map<String, String> bayes;

    @Override
    public String toString() {
        return "TrackResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", location='" + location + '\'' +
                ", bayes=" + bayes +
                '}';
    }
}
