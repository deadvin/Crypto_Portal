package com.upperhand.cryptoterminal.interfaces;

import com.upperhand.cryptoterminal.objects.event;
import com.upperhand.cryptoterminal.objects.div;
import com.upperhand.cryptoterminal.objects.last_price;
import com.upperhand.cryptoterminal.objects.tweet;
import com.upperhand.cryptoterminal.objects.word;
import com.upperhand.cryptoterminal.objects.video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface get_data {
    @GET("1")
    Call<List<tweet>> get_1();

    @GET("10")
    Call<List<tweet>> get_10();

    @GET("100")
    Call<List<tweet>> get_100();

    @GET("500")
    Call<List<tweet>> get_500();

    @GET("rep")
    Call<List<tweet>> get_rep();

    @GET("rep_f")
    Call<List<tweet>> get_rep_f();

    @GET("sym")
    Call<List<tweet>> get_sym();

    @GET("sym_f")
    Call<List<tweet>> get_sym_f();

    @GET("videos")
    Call<List<video>> get_video();

    @GET("event")
    Call<List<event>> get_events();

    @GET("news")
    Call<List<video>> get_news();

    @GET("news_f")
    Call<List<video>> get_news_f();

    @GET("trends")
    Call<List<word>> get_trend();

    @GET("trends_btc")
    Call<List<word>> get_trend_btc();

    @GET("div_1m")
    Call<List<div>> get_div_1m();

    @GET("div_15m")
    Call<List<div>> get_div_15m();

    @GET("div_1d")
    Call<List<div>> get_div_1d();

    @GET("div_1h")
    Call<List<div>> get_div_1h();

    @GET
    Call<List<last_price>> get_last_price(@Url String url);



}