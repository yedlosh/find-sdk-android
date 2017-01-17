package com.orchestral.findsdk.api.track;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Sample request:
 * <p>
 * Copyright © 2017. Orion Health. All rights reserved.
 */
public interface TrackApiService {

    @POST("track")
    Single<TrackResponse> track(@Body TrackRequest trackRequest);

}
