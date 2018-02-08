package com.kyros.technologies.fieldout.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.models.SignUpModel;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.SignUpViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SignUpActivity extends AppCompatActivity {

    private EditText first_name_editText,last_name_editText,company_name_edit_text,mobile_number_edit_text,email_edit_text,company_domain_edit_text;
    private Button sign_up_button;
    @Inject
    SignUpViewModel signUpViewModel;
    private CompositeSubscription subscription;
    private ProgressDialog progressDialog=null;
    private String TAG=SignUpActivity.class.getSimpleName();
    private PreferenceManager store;
    private String domainName=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //dependency injection
        ((ServiceHandler)getApplication()).getApplicationComponent().inject(this);
        //Subscription component
        subscription=new CompositeSubscription();
        //Button
        sign_up_button= findViewById(R.id.sign_up_button);
        store=PreferenceManager.getInstance(getApplicationContext());
        //Edit text
        first_name_editText= findViewById(R.id.first_name_editText);
        last_name_editText=findViewById(R.id.last_name_editText);
        company_name_edit_text=findViewById(R.id.company_name_edit_text);
        mobile_number_edit_text=findViewById(R.id.mobile_number_edit_text);
        email_edit_text=findViewById(R.id.email_edit_text);
        company_domain_edit_text=findViewById(R.id.company_domain_edit_text);
        sign_up_button.setOnClickListener(view -> {
            String firstName=first_name_editText.getText().toString();
            String lastName=last_name_editText.getText().toString();
            String companyName=company_name_edit_text.getText().toString();
            String mobileNumber=mobile_number_edit_text.getText().toString();
            String emailUser=email_edit_text.getText().toString();
            String domainName=company_domain_edit_text.getText().toString();
            logicMethod(firstName,lastName,companyName,mobileNumber,emailUser,domainName);
        });


    }

    private void logicMethod(String firstName, String lastName, String companyName, String mobileNumber, String emailUser, String domainName) {
                if( firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty() && companyName != null && !companyName.isEmpty() && mobileNumber != null && !mobileNumber.isEmpty() && emailUser != null && !emailUser.isEmpty() && domainName != null && !domainName.isEmpty()){
                    if(isValidEmailAddress(emailUser)){
                        callApi(firstName,lastName,companyName,mobileNumber,emailUser,domainName);
                    }else{
                        sendToast("Invalid Email Address!");
                    }
                }else{
                    sendToast("Please Enter all Details! ");
                }

    }

    private void callApi(String firstName, String lastName, String companyName, String mobileNumber, String emailUser, String domainName) {
        SignUpModel signUpModel=new SignUpModel();
        signUpModel.setFirstName(firstName);
        signUpModel.setLastName(lastName);
        signUpModel.setCompanyName(companyName);
        signUpModel.setPhone(mobileNumber);
        signUpModel.setEmail(emailUser);
        signUpModel.setDomain(domainName);
        this.domainName=domainName;
        showDialog();
        initBindings(signUpModel);

    }

    private void initBindings(SignUpModel signUpModel) {

        subscription.add(signUpViewModel.signUpModelObservable(signUpModel)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable ->Log.e("Error : ",""+TAG+" / /"+throwable.getMessage()))
                    .subscribe(checkSignUp));

    }
        private Subscriber<SignUpModel>checkSignUp=new Subscriber<SignUpModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("Error : ",""+TAG+" / /"+throwable.getMessage());
                dismissDialog();
                sendToast(""+throwable.getMessage());


            }

            @Override
            public void onNext(SignUpModel model) {
                dismissDialog();
                Log.d("SignUp : ",TAG+" / /"+model.toString());
                boolean isSuccess=model.getIsSuccess();
                String message=model.getMessage();
                sendToast(message);
                if(isSuccess){
                    store.putDomainName(domainName);
                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                }
            }
        };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissDialog();
    }

    private void sendToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void showDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(SignUpActivity.this);
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
    public boolean isValidEmailAddress(String email) {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
