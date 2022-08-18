package com.upperhand.cryptoterminal.objects;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface get_data {
    @GET("1")
    Call<List<post>> get_1();

    @GET("10")
    Call<List<post>> get_10();

    @GET("100")
    Call<List<post>> get_100();

    @GET("500")
    Call<List<post>> get_500();

    @GET("rep")
    Call<List<post>> get_rep();

    @GET("rep_f")
    Call<List<post>> get_rep_f();

    @GET("sym")
    Call<List<post>> get_sym();

    @GET("sym_f")
    Call<List<post>> get_sym_f();

    @GET("videos")
    Call<List<post>> get_video();

    @GET("events")
    Call<List<post>> get_events();

    @GET("news")
    Call<List<post>> get_news();

    @GET("news_f")
    Call<List<post>> get_news_f();

    @GET("trends")
    Call<List<trend>> get_trend();


}