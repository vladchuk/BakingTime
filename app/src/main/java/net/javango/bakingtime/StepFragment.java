package net.javango.bakingtime;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import net.javango.bakingtime.model.Step;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepPagerActivity}
 * on handsets.
 */
public class StepFragment extends Fragment {

    private static final String ARG_STEP = "step_obj";

    private Step step;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private TextView noMediaView;
    private ImageView stepImage;

    public static StepFragment newInstance(Step step) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STEP, step);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        step = (Step) getArguments().getSerializable(ARG_STEP);
        getActivity().setTitle(step.getShortDescription());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        TextView descrView = rootView.findViewById(R.id.step_detail);
        descrView.setText(step.getDescription());
        playerView = rootView.findViewById(R.id.player_view);
        noMediaView = rootView.findViewById(R.id.no_media_view);
        stepImage = rootView.findViewById(R.id.step_image);
        setupMedia();
        return rootView;
    }

    private void setupMedia() {
        if (step.getVideoUrl().length() > 0) {
            setVisible(true, false, false);
            Uri mediaUri = Uri.parse(step.getVideoUrl());
            setupPlayer(mediaUri);
        } else {
            setVisible(false, false, true);
        }
        if (step.getThumbnailUrl().length() > 0) {
            Picasso.with(getContext()).
                    load(step.getThumbnailUrl()).
            into(stepImage);
        }
        else {
            stepImage.setVisibility(View.GONE);
        }
    }

    // controls visibility of the media, only one parameter can be true
    private void setVisible(boolean video, boolean image, boolean noMedia) {
        playerView.setVisibility(video ? View.VISIBLE : View.GONE);
        noMediaView.setVisibility(noMedia ? View.VISIBLE : View.GONE);
    }

    private void setupPlayer(Uri mediaUri) {
        if (player == null) {
            Context context = getActivity();

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(player);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(context, "BakingTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
        }
    }

    @Override
    public void setUserVisibleHint(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
