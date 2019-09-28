package com.nade.mywikiapplication.api;

import com.nade.mywikiapplication.models.Query;
import com.nade.mywikiapplication.models.Random;
import com.nade.mywikiapplication.models.WikiArticle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("w/api.php?action=query&list=random&format=json&rnnamespace=0&rnlimit=10")
    Call<WikiArticle> getRandArticle();
}
