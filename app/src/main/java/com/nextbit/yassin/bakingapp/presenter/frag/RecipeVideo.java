package com.nextbit.yassin.bakingapp.presenter.frag;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nextbit.yassin.bakingapp.R;
import com.nextbit.yassin.bakingapp.domain.model.Step;
import com.nextbit.yassin.bakingapp.presenter.activity.RecipeDetails;

import java.util.ArrayList;
import java.util.List;


public class RecipeVideo extends Fragment {
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;

    private Handler mainHandler;

    private View rootView;
    private ArrayList<Step> step;
    private int clickpostion;
    private static long positionHandler = C.TIME_UNSET;


    private ListItemClickListener itemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(List<Step> allSteps, int Index);
    }

    public RecipeVideo() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //...your code...
            positionHandler = savedInstanceState.getLong("position", C.TIME_UNSET);
        }
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.recipe_step_detail_fragment_body_part, container, false);
        itemClickListener =(RecipeDetails)getActivity();



    stepFragment();


        Button mPrevStep =  rootView.findViewById(R.id.previousStep);
        Button mNextstep =  rootView.findViewById(R.id.nextStep);

        mPrevStep.setOnClickListener(view -> {
            int newClick=clickpostion-1;


            if (newClick<step.size()&&newClick>=0) {
                if (player!=null){
                    player.stop();
                }
                itemClickListener.onListItemClick(step,newClick);
            }
            else {
                Toast.makeText(getActivity(),"You already are in the First step of the recipe", Toast.LENGTH_SHORT).show();

            }
        });

        mNextstep.setOnClickListener(view -> {
            int newClick=clickpostion+1;


            if (newClick<step.size()) {
                if (player!=null){
                    player.stop();
                }
                itemClickListener.onListItemClick(step,newClick);
            }
            else {
                Toast.makeText(getContext(),"You already are in the Last step of the recipe", Toast.LENGTH_SHORT).show();

            }

        });


        return rootView;
    }

    private void stepFragment(){
        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
        Bundle args=getArguments();
        step=new ArrayList<>();
        step=args.getParcelableArrayList("steps");
        clickpostion=args.getInt("clickpos");
        TextView textView = rootView.findViewById(R.id.recipe_step_detail_text);
        textView.setText(step.get(clickpostion).getDescription());
        textView.setVisibility(View.VISIBLE);
        simpleExoPlayerView =  rootView.findViewById(R.id.playerView);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        String videoURL = step.get(clickpostion).getVideoURL();
        String imageURL=step.get(clickpostion).getThumbnailURL();
        if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail")!=null) {
            ((RecipeDetails) getActivity()).getSupportActionBar().setTitle(args.getString("description"));
        }
        if (imageURL!=null){
            Uri builtUri = Uri.parse(imageURL).buildUpon().build();
            ImageView thumbImage =  rootView.findViewById(R.id.thumbImage);
            Glide.with(getActivity()).load(builtUri).into(thumbImage);

        }

        if (videoURL!=null) {


            initializePlayer(Uri.parse(videoURL));

            if (rootView.findViewWithTag("sw600dp-land-recipe_step_detail")!=null) {
                getActivity().findViewById(R.id.fragment_container2).setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            }
            else if (isInLandscapeMode(getContext())){
                textView.setVisibility(View.GONE);
            }
        }
        else {
            player=null;
            simpleExoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_off_white_36dp));
            simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }
    }
    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            if (positionHandler != C.TIME_UNSET){ player.seekTo(positionHandler);}
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }

    }

    public boolean isInLandscapeMode( Context context ) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }
    @Override
    public void onDetach() {
        super.onDetach();

        if (player!=null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player!=null) {
            player.stop();
            player.release();
            player=null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player!=null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player!=null) {
            player.stop();
            player.release();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putLong("position",player.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }
}
