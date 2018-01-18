package com.example.rohin.fieldoutadmin.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.ActivitiesUpdateDelete;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.FragmentRegionalSettingsBinding;
import com.example.rohin.fieldoutadmin.models.DateTimeFormat;
import com.example.rohin.fieldoutadmin.models.DecimalAndCurrencyFormat;
import com.example.rohin.fieldoutadmin.models.RegionalSettings;
import com.example.rohin.fieldoutadmin.models.RegionalSettingsResponse;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.RegionalSettingsFragmentViewModel;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 29-12-2017.
 */

public class RegionalSettingsFragment extends Fragment{
    private PreferenceManager store;
    private CompositeSubscription subscription;
    private String TAG=RegionalSettingsFragment.class.getSimpleName();
    private FragmentRegionalSettingsBinding binding;
    private List<String>short_date_list=new ArrayList<>();
    private List<String>short_date_original_list=new ArrayList<>();
    private List<String>short_time_list=new ArrayList<>();
    private List<String>short_time_original_list=new ArrayList<>();
    private List<String>long_date_list=new ArrayList<>();
    private List<String>long_date_original_list=new ArrayList<>();
    private List<String>timeZoneList=new ArrayList<>();
    private List<String>decimalSeparatorList=new ArrayList<>();
    private List<String>groupSeparatorList=new ArrayList<>();
    private List<String>currencyList=new ArrayList<>();
    private List<String>distanceUnitList=new ArrayList<>();
    private List<String>addressFormatList=new ArrayList<>();

