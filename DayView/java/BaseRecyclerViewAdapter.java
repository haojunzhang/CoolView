package com.example.dragdropview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context context;
    private List<T> list;
    private int layoutId;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public BaseRecyclerViewAdapter(Context context, int layoutId) {
        this.context = context;
        this.list = new ArrayList<>();
        this.layoutId = layoutId;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        list = data;
        notifyDataSetChanged();
    }

    public void append(T data) {
        list.add(data);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return list;
    }

    public void addAll(List<T> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        convert(context, holder, list.get(position));
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, position);
                }
            });
        }
        if (itemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemLongClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
    }

    protected abstract void convert(Context context, BaseViewHolder holder, T t);


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setonLongItemClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }
}