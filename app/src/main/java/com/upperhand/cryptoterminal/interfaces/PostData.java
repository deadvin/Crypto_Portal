package com.upperhand.cryptoterminal.interfaces;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostData {
    @POST("hide_1")
    Call<String> postlink_1(@Body String body);

    @POST("hide_10")
    Call<String> postlink_10(@Body String body);

    @POST("hide_100")
    Call<String> postlink_100(@Body String body);

    @POST("hide_500")
    Call<String> postlink_500(@Body String body);

    @POST("hide_events")
    Call<String> postlink_events(@Body String body);

    @POST("hide_rep")
    Call<String> postlink_rep(@Body String body);

    @POST("hide_rep_f")
    Call<String> postlink_rep_f(@Body String body);

    @POST("hide_sym")
    Call<String> postlink_sym(@Body String body);

//    @POST("hide_sym_f")
//    Call<String> postlink_sym_f(@Body String body);

    @POST("form")
    Call<RequestBody> form(@Body RequestBody body);


}