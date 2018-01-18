package com.example.rohin.fieldoutadmin.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.AddTeamActivity;
import com.example.rohin.fieldoutadmin.activity.UpdateTeamActivity;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.FragmentTeamsListBinding;
import com.example.rohin.fieldoutadmin.models.TagInfo;
import com.example.rohin.fieldoutadmin.models.TeamsItem;
import com.example.rohin.fieldoutadmin.models.TeamsResponse;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.AddUserActivityViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 20-12-2017.
 */

public class TeamsFragment extends Fragment {
    private CompositeSubscription subscription;
    private String TAG=TeamsFragment.class.getSimpleName();
    private FragmentTeamsListBinding binding;
    private PreferenceManager store;
    private View view;
    @Inject
    AddUserActivityViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_teams_list,container,false);
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectTeamsFragment(this);
        subscription=new CompositeSubscription();
        view=binding.getRoot();
        store=PreferenceManager.getInstance(getContext());
        binding.addTeamsButton.setOnClickListener(view-> startActivity(new Intent(getContext(), AddTeamActivity.class)));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        loadTeamsAPI(domainId,authKey);

    }

    private void loadTeamsAPI(String domainId, String authKey) {
        subscription.add(viewModel.getTeamResponse(domainId,authKey)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(this::responseTeam,this::errorResponseTeam,this::completedResponseTeam));
    }

    private void completedResponseTeam() {

    }

    private void errorResponseTeam(Throwable throwable) {

        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void responseTeam(TeamsResponse teamsResponse) {
          validateResponse(teamsResponse);
    }


    private void validateResponse(TeamsResponse teamsResponse) {
        if(teamsResponse!=null){
            Log.d("Team Response : ",TAG+" / / "+teamsResponse.toString());
            List<TeamsItem>teamsItemList=teamsResponse.getTeams();
            binding.teamsTableLayout.removeAllViews();
            binding.teamsTableLayout.addView(binding.rowTeamTable);
            if(teamsItemList!=null && teamsItemList.size()!=0){
                for (TeamsItem teamsItem:teamsItemList){
                    String teamName=teamsItem.getName();
                    String description=teamsItem.getDescriptions();
                    List<String>technicians=teamsItem.getTechnicians();
                    List<String>managers=teamsItem.getManagers();
                    List<String>tags=teamsItem.getTags();
                    String teamId=teamsItem.getId();
                    List<TagInfo>tagInfos=teamsItem.getTagInfo();
                    //Tables Rows
                    TableRow tableRow=new TableRow(getContext());
                    tableRow.setBackground(getResources().getDrawable(R.color.bg));
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    //TextView name
                    TextView nameTextView=new TextView(getContext());
                    if(teamName!=null){
                        nameTextView.setText(teamName);
                    }
                    nameTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowuserTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowuserTextParams.setMargins(10,10,0,10);
                    nameTextView.setLayoutParams(tableRowuserTextParams);
                    nameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    nameTextView.setTextColor(getResources().getColor(R.color.light_black));
                    nameTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(nameTextView);

                    //TextView Description
                    TextView descriptionTextView=new TextView(getContext());
                    if(description!=null){
                        descriptionTextView.setText(description);
                    }
                    descriptionTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowuserNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowuserNameParams.setMargins(10,10,0,10);
                    descriptionTextView.setLayoutParams(tableRowuserNameParams);
                    descriptionTextView.setTextColor(getResources().getColor(R.color.light_black));
                    descriptionTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    descriptionTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(descriptionTextView);

                    //Technicians list Text View
                    TextView techniciansTextView=new TextView(getContext());
                    String sizeTechnician= String.valueOf(technicians.size());
                    techniciansTextView.setText(sizeTechnician);
                    techniciansTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowlanguageParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowlanguageParams.setMargins(10,10,0,10);
                    techniciansTextView.setLayoutParams(tableRowlanguageParams);
                    techniciansTextView.setTextColor(getResources().getColor(R.color.light_black));
                    techniciansTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    techniciansTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(techniciansTextView);

                    //Manages list Text View
                    TextView managersTextView=new TextView(getContext());
                    String sizeManagers= String.valueOf(managers.size());
                    managersTextView.setText(sizeManagers);
                    managersTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowemailParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowemailParams.setMargins(10,10,0,10);
                    managersTextView.setLayoutParams(tableRowemailParams);
                    managersTextView.setTextColor(getResources().getColor(R.color.light_black));
                    managersTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    managersTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(managersTextView);

                    //Tags name list Text View
                    TextView tagsTextView=new TextView(getContext());
                    String tagValue="";
                    for(TagInfo  tag:tagInfos){
                        tagValue=tag.getName();
                    }
                    tagsTextView.setText(tagValue);
                    tagsTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowtagsParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowtagsParams.setMargins(10,10,0,10);
                    tagsTextView.setLayoutParams(tableRowtagsParams);
                    tagsTextView.setTextColor(getResources().getColor(R.color.light_black));
                    tagsTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    tagsTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(tagsTextView);

                    tableRow.setTag(teamId);

                    tableRow.setOnClickListener(view1 ->{
                        Object valueId=view1.getTag();
                        Log.d("Team Id : ",TAG+" / / "+valueId.toString());
                        Intent intent=new Intent(getContext(), UpdateTeamActivity.class);
                        intent.putExtra("teamId",valueId.toString());
                        startActivity(intent);

                    } );

                    binding.teamsTableLayout.addView(tableRow);
                }

            }else{
                binding.linearLayoutNoJobs.setVisibility(View.VISIBLE);
            }

        }else{
            showToast("Team Response is null!");
        }
    }

    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
