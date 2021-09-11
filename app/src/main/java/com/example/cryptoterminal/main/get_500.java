package com.example.cryptoterminal.main;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface get_500 {
    @GET("500")
    Call<List<post>> getPosts();
}