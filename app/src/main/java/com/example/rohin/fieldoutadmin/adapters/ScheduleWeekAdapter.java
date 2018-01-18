package com.example.rohin.fieldoutadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.ScheduleWeekDetails;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Rohin on 13-12-2017.
 */

public class ScheduleWeekAdapter extends RecyclerView.Adapter<ScheduleWeekAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;


    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView week_date,week_first_last_name,week_start_time,week_site_name,week_description,week_address,
                week_mob_number,week_status;
        public CardView week_jobs_card;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            week_date=itemView.findViewById(R.id.week_date);
            week_jobs_card=itemView.findViewById(R.id.week_jobs_card);
            week_first_last_name=itemView.findViewById(R.id.week_first_last_name);
            week_start_time=itemView.findViewById(R.id.week_start_time);
            week_site_name=itemView.findViewById(R.id.week_site_name);
            week_description=itemView.findViewById(R.id.week_description);
            week_address=itemView.findViewById(R.id.week_address);
            week_mob_number=itemView.findViewById(R.id.week_mob_number);
            week_status=itemView.findViewById(R.id.week_status);

        }
    }
    public ScheduleWeekAdapter(Context mContext, ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public ScheduleWeekAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_frag_week_schedule,parent,false);

        return new ScheduleWeekAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(ScheduleWeekAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        final String sitename=commonJobs.getSitename();
        String firstname=commonJobs.getFirstname();
        String lastname=commonJobs.getLastname();
        String priority=commonJobs.getPriority();
        String contactname=commonJobs.getContactname();
        String mobile=commonJobs.getMobilenum();
        String jobid=commonJobs.getJobid();
        String phone=commonJobs.getPhone();
        String email=commonJobs.getEmail();
        String myid=commonJobs.getMyId();
        String jobtype=commonJobs.getJobTypeName();
        String desc=commonJobs.getDescription();
        String latlng=commonJobs.getLatlng();
        String startdate=commonJobs.getScheduledBeginDate();
        String time1=startdate.substring(11);
        String enddate=commonJobs.getScheduleenddate();
        String time2=enddate.substring(11);
        String address=commonJobs.getAddress();
        String status=commonJobs.getStatus();
        String date1=startdate.substring(0,enddate.length()-9);
        StringTokenizer st=new StringTokenizer(date1,"-");
        final String year=st.nextToken();
        final String month=st.nextToken();
        final String dates=st.nextToken();
        String fmonth=null;
        switch (month){
            case "01":
                fmonth="January";
                break;
            case "02":
                fmonth="February";
                break;
            case "03":
                fmonth="March";
                break;
            case "04":
                fmonth="April";
                break;
            case "05":
                fmonth="May";
                break;
            case "06":
                fmonth="June";
                break;
            case "07":
                fmonth="July";
                break;
            case "08":
                fmonth="August";
                break;
            case "09":
                fmonth="September";
                break;
            case "10":
                fmonth="October";
                break;
            case "11":
                fmonth="November";
                break;
            case "12":
                fmonth="December";
                break;
        }

        holder.week_jobs_card.setOnClickListener(view -> {
            Intent i=new Intent(mContext, ScheduleWeekDetails.class);
            i.putExtra("jobid",jobid);
            i.putExtra("myid",myid);
            i.putExtra("status",status);
            i.putExtra("address",address);
            i.putExtra("jobtypename",jobtype);
            i.putExtra("priority",priority);
            i.putExtra("contactname",contactname);
            i.putExtra("mobile",mobile);
            i.putExtra("phone",phone);
            i.putExtra("email",email);
            i.putExtra("desc",desc);
            i.putExtra("latlng",latlng);
            mContext.startActivity(i);
        });

        holder.week_site_name.setText(sitename);
        holder.week_date.setText(dates+" "+fmonth+" "+year);
        holder.week_first_last_name.setText(firstname+lastname);
        holder.week_start_time.setText(time1+" - "+time2);
        holder.week_description.setText(desc);
        holder.week_address.setText(address);
        holder.week_mob_number.setText(mobile);
        holder.week_status.setText(status);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}