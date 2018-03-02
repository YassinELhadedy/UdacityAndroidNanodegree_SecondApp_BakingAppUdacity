package com.nextbit.yassin.bakingapp.presenter.frag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


import com.nextbit.yassin.bakingapp.R;
import com.nextbit.yassin.bakingapp.domain.model.RecipeList;
import com.nextbit.yassin.bakingapp.domain.repository.RecipeRepository;
import com.nextbit.yassin.bakingapp.infrastructure.cache.CacheImpl;
import com.nextbit.yassin.bakingapp.infrastructure.repository.RecipeDataRepository;
import com.nextbit.yassin.bakingapp.infrastructure.repository.datasource.RecipeDataStoreFactory;
import com.nextbit.yassin.bakingapp.presenter.adapter.BakingGridAdapter;
import com.nextbit.yassin.bakingapp.presenter.views.ScaleInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.VISIBLE;

public class RecipeCardGrid extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    public static BakingGridAdapter adapter;


    private SwipeRefreshLayout mRefreshLayout;
    private List<RecipeList> recipeListList ;
    private Animation anim;
    private  static int itemPos =0;
    LinearLayoutManager manager;




    public RecipeCardGrid() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState !=null){
            itemPos = savedInstanceState.getInt("itempostion",0);
//             Toast.makeText(getContext(), "savde is  not null "+itemPos, Toast.LENGTH_SHORT).show();

        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipe_card_grid, container, false);

        recyclerView = v.findViewById(R.id.mainGrid);
        anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        anim.setDuration(600);


        int rotation = getResources().getConfiguration().orientation;




        recipeListList = new ArrayList<>();
        mRefreshLayout =  v.findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeColors(0xff6b4d9c);
        mRefreshLayout.setRefreshing(true);

        if (isTablet(getActivity())) {
            switch (rotation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    Toast.makeText(getActivity(), "in tablet port", Toast.LENGTH_SHORT).show();
                    manager = new GridLayoutManager(getActivity(), 1);

                    recyclerView.setLayoutManager(manager);
                    getRecipe();


                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    Toast.makeText(getActivity(), "in tablet land", Toast.LENGTH_SHORT).show();
                    manager = new GridLayoutManager(getActivity(), 2);

                    recyclerView.setLayoutManager(manager);
                    getRecipe();
                    break;
            }
        } else {
            switch (rotation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    Toast.makeText(getActivity(), "in mobile port", Toast.LENGTH_SHORT).show();
                    manager = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setLayoutManager(manager);
                    getRecipe();
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    Toast.makeText(getActivity(), "in mobile land ", Toast.LENGTH_SHORT).show();
                    manager = new GridLayoutManager(getActivity(), 3);

                    recyclerView.setLayoutManager(manager);
                    getRecipe();
                    break;
            }
        }
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(true);
            getRecipe();
        });
        return v;


    }









    public  boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }
    void  getRecipe(){

        RecipeRepository recipeRepository=new RecipeDataRepository(new RecipeDataStoreFactory(getActivity(),new CacheImpl(getActivity())));
        recipeRepository.recipeSteps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<RecipeList>>() {
                    @Override
                    public void onCompleted() {
                        mRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onNext(List<RecipeList> recipeList) {
                        adapter = new BakingGridAdapter(recipeListList, getActivity());
                        final ScaleInAnimationAdapter alphaAdapter = adapterAnim(adapter);
                        recyclerView.setAdapter(alphaAdapter);
                        recyclerView.smoothScrollToPosition(itemPos);
                        recyclerView.startAnimation(anim);
                        recyclerView.setVisibility(VISIBLE);
                        adapter.updateMovies(recipeList);
                        adapter.notifyDataSetChanged();

                    }
                });

    }
    private ScaleInAnimationAdapter adapterAnim(BakingGridAdapter adapter) {
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);
        alphaAdapter.setDuration(600);
        return alphaAdapter;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
//        Toast.makeText(getContext(), "item postion saved now "+manager.findLastVisibleItemPosition()+"/  "+manager.findFirstCompletelyVisibleItemPosition()
//               +"/  "+ manager.findLastCompletelyVisibleItemPosition()
//                , Toast.LENGTH_LONG).show();

//        Parcelable recylerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
//        outState.putParcelable("recycleview",recylerViewState);

        if (manager.findFirstCompletelyVisibleItemPosition()<0){
            outState.putInt("itempostion", manager.findLastCompletelyVisibleItemPosition());
            outState.putInt("itempostion", manager.findLastVisibleItemPosition());



        }
        else {
            outState.putInt("itempostion", manager.findFirstCompletelyVisibleItemPosition());
        }
        super.onSaveInstanceState(outState);
    }
}
