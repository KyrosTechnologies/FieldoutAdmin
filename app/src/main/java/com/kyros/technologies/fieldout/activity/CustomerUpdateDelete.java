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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.CustomFieldsAdapter;
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

public class CustomerUpdateDelete extends AppCompatActivity implements CustomFieldsAdapter.OnItemClickListener {

    private PreferenceManager store;
    private Spinner spinner_time_zone_customer;
    private List<String> timeZoneList=new ArrayList<>();
    private List<String> cusnamestring=new ArrayList<String>();
    ArrayList<CommonJobs> cusDetailsArrayList = new ArrayList<CommonJobs>();
    private AlertDialog.Builder builder;
    private String selectedTimeZone=null;
    private EditText address_update_customer,additional_address_update,job_number_update,
            customer_first_name_update,customer_last_name_update,phone_customer_update,mobile_customer_update,fax_customer_update,
            email_customer_update;
    private Button save_customer_update;
    private AutoCompleteTextView customer_update_edit_text;
    private TextView tags_add_customer_text;
    private RecyclerView tags_selected_customer;
    private double latitude=0;
    private double longitude=0;
    private String domainid=null;
    private String myid=null;
    private String cusname=null;
    private String address=null;
    private String addressComplement=null;
    private String contactFirstName=null;
    private String contactLastName=null;
    private String contactPhone=null;
    private String contactMobile=null;
    private String contactEmail=null;
    private String contactFax=null;
    private String customerid=null;
    private String customername=null;
    private String cusaddress=null;
    private String compaddress=null;
    private String cusmyid=null;
    private String cusfirstname=null;
    private String cuslastname=null;
    private String cusmobile=null;
    private String cusphone=null;
    private String cusfax=null;
    private String cusemail=null;
    private List<String>tagsList=new ArrayList<>();
    private  JSONArray tagarray=null;
    private android.app.AlertDialog tagsDialog=null;
    private SkilledTradersAdapter adapter;
    private SkilledTradersAdapter selectedAdapter;
    ArrayList<String>tagsArrayList=new ArrayList<String>();
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private String taginfo=null;
    private TableLayout table_layout_custom_fields_customer_update;
    private CompositeSubscription subscription;
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
    private String TAG=CustomerUpdateDelete.class.getSimpleName();
    private int spinnerCustomFieldSeletectedChoice=0;
    private String customFields;
    private List<CustomField>updateCustomFieldList=new ArrayList<>();
    private ProgressDialog pDialog;
    private CustomFieldsAdapter customFieldsAdapter;
    private RecyclerView recycler_custom_update;
    private android.app.AlertDialog multipleSelectDialog;
    private SkilledTradersAdapter tagsAdapter;
    private List<String>selectedTagListName=new ArrayList<>();
    private AlertDialog customDialogBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.customers_update_delete);
        store= PreferenceManager.getInstance(getApplicationContext());
        ((ServiceHandler)getApplication()).getApplicationComponent().injectCustomerUpdateDelete(this);
        subscription=new CompositeSubscription();
        spinner_time_zone_customer=findViewById(R.id.spinner_time_zone_customer);
        customFieldsAdapter=new CustomFieldsAdapter();
        recycler_custom_update=findViewById(R.id.recycler_custom_update);
        //table layout
        table_layout_custom_fields_customer_update=findViewById(R.id.table_layout_custom_fields_customer_update);
        customer_update_edit_text=findViewById(R.id.customer_update_edit_text);
        additional_address_update=findViewById(R.id.additional_address_update);
        address_update_customer=findViewById(R.id.address_update_customer);
        job_number_update=findViewById(R.id.job_number_update);
        customer_first_name_update=findViewById(R.id.customer_first_name_update);
        customer_last_name_update=findViewById(R.id.customer_last_name_update);
        phone_customer_update=findViewById(R.id.phone_customer_update);
        mobile_customer_update=findViewById(R.id.mobile_customer_update);
        fax_customer_update=findViewById(R.id.fax_customer_update);
        email_customer_update=findViewById(R.id.email_customer_update);
        save_customer_update=findViewById(R.id.save_customer_update);
        tags_selected_customer=findViewById(R.id.tags_selected_customer);
        tags_add_customer_text=findViewById(R.id.tags_add_customer_text);
        domainid=store.getIdDomain();
        GetCustomerList();
        GetTagsList();

        try {

            Bundle bundle=getIntent().getExtras();
            customerid=bundle.getString("customerid");
            customername=bundle.getString("customername");
            cusaddress=bundle.getString("address");
            compaddress=bundle.getString("compaddress");
            cusmyid=bundle.getString("myid");
            cusfirstname=bundle.getString("firstname");
            cuslastname=bundle.getString("lastname");
            cusmobile=bundle.getString("mobile");
            cusphone=bundle.getString("phone");
            cusfax=bundle.getString("fax");
            cusemail=bundle.getString("email");
            taginfo=bundle.getString("tags");
            customFields=bundle.getString("customFields");
            if(customFields!=null){
                updateCustomFieldList=new Gson().fromJson(customFields,new TypeToken<List<CustomField>>(){}.getType());
            }
            Log.d("CustomFileds : ",TAG+" / / "+customFields);
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
        callCustomFieldsAPI(domainid,store.getToken());

        if (customername!=null){
            customer_update_edit_text.setText(customername);
        }
        if (cusaddress!=null){
            address_update_customer.setText(cusaddress);
        }
        if (compaddress!=null){
            additional_address_update.setText(compaddress);
        }
        if (cusmyid!=null){
            job_number_update.setText(cusmyid);
        }
        if (cusfirstname!=null){
            customer_first_name_update.setText(cusfirstname);
        }
        if (cuslastname!=null){
            customer_last_name_update.setText(cuslastname);
        }
        if (cusphone!=null){
            phone_customer_update.setText(cusphone);
        }
        if (cusmobile!=null){
            mobile_customer_update.setText(cusmobile);
        }
        if (cusfax!=null){
            fax_customer_update.setText(cusfax);
        }
        if (cusemail!=null){
            email_customer_update.setText(cusemail);
        }

        tags_add_customer_text.setOnClickListener(view -> {
            if(tagsArrayList.size() != 0){
                multipleSelectDialogBox("Select Tags",tagsArrayList.toArray(new String[tagsArrayList.size()]),new boolean[tagsArrayList.size()]);
            }else{
                showToast("Tag list is empty!");
            }
        });

        String[] ids= TimeZone.getAvailableIDs();
        for(String id:ids){
            timeZoneList.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        ArrayAdapter<String> adapterTimeZone=new  ArrayAdapter<>(CustomerUpdateDelete.this,android.R.layout.simple_spinner_item,
                timeZoneList);
        adapterTimeZone.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_time_zone_customer.setAdapter(adapterTimeZone);
        spinner_time_zone_customer.setPrompt("Time Zone");

        spinner_time_zone_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    String text = spinner_time_zone_customer.getSelectedItem().toString();
                    selectedTimeZone=text;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save_customer_update.setOnClickListener(view -> {
            Log.d("CustomField OutPut : ",""+new Gson().toJson(customFieldsAdapter.getCustomFieldListOutput()));
            cusname=customer_update_edit_text.getText().toString();
            if (cusname==null && cusname.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Customer Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            address=address_update_customer.getText().toString();
            if (address==null && address.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Address!", Toast.LENGTH_SHORT).show();
                return ;
            }
            myid=job_number_update.getText().toString();
            if (myid==null && myid.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Custom Job!", Toast.LENGTH_SHORT).show();
                return ;
            }

            if(cusname!=null &&!cusname.isEmpty()&&address!=null &&!address.isEmpty()&&myid!=null &&!myid.isEmpty()&&
                    isValidEmailAddress(email_customer_update.getText().toString())){
                addressComplement=additional_address_update.getText().toString();
                contactFirstName=customer_first_name_update.getText().toString();
                contactLastName=customer_last_name_update.getText().toString();
                contactPhone=phone_customer_update.getText().toString();
                contactMobile=mobile_customer_update.getText().toString();
                contactFax=fax_customer_update.getText().toString();
                contactEmail=email_customer_update.getText().toString();
                GetLatLngList(address);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void callCustomFieldsAPI(String domainId, String authKey) {
        if(domainId!= null && authKey !=null){
            subscription.add(customFieldsFragmentViewModel.getcustomFieldResponseObservable(authKey,domainId)
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
                        case "customers":
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
                bindRecyclerViews();
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
                    bindRecyclerViews();
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
                    bindRecyclerViews();

                    Log.d("DELETE SIZE : ",TAG+" / / "+updateCustomFieldList.size());


                }




                showToast("validate customfield else is executing");
            }
        }else{
            showToast("list is empty");
        }
    }
    private void bindRecyclerViews(){
      //  updateCustomFieldList
        recycler_custom_update.setLayoutManager(new LinearLayoutManager(this));
        recycler_custom_update.setItemAnimator(new DefaultItemAnimator());
        customFieldsAdapter.setCustomFieldData(updateCustomFieldList, this,"update","customers",this);
        recycler_custom_update.setAdapter(customFieldsAdapter);
    }

//    private void bindTableViews() {
//        if(updateCustomFieldList!=null && updateCustomFieldList.size()!=0){
//            table_layout_custom_fields_customer_update.removeAllViews();
//            typeWhichList.clear();
//            int position=0;
//            for(CustomField customField:updateCustomFieldList){
//                Log.d("Cus Field New List : ",""+customField.toString());
//
//                TypeWhich typeWhich=new TypeWhich();
//                position++;
//                choicesList=customField.getChoices();
//                if(choicesList==null){
//                    choicesList=new ArrayList<>();
//                }
//                String customFieldId=customField.getId();
//                String name=customField.getTextValue();
//                if(name==null){
//                    name="";
//                }
//                String typeOfField=customField.getTypeOfField();
//
//                //Tables Rows
//                TableRow tableRow=new TableRow(this);
//                //   tableRow.setBackground(getResources().getDrawable(R.color.bg));
//                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//                switch (typeOfField){
//                    case "Text":
//                        typeWhich.setId(position);
//                        typeWhich.setType("Text");
//                        typeWhich.setCustomFieldId(customFieldId);
//                        inputTextView =new EditText(this);
//                        String valueInput="Enter user input "+name;
//                        inputTextView.setText(name);
//                        inputTextView.setTextSize(20);
//                        inputTextView.setId(position);
//                        TableRow.LayoutParams tableRowInputTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
//                        tableRowInputTextParams.setMargins(10,20,0,10);
//                        inputTextView.setLayoutParams(tableRowInputTextParams);
//                        inputTextView.setSingleLine(true);
//                        inputTextView.setBackground(getResources().getDrawable(R.drawable.button_bg));
//                        inputTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
//                        inputTextView.setTextColor(getResources().getColor(R.color.light_black));
//                        inputTextView.setPadding(15, 15, 5, 15);
//                        tableRow.addView(inputTextView);
//                        break;
//                    case "List Of Values":
//                        typeWhich.setId(position);
//                        typeWhich.setType("List Of Values");
//                        typeWhich.setCustomFieldId(customFieldId);
//                        spinnerCustomField=new Spinner(this);
//                        TableRow.LayoutParams tableRowSpinnerParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
//                        tableRowSpinnerParams.setMargins(10,20,0,10);
//                        spinnerCustomField.setLayoutParams(tableRowSpinnerParams);
//                        spinnerCustomField.setPrompt(name);
//                        spinnerCustomField.setGravity(Gravity.LEFT|Gravity.CENTER);
//                        spinnerCustomField.setId(position);
//                        spinnerCustomField.setPadding(15, 15, 5, 15);
//                        spinnerCustomField.setTag(customField);
//                        ArrayAdapter<String> adapterSpinner=new  ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
//                                choicesList);
//                        adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_1);
//                        spinnerCustomField.setAdapter(adapterSpinner);
//                        spinnerCustomField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                spinnerCustomFieldSeletectedChoice=i;
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//
//                            }
//                        });
//                        tableRow.addView(spinnerCustomField);
//                        break;
//                    case "Date":
//                        typeWhich.setId(position);
//                        typeWhich.setType("Date");
//                        typeWhich.setCustomFieldId(customFieldId);
//                        String valueDate="Date "+name;
//                        dateTextView=new TextView(this);
//                        dateTextView.setText(name);
//                        dateTextView.setTextSize(20);
//                        dateTextView.setId(position);
//                        TableRow.LayoutParams tableRowuserNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
//                        tableRowuserNameParams.setMargins(10,20,0,10);
//                        dateTextView.setLayoutParams(tableRowuserNameParams);
//                        dateTextView.setTextColor(getResources().getColor(R.color.black));
//                        dateTextView.setBackground(getResources().getDrawable(R.drawable.default_text_view_background));
//                        dateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
//                        dateTextView.setPadding(15, 15, 5, 15);
//                        dateTextView.setOnClickListener(view ->{
//                            Calendar mcurrentDate=Calendar.getInstance();
//                            final int mYear = mcurrentDate.get(Calendar.YEAR);
//                            final int mMonth=mcurrentDate.get(Calendar.MONTH);
//                            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
//
//                            DatePickerDialog mDatePicker=new DatePickerDialog(this, (datepicker, selectedyear, selectedmonth, selectedday) -> {
//                                selectedMonth=selectedmonth+1;
//                                selectedDay=selectedday;
//                                selectedYear=selectedyear;
//                                String finalTimeDay=String.format("%02d",selectedDay);
//                                String finalTimeMonth=String.format("%02d",selectedMonth);
//
//                                String value =selectedYear+"-"+finalTimeMonth+"-"+finalTimeDay;
//                                dateTextView.setText(value);
//                            },mYear, mMonth, mDay);
//                            mDatePicker.show();
//                        });
//                        tableRow.addView(dateTextView);
//                        break;
//                    case "Numeric":
//                        typeWhich.setId(position);
//                        typeWhich.setType("Numeric");
//                        typeWhich.setCustomFieldId(customFieldId);
//                        numericEditText=new EditText(this);
//                        String valueNumeric="Input "+name;
//                        numericEditText.setText(name);
//                        numericEditText.setTextSize(20);
//                        numericEditText.setId(position);
//                        numericEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
//                        TableRow.LayoutParams tableRowNumericParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
//                        tableRowNumericParams.setMargins(10,20,0,10);
//                        numericEditText.setLayoutParams(tableRowNumericParams);
//                        numericEditText.setSingleLine(true);
//                        numericEditText.setBackground(getResources().getDrawable(R.drawable.button_bg));
//                        numericEditText.setGravity(Gravity.LEFT|Gravity.CENTER);
//                        numericEditText.setTextColor(getResources().getColor(R.color.light_black));
//                        numericEditText.setPadding(15, 15, 5, 15);
//                        tableRow.addView(numericEditText);
//                        break;
//                    case "CheckBox":
//                        typeWhich.setId(position);
//                        typeWhich.setType("CheckBox");
//                        typeWhich.setCustomFieldId(customFieldId);
//                        checkBoxCustomField=new CheckBox(this);
//                        checkBoxCustomField.setChecked(true);
//                        checkBoxCustomField.setId(position);
//                        String valueCheckBox="Select "+name;
//                        checkBoxCustomField.setText(name);
//                        TableRow.LayoutParams tableRowCheckBoxParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
//                        tableRowCheckBoxParams.setMargins(10,20,0,10);
//                        checkBoxCustomField.setLayoutParams(tableRowCheckBoxParams);
//                        checkBoxCustomField.setGravity(Gravity.LEFT|Gravity.CENTER);
//                        checkBoxCustomField.setTextColor(getResources().getColor(R.color.light_black));
//                        checkBoxCustomField.setPadding(15, 15, 5, 15);
//                        tableRow.addView(checkBoxCustomField);
//                        break;
//                    case "AutoCompleteBox":
//                        typeWhich.setId(position);
//                        typeWhich.setType("AutoCompleteBox");
//                        typeWhich.setCustomFieldId(customFieldId);
//                        autoCompleteTextView=new AutoCompleteTextView(this);
//                        String valueACTV=""+name;
//                        autoCompleteTextView.setText(name);
//                        autoCompleteTextView.setTextSize(20);
//                        autoCompleteTextView.setId(position);
//                        TableRow.LayoutParams tableRowACTVParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
//                        tableRowACTVParams.setMargins(10,20,0,10);
//                        autoCompleteTextView.setLayoutParams(tableRowACTVParams);
//                        autoCompleteTextView.setSingleLine(true);
//                        autoCompleteTextView.setBackground(getResources().getDrawable(R.drawable.button_bg));
//                        autoCompleteTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
//                        autoCompleteTextView.setTextColor(getResources().getColor(R.color.black));
//                        autoCompleteTextView.setPadding(15, 15, 5, 15);
//                        ArrayAdapter<String> adapterACTV = new ArrayAdapter<>(this,
//                                android.R.layout.simple_dropdown_item_1line, choicesList);
//                        autoCompleteTextView.setAdapter(adapterACTV);
//                        tableRow.addView(autoCompleteTextView);
//                        break;
//                }
//                typeWhichList.add(typeWhich);
//                table_layout_custom_fields_customer_update.addView(tableRow);
//
//            }
//        }
//
//    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(CustomerUpdateDelete.this);
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
                    AddTagsApi(tagsList.get(i));
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
        dismissCustomDialogBox();
        dismissMultipleSelectDialogBox();

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

//                    if (tagsArrayList.size()!=0){
//                        tags_selected_customer.setVisibility(View.VISIBLE);
//                        tags_selected_customer = findViewById(R.id.tags_selected_customer);
//                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
//                        tags_selected_customer.setLayoutManager(layoutManager);
//                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
//                        tags_selected_customer.setItemAnimator(new DefaultItemAnimator());
//                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                        SkilledTradersAdapter projectsAdapter = new SkilledTradersAdapter(CustomerUpdateDelete.this,tagsArrayList);
//                        tags_selected_customer.setAdapter(projectsAdapter);
//                        projectsAdapter.notifyDataSetChanged();
//                    }else {
//                        tags_selected_customer.setVisibility(View.GONE);
//                    }

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
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20*10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void AddTagsApi(String name) {

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "tags/add";
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();

        try {
            inputLogin.put("name",name);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("inputJsonuser", inputLogin.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("tag");
                        String tagid=first.getString("id");
                        store.putTagId(String.valueOf(tagid));

                        tagsArrayList.add(tagid);

                    }


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
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20*10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                        CustomerUpdateDelete.this.latitude=lat;
                        double lng=location.getDouble("lng");
                        CustomerUpdateDelete.this.longitude=lng;

                    }

                    UpdateCustomerApi(cusname,myid,address,addressComplement,contactFirstName,contactLastName,contactPhone,contactMobile,contactEmail,contactFax,latitude,longitude);

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

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(CustomerUpdateDelete.this);
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

    private void UpdateCustomerApi(String myid, String name, String globalAddress, String complementaddress, String firstname,
                                String lastname,String phone, String mobile,  String email, String fax, double latitude, double longitude) {

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "customers/update/"+customerid;
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

//        ArrayList<String>tagListId=new ArrayList<String>();
//        for (int i=0;i<tagarray.length();i++){
//            JSONObject first= null;
//            try {
//                first = tagarray.getJSONObject(i);
//                String tagid=first.getString("id");
//                String tagname=first.getString("name");
//                if (tagsList.get(i).equals(tagname)){
//                    tagListId.add(tagid);
//                }
//            } catch (JSONException e) {
//            }
//
//        }

        ArrayList<String>tagListId=new ArrayList<String>();
        for (int i=0;i<selectedTagListName.size();i++) {
            for (int j = 0; j < commonJobsArrayList.size(); j++) {
                if (commonJobsArrayList.get(j).getTagname().equals(selectedTagListName.get(i))) {
                    tagListId.add(commonJobsArrayList.get(j).getTagid());
                }
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
            inputLogin.put("addressComplement",complementaddress);
            inputLogin.put("contactFirstName",firstname);
            inputLogin.put("contactLastName",lastname);
            inputLogin.put("contactPhone",phone);
            inputLogin.put("contactMobile",mobile);
            inputLogin.put("contactEmail",email);
            inputLogin.put("contactFax",fax);
            inputLogin.put("positions",position);
            inputLogin.put("tags",jsonArray);
            inputLogin.put("CustomFieldValues",new JSONArray(new Gson().toJson(customFieldsAdapter.getCustomFieldListOutput())));
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
                        String customerid = first.getString("id");
                        store.putCustomerId(String.valueOf(customerid));

                    }
                    CustomerUpdateDelete.this.finish();
//

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
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20*10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

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
                            String formType="customers";
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
                            String formType="customers";
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
                            String formType="customers";
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
                            String formType="customers";
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
                            String formType="customers";
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
                            String formType="customers";
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
                        CustomerUpdateDelete.this.customerid=customerid;
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
                            (CustomerUpdateDelete.this,android.R.layout.select_dialog_item,customer);
                    customer_update_edit_text= findViewById(R.id.customer_update_edit_text);
                    customer_update_edit_text.setThreshold(1);//will start working from first character
                    customer_update_edit_text.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    customer_update_edit_text.setTextColor(R.color.text_light);

                    customer_update_edit_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20*10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void bindViews(String customername) {
        for (int i = 0; i < cusDetailsArrayList.size(); i++) {
            String cname = cusDetailsArrayList.get(i).getCustomername();

        }
    }

    private void DeleteActivitiesList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"customers/delete/"+customerid;
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
                CustomerUpdateDelete.this.finish();
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
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20*10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void showDeleteDialog(){
        if(builder==null){
            builder = new AlertDialog.Builder(CustomerUpdateDelete.this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        DeleteActivitiesList();
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
                CustomerUpdateDelete.this.finish();
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
            showToast("Position : "+position);
            try{
               if(position<=updateCustomFieldList.size()){
                   CustomField customField=updateCustomFieldList.get(position);
                   initiateDialogBox(customField);
               }else{
                   showToast("position value must be less than or equal to custom list");
               }
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void initiateDialogBox(CustomField customField) {
        String typeOfField=customField.getTypeOfField();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.dialog_custom_field,null);
        Button button=view.findViewById(R.id.button_save_update);
        builder.setView(view);
        customDialogBox=builder.create();
        List<String>choicesList=customField.getChoices();
        switch (typeOfField){
            case "Text":
               EditText editText=view.findViewById(R.id.edit_text_input);
               editText.setVisibility(View.VISIBLE);
               button.setOnClickListener(view1 -> {
                   for(CustomField customField1:updateCustomFieldList){
                       if(customField1.getId().equals(customField.getId())){
                           customField.setTextValue(editText.getText().toString());
                       }
                   }
                   customFieldsAdapter.setUpdatedCustomFields(updateCustomFieldList);
                   dismissCustomDialogBox();
               });
                break;
            case "List Of Values":
                Spinner spinner=view.findViewById(R.id.spinner_list_of_values);
                spinner.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapterSpinner=new  ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                        choicesList);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spinner.setAdapter(adapterSpinner);
                button.setOnClickListener(view1 -> {
                    for(CustomField customField1:updateCustomFieldList){
                        if(customField1.getId().equals(customField.getId())){
                            customField.setTextValue(spinner.getSelectedItem().toString());
                        }
                    }
                    customFieldsAdapter.setUpdatedCustomFields(updateCustomFieldList);
                    dismissCustomDialogBox();
                });
                break;
            case "Date":
                TextView textView=view.findViewById(R.id.text_view_date_update);
                textView.setVisibility(View.VISIBLE);
                final String[] valueDate = new String[1];
                textView.setOnClickListener(view1 -> {
                    Calendar mcurrentDate=Calendar.getInstance();
                    final int mYear = mcurrentDate.get(Calendar.YEAR);
                    final int mMonth=mcurrentDate.get(Calendar.MONTH);
                    final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog mDatePicker=new DatePickerDialog(this,(datepicker,selectedyear,selectedmonth,selectedday)->{
                        selectedMonth=selectedmonth+1;
                        selectedDay=selectedday;
                        selectedYear=selectedyear;
                        String finalTimeDay=String.format("%02d",selectedDay);
                        String finalTimeMonth=String.format("%02d",selectedMonth);
                        valueDate[0] =selectedYear+"-"+finalTimeMonth+"-"+finalTimeDay;
                        textView.setText(valueDate[0]);

                    },mYear,mMonth,mDay);
                    mDatePicker.show();
                });
                button.setOnClickListener(view1 -> {
                    for(CustomField customField1:updateCustomFieldList){
                        if(customField1.getId().equals(customField.getId())){
                            customField.setTextValue(valueDate[0]);
                        }
                    }
                    customFieldsAdapter.setUpdatedCustomFields(updateCustomFieldList);
                    dismissCustomDialogBox();
                });
                break;
            case "Numeric":
                EditText editText1=view.findViewById(R.id.edit_text_input_number);
                editText1.setVisibility(View.VISIBLE);
                button.setOnClickListener(view1 -> {
                    for(CustomField customField1:updateCustomFieldList){
                        if(customField1.getId().equals(customField.getId())){
                            customField.setTextValue(editText1.getText().toString());
                        }
                    }
                    customFieldsAdapter.setUpdatedCustomFields(updateCustomFieldList);
                    dismissCustomDialogBox();
                });
                break;
            case "CheckBox":
                CheckBox checkBox=view.findViewById(R.id.check_box_custom_update);
                checkBox.setVisibility(View.VISIBLE);
                button.setOnClickListener(view1 -> {

                    for(CustomField customField1:updateCustomFieldList){
                        if(customField1.getId().equals(customField.getId())){
                            customField.setTextValue(String.valueOf(checkBox.isChecked()));
                        }
                    }
                    customFieldsAdapter.setUpdatedCustomFields(updateCustomFieldList);
                    dismissCustomDialogBox();
                });
                break;
            case "AutoCompleteBox":
                AutoCompleteTextView autoCompleteTextView=view.findViewById(R.id.auto_complete_text_custom);
                autoCompleteTextView.setVisibility(View.VISIBLE);
                button.setOnClickListener(view1 -> {
                    for(CustomField customField1:updateCustomFieldList){
                        if(customField1.getId().equals(customField.getId())){
                            customField.setTextValue(autoCompleteTextView.getText().toString());
                        }
                    }
                    customFieldsAdapter.setUpdatedCustomFields(updateCustomFieldList);
                    dismissCustomDialogBox();
                });
                break;

        }
        customDialogBox.show();
    }
    private void dismissCustomDialogBox(){
        if(customDialogBox != null && customDialogBox.isShowing()){
            customDialogBox.dismiss();
        }
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
    private void dismissMultipleSelectDialogBox(){
        if(multipleSelectDialog!=null && multipleSelectDialog.isShowing()){
            multipleSelectDialog.dismiss();
        }
    }
    private void multipleSelectDialogBox(String title,String[] values,boolean[] checkedItems){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMultiChoiceItems(values, checkedItems, (dialog, which, isChecked) -> {

            if(isChecked){
                if(!selectedTagListName.contains(values[which]))
                    selectedTagListName.add(values[which]);
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            tags_selected_customer.setVisibility(View.VISIBLE);
            tags_selected_customer.setLayoutManager(new LinearLayoutManager(this));
            tags_selected_customer.setItemAnimator(new DefaultItemAnimator());
            tagsAdapter=new SkilledTradersAdapter(this,selectedTagListName);
            tags_selected_customer.setAdapter(tagsAdapter);
            tagsAdapter.notifyDataSetChanged();
            dialog.cancel();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        multipleSelectDialog = builder.create();
        multipleSelectDialog.show();


    }

}