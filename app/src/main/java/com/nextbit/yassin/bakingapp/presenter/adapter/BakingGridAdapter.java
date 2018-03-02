package com.nextbit.yassin.bakingapp.presenter.adapter;


import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.glidepalette.GlidePalette;
import com.nextbit.yassin.bakingapp.R;
import com.nextbit.yassin.bakingapp.domain.model.RecipeList;
import com.nextbit.yassin.bakingapp.presenter.activity.RecipeDetails;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.nextbit.yassin.bakingapp.presenter.activity.RecipeDetails.EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET;
import static com.nextbit.yassin.bakingapp.presenter.activity.RecipeDetails.EXTRA_RECIPE_Name_CLICKED_FROM_MAINACT_OR_WIDGET;

public class BakingGridAdapter extends RecyclerView.Adapter<BakingGridAdapter.PosterHolder>{
    private List<RecipeList> recipeListList;
    private Context context;
    private int[] covers;

    public BakingGridAdapter(List<RecipeList> recipeListList, Context context) {
        this.recipeListList = recipeListList;
        this.context = context;
        covers = new int[]{
                R.drawable.nuttella,
                R.drawable.browins,
                R.drawable.yellowcake,
                R.drawable.chessacake
               };
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);

        return new PosterHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(PosterHolder holder, int position) {
        RecipeList r=recipeListList.get(position);
        holder.fav.setVisibility(VISIBLE);
        holder.unFav.setVisibility(GONE);
        holder.fav.invalidate();
        holder.unFav.invalidate();


        String tit="  Just "+r.getIngredients().size()+" Ingredients"+" &  Making "+r.getSteps().size()+" Steps";

        holder.posterTitle.setTextColor(Color.BLACK);
        holder.posterYear.setTextColor(Color.BLACK);
        holder.posterTitle.setText(tit);
        holder.posterTitle.setSelected(true);
        holder.posterYear.setText(r.getName());
        holder.posterTitleBackground.setBackgroundColor(GlidePalette.Profile.MUTED_DARK);
        holder.poster.setBackgroundColor(GlidePalette.Swatch.BODY_TEXT_COLOR);
        holder.poster.setImageResource(covers[position]);

    }

    @Override
    public int getItemCount() {
        return recipeListList.size();
    }

    public void updateMovies(List<RecipeList> items) {
        recipeListList = items;
        notifyDataSetChanged();
    }

    class PosterHolder extends RecyclerView.ViewHolder {
        CardView gridCard;
        ImageView poster, fav, unFav;
        View posterTitleBackground, dummyView;
        TextView posterTitle, posterYear;

        PosterHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.gridPoster);
            posterTitle =  itemView.findViewById(R.id.posterTitle);
            gridCard =  itemView.findViewById(R.id.gridCard);
            posterTitleBackground = itemView.findViewById(R.id.posterTitleBackground);
            fav =  itemView.findViewById(R.id.posterFav);
            unFav =  itemView.findViewById(R.id.posterUnFav);
            posterYear =  itemView.findViewById(R.id.posterYear);
            dummyView = itemView.findViewById(R.id.dummyView);

            itemView.setOnClickListener((View view) -> {
                int clickPos=getAdapterPosition();
try {
    context.startActivity(new Intent(context, RecipeDetails.class)
            .putExtra(EXTRA_RECIPE_ID_CLICKED_FROM_MAINACT_OR_WIDGET,clickPos)
            .putExtra(EXTRA_RECIPE_Name_CLICKED_FROM_MAINACT_OR_WIDGET,recipeListList.get(clickPos).getName())
    );
}catch (Exception ignored){}
            });
        }
    }

}
