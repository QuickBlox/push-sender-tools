package com.quickblox.push_sender_tools.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.messages.model.QBPushType;
import com.quickblox.push_sender_tools.R;
import com.quickblox.push_sender_tools.models.EventParameter;
import com.quickblox.push_sender_tools.ui.adapters.ParametersAdapter;
import com.quickblox.push_sender_tools.utils.Consts;
import com.quickblox.push_sender_tools.utils.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SendPusFragment extends Fragment {
    private static final String TAG = SendPusFragment.class.getSimpleName();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String EMPTY_STRING = "";


    private static Context context;
    private boolean isAndroidBasedPushes;
    private String[] parametersArray;
    private ProgressBar createEventProgressBar;
    private RecyclerView parametersList;
    private ParametersAdapter parametersAdapter;
    private Button sendButton, clearReseivedDataButton;
    private TextView pushPreviewTextView;

    private BroadcastReceiver pushBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(Consts.EXTRA_GCM_MESSAGE);
            Log.i(TAG, "Receiving event " + Consts.ACTION_NEW_GCM_EVENT + " with data: " + message);
            pushPreviewTextView.setText(prepareTextFromExtras(intent.getExtras()));
        }
    };

    public SendPusFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SendPusFragment newInstance(Context context, int sectionNumber) {
        SendPusFragment.context = context;
        SendPusFragment fragment = new SendPusFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isAndroidBasedPushes = getArguments().getInt(ARG_SECTION_NUMBER) == 0;
        View rootView = inflater.inflate(R.layout.fragment_parameters_list, container, false);

        parametersList = (RecyclerView) rootView.findViewById(R.id.parameters_list_recycle_view);
        parametersList.setLayoutManager(new LinearLayoutManager(getContext()));

        parametersArray = getResources().getStringArray(isAndroidBasedPushes
                ? R.array.android_based_parameters
                : R.array.universal_parameters);

        parametersAdapter = new ParametersAdapter(context, buildBlankParametersList(parametersArray));

        parametersList.setAdapter(parametersAdapter);
        parametersAdapter.notifyDataSetChanged();

        sendButton = (Button) rootView.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEvent();
            }
        });

        clearReseivedDataButton = (Button) rootView.findViewById(R.id.clear_received_data);
        clearReseivedDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushPreviewTextView.setText(EMPTY_STRING);
            }
        });

        createEventProgressBar = (ProgressBar) rootView.findViewById(R.id.create_event_progress_bar);

        pushPreviewTextView = (TextView) rootView.findViewById(R.id.push_preview_text_view);

        registerReceiver();
        return rootView;
    }

    public void sendEvent() {
        createEventProgressBar.setVisibility(View.VISIBLE);
        QBPushNotifications.createEvent(createEvent()).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {
                createEventProgressBar.setVisibility(View.GONE);
                clearParametersAdapter();

            }

            @Override
            public void onError(QBResponseException e) {
                createEventProgressBar.setVisibility(View.GONE);
                Toaster.longToast("Create event error: " + e.getMessage());
            }
        });
    }

    private QBEvent createEvent() {
        QBEvent event = new QBEvent();
        event.setEnvironment(QBEnvironment.DEVELOPMENT);
        event.setNotificationType(QBNotificationType.PUSH);

        StringifyArrayList<Integer> userIds = new StringifyArrayList<>();
        userIds.add(QBSessionManager.getInstance().getSessionParameters().getUserId());
        event.setUserIds(userIds);

        if (isAndroidBasedPushes) {
            event.setPushType(QBPushType.GCM);
            event.setMessage(collectMapEnteredParameters());
        } else {
            event.setMessage(collectJsonEnteredParameters().toString());
        }

        return event;
    }

    private HashMap<String, Object> collectMapEnteredParameters() {
        HashMap<String, Object> parameters = new HashMap<>();

        ArrayList<EventParameter> eventParameters = parametersAdapter.getItems();

        for (EventParameter parameter : eventParameters) {
            if (!TextUtils.isEmpty(parameter.getParameterValue())) {
                parameters.put(parameter.getParameterName(), parameter.getParameterValue());
            }
        }

        return parameters;
    }

    private JSONObject collectJsonEnteredParameters() {
        JSONObject parameters = new JSONObject();
        ArrayList<EventParameter> eventParameters = parametersAdapter.getItems();

        for (EventParameter parameter : eventParameters) {
            if (!TextUtils.isEmpty(parameter.getParameterValue())) {
                try {
                    parameters.put(parameter.getParameterName(), parameter.getParameterValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return parameters;
    }

    private ArrayList<EventParameter> buildBlankParametersList(String[] parametersArray) {
        ArrayList<EventParameter> parameters = new ArrayList<>(parametersArray.length);

        for (String aParametersArray : parametersArray) {
            parameters.add(new EventParameter(aParametersArray, null));
        }

        return parameters;
    }

    private void clearParametersAdapter(){
        parametersAdapter.setData(buildBlankParametersList(parametersArray));
    }

    private void registerReceiver() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(pushBroadcastReceiver,
                new IntentFilter(Consts.ACTION_NEW_GCM_PUSH_RECEIVED));
    }

    private String prepareTextFromExtras(Bundle extras) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String extrasKey : extras.keySet()){
            stringBuilder.append(extrasKey).append(" = ").append(String.valueOf(extras.get(extrasKey))).append(";\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(pushBroadcastReceiver);
    }
}
