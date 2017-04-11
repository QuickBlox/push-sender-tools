package com.quickblox.push_sender_tools.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationChannel;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.push_sender_tools.R;
import com.quickblox.push_sender_tools.models.EventParameter;
import com.quickblox.push_sender_tools.ui.adapters.ParametersAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static Context context;
    private boolean isAndroidBasedPushes;
    private String[] parametersArray;
    private RecyclerView parametersList;

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
        isAndroidBasedPushes = getArguments().getInt(ARG_SECTION_NUMBER) == 0;
        View rootView = inflater.inflate(R.layout.fragment_android_based_pushes, container, false);

        parametersList = (RecyclerView) rootView.findViewById(R.id.parameters_list);
        parametersList.setLayoutManager(new LinearLayoutManager(getContext()));

        parametersArray = getResources().getStringArray(isAndroidBasedPushes
                ? R.array.android_based_parameters
                : R.array.universal_parameters);


        ParametersAdapter parametersAdapter = new ParametersAdapter(context, buildParametersList(parametersArray));

        parametersList.setAdapter(parametersAdapter);
        parametersAdapter.notifyDataSetChanged();

        return rootView;
    }

    public void sendEvent(){

    }

    private QBEvent createEvent(){
        QBEvent event = new QBEvent();
        event.setEnvironment(QBEnvironment.DEVELOPMENT);
        event.setNotificationType(QBNotificationType.PUSH);

        if (isAndroidBasedPushes) {
            event.setNotificationChannel(QBNotificationChannel.GCM);
            event.setMessage(collectMapEnteredParameters());
        } else {
            event.setMessage(collectJsonEnteredParameters().toString());
        }

        return event;
    }

    private HashMap<String, Object> collectMapEnteredParameters(){
        HashMap<String, Object> parameters = new HashMap<>();
        int childCount = parametersList.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parametersList.getChildAt(i);
            ParametersAdapter.ViewHolder childViewHolder = (ParametersAdapter.ViewHolder) parametersList.getChildViewHolder(childView);
            if (TextUtils.isEmpty(childViewHolder.getParameterValue())) {
                parameters.put(parametersArray[i], childViewHolder.getParameterValue());
            }
        }

        return parameters;
    }

    private JSONObject collectJsonEnteredParameters(){
        JSONObject parameters = new JSONObject();
        int childCount = parametersList.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parametersList.getChildAt(i);
            ParametersAdapter.ViewHolder childViewHolder = (ParametersAdapter.ViewHolder) parametersList.getChildViewHolder(childView);
            if (TextUtils.isEmpty(childViewHolder.getParameterValue())) {
                try {
                    parameters.put(parametersArray[i], childViewHolder.getParameterValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return parameters;
    }

    private ArrayList<EventParameter> buildParametersList(String[] parametersArray){
        ArrayList<EventParameter> parameters = new ArrayList<>(parametersArray.length);

        for (String aParametersArray : parametersArray) {
            parameters.add(new EventParameter(aParametersArray, null));
        }

        return parameters;
    }
}