    private int selectedShortDate=0;
    private int selectedShortTime=0;
    private int selectedLongDate=0;
    private int selectedTimeZone=0;
    private int selectedDecimalSeparator=0;
    private int selectedGroupSeparator=0;
    private int selectedCurrency=0;
    private int selectedDistanceUnit=0;
    private int selectedAddressFormat=0;
    private AlertDialog saveDialog=null;
    private ProgressDialog progressDialog;
    @Inject
    RegionalSettingsFragmentViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectRegionalSettingsFragment(this);
       binding= DataBindingUtil.inflate(inflater,R.layout.fragment_regional_settings,container,false);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getContext());
        View view=binding.getRoot();
        bindViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.clear();
        dismissSaveDialog();
        dismissProgressDialog();
    }
    private void addDataToList(){
        short_date_list.add("yyyy/MM/dd");
        short_date_original_list.add("2017/12/26");
        short_date_list.add("MM/dd/yyyy");
        short_date_original_list.add("12/26/2017");
        short_date_list.add("dd/MM/yyyy");
        short_date_original_list.add("26/12/2017");
        short_date_list.add("dd.MM.yyyy");
        short_date_original_list.add("26.12.2017");


        short_time_list.add("HH:mm");
        short_time_original_list.add("12:05");
        short_time_list.add("hh:mm tt");
        short_time_original_list.add("12:05 PM");



        long_date_list.add("dddd, MMMM dd, yyyy");
        long_date_original_list.add("Wednesday, December 26, 2017");
        long_date_list.add("dddd, dd MMMM, yyyy");
        long_date_original_list.add("Wednesday, 26 December, 2017");
        long_date_list.add("dddd MMMM dd yyyy");
        long_date_original_list.add("Wednesday December 26 2017");

        decimalSeparatorList.add(".");
        decimalSeparatorList.add(",");

        groupSeparatorList.add(",");
        groupSeparatorList.add(" ");

        distanceUnitList.add("km");
        distanceUnitList.add("miles");

        addressFormatList.add("Street/ Zip code City/ Country");
        addressFormatList.add("Street/  City/ Zip code/ Country");
        addressFormatList.add("Street/  City, Zip code/ Country");
        addressFormatList.add("Street/  City (District) Zip code/ Country");
        addressFormatList.add("Street/  City, District Zip code/ Country");
        String[] ids= TimeZone.getAvailableIDs();
        for(String id:ids){
            timeZoneList.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        getCurrencyList();
    }
    private void bindViews(){
        addDataToList();

        ArrayAdapter<String> adapterShortDate=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,short_date_list);
        ArrayAdapter<String> adapterShortTime=new  ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,short_time_list);
        ArrayAdapter<String> adapterLongDate=new  ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,long_date_list);
        ArrayAdapter<String> adapterTimeZone=new  ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,timeZoneList);
        ArrayAdapter<String> adapterDecimalSeparator=new  ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,decimalSeparatorList);
        ArrayAdapter<String> adapterGroupSeparator=new  ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,groupSeparatorList);
        ArrayAdapter<String> adapterCurrency=new  ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,currencyList);
        ArrayAdapter<String> adapterDistanceUnit=new  ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,distanceUnitList);
        ArrayAdapter<String> adapterAddressFormat=new  ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,addressFormatList);


        binding.spinnerShortDateFormat.setPrompt("Short Date");
        binding.spinnerShortTimeFormat.setPrompt("Short Time");
        binding.spinnerLongDate.setPrompt("Long Date");
        binding.spinnerTimeZoneRegional.setPrompt("Time Zone");
        binding.spinnerDecimalSeparator.setPrompt("Decimal Separator");
        binding.spinnerGroupSeparator.setPrompt("Group Separator");
        binding.spinnerCurrency.setPrompt("Currency");
        binding.spinnerDistance.setPrompt("Distance");
        binding.spinnerAddressFormat.setPrompt("Address Format");

        adapterShortDate.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterShortTime.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterLongDate.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterTimeZone.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterDecimalSeparator.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterGroupSeparator.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterCurrency.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterDistanceUnit.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterAddressFormat.setDropDownViewResource(android.R.layout.simple_list_item_1);

        binding.spinnerShortDateFormat.setAdapter(adapterShortDate);
        binding.spinnerShortTimeFormat.setAdapter(adapterShortTime);
        binding.spinnerLongDate.setAdapter(adapterLongDate);
        binding.spinnerTimeZoneRegional.setAdapter(adapterTimeZone);
        binding.spinnerDecimalSeparator.setAdapter(adapterDecimalSeparator);
        binding.spinnerGroupSeparator.setAdapter(adapterGroupSeparator);
        binding.spinnerCurrency.setAdapter(adapterCurrency);
        binding.spinnerDistance.setAdapter(adapterDistanceUnit);
        binding.spinnerAddressFormat.setAdapter(adapterAddressFormat);

        binding.spinnerAddressFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAddressFormat=i;
                String selectedAddressFormatString=addressFormatList.get(i);
                if(selectedAddressFormatString!=null){
                    binding.textViewAddressSelected.setText(selectedAddressFormatString);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDistanceUnit=i;
                String selectedDistanceUnitString=distanceUnitList.get(i);
                if(selectedDistanceUnitString!=null){
                    String value="125 "+selectedDistanceUnitString;
                    binding.textViewKms.setText(value);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCurrency=i;
                String selectedCurrencyString=currencyList.get(selectedCurrency);
                if(selectedCurrencyString!=null){
                    String value="1,512.56 "+selectedCurrencyString;
                    binding.textViewCurrency.setText(value);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerGroupSeparator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGroupSeparator=i;
                String selectedGroupSeparatorString=groupSeparatorList.get(selectedGroupSeparator);
                if(selectedGroupSeparatorString!=null){
                    String value="125"+selectedGroupSeparatorString+"452";
                    binding.textViewGroupSeparator.setText(value);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerDecimalSeparator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDecimalSeparator=i;
                String selectedDecimalSeparatorString=decimalSeparatorList.get(selectedDecimalSeparator);
                if(selectedDecimalSeparatorString!=null){
                    String value="153"+selectedDecimalSeparatorString+"60";
                    binding.textViewDecimalSeparator.setText(value);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerTimeZoneRegional.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTimeZone=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerLongDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLongDate=i;
                String longDateSelected=long_date_original_list.get(i);
                if(longDateSelected!=null){
                binding.textViewLongDateFormat.setText(longDateSelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerShortDateFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedShortDate=i;
                String selectedShortDateString=short_date_original_list.get(i);
                if(selectedShortDateString!=null){
                    binding.textViewShortDateFormat.setText(selectedShortDateString);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerShortTimeFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedShortTime=i;
                String selectedShortTimeString=short_time_original_list.get(i);
                if(selectedShortTimeString!=null){
                    binding.textViewShortTimeFormat.setText(selectedShortTimeString);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.saveRegionalButton.setOnClickListener(view -> showSaveDialog());
    }

    private void showSaveDialog() {
        if(saveDialog==null){
         AlertDialog.Builder   builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Do you want to Save it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                            validateFieldsForAPI();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());
            saveDialog = builder.create();
        }

        saveDialog.show();
    }

    private void validateFieldsForAPI() {
         String selectedShortDateString=short_date_list.get(selectedShortDate);
         String selectedShortTimeString=short_time_list.get(selectedShortTime);
         String selectedLongDateString=long_date_list.get(selectedLongDate);
         String selectedTimeZoneString=timeZoneList.get(selectedTimeZone);
         String selectedDecimalSeparatorString=decimalSeparatorList.get(selectedDecimalSeparator);
         String selectedGroupSeparatorString=groupSeparatorList.get(selectedGroupSeparator);
         String selectedCurrencyString=currencyList.get(selectedCurrency);
         String selectedDistanceUnitString=distanceUnitList.get(selectedDistanceUnit);
         String selectedAddressFormatString=addressFormatList.get(selectedAddressFormat);
         String domainId=store.getIdDomain();
         String authKey=store.getToken();
        DecimalAndCurrencyFormat decimalAndCurrencyFormat=new DecimalAndCurrencyFormat();
        decimalAndCurrencyFormat.setCurrencySymbol(selectedCurrencyString);
        decimalAndCurrencyFormat.setDecimalSeperator(selectedDecimalSeparatorString);
        decimalAndCurrencyFormat.setDistanceUnit(selectedDistanceUnitString);
        decimalAndCurrencyFormat.setGroupSeperator(selectedGroupSeparatorString);
        DateTimeFormat dateTimeFormat=new DateTimeFormat();
        dateTimeFormat.setLongDateFormat(selectedLongDateString);
        dateTimeFormat.setShortDateFormat(selectedShortDateString);
        dateTimeFormat.setShortTimeFormat(selectedShortTimeString);
        dateTimeFormat.setTimeZone(selectedTimeZoneString);
        RegionalSettings regionalSettings=new RegionalSettings();
        regionalSettings.setDateTimeFormat(dateTimeFormat);
        regionalSettings.setDecimalAndCurrencyFormat(decimalAndCurrencyFormat);
        regionalSettings.setIdDomain(domainId);
        regionalSettings.setAddressFormat(selectedAddressFormatString);
        showProgressDialog();
        initiateAPI(authKey,regionalSettings);


    }

    private void initiateAPI(String authKey, RegionalSettings regionalSettings) {
        subscription.add(viewModel.addRegionalSettingsResponseObservable(authKey,regionalSettings)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::addRegionalResponse,this::errorAddRegionalResponse,this::completedAddRegionalResponse));
    }

    private void completedAddRegionalResponse() {
        dismissProgressDialog();
    }

    private void errorAddRegionalResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        dismissProgressDialog();
    }

    private void addRegionalResponse(RegionalSettingsResponse regionalSettingsResponse) {
        dismissProgressDialog();
        if(regionalSettingsResponse!=null){
            Log.d("RegionalResponse : ",TAG+" / / "+regionalSettingsResponse.toString());
            boolean success=regionalSettingsResponse.isSuccess();
            if(success){
                showToast("Updated Successfully!");
            }
        }else {
            showToast("response is null!");
        }
    }

    private void dismissSaveDialog(){
        if(saveDialog!=null && saveDialog.isShowing()){
            saveDialog.dismiss();
        }
    }
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void dismissProgressDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
    private  String displayTimeZone(TimeZone timeZone){
        long hours= TimeUnit.MILLISECONDS.toHours(timeZone.getRawOffset());
        long minutes=TimeUnit.MILLISECONDS.toMinutes(timeZone.getRawOffset()-TimeUnit.HOURS.toMinutes(hours));
        minutes=Math.abs(minutes);
        String result="";
        if (hours > 0) {
            result = String.format("(GMT+%d:%02d) %s", hours, minutes, timeZone.getID());
        } else {
            result = String.format("(GMT%d:%02d) %s", hours, minutes, timeZone.getID());
        }

        return result;

    }
    private List<String>getCurrencyList(){
        Set<Currency>currencySet=Currency.getAvailableCurrencies();
        for(Currency currency:currencySet){
            currencyList.add(currency.getCurrencyCode());
        }
    return currencyList;
    }
}
