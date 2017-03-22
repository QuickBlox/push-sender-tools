package com.quickblox.push_sender_tools.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.quickblox.push_sender_tools.R;

import java.util.ArrayList;

public class ParametersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<String> parameters;
    private final Context context;

    public ParametersAdapter(Context context, ArrayList<String> parameters) {
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
        ((ViewHolder) holder).parameterValue.setHint(parameters.get(position));
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText parameterValue;

        public ViewHolder(View itemView) {
            super(itemView);
            parameterValue = (EditText) itemView.findViewById(R.id.parameter_value);
        }
    }
}
