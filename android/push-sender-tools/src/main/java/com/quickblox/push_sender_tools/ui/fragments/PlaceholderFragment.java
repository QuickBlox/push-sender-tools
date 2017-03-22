package com.quickblox.push_sender_tools.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickblox.push_sender_tools.R;
import com.quickblox.push_sender_tools.ui.adapters.ParametersAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static Context context;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(Context context, int sectionNumber) {
        PlaceholderFragment.context = context;
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_android_based_pushes, container, false);

        RecyclerView parametersList = (RecyclerView) rootView.findViewById(R.id.parameters_list);

        String[] parameters = getResources().getStringArray(R.array.universal_parameters);
        ArrayList<String> parametersArrayList = new ArrayList<>(Arrays.asList(parameters));

        ParametersAdapter parametersAdapter = new ParametersAdapter(context, parametersArrayList);

        parametersList.setAdapter(parametersAdapter);
        return rootView;
    }
}
