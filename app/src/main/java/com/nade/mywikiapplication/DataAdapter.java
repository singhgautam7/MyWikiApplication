package com.nade.mywikiapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nade.mywikiapplication.models.Random;


import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<Random> randomList;

    public DataAdapter(List<Random> randomList) {
        this.randomList = randomList;
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tv_title.setText(randomList.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return randomList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title;
        public ViewHolder(View view) {
            super(view);

            tv_title = (TextView)view.findViewById(R.id.tv_title);

        }
    }
}
