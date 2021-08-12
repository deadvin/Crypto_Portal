package com.example.cryptoterminal.main;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface get_break {
    @GET("break")
    Call<List<post>> getPosts();
}