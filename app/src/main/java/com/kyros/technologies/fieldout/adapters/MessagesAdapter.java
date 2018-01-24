package com.kyros.technologies.fieldout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Rohin on 19-01-2018.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;

    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView msg_title,msg_body,msg_sent,msg_read,msg_delete;
        public LinearLayout messages_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            msg_title=itemView.findViewById(R.id.msg_title);
            msg_body=itemView.findViewById(R.id.msg_body);
            msg_sent=itemView.findViewById(R.id.msg_sent);
            msg_read=itemView.findViewById(R.id.msg_read);
            msg_delete=itemView.findViewById(R.id.msg_delete);
            messages_linear=itemView.findViewById(R.id.messages_linear);

        }
    }
    public MessagesAdapter(Context mContext, ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public MessagesAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_messages_list,parent,false);

        return new MessagesAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String msgtitle=commonJobs.getMessagetitle();
        String msgbody=commonJobs.getMessagebody();
        int issent=commonJobs.getIssent();
        int isread=commonJobs.getIsread();
        int isdelete=commonJobs.getIsdelete();

        holder.msg_title.setText(msgtitle);
        holder.msg_body.setText(msgbody);
        holder.msg_sent.setText(String.valueOf(issent));
        holder.msg_read.setText(String.valueOf(isread));
        holder.msg_delete.setText(String.valueOf(isdelete));

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}