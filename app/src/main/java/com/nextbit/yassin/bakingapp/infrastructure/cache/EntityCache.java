package com.nextbit.yassin.bakingapp.infrastructure.cache;

import com.nextbit.yassin.bakingapp.domain.model.RecipeList;

import java.util.List;

import rx.Observable;

/**
 * Created by yassin on 10/21/17.
 */

public interface EntityCache {
    Observable<List<RecipeList>> getRecipesCache();
    void putRecipes(RecipeList recipeList);


    boolean isCached();
    void evictAll();



}
