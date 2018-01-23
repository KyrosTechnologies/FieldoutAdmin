package com.example.rohin.fieldoutadmin.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.ActivityAccountInformationDetailsBinding;
import com.example.rohin.fieldoutadmin.models.Result;
import com.example.rohin.fieldoutadmin.models.SkilledTradesModel;
import com.example.rohin.fieldoutadmin.models.TeamAdd;
import com.example.rohin.fieldoutadmin.models.UserUpdateResponse;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.AccountInformationDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 13-12-2017.
 */

public class AccountInformationDetailsActivity extends AppCompatActivity {
    private PreferenceManager store;
    private String TAG= AccountInformationDetailsActivity.class.getSimpleName();
    private ProgressDialog progressDialog=null;
    private ActivityAccountInformationDetailsBinding binding;
    @Inject
    AccountInformationDetailsViewModel viewModel;
    private CompositeSubscription subscription;
    private AlertDialog.Builder builder;
    private String token=null;
    private String userId=null;
    private String teams=null;
    private String skilledTrades=null;
    private String profile=null;
    private String customFieldValues=null;
    private String language=null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_account_information_details);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectUserUpdate(this);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getApplicationContext());
         userId=store.getUserid();
         token=store.getToken();
         teams=store.getTeamId();
         skilledTrades=store.getSkilledTrades();
         profile=store.getProfile();
        customFieldValues=store.getCustomFieldValues();
        language=store.getLanguage();
        String userName=store.getUsername();
        if(userName!=null){
            binding.userNameEditText.setText(userName);
        }
        String firstName=store.getFirstName();
        if(firstName!=null){
            binding.firstNameEditText.setText(firstName);
        }
        String lastName=store.getLastName();
        if(lastName!=null){
            binding.lastNameEditText.setText(lastName);
        }
        String phone=store.getPhone();
        if(phone!=null){
            binding.phoneEditText.setText(phone);
        }
        String email=store.getEmail();
        if(email!=null){
            binding.emailAddressEditText.setText(email);
        }

        binding.saveButtonUserUpdate.setOnClickListener(view ->showSaveDialog() );

    }
    private void showSaveDialog(){
        if(builder==null){
            builder = new AlertDialog.Builder(AccountInformationDetailsActivity.this);
            builder.setMessage("Do you want to save it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        checkValidation();
                        Log.d("Dialog Yes ","dialog initialization");
                        dialog.cancel();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());

        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkValidation() {
        String userNameEditText=binding.userNameEditText.getText().toString();
        String firstNameEditText=binding.firstNameEditText.getText().toString();
        String lastNameEditText=binding.lastNameEditText.getText().toString();
        String phoneEditText=binding.phoneEditText.getText().toString();
        String mobileNumberEditText=binding.mobileEditText.getText().toString();
        String faxNumberEditText=binding.faxEditText.getText().toString();
        String emailEditText=binding.emailAddressEditText.getText().toString();
        initApiCall(userNameEditText,firstNameEditText,lastNameEditText,phoneEditText,emailEditText,language,teams,skilledTrades,profile,customFieldValues,userId,token);


    }

    private void initApiCall(String userNameEditText, String firstNameEditText, String lastNameEditText, String phoneEditText, String emailEditText, String language, String teams, String skilledTrades, String profile, String customFieldValues, String userId, String token) {
            Result result =new Result();
            result.setFirstName(firstNameEditText);
            result.setLastName(lastNameEditText);
            result.setUsername(userNameEditText);
            result.setLanguage(language);
            result.setEmail(emailEditText);
            result.setPhone(phoneEditText);
        List<String>skillList=new ArrayList<>();
        skillList.add(skilledTrades);
        SkilledTradesModel skilledTradesModel=new SkilledTradesModel();
        skilledTradesModel.setName(skillList);
            result.setSkilledTrades(skilledTradesModel);
            result.setProfile(profile);
            result.setCustomFieldValues(new ArrayList<>());
            List<String >teamId=new ArrayList<>();
            teamId.add(teams);
        TeamAdd teamAdd=new TeamAdd();
        teamAdd.setId(teamId);
            result.setTeams(teamAdd);
            updateUserApi(result,userId,token);
            showDialog();
    }

    private void updateUserApi(Result result, String userId, String token) {
        subscription.add(viewModel.updateUserInformation(result,token,userId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable ->{
                    Log.e("Error : ",TAG+" / / "+throwable.getMessage());
                    dismissDialog();
                })
                .subscribe(checkUserUpdateResponse));
    }
    private Subscriber<UserUpdateResponse>checkUserUpdateResponse=new Subscriber<UserUpdateResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            Log.e("Error : ",TAG+" / / "+throwable.getMessage());
            showToast(""+throwable.getMessage());
            dismissDialog();
        }

        @Override
        public void onNext(UserUpdateResponse userUpdateResponse) {
            bindViews(userUpdateResponse);
            dismissDialog();
        }
    };

    private void bindViews(UserUpdateResponse response) {
        if(response!=null){
            Log.d("Update TeamsResponse : ",TAG+" / / "+response.toString());
            boolean isSuccess=response.getIsSuccess();
            if(isSuccess){
                showToast("Successfully updated!");
                    Object customFieldValues=response.getResult().getCustomFieldValues();
                    String email=response.getResult().getEmail();
                    String firstName=response.getResult().getFirstName();
                    String language=response.getResult().getLanguage();
                    String lastName=response.getResult().getLastName();
                    String phone=response.getResult().getPhone();
                    String userName=response.getResult().getUsername();
                    bindValues(customFieldValues,email,firstName,language,lastName,phone,userName);
            }else{
                showToast("TeamsResponse is False!");
            }
        }else{
            showToast("TeamsResponse is Empty!");
        }

    }

    private void bindValues(Object customFieldValues, String email, String firstName, String language, String lastName, String phone, String userName) {
                if(email!=null){
                    binding.emailAddressEditText.setText(email);
                    store.putEmail(email);
                }
                if(firstName!=null){
                    binding.firstNameEditText.setText(firstName);
                    store.putFirstName(firstName);
                }
                if(lastName!=null){
                    binding.lastNameEditText.setText(lastName);
                    store.putLastName(lastName);
                }
                if(phone!=null){
                    binding.phoneEditText.setText(phone);
                    store.putPhone(phone);
                }
                if(userName!=null){
                    binding.userNameEditText.setText(userName);
                    store.putUsername(userName);
                }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissDialog();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AccountInformationDetailsActivity.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(AccountInformationDetailsActivity.this);
            progressDialog.setMessage("Loading Please wait..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);


        }
        progressDialog.show();
    }
    private void dismissDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
