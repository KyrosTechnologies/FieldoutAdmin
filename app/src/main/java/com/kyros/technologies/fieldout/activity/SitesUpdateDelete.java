package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.SkilledTradersAdapter;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.models.CustomField;
import com.kyros.technologies.fieldout.models.CustomFieldResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.CustomFieldsFragmentViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rohin on 22-12-2017.
 */

public class SitesUpdateDelete extends AppCompatActivity {

    private PreferenceManager store;
    private Spinner spinner_time_zone_site;
    private AlertDialog.Builder builder;
    private List<String> timeZoneList=new ArrayList<>();
    private List<String> cusnamestring=new ArrayList<String>();
    ArrayList<CommonJobs> cusDetailsArrayList = new ArrayList<CommonJobs>();
    private String selectedTimeZone=null;
    private EditText site_add_edit_text_update,additional_address_site_update,job_number_site_update,site_first_name_update,
            site_last_name_update,phone_site_update,mobile_site_update,fax_site_update,email_site_update;
    private AutoCompleteTextView address_auto_complete_site_update,customer_add_site_update;
    private Button save_site_update;
    private TextView tags_add_customer_text;
    private RecyclerView tags_selected_customer;
    private String cusid=null;
    private double latitude=0;
    private double longitude=0;
    private String domainid=null;
    private String myid=null;
    private String cusname=null;
    private String sitename=null;
    private String address=null;
    private String addressComplement=null;
    private String contactFirstName=null;
    private String contactLastName=null;
    private String contactPhone=null;
    private String contactMobile=null;
    private String contactEmail=null;
    private String contactFax=null;
    private String siteid=null;
    private String sitname=null;
    private String siteaddress=null;
    private String sitecompaddress=null;
    private String sitemyid=null;
    private String sitefirstname=null;
    private String sitelastname=null;
    private String sitemobile=null;
    private String sitephone=null;
    private String sitefax=null;
    private String siteemail=null;
    private String taginfo=null;
    private  JSONArray tagarray=null;
    private android.app.AlertDialog tagsDialog=null;
    private SkilledTradersAdapter adapter;
    private SkilledTradersAdapter selectedAdapter;
    ArrayList<String>tagsArrayList=new ArrayList<String>();
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private List<String>tagsList=new ArrayList<>();
    @Inject
    CustomFieldsFragmentViewModel customFieldsFragmentViewModel;
    private List<CustomField>usersCustomFieldList=new ArrayList<>();
    private List<TypeWhich>typeWhichList=new ArrayList<>();
    private  List<String>choicesList=new ArrayList<>();
    private EditText inputTextView=null;
    private Spinner spinnerCustomField=null;
    private TextView dateTextView=null;
    private EditText numericEditText=null;
    private CheckBox checkBoxCustomField=null;
    private AutoCompleteTextView autoCompleteTextView =null;
    private int selectedYear=0;
    private int selectedMonth=0;
    private int selectedDay=0;
    private String TAG=SitesUpdateDelete.class.getSimpleName();
    private int spinnerCustomFieldSeletectedChoice=0;
    private String customFields;
    private List<CustomField>updateCustomFieldList=new ArrayList<>();
    private TableLayout table_layout_custom_fields_site_update;
    private CompositeSubscription subscription;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.sites_update_delete);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectSitesUpdateDelete(this);
        store= PreferenceManager.getInstance(getApplicationContext());
        spinner_time_zone_site=findViewById(R.id.spinner_time_zone_site);
        subscription=new CompositeSubscription();
        //table layout
        table_layout_custom_fields_site_update=findViewById(R.id.table_layout_custom_fields_site_update);
        site_add_edit_text_update=findViewById(R.id.site_add_edit_text_update);
        customer_add_site_update=findViewById(R.id.customer_add_site_update);
        additional_address_site_update=findViewById(R.id.additional_address_site_update);
        address_auto_complete_site_update=findViewById(R.id.address_auto_complete_site_update);
        job_number_site_update=findViewById(R.id.job_number_site_update);
        site_first_name_update=findViewById(R.id.site_first_name_update);
        site_last_name_update=findViewById(R.id.site_last_name_update);
        phone_site_update=findViewById(R.id.phone_site_update);
        mobile_site_update=findViewById(R.id.mobile_site_update);
        fax_site_update=findViewById(R.id.fax_site_update);
        email_site_update=findViewById(R.id.email_site_update);
        save_site_update=findViewById(R.id.save_site_update);
        tags_selected_customer=findViewById(R.id.tags_selected_customer);
        tags_add_customer_text=findViewById(R.id.tags_add_customer_text);
        domainid=store.getIdDomain();
        GetCustomerList();
        GetTagsList();
        callCustomFieldsAPI(domainid,store.getToken());

        try {

            Bundle bundle=getIntent().getExtras();
            siteid=bundle.getString("siteid");
            cusid=bundle.getString("customerid");
            sitname=bundle.getString("sitename");
            cusname=bundle.getString("customername");
            siteaddress=bundle.getString("address");
            sitecompaddress=bundle.getString("compaddress");
            sitemyid=bundle.getString("myid");
            sitefirstname=bundle.getString("firstname");
            sitelastname=bundle.getString("lastname");
            sitemobile=bundle.getString("mobile");
            sitephone=bundle.getString("phone");
            sitefax=bundle.getString("fax");
            siteemail=bundle.getString("email");
            customFields=bundle.getString("customFields");
            taginfo=bundle.getString("tags");
            if(customFields!=null){
                updateCustomFieldList=new Gson().fromJson(customFields,new TypeToken<List<CustomField>>(){}.getType());
            }
            tagarray=new JSONArray(taginfo);
            for (int i=0;i<tagarray.length();i++){
                JSONObject first=tagarray.getJSONObject(i);
                String tagid=first.getString("id");
                store.putTagId(String.valueOf(tagid));
                String tagname=first.getString("name");
                tagsList.add(tagname);
            }
            if(tagsList.size()!=0){
                tags_selected_customer.setVisibility(View.VISIBLE);
                selectedAdapter=new SkilledTradersAdapter(this,tagsList);
                tags_selected_customer.setItemAnimator(new DefaultItemAnimator());
                tags_selected_customer.setLayoutManager(new LinearLayoutManager(this));
                tags_selected_customer.setAdapter(selectedAdapter);
                selectedAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if (cusname!=null){
            customer_add_site_update.setText(cusname);
        }
        if (sitname!=null){
            site_add_edit_text_update.setText(sitname);
        }
        if (siteaddress!=null){
            address_auto_complete_site_update.setText(siteaddress);
        }
        if (sitecompaddress!=null){
            additional_address_site_update.setText(sitecompaddress);
        }
        if (sitemyid!=null){
            job_number_site_update.setText(sitemyid);
        }
        if (sitefirstname!=null){
            site_first_name_update.setText(sitefirstname);
        }
        if (sitelastname!=null){
            site_last_name_update.setText(sitelastname);
        }
        if (sitephone!=null){
            phone_site_update.setText(sitephone);
        }
        if (sitemobile!=null){
            mobile_site_update.setText(sitemobile);
        }
        if (sitefax!=null){
            fax_site_update.setText(sitefax);
        }
        if (siteemail!=null){
            email_site_update.setText(siteemail);
        }

        String[] ids= TimeZone.getAvailableIDs();
        for(String id:ids){
            timeZoneList.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        ArrayAdapter<String> adapterTimeZone=new  ArrayAdapter<>(SitesUpdateDelete.this,android.R.layout.simple_spinner_item,
                timeZoneList);
        adapterTimeZone.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_time_zone_site.setAdapter(adapterTimeZone);
        spinner_time_zone_site.setPrompt("Time Zone");

        tags_add_customer_text.setOnClickListener(view -> skilledTradesDialog(new ArrayList<>(),tags_add_customer_text));

        spinner_time_zone_site.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    String text = spinner_time_zone_site.getSelectedItem().toString();
                    selectedTimeZone=text;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save_site_update.setOnClickListener(view -> {
            cusname=customer_add_site_update.getText().toString();
            if (cusname==null && cusname.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Customer Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            sitename=site_add_edit_text_update.getText().toString();
            if (sitename==null && sitename.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Site Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            address=address_auto_complete_site_update.getText().toString();
            if (address==null && address.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Address!", Toast.LENGTH_SHORT).show();
                return ;
            }
            myid=job_number_site_update.getText().toString();
            if (myid==null && myid.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Custom Job!", Toast.LENGTH_SHORT).show();
                return ;
            }

            if(cusname!=null &&!cusname.isEmpty()&&sitename!=null &&!sitename.isEmpty()&&address!=null &&!address.isEmpty()&&myid!=null
                    &&!myid.isEmpty()&&isValidEmailAddress(email_site_update.getText().toString())){
                addressComplement=additional_address_site_update.getText().toString();
                contactFirstName=site_first_name_update.getText().toString();
                contactLastName=site_last_name_update.getText().toString();
                contactPhone=phone_site_update.getText().toString();
                contactMobile=mobile_site_update.getText().toString();
                contactFax=fax_site_update.getText().toString();
                contactEmail=email_site_update.getText().toString();
                GetLatLngList(address);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void callCustomFieldsAPI(String domainId, String authKey) {
        if(domainId!= null && authKey !=null){
            subscription.add(customFieldsFragmentViewModel.getcustomFieldResponseObservable(authKey,domainId,domainId)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                    .subscribe(this::customFieldResponse,this::customFieldErrorResponse,this::customFieldCompletedResponse));
        }else{
            showToast("domain id or authKey is null!");
        }
    }

    private void customFieldCompletedResponse() {
    }

    private void customFieldErrorResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());
    }

    private void customFieldResponse(CustomFieldResponse customFieldResponse) {
        if(customFieldResponse!=null){
            usersCustomFieldList.clear();
            Log.d("Custom Field Res : ",TAG+" / / "+customFieldResponse);
            List<CustomField>customFieldList=customFieldResponse.getCustomFields();
            if(customFieldList!=null && customFieldList.size()!=0){
                for(CustomField customField:customFieldList){
                    String formTYpe=customField.getFormType();
                    switch (formTYpe){
                        case "sites":
                            usersCustomFieldList.add(customField);
                            break;
                    }

                }
            }
            validateCustomField();
        }else{
            showToast("customFieldResponse is null!");
        }
    }
    private void validateCustomField() {
        if(updateCustomFieldList.size()!=0 && usersCustomFieldList.size()!=0 ){
            if(updateCustomFieldList.size()==usersCustomFieldList.size()){
                bindTableViews();
            }else{
                if(updateCustomFieldList.size()<usersCustomFieldList.size()){

                    //separating ids from arraylist
                    List<String>usersCustomFieldIdList=new ArrayList<>();
                    List<String>updateCustomFieldIdList=new ArrayList<>();

                    for(CustomField customField:usersCustomFieldList){
                        usersCustomFieldIdList.add(customField.getId());
                    }
                    for(CustomField customField:updateCustomFieldList){
                        updateCustomFieldIdList.add(customField.getId());
                    }
                    for(int i=0;i<usersCustomFieldIdList.size();i++){
                        if(!updateCustomFieldIdList.contains(usersCustomFieldIdList.get(i))){
                            for(CustomField customField:usersCustomFieldList){
                                if(usersCustomFieldIdList.get(i).equals(customField.getId())){
                                    CustomField customField1=new CustomField();
                                    customField1.setTextValue("");
                                    customField1.setId(customField.getId());
                                    customField1.setFormType(customField.getFormType());
                                    customField1.setTypeOfField(customField.getTypeOfField());
                                    updateCustomFieldList.add(customField1);
                                }
                            }
                        }
                    }
                    bindTableViews();
                }
                if(updateCustomFieldList.size()>usersCustomFieldList.size()){
                    //separating ids from arraylist
                    List<String>usersCustomFieldIdList=new ArrayList<>();
                    List<String>updateCustomFieldIdList=new ArrayList<>();

                    for(CustomField customField:usersCustomFieldList){
                        usersCustomFieldIdList.add(customField.getId());
                    }
                    for(CustomField customField:updateCustomFieldList){
                        updateCustomFieldIdList.add(customField.getId());
                    }
                    for(int i=0;i<updateCustomFieldIdList.size();i++) {
                        if (!usersCustomFieldIdList.contains(updateCustomFieldIdList.get(i))) {
                            for(int j=0;j<updateCustomFieldList.size();j++){
                                if(updateCustomFieldList.get(j).getId().equals(updateCustomFieldIdList.get(i))){
                                    updateCustomFieldList.remove(j);
                                }
                            }
                        }
                    }
                    bindTableViews();

                    Log.d("DELETE SIZE : ",TAG+" / / "+updateCustomFieldList.size());


                }




                showToast("validate customfield else is executing");
            }
        }else{
            showToast("list is empty");
        }
    }

    private void bindTableViews() {
        if(updateCustomFieldList!=null && updateCustomFieldList.size()!=0){
            table_layout_custom_fields_site_update.removeAllViews();
            typeWhichList.clear();
            int position=0;
            for(CustomField customField:updateCustomFieldList){
                Log.d("Cus Field New List : ",""+customField.toString());

                TypeWhich typeWhich=new TypeWhich();
                position++;
                choicesList=customField.getChoices();
                if(choicesList==null){
                    choicesList=new ArrayList<>();
                }
                String customFieldId=customField.getId();
                String name=customField.getTextValue();
                if(name==null){
                    name="";
                }
                String typeOfField=customField.getTypeOfField();

                //Tables Rows
                TableRow tableRow=new TableRow(this);
                //   tableRow.setBackground(getResources().getDrawable(R.color.bg));
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                switch (typeOfField){
                    case "Text":
                        typeWhich.setId(position);
                        typeWhich.setType("Text");
                        typeWhich.setCustomFieldId(customFieldId);
                        inputTextView =new EditText(this);
                        String valueInput="Enter user input "+name;
                        inputTextView.setText(name);
                        inputTextView.setTextSize(20);
                        inputTextView.setId(position);
                        TableRow.LayoutParams tableRowInputTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                        tableRowInputTextParams.setMargins(10,20,0,10);
                        inputTextView.setLayoutParams(tableRowInputTextParams);
                        inputTextView.setSingleLine(true);
                        inputTextView.setBackground(getResources().getDrawable(R.drawable.button_bg));
                        inputTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                        inputTextView.setTextColor(getResources().getColor(R.color.light_black));
                        inputTextView.setPadding(15, 15, 5, 15);
                        tableRow.addView(inputTextView);
                        break;
                    case "List Of Values":
                        typeWhich.setId(position);
                        typeWhich.setType("List Of Values");
                        typeWhich.setCustomFieldId(customFieldId);
                        spinnerCustomField=new Spinner(this);
                        TableRow.LayoutParams tableRowSpinnerParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                        tableRowSpinnerParams.setMargins(10,20,0,10);
                        spinnerCustomField.setLayoutParams(tableRowSpinnerParams);
                        spinnerCustomField.setPrompt(name);
                        spinnerCustomField.setGravity(Gravity.LEFT|Gravity.CENTER);
                        spinnerCustomField.setId(position);
                        spinnerCustomField.setPadding(15, 15, 5, 15);
                        spinnerCustomField.setTag(customField);
                        ArrayAdapter<String> adapterSpinner=new  ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                                choicesList);
                        adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        spinnerCustomField.setAdapter(adapterSpinner);
                        spinnerCustomField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                spinnerCustomFieldSeletectedChoice=i;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        tableRow.addView(spinnerCustomField);
                        break;
                    case "Date":
                        typeWhich.setId(position);
                        typeWhich.setType("Date");
                        typeWhich.setCustomFieldId(customFieldId);
                        String valueDate="Date "+name;
                        dateTextView=new TextView(this);
                        dateTextView.setText(name);
                        dateTextView.setTextSize(20);
                        dateTextView.setId(position);
                        TableRow.LayoutParams tableRowuserNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                        tableRowuserNameParams.setMargins(10,20,0,10);
                        dateTextView.setLayoutParams(tableRowuserNameParams);
                        dateTextView.setTextColor(getResources().getColor(R.color.black));
                        dateTextView.setBackground(getResources().getDrawable(R.drawable.default_text_view_background));
                        dateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                        dateTextView.setPadding(15, 15, 5, 15);
                        dateTextView.setOnClickListener(view ->{
                            Calendar mcurrentDate=Calendar.getInstance();
                            final int mYear = mcurrentDate.get(Calendar.YEAR);
                            final int mMonth=mcurrentDate.get(Calendar.MONTH);
                            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog mDatePicker=new DatePickerDialog(this, (datepicker, selectedyear, selectedmonth, selectedday) -> {
                                selectedMonth=selectedmonth+1;
                                selectedDay=selectedday;
                                selectedYear=selectedyear;
                                String finalTimeDay=String.format("%02d",selectedDay);
                                String finalTimeMonth=String.format("%02d",selectedMonth);

                                String value =selectedYear+"-"+finalTimeMonth+"-"+finalTimeDay;
                                dateTextView.setText(value);
                            },mYear, mMonth, mDay);
                            mDatePicker.show();
                        });
                        tableRow.addView(dateTextView);
                        break;
                    case "Numeric":
                        typeWhich.setId(position);
                        typeWhich.setType("Numeric");
                        typeWhich.setCustomFieldId(customFieldId);
                        numericEditText=new EditText(this);
                        String valueNumeric="Input "+name;
                        numericEditText.setText(name);
                        numericEditText.setTextSize(20);
                        numericEditText.setId(position);
                        numericEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        TableRow.LayoutParams tableRowNumericParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                        tableRowNumericParams.setMargins(10,20,0,10);
                        numericEditText.setLayoutParams(tableRowNumericParams);
                        numericEditText.setSingleLine(true);
                        numericEditText.setBackground(getResources().getDrawable(R.drawable.button_bg));
                        numericEditText.setGravity(Gravity.LEFT|Gravity.CENTER);
                        numericEditText.setTextColor(getResources().getColor(R.color.light_black));
                        numericEditText.setPadding(15, 15, 5, 15);
                        tableRow.addView(numericEditText);
                        break;
                    case "CheckBox":
                        typeWhich.setId(position);
                        typeWhich.setType("CheckBox");
                        typeWhich.setCustomFieldId(customFieldId);
                        checkBoxCustomField=new CheckBox(this);
                        checkBoxCustomField.setChecked(true);
                        checkBoxCustomField.setId(position);
                        String valueCheckBox="Select "+name;
                        checkBoxCustomField.setText(name);
                        TableRow.LayoutParams tableRowCheckBoxParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                        tableRowCheckBoxParams.setMargins(10,20,0,10);
                        checkBoxCustomField.setLayoutParams(tableRowCheckBoxParams);
                        checkBoxCustomField.setGravity(Gravity.LEFT|Gravity.CENTER);
                        checkBoxCustomField.setTextColor(getResources().getColor(R.color.light_black));
                        checkBoxCustomField.setPadding(15, 15, 5, 15);
                        tableRow.addView(checkBoxCustomField);
                        break;
                    case "AutoCompleteBox":
                        typeWhich.setId(position);
                        typeWhich.setType("AutoCompleteBox");
                        typeWhich.setCustomFieldId(customFieldId);
                        autoCompleteTextView=new AutoCompleteTextView(this);
                        String valueACTV=""+name;
                        autoCompleteTextView.setText(name);
                        autoCompleteTextView.setTextSize(20);
                        autoCompleteTextView.setId(position);
                        TableRow.LayoutParams tableRowACTVParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                        tableRowACTVParams.setMargins(10,20,0,10);
                        autoCompleteTextView.setLayoutParams(tableRowACTVParams);
                        autoCompleteTextView.setSingleLine(true);
                        autoCompleteTextView.setBackground(getResources().getDrawable(R.drawable.button_bg));
                        autoCompleteTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                        autoCompleteTextView.setTextColor(getResources().getColor(R.color.black));
                        autoCompleteTextView.setPadding(15, 15, 5, 15);
                        ArrayAdapter<String> adapterACTV = new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line, choicesList);
                        autoCompleteTextView.setAdapter(adapterACTV);
                        tableRow.addView(autoCompleteTextView);
                        break;
                }
                typeWhichList.add(typeWhich);
                table_layout_custom_fields_site_update.addView(tableRow);

            }
        }

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private List<CustomField> getCustomFilesList(){
        List<CustomField>customFieldList=new ArrayList<>();
        if(typeWhichList!=null && typeWhichList.size()!=0){
            int position=0;
            for(TypeWhich typeWhich:typeWhichList){
                position++;
                switch (typeWhich.getType()){
                    case "Text":
                        if(inputTextView!=null){
                            inputTextView.findViewById(typeWhich.getId());
                            String textValue=inputTextView.getText().toString();
                            String formType="sites";
                            String typeOfField="Text";
                            CustomField customField=new CustomField();
                            customField.setTextValue(textValue);
                            customField.setFormType(formType);
                            customField.setId(typeWhich.getCustomFieldId());
                            customField.setTypeOfField(typeOfField);
                            customFieldList.add(customField);
                        }
                        break;
                    case "List Of Values":
                        if(spinnerCustomField!=null){
                            String value ="";
                            spinnerCustomField.findViewById(typeWhich.getId());
                            value=spinnerCustomField.getSelectedItem().toString();
                            CustomField customFieldTemp =(CustomField)spinnerCustomField.getTag();
                            List<String>choiceList=customFieldTemp.getChoices();
                            String formType="sites";
                            String typeOfField="List Of Values";
                            CustomField customField=new CustomField();
                            customField.setTextValue(value);
                            customField.setFormType(formType);
                            customField.setId(typeWhich.getCustomFieldId());
                            customField.setChoices(choiceList);
                            customField.setTypeOfField(typeOfField);
                            customFieldList.add(customField);

                        }
                        break;
                    case "Date":
                        if(dateTextView!=null){
                            String textValue ="";
                            dateTextView.findViewById(typeWhich.getId());
                            textValue=dateTextView.getText().toString();
                            String formType="sites";
                            String typeOfField="Date";
                            CustomField customField=new CustomField();
                            customField.setTextValue(textValue);
                            customField.setId(typeWhich.getCustomFieldId());
                            customField.setFormType(formType);
                            customField.setTypeOfField(typeOfField);
                            customFieldList.add(customField);
                        }
                        break;
                    case "Numeric":
                        if(numericEditText!=null){
                            numericEditText.findViewById(typeWhich.getId());
                            String textValue=numericEditText.getText().toString();
                            String formType="sites";
                            String typeOfField="Numeric";
                            CustomField customField=new CustomField();
                            customField.setTextValue(textValue);
                            customField.setFormType(formType);
                            customField.setId(typeWhich.getCustomFieldId());
                            customField.setTypeOfField(typeOfField);
                            customFieldList.add(customField);
                        }
                        break;
                    case "CheckBox":
                        if(checkBoxCustomField!=null){
                            String textValue="";
                            checkBoxCustomField.findViewById(typeWhich.getId());
                            boolean isChecked=checkBoxCustomField.isChecked();
                            if(isChecked){
                                textValue="True";
                            }else{
                                textValue="False";
                            }
                            String formType="sites";
                            String typeOfField="CheckBox";
                            CustomField customField=new CustomField();
                            customField.setTextValue(textValue);
                            customField.setId(typeWhich.getCustomFieldId());
                            customField.setFormType(formType);
                            customField.setTypeOfField(typeOfField);
                            customFieldList.add(customField);
                        }
                        break;
                    case "AutoCompleteBox":
                        if(autoCompleteTextView!=null){
                            autoCompleteTextView.findViewById(typeWhich.getId());
                            String textValue=autoCompleteTextView.getText().toString();
                            String formType="sites";
                            String typeOfField="AutoCompleteBox";
                            CustomField customField=new CustomField();
                            customField.setTextValue(textValue);
                            customField.setId(typeWhich.getCustomFieldId());
                            customField.setFormType(formType);
                            customField.setTypeOfField(typeOfField);
                            customFieldList.add(customField);
                        }
                        break;

                }
            }
        }
        return customFieldList;
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

    private void skilledTradesDialog(List<String> skilledName,TextView tView) {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(SitesUpdateDelete.this);
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.adapter_teams_list,null);
        EditText editText=view.findViewById(R.id.et_skilled);
        editText.setHint("Enter tags");
        Button button=view.findViewById(R.id.button_add_skill);
        RecyclerView recyclerView=view.findViewById(R.id.skilled_traders_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new SkilledTradersAdapter(this,skilledName);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
        button.setOnClickListener(views->{
            String edName=editText.getText().toString();
            if(edName!=null && !edName.isEmpty()){
                adapter.setSkilledTradersName(edName);
                editText.setText("");
            }
        });
        builder.setPositiveButton("OK",(dialog,id)->{
            tagsList=adapter.getSkilledTradersList();
            if(tagsList.size()!=0){
                tags_selected_customer.setVisibility(View.VISIBLE);
                selectedAdapter=new SkilledTradersAdapter(this,tagsList);
                tags_selected_customer.setItemAnimator(new DefaultItemAnimator());
                tags_selected_customer.setLayoutManager(new LinearLayoutManager(this));
                tags_selected_customer.setAdapter(selectedAdapter);
                selectedAdapter.notifyDataSetChanged();
                for (int i=0;i<tagsList.size();i++){
                }
            }

        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissTagsDialog();
        });
        tagsDialog=builder.create();
        tagsDialog.show();

    }
    private  void dismissTagsDialog(){
        if(tagsDialog!=null && tagsDialog.isShowing()){
            tagsDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissTagsDialog();
        subscription.clear();

    }

    private void GetTagsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"tags/getByDomainId/"+domainid;
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso=new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                commonJobsArrayList.clear();

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("tags");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String tagid=first.getString("id");
                        String tagname=first.getString("name");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setTagid(tagid);
                        commonJobs.setTagname(tagname);
                        commonJobsArrayList.add(commonJobs);
                        tagsArrayList.add(tagname);

                    }

                    if (tagsArrayList.size()!=0){
                        tags_selected_customer.setVisibility(View.VISIBLE);
                        tags_selected_customer = findViewById(R.id.tags_selected_customer);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                        tags_selected_customer.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        tags_selected_customer.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        SkilledTradersAdapter projectsAdapter = new SkilledTradersAdapter(SitesUpdateDelete.this,tagsArrayList);
                        tags_selected_customer.setAdapter(projectsAdapter);
                        projectsAdapter.notifyDataSetChanged();
                    }else {
                        tags_selected_customer.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //dismissProgressDialog();

            }
        }) {

            @Override
            public Map<String, String> getHeaders()throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idDomain",store.getIdDomain());
                params.put("Authorization", store.getToken());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void GetLatLngList(String address) {
        String tag_json_obj = "json_obj_req";
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+address+"&key=AIzaSyD917w2chNF_jp_9W5f7s-yZ-jTqcYY3Lg";
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("results");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        JSONObject geometry=first.getJSONObject("geometry");
                        JSONObject location=geometry.getJSONObject("location");
                        double lat=location.getDouble("lat");
                        SitesUpdateDelete.this.latitude=lat;
                        double lng=location.getDouble("lng");
                        SitesUpdateDelete.this.longitude=lng;

                    }
                    UpdateSiteApi(sitename,myid,cusid,address,addressComplement,contactFirstName,contactLastName,contactPhone,contactMobile,contactEmail,contactFax,latitude,longitude);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();

            }
        }) {

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    //Update Sites Put Method//

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(SitesUpdateDelete.this);
            pDialog.setTitle("Please Wait");
            pDialog.setMessage("Synchronization in progress...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
        }
        pDialog.show();
    }

    private void dismissProgressDialog() {
        try{
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void UpdateSiteApi(String myid, String name,String cusid, String globalAddress, String complementaddress, String firstname,
                            String lastname,String phone, String mobile,  String email, String fax, double latitude, double longitude) {

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "sites/update/"+siteid;
        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin = new JSONObject();

        JSONObject position=new JSONObject();
        try {
            position.put("lat",latitude);
            position.put("lng",longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String>tagListId=new ArrayList<String>();
        for (int i=0;i<tagarray.length();i++){
            JSONObject first= null;
            try {
                first = tagarray.getJSONObject(i);
                String tagid=first.getString("id");
                String tagname=first.getString("name");
                if (tagsArrayList.get(i).equals(tagname)){
                    tagListId.add(tagid);
                }
            } catch (JSONException e) {
            }

        }
        JSONArray jsonArray=new JSONArray();
        for (int i=0;i<tagListId.size();i++){
            jsonArray.put(tagListId.get(i));
        }

        try {
            inputLogin.put("name",myid);
            inputLogin.put("myId",name);
            inputLogin.put("address",globalAddress);
            inputLogin.put("idCustomer",cusid);
            inputLogin.put("addressComplement",complementaddress);
            inputLogin.put("contactFirstName",firstname);
            inputLogin.put("contactLastName",lastname);
            inputLogin.put("contactPhone",phone);
            inputLogin.put("contactMobile",mobile);
            inputLogin.put("contactEmail",email);
            inputLogin.put("contactFax",fax);
            inputLogin.put("positions",position);
            inputLogin.put("tags",jsonArray);
            inputLogin.put("CustomFieldValues",new JSONArray(new Gson().toJson(getCustomFilesList())));

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("inputJsonuser", inputLogin.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("result");
                        String siteid = first.getString("id");
                        store.putSiteId(String.valueOf(siteid));
                    }
                    SitesUpdateDelete.this.finish();
//                    SiteFragment h= new SiteFragment();
//                    android.support.v4.app.FragmentTransaction k=
//                            getSupportFragmentManager().beginTransaction();
//                    k.replace(R.id.container_fragments,h);
//                    k.commit();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.e("Error", "" + error.toString());
                }
                dismissProgressDialog();
//                texts.setText(error.toString());
            }
        }) {


            @Override
            public Map<String, String> getHeaders()throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", store.getToken());
                params.put("idDomain",store.getIdDomain());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void GetCustomerList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"customers/getByDomainId/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                cusDetailsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("customers");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String customerid=first.getString("id");
                        store.putCustomerId(String.valueOf(customerid));
                        String name="";
                        try {
                            name=first.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactEmail="";
                        try {
                            contactEmail=first.getString("contactEmail");
                        }catch (Exception e){

                        }
                        String contactfirstname="";
                        try {
                            contactfirstname=first.getString("contactFirstName");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactlastname="";
                        try {
                            contactlastname=first.getString("contactLastName");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactmobile="";
                        try {
                            contactmobile=first.getString("contactMobile");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactphone="";
                        try {
                            contactphone=first.getString("contactPhone");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String address="";
                        try {
                            address=first.getString("address");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String addressComplement="";
                        try {
                            addressComplement=first.getString("addressComplement");
                        }catch (Exception e){

                        }
                        JSONObject tagInfo =null;
                        try {
                            tagInfo = first.getJSONObject("tagInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        String tagid=null;
                        try {
                            tagid=tagInfo.getString("id");
                            store.putTagId(String.valueOf(tagid));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String tagname="";
                        try {
                            tagname=tagInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setCustomername(name);
                        commonJobs.setCustomerid(customerid);
                        SitesUpdateDelete.this.cusid=customerid;
                        commonJobs.setFirstname(contactfirstname);
                        commonJobs.setLastname(contactlastname);
                        commonJobs.setMobilenum(contactmobile);
                        commonJobs.setPhone(contactphone);
                        commonJobs.setAddress(address);
                        commonJobs.setTagname(tagname);
                        commonJobs.setComplementAddress(addressComplement);
                        commonJobs.setEmail(contactEmail);
                        cusDetailsArrayList.add(commonJobs);
                        cusnamestring.add(name);

                    }
                    String[]customer=cusnamestring.stream().toArray(String[]::new);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (SitesUpdateDelete.this,android.R.layout.select_dialog_item,customer);
                    customer_add_site_update= findViewById(R.id.customer_add_site_update);
                    customer_add_site_update.setThreshold(1);//will start working from first character
                    customer_add_site_update.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    customer_add_site_update.setTextColor(R.color.text_light);

                    customer_add_site_update.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String customername=customer[i];
                            bindViews(customername);
                        }
                    });


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();
                Log.e("Error"," "+error.getMessage());

            }
        }) {

            @Override
            public Map<String, String> getHeaders()throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", store.getToken());
                params.put("idDomain",store.getIdDomain());
                return params;
            }


        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void bindViews(String customername) {
        for (int i = 0; i < cusDetailsArrayList.size(); i++) {
            String cname = cusDetailsArrayList.get(i).getCustomername();

        }
    }

    private void DeleteSitesList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"sites/delete/"+siteid;
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso=new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.DELETE, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean array=obj.getBoolean("result");
                    String message=obj.getString("message");
                    if (message!=null){
                        Toast.makeText(getApplicationContext()," "+message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                SitesUpdateDelete.this.finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //dismissProgressDialog();

            }
        }) {

            @Override
            public Map<String, String> getHeaders()throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idDomain",store.getIdDomain());
                params.put("Authorization", store.getToken());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void showDeleteDialog(){
        if(builder==null){
            builder = new AlertDialog.Builder(SitesUpdateDelete.this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        DeleteSitesList();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                SitesUpdateDelete.this.finish();
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    public class TypeWhich{
        private int id;
        private String type;
        private String customFieldId;
        public TypeWhich(){

        }

        public String getCustomFieldId() {
            return customFieldId;
        }

        public void setCustomFieldId(String customFieldId) {
            this.customFieldId = customFieldId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    public boolean isValidEmailAddress(String email) {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}