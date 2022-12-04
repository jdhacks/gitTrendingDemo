package com.github.display.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.display.R;
import com.github.display.Utility.Utility;
import com.github.display.models.Item;
import com.github.display.models.Owner;
import com.github.display.ui.MainActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

public class GitRepoAdapter extends RecyclerView.Adapter<GitRepoAdapter.MyViewHolder> {


    private ArrayList<Item> productsListByCategories;
    private Context context;
    private AppCompatActivity fragmentActivity;

    public GitRepoAdapter(ArrayList<Item> productsListByCategories, AppCompatActivity fragmentActivity) {
        this.productsListByCategories = productsListByCategories;
        this.fragmentActivity = fragmentActivity;
    }

    public void setProductsListByCategories(ArrayList<Item> productsListByCategories) {
        this.productsListByCategories = productsListByCategories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_repo, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Owner item = productsListByCategories.get(position).getOwner();
        Log.d("okhttp", "oneshot: response" + new Gson().toJson(item));

        if (item.getSelected()) {
            holder.imgSelected.setImageResource(R.drawable.ic_selected);
        } else {
            holder.imgSelected.setImageResource(R.drawable.ic_unselected);
        }
        holder.imgSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getSelected()) {
                    MainActivity.repolist.get(holder.getAdapterPosition()).getOwner().setSelected(false);
                    item.setSelected(false);
                    holder.imgSelected.setImageResource(R.drawable.ic_unselected);
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    MainActivity.repolist.get(holder.getAdapterPosition()).getOwner().setSelected(true);
                    item.setSelected(true);
                    holder.imgSelected.setImageResource(R.drawable.ic_selected);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
        holder.carditem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (item.getSelected()) {
                    MainActivity.repolist.get(holder.getAdapterPosition()).getOwner().setSelected(false);
                    item.setSelected(false);
                    holder.imgSelected.setImageResource(R.drawable.ic_unselected);
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    MainActivity.repolist.get(holder.getAdapterPosition()).getOwner().setSelected(true);
                    item.setSelected(true);
                    holder.imgSelected.setImageResource(R.drawable.ic_selected);
                    notifyItemChanged(holder.getAdapterPosition());
                }

                return false;
            }
        });

        holder.txtUserName.setText(item.getLogin());
        if (item.getAvatarUrl() != null)
            Glide.with(context)
                    .load(item.getAvatarUrl())
                    .placeholder(Utility.getCircularProgressDrawable(context))
                    .error(R.drawable.ic_user_img)
                    .apply(new RequestOptions().fitCenter())
                    .apply(new RequestOptions()).into(holder.imgUser);

    }

    @Override
    public int getItemCount() {
        return productsListByCategories.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgUser, imgSelected;
        private AppCompatTextView txtUserName;
        ConstraintLayout root1;
        CardView carditem;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgSelected = itemView.findViewById(R.id.selectedImg);
            imgUser = itemView.findViewById(R.id.imgUser);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            root1 = itemView.findViewById(R.id.root1);
            carditem = itemView.findViewById(R.id.cardView1);
        }
    }

}