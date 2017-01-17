package com.orchestral.findsdk.api.learn;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Sample request:
 * <p>
 * Copyright © 2017. Orion Health. All rights reserved.
 */
public interface LearnApiService {

    @POST("learn")
    Single<LearnResponse> learn(@Body LearnRequest learnRequest);

}
