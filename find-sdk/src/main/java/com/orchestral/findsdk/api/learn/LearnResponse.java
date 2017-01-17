package com.orchestral.findsdk.api.learn;

/**
 * {
 "success": true,
 "message": "Inserted fingerprint containing 23 APs for zack at zakhome floor 2 office"
 }

 * Copyright © 2017. Orion Health. All rights reserved.
 */
public class LearnResponse {

    public LearnResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public final Boolean success;
    public final String message;

    @Override
    public String toString() {
        return "LearnResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
