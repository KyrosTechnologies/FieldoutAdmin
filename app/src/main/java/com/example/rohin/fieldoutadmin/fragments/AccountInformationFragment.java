package com.example.rohin.fieldoutadmin.fragments;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.AccountInformationDetailsActivity;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.models.BussinessHoursModel;
import com.example.rohin.fieldoutadmin.models.BussinessHoursResponse;
import com.example.rohin.fieldoutadmin.models.DomainResponse;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.AccountInformationFragmentViewModel;
import com.google.gson.Gson;

import java.util.Calendar;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 13-12-2017.
 */

public class AccountInformationFragment extends Fragment {
    private View view;
    private Button edit_profile_button,save_bussiness_button;
    private TimePickerDialog timePickerDialog;
    private CheckBox monday_checkbox,tuesday_checkbox,wednesday_checkbox,thursday_checkbox,friday_checkbox,saturday_checkbox,sunday_checkbox;
    private TextView monday_start_time_edit_text,monday_end_time_edit_text,tuesday_start_date_edit_text,tuesday_end_date_edit_text
            ,wednesday_start_date_edit_text,wednesday_end_date_edit_text, thursday_start_date_edit_text,thursday_end_date_edit_text,
            friday_start_date_edit_text,friday_end_date_edit_text,saturday_start_date_edit_text,saturday_end_date_edit_text,
            sunday_start_date_edit_text,sunday_end_date_edit_text,domain_name_text_view,user_name_text_view,email_address_text_view,phone_number_text_view;
    private PreferenceManager store;
    private ProgressDialog progressDialog=null;

