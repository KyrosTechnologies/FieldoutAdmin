package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

public class AddSite extends AppCompatActivity {

    private PreferenceManager store;
    private Spinner spinner_time_zone_site;
    private List<String> timeZoneList=new ArrayList<>();
    private List<String> cusnamestring=new ArrayList<String>();
    ArrayList<CommonJobs> cusDetailsArrayList = new ArrayList<CommonJobs>();
    private String selectedTimeZone=null;
    private EditText site_add_edit_text,additional_address_site,job_number_site,site_first_name,site_last_name,
            phone_site,mobile_site,fax_site,email_site;
    private AutoCompleteTextView address_auto_complete_site,customer_add_auto_complete;
    private RecyclerView tags_selected_site;
    private TextView tags_add_site_text;
    private Button save_site;
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
    private AlertDialog tagsDialog=null;
    private SkilledTradersAdapter adapter;
    private SkilledTradersAdapter selectedAdapter;
    private List<String>tagsList=new ArrayList<>();
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<String>tagsArrayList=new ArrayList<String>();
    private  JSONArray tagarray=null;
    @Inject
    CustomFieldsFragmentViewModel customFieldsFragmentViewModel;
    private CompositeSubscription subscription;
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
    private int spinnerCustomFieldSeletectedChoice=0;
    private String TAG=AddSite.class.getSimpleName();
    private TableLayout table_layout_custom_fields_site;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_site);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectAddSite(this);
        subscription=new CompositeSubscription();
        store= PreferenceManager.getInstance(getApplicationContext());
        spinner_time_zone_site=findViewById(R.id.spinner_time_zone_site);
        site_add_edit_text=findViewById(R.id.site_add_edit_text);
        customer_add_auto_complete=findViewById(R.id.customer_add_auto_complete);
        additional_address_site=findViewById(R.id.additional_address_site);
        address_auto_complete_site=findViewById(R.id.address_auto_complete_site);
        job_number_site=findViewById(R.id.job_number_site);
        //table layout
        table_layout_custom_fields_site=findViewById(R.id.table_layout_custom_fields_site);
        site_first_name=findViewById(R.id.site_first_name);
        site_last_name=findViewById(R.id.site_last_name);
        phone_site=findViewById(R.id.phone_site);
        mobile_site=findViewById(R.id.mobile_site);
        fax_site=findViewById(R.id.fax_site);
        email_site=findViewById(R.id.email_site);
        save_site=findViewById(R.id.save_site);
        tags_selected_site=findViewById(R.id.tags_selected_site);
        tags_add_site_text=findViewById(R.id.tags_add_site_text);
        domainid=store.getIdDomain();
        GetCustomerList();
        GetTagsList();
        callCustomFieldsAPI(domainid,store.getToken());

        String[] ids= TimeZone.getAvailableIDs();
        for(String id:ids){
            timeZoneList.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        ArrayAdapter<String> adapterTimeZone=new  ArrayAdapter<>(AddSite.this,android.R.layout.simple_spinner_item,
                timeZoneList);
        adapterTimeZone.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_time_zone_site.setAdapter(adapterTimeZone);
        spinner_time_zone_site.setPrompt("Time Zone");

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

        tags_add_site_text.setOnClickListener(view -> skilledTradesDialog(new ArrayList<>(),tags_add_site_text));


        save_site.setOnClickListener(view -> {
            cusname=customer_add_auto_complete.getText().toString();
            if (cusname==null && cusname.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Customer Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            sitename=site_add_edit_text.getText().toString();
            if (sitename==null && sitename.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Site Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            address=address_auto_complete_site.getText().toString();
            if (address==null && address.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Address!", Toast.LENGTH_SHORT).show();
                return ;
            }
            myid=job_number_site.getText().toString();
            if (myid==null && myid.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Custom Job!", Toast.LENGTH_SHORT).show();
                return ;
            }

            if(cusname!=null &&!cusname.isEmpty()&&sitename!=null &&!sitename.isEmpty()&&address!=null &&!address.isEmpty()&&myid!=null
                    &&!myid.isEmpty()&&isValidEmailAddress(email_site.getText().toString())){
                addressComplement=additional_address_site.getText().toString();
                contactFirstName=site_first_name.getText().toString();
                contactLastName=site_last_name.getText().toString();
                contactPhone=phone_site.getText().toString();
                contactMobile=mobile_site.getText().toString();
                contactFax=fax_site.getText().toString();
                contactEmail=email_site.getText().toString();
                String cusname=customer_add_auto_complete.getText().toString();
                for (int i=0;i<cusDetailsArrayList.size();i++){
                    if (cusname.equals(cusDetailsArrayList.get(i).getCustomername())){
                        cusid=cusDetailsArrayList.get(i).getCustomerid();
                    }
                }
                GetLatLngList(address);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void callCustomFieldsAPI(String domainId, String authKey) {
        if(domainId!= null && authKey !=null){
            subscription.add(customFieldsFragmentViewModel.getcustomFieldResponseObservable(authKey,domainId,store.getIdDomain())
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
            Log.d("CustomFieRes : ",TAG+" / / "+customFieldResponse);
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
        if(usersCustomFieldList!=null && usersCustomFieldList.size()!=0){
            table_layout_custom_fields_site.removeAllViews();
            typeWhichList.clear();
            int position=0;
            for(CustomField customField:usersCustomFieldList){
                TypeWhich typeWhich=new TypeWhich();
                position++;
                choicesList=customField.getChoices();
                if(choicesList==null){
                    choicesList=new ArrayList<>();
                }
                String id=customField.getId();
                boolean isPrivate=customField.getIsPrivate();
                String name=customField.getName();
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
                        typeWhich.setCustomFieldId(id);
                        inputTextView =new EditText(this);
                        String valueInput="Enter user input "+name;
                        inputTextView.setHint(valueInput);
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
                        typeWhich.setCustomFieldId(id);
                        spinnerCustomField=new Spinner(this);
                        TableRow.LayoutParams tableRowSpinnerParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                        tableRowSpinnerParams.setMargins(10,20,0,10);
                        spinnerCustomField.setLayoutParams(tableRowSpinnerParams);
                        spinnerCustomField.setPrompt(""+name);
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
                        String valueDate="Date "+name;
                        typeWhich.setCustomFieldId(id);
                        dateTextView=new TextView(this);
                        dateTextView.setText(valueDate);
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
                        typeWhich.setCustomFieldId(id);
                        numericEditText=new EditText(this);
                        String valueNumeric="Input "+name;
                        numericEditText.setHint(valueNumeric);
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
                        typeWhich.setCustomFieldId(id);
                        checkBoxCustomField=new CheckBox(this);
                        checkBoxCustomField.setChecked(true);
                        checkBoxCustomField.setId(position);
                        String valueCheckBox="Select "+name;
                        checkBoxCustomField.setText(valueCheckBox);
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
                        typeWhich.setCustomFieldId(id);
                        autoCompleteTextView=new AutoCompleteTextView(this);
                        String valueACTV=""+name;
                        autoCompleteTextView.setHint(valueACTV);
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
                table_layout_custom_fields_site.addView(tableRow);

            }
        }
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


    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void skilledTradesDialog(List<String> skilledName,TextView tView) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddSite.this);
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
                tags_selected_site.setVisibility(View.VISIBLE);
                selectedAdapter=new SkilledTradersAdapter(this,tagsList);
                tags_selected_site.setItemAnimator(new DefaultItemAnimator());
                tags_selected_site.setLayoutManager(new LinearLayoutManager(this));
                tags_selected_site.setAdapter(selectedAdapter);
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
                        AddSite.this.latitude=lat;
                        double lng=location.getDouble("lng");
                        AddSite.this.longitude=lng;

                    }
                    AddSiteApi(sitename,myid,cusid,address,addressComplement,contactFirstName,contactLastName,contactPhone,contactMobile,contactEmail,contactFax,latitude,longitude);

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

    private void GetTagsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"tags/getAll";
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
                    tagarray=new JSONArray(array.toString());
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
                        tags_selected_site.setVisibility(View.VISIBLE);
                        tags_selected_site = findViewById(R.id.tags_selected_site);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                        tags_selected_site.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        tags_selected_site.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        SkilledTradersAdapter projectsAdapter = new SkilledTradersAdapter(AddSite.this,tagsArrayList);
                        tags_selected_site.setAdapter(projectsAdapter);
                        projectsAdapter.notifyDataSetChanged();
                    }else {
                        tags_selected_site.setVisibility(View.GONE);
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

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(AddSite.this);
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

    private void AddSiteApi(String myid, String name,String cusid, String globalAddress, String complementaddress, String firstname,
                                String lastname,String phone, String mobile,  String email, String fax, double latitude, double longitude) {

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "sites/add";
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
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("site");
                        String siteid = first.getString("id");
                        store.putSiteId(String.valueOf(siteid));
                    }
                    AddSite.this.finish();


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
        String url = EndURL.URL+"customers/getAll";
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
                        AddSite.this.cusid=customerid;
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
                            (AddSite.this,android.R.layout.select_dialog_item,customer);
                    customer_add_auto_complete= findViewById(R.id.customer_add_auto_complete);
                    customer_add_auto_complete.setThreshold(1);//will start working from first character
                    customer_add_auto_complete.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    customer_add_auto_complete.setTextColor(R.color.text_light);

                    customer_add_auto_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddSite.this.finish();
                return true;

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