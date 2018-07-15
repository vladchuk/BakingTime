package net.javango.bakingtime;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.javango.bakingtime.model.Step;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepActivity}
 * on handsets.
 */
public class StepFragment extends Fragment {

    private static final String ARG_STEP = "step_obj";

    private Step step;

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
        ((TextView) rootView.findViewById(R.id.step_detail)).setText(step.getDescription());
        return rootView;
    }
}
