package com.example.appchat.TabPage;

import com.example.appchat.Notifications.MyRespone;
import com.example.appchat.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAANub5l10:APA91bFqxXhk5b9HighqjLslqLJBiDAAQPlOjA-uAa9Vu4LwNguv2gO02HHF5PIKCK6LMC1F9wnfNx1T4d_lHYEQKE2RGC-GYs80pBvuCAgN0eYBt1VLxfHjNQ1--kX3x2HX4TINrmDY"
            }
    )
    @POST("fcm/send")
    Call<MyRespone> sendNotification(@Body Sender body);
}
