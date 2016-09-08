package com.example.yehu.practice1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yehu on 7/25/16.
 */
public class AdapterRecyclerItemAnimation extends RecyclerView.Adapter<AdapterRecyclerItemAnimation.Holder> {

    private ArrayList<String> mListData = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context context;
    public AdapterRecyclerItemAnimation(Context context){
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = mLayoutInflater.inflate(R.layout.custom_row_item_animations, parent, false);
        Holder holder = new Holder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {

        String data = mListData.get(position);
        holder.textDataItem.setText(data);

        Toast.makeText(context, holder.textDataItem.getText(), Toast.LENGTH_SHORT).show();
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public void addItem(String item){
        mListData.add(item);
        notifyItemInserted(mListData.size());
    }

    public void removeItem(String item){
        int position = mListData.indexOf(item);
        if(position != -1){
            mListData.remove(item);
            notifyItemRemoved(position);
        }
    }

    public void removeItem(int position){
        mListData.remove(position);
        notifyItemRemoved(position);
    }

    public static class Holder extends RecyclerView.ViewHolder{
        TextView textDataItem;
        ImageButton buttonDelete;

        public Holder(View itemView) {
            super(itemView);
            textDataItem = (TextView) itemView.findViewById(R.id.text_item);
            buttonDelete = (ImageButton) itemView.findViewById(R.id.button_delete);

        }


    }

}
