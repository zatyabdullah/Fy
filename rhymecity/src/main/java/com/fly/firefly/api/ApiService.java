package com.fly.firefly.api;

import com.fly.firefly.api.obj.ChangePasswordReceive;
import com.fly.firefly.api.obj.DeviceInfoSuccess;
import com.fly.firefly.api.obj.ForgotPasswordReceive;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.RegisterReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.UpdateProfileReceive;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.api.obj.tryObj;
import com.fly.firefly.ui.object.ChangePasswordRequest;
import com.fly.firefly.ui.object.DeviceInformation;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.PasswordRequest;
import com.fly.firefly.ui.object.RegisterObj;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.object.UpdateProfileRequest;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface ApiService {

    @GET("/users/{user}")
    void getFeed2(@Path("user") String user, Callback<LoginRequest> callback);

    @GET("/apis/8891c88e")
        //void getGoogleSpreedSheetData(@Path("user") String user, Callback<tryObj> callback);
    void getGoogleSpreedSheetData(Callback<tryObj> callback);

    //@FormUrlEncoded
    @POST("/login")
    //void getGoogleSpreedSheetData(@Path("user") String user, Callback<tryObj> callback);
    //void onRequestToLogin(@Field("username") String username, @Field("password") String password,Callback<LoginRequest> callback);
    //void onRequestToLogin(@Field("number") int number, @Field("username") String username, @Field("password") String password,Callback<LoginRequest> callback);
    void onRequestToLogin(@Body LoginRequest task, Callback<LoginReceive> callback);

    // @Body JSONObject searchstring


    @POST("/loading")
    void onSendDeviceInfo(@Body DeviceInformation task, Callback<DeviceInfoSuccess> callback);

    @POST("/register")
    void onRegisterRequest(@Body RegisterObj obj, Callback<RegisterReceive> callback);

    @POST("/searchFlight")
    void onSearchFlightRequest(@Body SearchFlightObj obj, Callback<SearchFlightReceive> callback);

    @POST("/forgotPassword")
    void onRequestPassword(@Body PasswordRequest task, Callback<ForgotPasswordReceive> callback);

    @POST("/changePassword")
    void onRequestChangePassword(@Body ChangePasswordRequest task, Callback<ChangePasswordReceive> callback);

    @POST("/updateProfile")
    void onUpdateProfileRequest(@Body UpdateProfileRequest task, Callback<UpdateProfileReceive> callback);

    @POST("/checkInFlight")
    void onMobileCheckinRequest(@Body MobileCheckinObj obj, Callback<MobileCheckinReceive> callback);

}