    @Inject
    AccountInformationFragmentViewModel viewModel;
    private CompositeSubscription subscription;
    private String TAG=AccountInformationFragment.class.getSimpleName();
    private AlertDialog.Builder builder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_account_information,container,false);
        //dependency injection
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectBussiness(this);
        //subscription  component
        subscription=new CompositeSubscription();
       //storage initialization
        store=PreferenceManager.getInstance(getContext());
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        callDomainAPI(domainId,authKey);
        callBussinessHoursAPI(domainId,authKey);
        //Button
        edit_profile_button=view.findViewById(R.id.edit_profile_button);
        save_bussiness_button=view.findViewById(R.id.save_bussiness_button);
        //TextView
        monday_start_time_edit_text=view.findViewById(R.id.monday_start_time_edit_text);
        phone_number_text_view=view.findViewById(R.id.phone_number_text_view);
        email_address_text_view=view.findViewById(R.id.email_address_text_view);
        user_name_text_view=view.findViewById(R.id.user_name_text_view);
        domain_name_text_view=view.findViewById(R.id.domain_name_text_view);
        monday_end_time_edit_text=view.findViewById(R.id.monday_end_time_edit_text);
        tuesday_start_date_edit_text=view.findViewById(R.id.tuesday_start_date_edit_text);
        tuesday_end_date_edit_text=view.findViewById(R.id.tuesday_end_date_edit_text);
        wednesday_start_date_edit_text=view.findViewById(R.id.wednesday_start_date_edit_text);
        wednesday_end_date_edit_text=view.findViewById(R.id.wednesday_end_date_edit_text);
        thursday_start_date_edit_text=view.findViewById(R.id.thursday_start_date_edit_text);
        thursday_end_date_edit_text=view.findViewById(R.id.thursday_end_date_edit_text);
        friday_start_date_edit_text=view.findViewById(R.id.friday_start_date_edit_text);
        friday_end_date_edit_text=view.findViewById(R.id.friday_end_date_edit_text);
        saturday_start_date_edit_text=view.findViewById(R.id.saturday_start_date_edit_text);
        saturday_end_date_edit_text=view.findViewById(R.id.saturday_end_date_edit_text);
        sunday_start_date_edit_text=view.findViewById(R.id.sunday_start_date_edit_text);
        sunday_end_date_edit_text=view.findViewById(R.id.sunday_end_date_edit_text);
        //onClickListener for Edit Text
        monday_start_time_edit_text.setOnClickListener(v-> timePicker(monday_start_time_edit_text));
        monday_end_time_edit_text.setOnClickListener(v-> timePicker(monday_end_time_edit_text));
        tuesday_start_date_edit_text.setOnClickListener(v-> timePicker(tuesday_start_date_edit_text));
        tuesday_end_date_edit_text.setOnClickListener(v-> timePicker(tuesday_end_date_edit_text));
        wednesday_start_date_edit_text.setOnClickListener(v-> timePicker(wednesday_start_date_edit_text));
        wednesday_end_date_edit_text.setOnClickListener(v-> timePicker(wednesday_end_date_edit_text));
        thursday_start_date_edit_text.setOnClickListener(v-> timePicker(thursday_start_date_edit_text));
        thursday_end_date_edit_text.setOnClickListener(v-> timePicker(thursday_end_date_edit_text));
        friday_start_date_edit_text.setOnClickListener(v-> timePicker(friday_start_date_edit_text));
        friday_end_date_edit_text.setOnClickListener(v-> timePicker(friday_end_date_edit_text));
        saturday_start_date_edit_text.setOnClickListener(v-> timePicker(saturday_start_date_edit_text));
        saturday_end_date_edit_text.setOnClickListener(v-> timePicker(saturday_end_date_edit_text));
        sunday_start_date_edit_text.setOnClickListener(v-> timePicker(sunday_start_date_edit_text));
        sunday_end_date_edit_text.setOnClickListener(v-> timePicker(sunday_end_date_edit_text));

        //Check Box
        monday_checkbox=view.findViewById(R.id.monday_checkbox);
        tuesday_checkbox=view.findViewById(R.id.tuesday_checkbox);
        wednesday_checkbox=view.findViewById(R.id.wednesday_checkbox);
        thursday_checkbox=view.findViewById(R.id.thursday_checkbox);
        friday_checkbox=view.findViewById(R.id.friday_checkbox);
        saturday_checkbox=view.findViewById(R.id.saturday_checkbox);
        sunday_checkbox=view.findViewById(R.id.sunday_checkbox);

        edit_profile_button.setOnClickListener(view-> startActivity(new Intent(getContext(), AccountInformationDetailsActivity.class)));
        save_bussiness_button.setOnClickListener(view-> showSaveDialog());
       return view;
    }

    private void callBussinessHoursAPI(String domainId, String authKey) {
        subscription.add(viewModel.getBusinessHourByDomainId(authKey,domainId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(checkBussinessHoursRes));
    }
    private Subscriber<BussinessHoursResponse>checkBussinessHoursRes=new Subscriber<BussinessHoursResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        }

        @Override
        public void onNext(BussinessHoursResponse bussinessHoursResponse) {
            bindBussinessHoursViews(bussinessHoursResponse);
        }
    };

    private void bindBussinessHoursViews(BussinessHoursResponse bussinessHoursResponse) {
        if(bussinessHoursResponse!=null){
            Log.d("Bussiness Hour : ",TAG+" / / "+bussinessHoursResponse.toString());
            String mondayStartDate=bussinessHoursResponse.getBusinessHour().getMonStartTime();
            if(mondayStartDate!=null){
                monday_start_time_edit_text.setText(mondayStartDate);
            }
            String mondayEndDate=bussinessHoursResponse.getBusinessHour().getMonEndTime();
            if(mondayEndDate!=null){
                monday_end_time_edit_text.setText(mondayEndDate);
            }
            String tuesdayStartDate=bussinessHoursResponse.getBusinessHour().getTueStartTime();
            if(tuesdayStartDate!=null){
                tuesday_start_date_edit_text.setText(tuesdayStartDate);
            }
            String tuesdayEndDate=bussinessHoursResponse.getBusinessHour().getTueEndTime();
            if(tuesdayEndDate!=null){
                tuesday_end_date_edit_text.setText(tuesdayEndDate);
            }
            String wednesdayStartDate=bussinessHoursResponse.getBusinessHour().getWedStartTime();
            if(wednesdayStartDate!=null){
                wednesday_start_date_edit_text.setText(wednesdayStartDate);
            }
            String wednesdayEndDate=bussinessHoursResponse.getBusinessHour().getWedEndTime();
            if(wednesdayEndDate!=null){
                wednesday_end_date_edit_text.setText(wednesdayEndDate);
            }
            String thursdayStartDate=bussinessHoursResponse.getBusinessHour().getThuStartTime();
            if(thursdayStartDate!=null){
                thursday_start_date_edit_text.setText(thursdayStartDate);
            }
            String thursdayEndDate=bussinessHoursResponse.getBusinessHour().getThuEndTime();
            if(thursdayEndDate!=null){
                thursday_end_date_edit_text.setText(thursdayEndDate);
            }
            String fridayStartDate=bussinessHoursResponse.getBusinessHour().getFriStartTime();
            if(fridayStartDate!=null){
                friday_start_date_edit_text.setText(fridayStartDate);
            }
            String fridayEndDate=bussinessHoursResponse.getBusinessHour().getFriEndTime();
            if(fridayEndDate!=null){
                friday_end_date_edit_text.setText(fridayEndDate);
            }
            String saturdayStartDate=bussinessHoursResponse.getBusinessHour().getSatStartTime();
            if(saturdayStartDate!=null){
                saturday_start_date_edit_text.setText(saturdayStartDate);
            }
            String saturdayEndDate=bussinessHoursResponse.getBusinessHour().getSatEndTime();
            if(saturdayEndDate!=null){
                saturday_end_date_edit_text.setText(saturdayEndDate);
            }
            String sundayStartDate=bussinessHoursResponse.getBusinessHour().getSunStartTime();
            if(sundayStartDate!=null){
                sunday_start_date_edit_text.setText(sundayStartDate);
            }
            String sundayEndDate=bussinessHoursResponse.getBusinessHour().getSunEndTime();
            if(sundayEndDate!=null){
                sunday_end_date_edit_text.setText(sundayEndDate);
            }

            int isMondayClosed=bussinessHoursResponse.getBusinessHour().getIsMonClosed();
            if(isMondayClosed!=0){
                monday_checkbox.setChecked(true);
            }
            int isTuesdayClosed=bussinessHoursResponse.getBusinessHour().getIsTueClosed();
            if(isTuesdayClosed!=0){
                tuesday_checkbox.setChecked(true);
            }
            int isWednesdayClosed=bussinessHoursResponse.getBusinessHour().getIsWedClosed();
            if(isWednesdayClosed!=0){
                wednesday_checkbox.setChecked(true);
            }
            int isThursdayClosed=bussinessHoursResponse.getBusinessHour().getIsThuClosed();
            if(isThursdayClosed!=0){
                thursday_checkbox.setChecked(true);
            }
            int isFridayClosed=bussinessHoursResponse.getBusinessHour().getIsFriClosed();
            if(isFridayClosed!=0){
                friday_checkbox.setChecked(true);
            }
            int isSaturdayClosed=bussinessHoursResponse.getBusinessHour().getIsSatClosed();
            if(isSaturdayClosed!=0){
                saturday_checkbox.setChecked(true);
            }
            int isSundayClosed=bussinessHoursResponse.getBusinessHour().getIsSunClosed();
            if(isSundayClosed!=0){
                sunday_checkbox.setChecked(true);
            }

        }else{
            sendToastMessage("TeamsResponse is null!");
        }

    }

    private void callDomainAPI(String domainId, String authKey) {
            subscription.add(viewModel.domainResponseObservable(authKey,domainId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.d("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(checkDomainResponse));
    }
    private Subscriber<DomainResponse>checkDomainResponse=new Subscriber<DomainResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            Log.d("Error : ",TAG+" / / "+throwable.getMessage());
        }

        @Override
        public void onNext(DomainResponse domainResponse) {
            bindDomainNameViews(domainResponse);

        }
    };

    private void bindDomainNameViews(DomainResponse domainResponse) {
        if(domainResponse!=null){
            Log.d("Domain TeamsResponse : ",TAG+" / / "+domainResponse.toString());
            String domainName=domainResponse.getUser().getDomain();
            String siteName=domainResponse.getUser().getSite();
            if(domainName!=null){
                store.putDomainName(domainName);
                domain_name_text_view.setText(domainName);

            }
            if(siteName!=null){
                store.putSiteName(siteName);
            }
        }else{
            sendToastMessage("response is null!");
        }
        String userName=store.getCompanyName();
        if(userName!=null){
            user_name_text_view.setText(userName);
        }
        String email=store.getEmail();
        if(email!=null){
            email_address_text_view.setText(email);
        }
        String phoneNumber=store.getPhone();
        if(phoneNumber!=null){
            phone_number_text_view.setText(phoneNumber);
        }
    }


    private void bussinessHoursAPI(String mondayStartTime, String mondayEndTime, String tuesdayStartTime, String tuesdayEndTime, String wednesdayStartTime, String wednesdayEndtime, String thursdayStartTime, String thursdayEndTime, String fridayStartTime, String fridayEndTime, String saturdayStartTime, String saturdayEndTime, String sundayStartTime, String sundayEndTime) {
        Log.d("BussinessHours API "," initialization of Bussiness API");
        String idDomain=store.getIdDomain();
        String token=store.getToken();
        int mondayIntCheckBox=0;
        int tuesdayIntCheckBox=0;
        int wednesdayIntCheckBox=0;
        int thursdayIntCheckBox=0;
        int fridayIntCheckBox=0;
        int saturdayIntCheckBox=0;
        int sundayIntCheckBox=0;

        boolean mondayCheckBox=monday_checkbox.isChecked();
        if(mondayCheckBox){
            mondayIntCheckBox=1;
        }
        boolean tuesdayCheckBox=tuesday_checkbox.isChecked();
        if(tuesdayCheckBox){
            tuesdayIntCheckBox=1;
        }
        boolean wednesdayCheckBox=wednesday_checkbox.isChecked();
        if(wednesdayCheckBox){
           wednesdayIntCheckBox=1;
        }
        boolean thursdayCheckBox=thursday_checkbox.isChecked();
        if(thursdayCheckBox){
            thursdayIntCheckBox=1;
        }
        boolean fridayCheckBox=friday_checkbox.isChecked();
        if(fridayCheckBox){
            fridayIntCheckBox=1;
        }
        boolean saturdayCheckBox=saturday_checkbox.isChecked();
        if(saturdayCheckBox){
            saturdayIntCheckBox=1;
        }
        boolean sundayCheckBox=sunday_checkbox.isChecked();
        if(sundayCheckBox){
            sundayIntCheckBox=1;
        }
        BussinessHoursModel model=new BussinessHoursModel();
        model.setMonStartTime(mondayStartTime);
        model.setMonEndTime(mondayEndTime);
        model.setTueStartTime(tuesdayStartTime);
        model.setTueEndTime(tuesdayEndTime);
        model.setWedStartTime(wednesdayStartTime);
        model.setWedEndTime(wednesdayEndtime);
        model.setThuStartTime(thursdayStartTime);
        model.setThuEndTime(thursdayEndTime);
        model.setFriStartTime(fridayStartTime);
        model.setFriEndTime(fridayEndTime);
        model.setSatStartTime(saturdayStartTime);
        model.setSatEndTime(saturdayEndTime);
        model.setSunStartTime(sundayStartTime);
        model.setSunEndTime(sundayEndTime);
        model.setIdDomain(idDomain);
        model.setIsMonClosed(mondayIntCheckBox);
        model.setIsTueClosed(tuesdayIntCheckBox);
        model.setIsWedClosed(wednesdayIntCheckBox);
        model.setIsThuClosed(thursdayIntCheckBox);
        model.setIsFriClosed(fridayIntCheckBox);
        model.setIsSatClosed(saturdayIntCheckBox);
        model.setIsSunClosed(sundayIntCheckBox);
        initBindings(model,token);
    }

    private void initBindings(BussinessHoursModel model,String token) {
                Log.d("InitBindings ","Binding initialization");
            subscription.add(viewModel.bussinessHoursResponseObservable(token,model)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable ->{Log.e("Error : ",TAG+" / "+throwable.getMessage());
                        dismissDialog();} )
                        .subscribe(checkBussinessHoursResponse));
    }
    private Subscriber<BussinessHoursResponse>checkBussinessHoursResponse=new Subscriber<BussinessHoursResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            Log.e("Error : ",TAG+" / "+throwable.getMessage());
        }

        @Override
        public void onNext(BussinessHoursResponse bussinessHoursResponse) {
            bindViews(bussinessHoursResponse);
            dismissDialog();
        }
    };

    private void bindViews(BussinessHoursResponse response) {
        Log.d("Binding Views ","Binding Views");
        Gson gson = new Gson();
        String responseJson = gson.toJson(response);
        Log.d("TeamsResponse",TAG+" / "+responseJson);
        if(response!=null){
            String mondayStartTime=response.getBusinessHour().getMonStartTime();
            if(mondayStartTime!=null){
                monday_start_time_edit_text.setText(mondayStartTime);
            }
            String mondayEndTime=response.getBusinessHour().getMonEndTime();
            if(mondayEndTime!=null){
                monday_end_time_edit_text.setText(mondayEndTime);
            }
            String tuesdayStartTime=response.getBusinessHour().getTueStartTime();
            if(tuesdayStartTime!=null){
                tuesday_start_date_edit_text.setText(tuesdayStartTime);
            }
            String tuesdayEndTime=response.getBusinessHour().getTueEndTime();
            if(tuesdayEndTime!=null){
                tuesday_end_date_edit_text.setText(tuesdayEndTime);
            }
            String wednesdayStartTime=response.getBusinessHour().getWedStartTime();
            if(wednesdayStartTime!=null){
                wednesday_start_date_edit_text.setText(wednesdayStartTime);
            }
            String wednesdayEndTime=response.getBusinessHour().getWedEndTime();
            if(wednesdayEndTime!=null){
                wednesday_end_date_edit_text.setText(wednesdayEndTime);
            }
            String thursdayStartTime=response.getBusinessHour().getThuStartTime();
            if(thursdayStartTime!=null){
                thursday_start_date_edit_text.setText(thursdayStartTime);
            }
            String thursdayEndTime=response.getBusinessHour().getThuEndTime();
            if(thursdayEndTime!=null){
                thursday_end_date_edit_text.setText(thursdayEndTime);
            }
            String fridayStartTime=response.getBusinessHour().getFriStartTime();
            if(fridayStartTime!=null){
                friday_start_date_edit_text.setText(fridayStartTime);
            }
            String fridayEndTime=response.getBusinessHour().getFriEndTime();
            if(fridayEndTime!=null){
                friday_end_date_edit_text.setText(fridayEndTime);
            }
            String saturdayStartTime=response.getBusinessHour().getSatStartTime();
            if(saturdayStartTime!=null){
                saturday_start_date_edit_text.setText(saturdayStartTime);
            }
            String saturdayEndTime=response.getBusinessHour().getSatEndTime();
            if(saturdayEndTime!=null){
                saturday_end_date_edit_text.setText(saturdayEndTime);
            }
            String sundayStartTime=response.getBusinessHour().getSunStartTime();
            if(sundayStartTime!=null){
                sunday_start_date_edit_text.setText(sundayStartTime);
            }
            String sundayEndTime=response.getBusinessHour().getSunEndTime();
            if(sundayEndTime!=null){
                sunday_end_date_edit_text.setText(sundayEndTime);
            }
            int mondayChecked=response.getBusinessHour().getIsMonClosed();
            if(mondayChecked!=0){
                monday_checkbox.setChecked(true);
            }
            int tuesdayChecked=response.getBusinessHour().getIsTueClosed();
            if(tuesdayChecked!=0){
                tuesday_checkbox.setChecked(true);
            }
            int wednesdayChecked=response.getBusinessHour().getIsWedClosed();
            if(wednesdayChecked!=0){
                wednesday_checkbox.setChecked(true);
            }
            int thursdayChecked=response.getBusinessHour().getIsThuClosed();
            if(thursdayChecked!=0){
                thursday_checkbox.setChecked(true);
            }
            int fridayChecked=response.getBusinessHour().getIsFriClosed();
            if(fridayChecked!=0){
                friday_checkbox.setChecked(true);
            }
            int saturdayChecked=response.getBusinessHour().getIsSatClosed();
            if(saturdayChecked!=0){
                saturday_checkbox.setChecked(true);
            }
            int sundayChecked=response.getBusinessHour().getIsSunClosed();
            if(sundayChecked!=0){
                sunday_checkbox.setChecked(true);
            }
            sendToastMessage("Business Hours is  updated!");
        }else{
            sendToastMessage("Business Hours is not updated!");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissDialog();
    }
    private void showSaveDialog(){
        if(builder==null){
            builder = new AlertDialog.Builder(getContext());
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
        Log.d("CheckValidation ","Check validation initialization");
        String mondayStartTime=monday_start_time_edit_text.getText().toString();
        String mondayEndTime=monday_end_time_edit_text.getText().toString();
        String tuesdayStartTime=tuesday_start_date_edit_text.getText().toString();
        String tuesdayEndTime=tuesday_end_date_edit_text.getText().toString();
        String wednesdayStartTime=wednesday_start_date_edit_text.getText().toString();
        String wednesdayEndtime=wednesday_end_date_edit_text.getText().toString();
        String thursdayStartTime=thursday_start_date_edit_text.getText().toString();
        String thursdayEndTime=thursday_end_date_edit_text.getText().toString();
        String fridayStartTime=friday_start_date_edit_text.getText().toString();
        String fridayEndTime=friday_end_date_edit_text.getText().toString();
        String saturdayStartTime=saturday_start_date_edit_text.getText().toString();
        String saturdayEndTime=saturday_end_date_edit_text.getText().toString();
        String sundayStartTime=sunday_start_date_edit_text.getText().toString();
        String sundayEndTime=sunday_end_date_edit_text.getText().toString();
        showDialog();
        //API Call for Business Hours
        bussinessHoursAPI(mondayStartTime,mondayEndTime,tuesdayStartTime,tuesdayEndTime,wednesdayStartTime,wednesdayEndtime,thursdayStartTime,thursdayEndTime,fridayStartTime,fridayEndTime,saturdayStartTime,saturdayEndTime,sundayStartTime,sundayEndTime);


        //validation for Business hours
//        if (!mondayStartTime.isEmpty() && mondayStartTime!=null && !mondayEndTime.isEmpty() && mondayEndTime!=null && !tuesdayStartTime.isEmpty() && tuesdayStartTime!=null && !tuesdayEndTime.isEmpty() && tuesdayEndTime!=null && !wednesdayStartTime.isEmpty() && wednesdayStartTime!=null && !wednesdayEndtime.isEmpty() && wednesdayEndtime!=null
//                && !thursdayStartTime.isEmpty() && thursdayStartTime!=null && !thursdayEndTime.isEmpty() && thursdayEndTime!=null && !fridayStartTime.isEmpty() && fridayStartTime!=null && !fridayEndTime.isEmpty() && fridayEndTime!=null && !saturdayStartTime.isEmpty() && saturdayStartTime!=null  && !saturdayEndTime.isEmpty() && saturdayEndTime!=null &&
//                !sundayStartTime.isEmpty() && sundayStartTime!=null && !sundayEndTime.isEmpty() && sundayEndTime!=null) {
//            bussinessHoursAPI(mondayStartTime,mondayEndTime,tuesdayStartTime,tuesdayEndTime,wednesdayStartTime,wednesdayEndtime,thursdayStartTime,thursdayEndTime,fridayStartTime,fridayEndTime,saturdayStartTime,saturdayEndTime,sundayStartTime,sundayEndTime);
//        } else {
//            sendToastMessage("please enter all Business Hours!");
//           }
    }
    private void sendToastMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void timePicker(TextView editText){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog=new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) -> onTimeSet(selectedHour,selectedMinute,editText),hour,minute,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
    private void onTimeSet(int hourOfDay, int minute,TextView editText) {
        String am_pm = "";
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";
        String hours = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
        String finalTimeHour=String.format("%02d",Integer.parseInt(hours));
        String finalTimeMinute=String.format("%02d",datetime.get(Calendar.MINUTE));
        String timeValue=finalTimeHour+":"+finalTimeMinute+" "+am_pm;
        editText.setText(timeValue);
    }
    private void showDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getContext());
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
