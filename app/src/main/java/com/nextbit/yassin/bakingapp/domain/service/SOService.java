package com.nextbit.yassin.bakingapp.domain.service;



import com.nextbit.yassin.bakingapp.domain.model.RecipeList;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;




public interface SOService {


        @GET("baking.json")
        Observable<List<RecipeList>> getRecipeList();


}