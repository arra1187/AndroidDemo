package com.example.androiddemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataModelRecyclerViewAdapter
        extends RecyclerView.Adapter<DataModelRecyclerViewAdapter.ViewHolder>
{
    ArrayList<DataModel> mData;

    public DataModelRecyclerViewAdapter(ArrayList<DataModel> data)
    {
        mData = data;
    }

    @NonNull
    @Override
    public DataModelRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_display, parent,
                                                          false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataModelRecyclerViewAdapter.ViewHolder holder,
                                 int position)
    {
        holder.setDataModel(mData.get(position));
        holder.bindData();
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private DataModel mDataModel;
        private TextView mTitle;
        private TextView mData;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }

        public void setDataModel(DataModel data)
        {
            mDataModel = data;
        }

        public DataModel getDataModel()
        {
            return mDataModel;
        }

        public void bindData()
        {
            if(mTitle == null)
            {
                mTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            }

            if(mData == null)
            {
                mData = (TextView) itemView.findViewById(R.id.textViewData);
            }

            mTitle.setText(mDataModel.getTitle());
            mData.setText(mDataModel.getData());
        }
    }
}
