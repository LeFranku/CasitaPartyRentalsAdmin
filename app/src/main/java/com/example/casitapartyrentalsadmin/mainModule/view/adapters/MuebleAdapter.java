package com.example.casitapartyrentalsadmin.mainModule.view.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.R;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MuebleAdapter extends RecyclerView.Adapter<MuebleAdapter.ViewHolder> {

    private List<Mueble> muebles;
    private OnItemClickListener listener;
    private Context context;

    public MuebleAdapter(List<Mueble> muebles, OnItemClickListener listener) {
        this.muebles = muebles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mueble,parent,false);
        context=parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mueble mueble= muebles.get(position);
        holder.setOnClickListener(mueble,listener);

        holder.card_title.setText(mueble.getName().toString());
        holder.card_description.setText(mueble.getDescription().toString());
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop();
        Glide.with(context)
                .load(mueble.getPhotoURL())
                .apply(options)
                .into(holder.imagePhoto);
    }

    @Override
    public int getItemCount() {
        return muebles.size();
    }

    public void add(Mueble mueble){
        if (!muebles.contains(mueble)){
            muebles.add(mueble);
            notifyItemInserted(muebles.size()-1);
        }else{
            update(mueble);
        }
    }

    public void update(Mueble mueble) {
        if (muebles.contains(mueble)){
          final int index=  muebles.indexOf(mueble);
            muebles.set(index,mueble);
            notifyItemChanged(index);
        }
    }
    public void remove(Mueble mueble){
        if (muebles.contains(mueble)){
            final int index=  muebles.indexOf(mueble);
            muebles.remove(index);
            notifyItemRemoved(index);
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imagePhoto)
        ImageView imagePhoto;
        @BindView(R.id.card_title)
        TextView card_title;
        @BindView(R.id.card_description)
        TextView card_description;


        private View view;

         ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view=itemView;
        }

         void setOnClickListener(Mueble mueble, OnItemClickListener listener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(mueble);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongItemClick(mueble);
                    return true;
                }
            });
        }
    }
}
