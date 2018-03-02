package com.nextbit.yassin.bakingapp.infrastructure.repository.datasource;

import com.nextbit.yassin.bakingapp.domain.model.RecipeList;

import java.util.List;

import rx.Observable;

/**
 * Created by yassin on 10/21/17.
 */

public interface RecipeDataStore {
    Observable<List<RecipeList>> recipeList();


}
