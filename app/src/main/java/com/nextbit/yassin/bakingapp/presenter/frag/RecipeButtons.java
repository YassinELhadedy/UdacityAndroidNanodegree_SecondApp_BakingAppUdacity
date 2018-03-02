package com.nextbit.yassin.bakingapp.presenter.frag;


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

import com.nextbit.yassin.bakingapp.R;
import com.nextbit.yassin.bakingapp.domain.model.RecipeList;
import com.nextbit.yassin.bakingapp.domain.repository.RecipeRepository;
import com.nextbit.yassin.bakingapp.infrastructure.cache.CacheImpl;
import com.nextbit.yassin.bakingapp.infrastructure.repository.RecipeDataRepository;
import com.nextbit.yassin.bakingapp.infrastructure.repository.datasource.RecipeDataStoreFactory;
import com.nextbit.yassin.bakingapp.presenter.adapter.RecipeStepsAdapter;
import com.nextbit.yassin.bakingapp.presenter.views.ScaleInAnimationAdapter;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.VISIBLE;
import static com.nextbit.yassin.bakingapp.presenter.activity.RecipeDetails.EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET;

public class RecipeButtons extends Fragment {
    private int clickpos ;
    private RecipeStepsAdapter adapter;
    private RecyclerView recyclerView;
    private Animation anim;
    private LinearLayoutManager manger;
    private  static int itemPos2 = 0;
    private SwipeRefreshLayout mRefreshLayout;







    public RecipeButtons() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState !=null){
            itemPos2 = savedInstanceState.getInt("itempostion2",0);


        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_buttons, container, false);
        recyclerView= view.findViewById(R.id.mainsteps);
        anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        anim.setDuration(600);
        mRefreshLayout =  view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeColors(0xff6b4d9c);
        mRefreshLayout.setRefreshing(true);        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args!=null) {
            clickpos = args.getInt(EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET);
        }
        manger = new GridLayoutManager(getActivity(),1);


        getRecipe();
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(true);
            getRecipe();
        });

        return view;
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


                        adapter = new RecipeStepsAdapter(recipeList, getActivity(),clickpos);
                        final ScaleInAnimationAdapter alphaAdapter = adapterAnim(adapter);
                        recyclerView.setLayoutManager(manger);
                        recyclerView.setAdapter(alphaAdapter);
                        recyclerView.smoothScrollToPosition(itemPos2);
                        recyclerView.startAnimation(anim);
                        recyclerView.setVisibility(VISIBLE);
                        adapter.updateMovies(recipeList);
                        adapter.notifyDataSetChanged();

                    }
                });

    }
    private ScaleInAnimationAdapter adapterAnim(RecipeStepsAdapter adapter) {
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

        if (manger.findFirstCompletelyVisibleItemPosition()<0){
            outState.putInt("itempostion2", manger.findLastCompletelyVisibleItemPosition());
            outState.putInt("itempostion2", manger.findLastVisibleItemPosition());



        }
        else {
            outState.putInt("itempostion2", manger.findFirstCompletelyVisibleItemPosition());
        }
        super.onSaveInstanceState(outState);
    }


}
