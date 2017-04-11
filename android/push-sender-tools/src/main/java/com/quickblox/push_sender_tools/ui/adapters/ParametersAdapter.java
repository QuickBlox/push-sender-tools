package com.quickblox.push_sender_tools.ui.adapters;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.quickblox.push_sender_tools.R;
import com.quickblox.push_sender_tools.models.EventParameter;

import java.util.ArrayList;

public class ParametersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<EventParameter> parameters;
    private final Context context;

    public ParametersAdapter(Context context, ArrayList<EventParameter> parameters) {
        this.context = context;
        this.parameters = parameters;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parameter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder currentHolder = (ViewHolder) holder;
        final EditText parameterValue = currentHolder.parameterValue;

        final EventParameter currentParameter = parameters.get(position);

        if (TextUtils.isEmpty(currentParameter.getParameterValue())){
            parameterValue.setHint(currentParameter.getParameterName());
        } else {
            parameterValue.setText(currentParameter.getParameterValue());
        }

        parameterValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentParameter.setParameterValue(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return parameters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText parameterValue;

        public ViewHolder(View itemView) {
            super(itemView);
            parameterValue = (EditText) itemView.findViewById(R.id.parameter_value);
        }

        public String getParameterValue(){
            return String.valueOf(parameterValue.getText());
        }
    }
}
