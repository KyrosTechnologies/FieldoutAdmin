package com.kyros.technologies.fieldout.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.activity.AddUserActivity;
import com.kyros.technologies.fieldout.activity.UpdateUserActivity;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.FragmentUsersBinding;
import com.kyros.technologies.fieldout.models.SingleTeamResponse;
import com.kyros.technologies.fieldout.models.SkilledTradesModel;
import com.kyros.technologies.fieldout.models.TeamAdd;
import com.kyros.technologies.fieldout.models.UsersItem;
import com.kyros.technologies.fieldout.models.UsersResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.UpdateTeamViewModel;
import com.kyros.technologies.fieldout.viewmodel.UsersFragmentViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 14-12-2017.
 */

public class UsersFragment extends Fragment {
    private FragmentUsersBinding binding;
    private View view;
    private PreferenceManager store;
    @Inject
    UsersFragmentViewModel viewModel;
    private CompositeSubscription subscription;
    private String TAG=UsersFragment.class.getSimpleName();
    private List<UsersItem>usersItemList=new ArrayList<>();
    @Inject
    UpdateTeamViewModel teamViewModel;
    private String teamNameFirst=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding= DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false);
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectUsersList(this);
            subscription=new CompositeSubscription();
         view = binding.getRoot();
        store=PreferenceManager.getInstance(getContext());
        Log.d("onCreate","view Initialized");
        binding.addUserButton.setOnClickListener(view-> startActivity(new Intent(getContext(), AddUserActivity.class)));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
            loadUsers(domainId,authKey);
    }

    private void loadUsers(String domainId, String authKey) {
        subscription.add(viewModel.getUsersResponse(domainId,authKey)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ",TAG+" / / / "+throwable.toString()))
                    .subscribe(this::usersGetResponse,this::usersGetErrorResponse,this::completedUsersResponse));
    }

    private void completedUsersResponse() {

    }

    private void usersGetErrorResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / / "+throwable.toString());
        showToast(""+throwable.getMessage());

    }
    private void usersGetResponse(UsersResponse usersResponse) {
        bindViews(usersResponse);

    }

    private void bindViews(UsersResponse usersResponse) {
        if(usersResponse!=null){
            Log.d("TeamsResponse : ",TAG+ " / / "+usersResponse.toString());
           usersItemList=usersResponse.getUsers();
            binding.usersTableLayout.removeAllViews();
            binding.usersTableLayout.addView(binding.rowAddUserTable);

            if(usersItemList.size()!=0){
                for(int i=0;i<usersItemList.size();i++){
                    TableRow tableRow=new TableRow(getContext());
                    tableRow.setBackground(getResources().getDrawable(R.color.bg));
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120));
                    TextView userTextView=new TextView(getContext());
                    userTextView.setText(usersItemList.get(i).getUsername());
                    userTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowuserTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,120,50);
                    tableRowuserTextParams.setMargins(15,15,15,15);
                    userTextView.setLayoutParams(tableRowuserTextParams);
                    userTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    userTextView.setTextColor(getResources().getColor(R.color.text_light));

                    tableRow.addView(userTextView);

                    TextView userNameTextView=new TextView(getContext());
                    userNameTextView.setText(usersItemList.get(i).getUsername());
                    userNameTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowuserNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowuserNameParams.setMargins(15,15,15,15);
                    userNameTextView.setLayoutParams(tableRowuserNameParams);
                    userNameTextView.setTextColor(getResources().getColor(R.color.text_light));
                    userNameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);

                    tableRow.addView(userNameTextView);

                    TextView languageTextView=new TextView(getContext());
                    languageTextView.setText(usersItemList.get(i).getLanguage());
                    languageTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowlanguageParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowlanguageParams.setMargins(15,15,15,15);
                    languageTextView.setLayoutParams(tableRowlanguageParams);

                    languageTextView.setTextColor(getResources().getColor(R.color.text_light));
                    languageTextView.setGravity(Gravity.LEFT|Gravity.CENTER);

                    tableRow.addView(languageTextView);

                    TextView emailTextView=new TextView(getContext());
                    emailTextView.setText(usersItemList.get(i).getEmail());
                    emailTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowemailParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowemailParams.setMargins(15,15,15,15);
                    emailTextView.setLayoutParams(tableRowemailParams);

                    emailTextView.setTextColor(getResources().getColor(R.color.text_light));
                    emailTextView.setGravity(Gravity.LEFT|Gravity.CENTER);

                    tableRow.addView(emailTextView);

                    TextView profileTextView=new TextView(getContext());
                    profileTextView.setText(usersItemList.get(i).getProfile());
                    profileTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowprofileParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowprofileParams.setMargins(15,15,15,15);
                    profileTextView.setLayoutParams(tableRowprofileParams);

                    profileTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    profileTextView.setTextColor(getResources().getColor(R.color.text_light));

                    tableRow.addView(profileTextView);

                    TextView skilledTradesTextView=new TextView(getContext());


                        SkilledTradesModel skTrades=usersItemList.get(i).getSkilledTrades();
                        if(skTrades != null ){
                            List<String>sList=skTrades.getName();
                            if(sList !=null && sList.size() != 0){
                                String sValue=skTrades.getName().get(0);
                                if(sValue!=null){
                                    skilledTradesTextView.setText(sValue);

                                }else{
                                    skilledTradesTextView.setText("");
                                }
                            }


                        }


                    skilledTradesTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowskilledTradesParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowskilledTradesParams.setMargins(15,15,15,15);
                    skilledTradesTextView.setLayoutParams(tableRowskilledTradesParams);

                    skilledTradesTextView.setTextColor(getResources().getColor(R.color.text_light));
                    skilledTradesTextView.setGravity(Gravity.LEFT|Gravity.CENTER);

                    tableRow.addView(skilledTradesTextView);


                    TextView teamsTextView=new TextView(getContext());

                        TeamAdd teamObject=usersItemList.get(i).getTeams();
                    if (teamObject != null) {
                        List<String>teamIdList=teamObject.getId();
                        if(teamIdList!=null  && teamIdList.size()!=0){
                            String[]value= getTeamNameAPI(teamIdList.get(0));
                            if(value[0]!=null){
                                teamsTextView.setText(value[0]);
                            }else {
                                teamsTextView.setText("");
                            }
                        }else {
                            teamsTextView.setText("");
                        }
                    }


                    teamsTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowParams.setMargins(15,15,15,15);
                    teamsTextView.setLayoutParams(tableRowParams);

                    teamsTextView.setTextColor(getResources().getColor(R.color.text_light));
                    teamsTextView.setGravity(Gravity.LEFT|Gravity.CENTER);

                    tableRow.addView(teamsTextView);

                    Log.d("Id List : ",""+usersItemList.get(i).getId());
                    tableRow.setTag(usersItemList.get(i).getId());


                    tableRow.setOnClickListener(view1 -> {
                        try{
                            Object value=view1.getTag();
                            Log.d("Value Tag : ",""+value.toString());
                            Intent intent=new Intent(getContext(), UpdateUserActivity.class);
                            intent.putExtra("userId",value.toString());
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    });
                    binding.usersTableLayout.addView(tableRow);
                }
            }else{
                binding.linearLayoutNoJobs.setVisibility(View.VISIBLE);
            }



        }else{
            showToast("TeamsResponse is null!");
        }
    }

    private String[] getTeamNameAPI(String teamId) {
        final String[] value = {null};
        subscription.add(teamViewModel.getSingleTeamObservable(store.getToken(),teamId)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" /  /"+throwable.getMessage()))
        .subscribe(singleTeamResponse -> value[0] =singleTeamResponse(singleTeamResponse),this::singleTeamErrorResponse,this::completedSingleTeam));
    return value;
    }

    private void completedSingleTeam() {

    }

    private void singleTeamErrorResponse(Throwable throwable) {
    Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private String singleTeamResponse(SingleTeamResponse singleTeamResponse) {
        if(singleTeamResponse!=null){
            Log.d("SingleTeamRes : ", TAG+" / / "+singleTeamResponse);
            teamNameFirst=singleTeamResponse.getTeams().getName();
        }else{
            showToast("single teams response is null");
        }
        return teamNameFirst;
    }


    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.clear();
    }


}
