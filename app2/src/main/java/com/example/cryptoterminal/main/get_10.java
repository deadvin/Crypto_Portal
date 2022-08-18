package com.upperhand.cryptoterminal.objects;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface get_10 {
    @GET("10")
    Call<List<post>> getPosts();
}