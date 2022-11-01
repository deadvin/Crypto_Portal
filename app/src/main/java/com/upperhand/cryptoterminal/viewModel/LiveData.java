//package com.upperhand.cryptoterminal.viewModel;
//
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.upperhand.cryptoterminal.objects.div;
//import com.upperhand.cryptoterminal.objects.event;
//import com.upperhand.cryptoterminal.objects.lastPrice;
//import com.upperhand.cryptoterminal.objects.tweet;
//import com.upperhand.cryptoterminal.objects.video;
//import com.upperhand.cryptoterminal.objects.word;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class LiveData extends ViewModel {
//
//    private MutableLiveData<List<tweet>> tweetsAll_1;
//    private MutableLiveData<List<tweet>> tweetsAll_100;
//    private MutableLiveData<List<tweet>> tweetsAll_10;
//    private MutableLiveData<List<tweet>> tweetsAll_500;
//    private MutableLiveData<List<tweet>> tweetsSym;
//    private MutableLiveData<List<tweet>> tweetsSymFiltered;
//    private MutableLiveData<List<tweet>> tweetsRep;
//    private MutableLiveData<List<tweet>> tweetsRepFiltered;
//
//    public LiveData(){
//        tweetsAll_1 = new MutableLiveData<>();
//        tweetsAll_100 = new MutableLiveData<>();
//        tweetsAll_10 = new MutableLiveData<>();
//        tweetsAll_500 = new MutableLiveData<>();
//        tweetsSym = new MutableLiveData<>();
//        tweetsSymFiltered = new MutableLiveData<>();
//        tweetsRep = new MutableLiveData<>();
//        tweetsRepFiltered = new MutableLiveData<>();
//    }
//
//    public MutableLiveData<List<MovieModel>> getMoviesListObserver() {
//        return moviesList;
//
//    }
//
//    public void makeApiCall() {
//        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
//        Call<List<MovieModel>> call = apiService.getMovieList();
//        call.enqueue(new Callback<List<MovieModel>>() {
//            @Override
//            public void onResponse(Call<List<MovieModel>> call, Response<List<MovieModel>> response) {
//                tweetsAll_1.postValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<List<MovieModel>> call, Throwable t) {
//                moviesList.postValue(null);
//            }
//        });
//    }
//}
