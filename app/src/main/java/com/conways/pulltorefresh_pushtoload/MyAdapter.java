package com.conways.pulltorefresh_pushtoload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Conways on 2017/7/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder>{

    Context context;
    List<Integer> list;
    public MyAdapter(Context context,List<Integer> list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public MyAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyAdapter.Holder holder, int position) {
        holder.tvPosition.setText(list.get(position).intValue()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView tvPosition;
        public Holder(View itemView) {
            super(itemView);
            tvPosition=(TextView)itemView.findViewById(R.id.tv);
        }
    }

}
