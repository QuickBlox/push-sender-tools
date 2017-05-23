package com.quickblox.push_sender_tools.ui.adapters;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.quickblox.push_sender_tools.R;
import com.quickblox.push_sender_tools.models.EventParameter;

import java.util.ArrayList;

public class ParametersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<EventParameter> parameters;
    private final Context context;

    public ParametersAdapter(Context context, ArrayList<EventParameter> parameters) {
        this.context = context;
        this.parameters = parameters;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parameter, parent, false);
        return new ViewHolder(view, new ParameterChangedListener());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder currentHolder = (ViewHolder) holder;
        currentHolder.parameterChangedListener.onParameterChanged(holder.getAdapterPosition());

        currentHolder.textInputLayout.setHint(parameters.get(holder.getAdapterPosition()).getParameterName());
        currentHolder.parameterValue.setText(parameters.get(holder.getAdapterPosition()).getParameterValue());
    }

    @Override
    public int getItemCount() {
        return parameters.size();
    }

    public ArrayList<EventParameter> getItems(){
        return parameters;
    }

    public void clearData() {
        parameters.clear();
    }

    public void setData(ArrayList<EventParameter> eventParameters) {
        parameters = eventParameters;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ParameterChangedListener parameterChangedListener;
        private final TextInputLayout textInputLayout;
        public EditText parameterValue;

        public ViewHolder(View itemView, ParameterChangedListener parameterChangedListener) {
            super(itemView);
            this.parameterValue = (EditText) itemView.findViewById(R.id.parameter_value);
            this.textInputLayout = (TextInputLayout) itemView.findViewById(R.id.textInputLayout);
            this.parameterChangedListener = parameterChangedListener;
            this.parameterValue.addTextChangedListener(parameterChangedListener);
        }

        public String getParameterValue(){
            return String.valueOf(parameterValue.getText());
        }

    }

    private class ParameterChangedListener implements TextWatcher{
        private int position;

        void onParameterChanged(int position){
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            parameters.get(position).setParameterValue(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
