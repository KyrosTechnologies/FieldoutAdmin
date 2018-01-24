package com.kyros.technologies.fieldout.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.databinding.TeamsListItemsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by kyros on 18-12-2017.
 */

public class SkilledTradersAdapter extends RecyclerView.Adapter<SkilledTradersAdapter.MyViewHolder> {
    private Context mContext;
    private List<String>skillList;
   private TeamsListItemsBinding binding;
    public SkilledTradersAdapter(Context mContext, List<String> skillList) {
        this.mContext = mContext;
        this.skillList = skillList;
    }
    public SkilledTradersAdapter(Context mContext,Set<String >skillList){
        this.mContext=mContext;
        this.skillList=new ArrayList<>(skillList);
    }
    public SkilledTradersAdapter(){}
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        TeamsListItemsBinding view= DataBindingUtil.inflate(layoutInflater,R.layout.teams_list_items,parent,false);
        return new ItemViewHolderBackdrop(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindTo(skillList.get(position));
        Log.d("Skilled Traders : ","Name : "+skillList.get(position)+" / Position: "+position);
        binding.teamNameList.setText(skillList.get(position));
        binding.deleteListValues.setOnClickListener(view -> {
            if(position<=skillList.size()){
                removeItem(holder.getAdapterPosition());
            }

        });

    }


    public List<String> getSkilledTradersList(){
        return skillList;
    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }
    abstract class MyViewHolder extends RecyclerView.ViewHolder{
        MyViewHolder(View view){
            super(view);
        }
        abstract void bindTo(String skillTradres);
    }
    public class ItemViewHolderBackdrop extends MyViewHolder{

        ItemViewHolderBackdrop(TeamsListItemsBinding view) {
            super(view.getRoot());
            binding=view;
        }

        @Override
        void bindTo(String skilledTraders) {
            binding.executePendingBindings();

        }


    }
    public void setSkilledTradersName(String skilledTradersName){
        skillList.add(skilledTradersName);
        notifyDataSetChanged();
    }
    public void setSkilledSetList(Set<String>skilledSetList){
        this.skillList=new ArrayList<>(skilledSetList);
        notifyDataSetChanged();
    }
    private void removeItem(int position){
        Log.d("Skilled Delete : ","Name : "+skillList.get(position)+" / Position: "+position);
        skillList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, skillList.size());
        Log.d("Skilled Size : ",""+skillList.size());
        for(String value : skillList){
            Log.d("list value : ",""+value);
        }

    }

}
