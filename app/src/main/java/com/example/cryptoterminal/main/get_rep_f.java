package com.example.cryptoterminal.main;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface get_rep_f {
    @GET("rep_f")
    Call<List<post>> getPosts();
}