package com.example.cryptoterminal.main;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface get_300 {
    @GET("300")
    Call<List<post>> getPosts();
}