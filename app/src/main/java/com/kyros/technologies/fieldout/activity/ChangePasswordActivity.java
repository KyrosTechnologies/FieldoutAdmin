package com.kyros.technologies.fieldout.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityChangePasswordBinding;
import com.kyros.technologies.fieldout.models.ChangePasswordResponse;
import com.kyros.technologies.fieldout.models.User;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.ChangePasswordActivityViewModel;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ChangePasswordActivity extends AppCompatActivity {
    private CompositeSubscription subscription;
    private PreferenceManager store;
    private ActivityChangePasswordBinding binding;
    private String userId=null;
    private String password=null;
    private ProgressDialog progressDialog=null;
    @Inject
    ChangePasswordActivityViewModel viewModel;
    private String TAG=ChangePasswordActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectChangePasswordActivity(this);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getApplicationContext());
        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_password);
        userId=store.getUserid();
        password=store.getPassword();
        binding.buttonSavePassword.setOnClickListener(view -> initiateValidation());
    }

    private void initiateValidation() {
        if(password!=null){
            String oldPassword=binding.editTextOldPassword.getText().toString();
            String newPassword=binding.editTextNewPassword.getText().toString();
            String confirmPassword=binding.editTextConfirmPassword.getText().toString();

            if(oldPassword!=null && !oldPassword.isEmpty() && newPassword!=null && !newPassword.isEmpty() && confirmPassword!=null && !confirmPassword.isEmpty()){
                if(!oldPassword.equals(password)){
                    showToast("Your old password is not valid!");
                    return;
                }
                if(!newPassword.equals(confirmPassword)){
                    showToast("Your new password and confirm password is not same!");
                    return;
                }
                if(oldPassword.equals(password) && newPassword.equals(confirmPassword)){
                    User user=new User();
                    user.setOldPassword(oldPassword);
                    user.setNewPassword(newPassword);
                    showProgressDialog();
                    initiateChangePasswordAPICall(user);
                }
            }else{
                showToast("please enter all details!");
            }
        }else{
            showToast("password is null!");
        }



    }

    private void initiateChangePasswordAPICall(User user) {
        if(userId!=null){
                subscription.add(viewModel.changePassword(store.getToken(),userId,user,store.getIdDomain())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(this::changePasswordResponse,this::changePasswordError,this::changePasswordCompleted));
        }else{
            showToast("User id is null!");
        }
    }

    private void changePasswordCompleted() {
        dismissProgressDialog();
    }

    private void changePasswordError(Throwable throwable) {
        dismissProgressDialog();
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());
    }

    private void changePasswordResponse(ChangePasswordResponse changePasswordResponse) {
        dismissProgressDialog();
        if(changePasswordResponse!=null){
            Log.d("ChPassRes : ",TAG+" / / "+changePasswordResponse);
            boolean isSuccess=changePasswordResponse.isSuccess();
            String result=changePasswordResponse.getResult();
            if(isSuccess){
                User user=changePasswordResponse.getUserDetails();
                if(user != null){
                    String password=user.getPassword();
                    if(password != null){
                        store.putPassword(password);
                    }
                }
                showToast("password updated successfully!");
                this.finish();
            }else{
                showToast(""+result);
            }

        }else{
            showToast("user response is null!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissProgressDialog();
    }

    private void dismissProgressDialog() {
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showProgressDialog(){

    }
}
