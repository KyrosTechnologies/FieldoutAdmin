package com.kyros.technologies.fieldout.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.databinding.AdapterManagerListItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by kyros on 21-12-2017.
 */

public class ManagerListAdapter extends RecyclerView.Adapter<ManagerListAdapter.MyManagerViewHolder> {
    private Context mContext;
    private List<String>managersList;
   private AdapterManagerListItemBinding binding;
    public ManagerListAdapter(Context mContext, List<String> managersList) {
        this.mContext = mContext;
        this.managersList = managersList;
    }
    public ManagerListAdapter(Context mContext,Set<String > skillList){
        this.mContext=mContext;
        this.managersList=new ArrayList<>(skillList);
    }

    public class MyManagerViewHolder extends RecyclerView.ViewHolder{

        public MyManagerViewHolder(AdapterManagerListItemBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }
    @Override
    public MyManagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        AdapterManagerListItemBinding view= DataBindingUtil.inflate(layoutInflater, R.layout.adapter_manager_list_item,parent,false);
        return new MyManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyManagerViewHolder holder, int position) {
        String name=managersList.get(position);
        if(name!=null){
            binding.managerItemName.setText(name);
        }
        binding.removeManagerList.setOnClickListener(view -> {
            if(position<managersList.size())
            managersList.remove(position);
            notifyItemRemoved(position);
//            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return managersList.size();
    }
    public List<String>getManagersList(){
        return managersList;
    }
}
