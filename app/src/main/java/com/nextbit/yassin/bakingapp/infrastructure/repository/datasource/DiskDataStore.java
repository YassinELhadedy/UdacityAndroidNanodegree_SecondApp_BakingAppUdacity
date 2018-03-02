package com.nextbit.yassin.bakingapp.infrastructure.repository.datasource;




import com.nextbit.yassin.bakingapp.domain.model.RecipeList;
import com.nextbit.yassin.bakingapp.infrastructure.cache.EntityCache;

import java.util.List;

import rx.Observable;

/**
 * Created by yassin on 10/8/17.
 */

public class DiskDataStore implements RecipeDataStore {
    private final EntityCache entityCache;

    public DiskDataStore(EntityCache entityCache) {
        this.entityCache = entityCache;
    }





    @Override
    public Observable<List<RecipeList>> recipeList() {
        return entityCache.getRecipesCache();
    }
}
