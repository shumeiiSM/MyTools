package com.example.mytools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AllToolDisplay_Adapter extends RecyclerView.Adapter<AllToolDisplay_Adapter.BookViewHolder> {

    Context mContext;
    List<AllToolDisplay> mTool;

    public AllToolDisplay_Adapter(Context mContext, List<AllToolDisplay> mTool) {
        this.mContext = mContext;
        this.mTool = mTool;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageURL;
        public TextView name;
        public TextView avail;
        public TextView id;
        public TextView date;
        public ImageView imageAvail;
        public TextView textAvail;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            imageURL = itemView.findViewById(R.id.ivToolImage);
            name = itemView.findViewById(R.id.tvName);
            avail = itemView.findViewById(R.id.tvA);
            id = itemView.findViewById(R.id.tvID);
            date = itemView.findViewById(R.id.tvDate);
            imageAvail = itemView.findViewById(R.id.ivAvail);
            textAvail = itemView.findViewById(R.id.tvAvail);

        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.showall, parent, false);
        BookViewHolder cvh = new BookViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final BookViewHolder holder, int position) {

        final AllToolDisplay currentItem = mTool.get(position);
        holder.name.setText(currentItem.getName());

        Picasso.with(mContext).load(currentItem.getImageURL()).into(holder.imageURL);
        holder.name.setText(currentItem.getName());
        holder.id.setText(String.valueOf(currentItem.getId()));
        holder.date.setText(currentItem.getDate());

        holder.avail.setText(String.valueOf(currentItem.getAvailability()));

        Boolean state = false;

        if(currentItem.getAvailability() == state) {
            Picasso.with(mContext).load(R.drawable.ic_no).into(holder.imageAvail);
            holder.textAvail.setText("Unavailable");
        } else {
            Picasso.with(mContext).load(R.drawable.ic_yes).into(holder.imageAvail);
            holder.textAvail.setText("Available");
        }

    }

    @Override
    public int getItemCount() {
        return mTool.size();
    }


}
