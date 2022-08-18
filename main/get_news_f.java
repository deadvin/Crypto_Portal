package com.upperhand.cryptoterminal.objects;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface get_news_f {
    @GET("news_f")
    Call<List<video>> getnews_f();

}