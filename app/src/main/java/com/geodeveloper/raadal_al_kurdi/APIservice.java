package com.geodeveloper.raadal_al_kurdi;


import com.geodeveloper.newtobetting.Notifications.MyResponse;
import com.geodeveloper.newtobetting.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIservice {
    @Headers({
            "Content-Type:application/json","Authorization:key=AAAAYACa9c4:APA91bG1IoUgBCtl8W_tpr_ZNRDRQabQg2x0odP4r-r6FGGyn1Brfh18Hubwnfzg8ekZzZ1uLZLnOqorixl_h1XVcOSGHhUE3M_7JLlYDM2IHXT5laZvQppwUbugjok5byFyymlgYYRb"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}