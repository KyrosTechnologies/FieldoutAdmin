package com.example.rohin.fieldoutadmin.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.adapters.SkilledTradersAdapter;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.ActivityAddUserBinding;
import com.example.rohin.fieldoutadmin.models.AddUserResponse;
import com.example.rohin.fieldoutadmin.models.CustomField;
import com.example.rohin.fieldoutadmin.models.CustomFieldResponse;
import com.example.rohin.fieldoutadmin.models.SkilledTradesModel;
import com.example.rohin.fieldoutadmin.models.TeamAdd;
import com.example.rohin.fieldoutadmin.models.TeamsItem;
import com.example.rohin.fieldoutadmin.models.TeamsResponse;
import com.example.rohin.fieldoutadmin.models.UserInfo;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.AddUserActivityViewModel;
import com.example.rohin.fieldoutadmin.viewmodel.CustomFieldsFragmentViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddUserActivity extends AppCompatActivity {
    private PreferenceManager store;
    private String TAG= AddUserActivity.class.getSimpleName();
    private ProgressDialog progressDialog=null;
    private CompositeSubscription subscription;
    private ActivityAddUserBinding binding;
    private List<String>timeZoneList=new ArrayList<>();
    private List<String>profileList=new ArrayList<>();
    private List<String>languageList=new ArrayList<>();
    private List<String>subContractorList=new ArrayList<>();
    @Inject
    AddUserActivityViewModel viewModel;
    private String selectedLanguage=null;
    private String selectedProfile=null;
    private String selectedTimeZone=null;
    private String selectedSubContractor=null;
    private String selectedTeamName=null;
    private List<TeamsItem>teamsTotalList=new ArrayList<>();
    private AlertDialog skilledTradersDialog;
    private SkilledTradersAdapter adapter;
    @Inject
    CustomFieldsFragmentViewModel customFieldsFragmentViewModel;
    private List<CustomField>usersCustomFieldList=new ArrayList<>();

    //customFieldValues
    private EditText inputTextView=null;
    private Spinner spinnerCustomField=null;
    private TextView dateTextView=null;
    private EditText numericEditText=null;
    private CheckBox checkBoxCustomField=null;
    private AutoCompleteTextView autoCompleteTextView =null;
    private int spinnerCustomFieldSeletectedChoice=0;
    private DatePickerDialog datePickerDialog;
    private int selectedYear=0;
    private int selectedMonth=0;
    private int selectedDay=0;
    private List<TypeWhich>typeWhichList=new ArrayList<>();
    private  List<String>choicesList=new ArrayList<>();
    private Set<String>setTeamsNameList=new HashSet<>();
    private AlertDialog multipleSelectDialog=null;
    private Set<String>selectedTeamNameList=new HashSet<>();
    private SkilledTradersAdapter teamsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_user);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectAddUsersActivity(this);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getApplicationContext());
        profileList.add("admin");
        profileList.add("manager");
        profileList.add("technician");
        languageList.add("English");
        languageList.add("Deutsch");
        languageList.add("Portuges");
        languageList.add("Polski");
        subContractorList.add("Yes");
        subContractorList.add("No");
        String[] ids= TimeZone.getAvailableIDs();
        for(String id:ids){
            timeZoneList.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        ArrayAdapter<String> adapterTimeZone=new  ArrayAdapter<>(AddUserActivity.this,android.R.layout.simple_spinner_item,
                timeZoneList);
        ArrayAdapter<String> adapterLanguage=new  ArrayAdapter<>(AddUserActivity.this,android.R.layout.simple_spinner_item,
                languageList);
        ArrayAdapter<String> adapterProfile=new  ArrayAdapter<>(AddUserActivity.this,android.R.layout.simple_spinner_item,
                profileList);
        ArrayAdapter<String> adapterSubContractor=new  ArrayAdapter<>(AddUserActivity.this,android.R.layout.simple_spinner_item,
                subContractorList);
        adapterTimeZone.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterLanguage.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterProfile.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapterSubContractor.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.spinnerTimeZoneAddUser.setPrompt("Time Zone");
        binding.spinnerLanguageAddUser.setPrompt("Language");
        binding.spinnerProfileAddUser.setPrompt("Profile");
        binding.spinnerSubcontractorAddUser.setPrompt("Subcontractor");
        binding.spinnerTimeZoneAddUser.setAdapter(adapterTimeZone);
        binding.spinnerLanguageAddUser.setAdapter(adapterLanguage);
        binding.spinnerProfileAddUser.setAdapter(adapterProfile);
        binding.spinnerSubcontractorAddUser.setAdapter(adapterSubContractor);

        binding.spinnerTimeZoneAddUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    String text = binding.spinnerTimeZoneAddUser.getSelectedItem().toString();
                    selectedTimeZone=text;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerLanguageAddUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    String text = binding.spinnerLanguageAddUser.getSelectedItem().toString();
                    selectedLanguage=text;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerProfileAddUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    String text = binding.spinnerProfileAddUser.getSelectedItem().toString();
                    selectedProfile=text;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerSubcontractorAddUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    String text = binding.spinnerSubcontractorAddUser.getSelectedItem().toString();
                    selectedSubContractor=text;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        callTeamsAPI(domainId,authKey);
        callCustomFieldsAPI(domainId,authKey);


        binding.buttonSaveAddUsers.setOnClickListener(view -> saveUserAPI(domainId,authKey));
        List<String>value=new ArrayList<>();
        binding.skilledTradesAddUserEtext.setOnClickListener(view->skilledTradesDialog(value,binding.skilledTradesAddUserEtext));


    }

    private void callCustomFieldsAPI(String domainId, String authKey) {
        if(domainId!=null && authKey!=null){
            subscription.add(customFieldsFragmentViewModel.getcustomFieldResponseObservable(authKey,domainId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
            .subscribe(this::customFieldResponse,this::customFieldErrorResponse,this::customFieldCompletedResponse));
        }
    }

    private void customFieldCompletedResponse() {

    }

    private void customFieldErrorResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
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
                        case "users":
                            usersCustomFieldList.add(customField);
                            break;
                    }

                }
            }
            validateCustomField();
        }else {
            showToast("customfield response is null!");
        }
    }

    private void validateCustomField() {
        if(usersCustomFieldList!=null && usersCustomFieldList.size()!=0){
            binding.tableLayoutCustomFields.removeAllViews();
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
               tableRow.setBackground(getResources().getDrawable(R.color.bg));
               tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                switch (typeOfField){
                    case "Text":
                        typeWhich.setId(position);
                        typeWhich.setType("Text");
                        inputTextView =new EditText(this);
                        String valueInput="Enter user input "+name;
                        inputTextView.setHint(valueInput);
                        inputTextView.setTextSize(20);
                        inputTextView.setId(position);
                        TableRow.LayoutParams tableRowInputTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                        tableRowInputTextParams.setMargins(10,10,0,10);
                        inputTextView.setLayoutParams(tableRowInputTextParams);
                        inputTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                        inputTextView.setTextColor(getResources().getColor(R.color.light_black));
                        inputTextView.setPadding(5, 5, 5, 5);
                        tableRow.addView(inputTextView);
                        break;
                    case "List Of Values":
                        typeWhich.setId(position);
                        typeWhich.setType("List Of Values");
                         spinnerCustomField=new Spinner(this);
                        TableRow.LayoutParams tableRowSpinnerParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                        tableRowSpinnerParams.setMargins(10,10,0,10);
                        spinnerCustomField.setLayoutParams(tableRowSpinnerParams);
                        spinnerCustomField.setPrompt(""+name);
                        spinnerCustomField.setGravity(Gravity.LEFT|Gravity.CENTER);
                        spinnerCustomField.setId(position);
                        spinnerCustomField.setPadding(5, 5, 5, 5);
                        spinnerCustomField.setTag(customField);
                        ArrayAdapter<String> adapterSpinner=new  ArrayAdapter<>(AddUserActivity.this,android.R.layout.simple_spinner_item,
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
                         dateTextView=new TextView(this);
                        dateTextView.setText(valueDate);
                        dateTextView.setTextSize(20);
                        dateTextView.setId(position);
                        TableRow.LayoutParams tableRowuserNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                        tableRowuserNameParams.setMargins(10,10,0,10);
                        dateTextView.setLayoutParams(tableRowuserNameParams);
                        dateTextView.setTextColor(getResources().getColor(R.color.black));
                        dateTextView.setBackground(getResources().getDrawable(R.drawable.default_text_view_background));
                        dateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                        dateTextView.setPadding(10, 5, 5, 5);
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
                         numericEditText=new EditText(this);
                         String valueNumeric="Input "+name;
                            numericEditText.setHint(valueNumeric);
                        numericEditText.setTextSize(20);
                        numericEditText.setId(position);
                        numericEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        TableRow.LayoutParams tableRowNumericParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                        tableRowNumericParams.setMargins(10,10,0,10);
                        numericEditText.setLayoutParams(tableRowNumericParams);
                        numericEditText.setGravity(Gravity.LEFT|Gravity.CENTER);
                        numericEditText.setTextColor(getResources().getColor(R.color.light_black));
                        numericEditText.setPadding(5, 5, 5, 15);
                        tableRow.addView(numericEditText);
                        break;
                    case "CheckBox":
                        typeWhich.setId(position);
                        typeWhich.setType("CheckBox");
                        checkBoxCustomField=new CheckBox(this);
                        checkBoxCustomField.setChecked(true);
                        checkBoxCustomField.setId(position);
                        String valueCheckBox="Select "+name;
                        checkBoxCustomField.setText(valueCheckBox);
                        TableRow.LayoutParams tableRowCheckBoxParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                        tableRowCheckBoxParams.setMargins(10,10,0,10);
                        checkBoxCustomField.setLayoutParams(tableRowCheckBoxParams);
                        checkBoxCustomField.setGravity(Gravity.LEFT|Gravity.CENTER);
                        checkBoxCustomField.setTextColor(getResources().getColor(R.color.light_black));
                        checkBoxCustomField.setPadding(5, 5, 5, 5);
                        tableRow.addView(checkBoxCustomField);
                        break;
                    case "AutoCompleteBox":
                        typeWhich.setId(position);
                        typeWhich.setType("AutoCompleteBox");
                         autoCompleteTextView=new AutoCompleteTextView(this);
                         String valueACTV=""+name;
                            autoCompleteTextView.setHint(valueACTV);
                        autoCompleteTextView.setTextSize(20);
                        autoCompleteTextView.setId(position);
                        TableRow.LayoutParams tableRowACTVParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                        tableRowACTVParams.setMargins(10,10,0,10);
                        autoCompleteTextView.setLayoutParams(tableRowACTVParams);
                        autoCompleteTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                        autoCompleteTextView.setTextColor(getResources().getColor(R.color.black));
                        autoCompleteTextView.setPadding(5, 5, 5, 15);
                        ArrayAdapter<String> adapterACTV = new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line, choicesList);
                        autoCompleteTextView.setAdapter(adapterACTV);
                        tableRow.addView(autoCompleteTextView);
                        break;
                }
                typeWhichList.add(typeWhich);
               binding.tableLayoutCustomFields.addView(tableRow);

           }
        }
    }



    private void dismissDatePickerDialog(){
        if(datePickerDialog!=null && datePickerDialog.isShowing()){
            datePickerDialog.dismiss();
        }
    }
    private void skilledTradesDialog(List<String> skilledName,TextView tView) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddUserActivity.this);
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.adapter_teams_list,null);
        EditText editText=view.findViewById(R.id.et_skilled);
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
                editText.setText("");
                adapter.setSkilledTradersName(edName);
            }
        });
        builder.setPositiveButton("OK",(dialog,id)->{
          List<String>stringList=  adapter.getSkilledTradersList();
          if(stringList!=null && stringList.size()!=0){
              binding.recyclerSkilledList.setVisibility(View.VISIBLE);
              binding.recyclerSkilledList.setLayoutManager(new LinearLayoutManager(this));
              binding.recyclerSkilledList.setItemAnimator(new DefaultItemAnimator());
              SkilledTradersAdapter  adapterSkilled=new SkilledTradersAdapter(this,stringList);
              binding.recyclerSkilledList.setAdapter(adapterSkilled);
              adapterSkilled.notifyDataSetChanged();
          }

        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissSkilledTradesDialog();
        });
        skilledTradersDialog=builder.create();
        skilledTradersDialog.show();

    }
    private  void dismissSkilledTradesDialog(){
        if(skilledTradersDialog!=null && skilledTradersDialog.isShowing()){
            skilledTradersDialog.dismiss();
        }
    }


    private void saveUserAPI(String domainId, String authKey) {
        String firstNameEditText=binding.firstNameAddUserEtext.getText().toString();
        String lastNameEditText=binding.lastNameAddUserEtext.getText().toString();
        String mobileNumberEditText=binding.mobileAddUserEtext.getText().toString();
        String emailEditText=binding.emailAddUserEtext.getText().toString();
        String userNameEditText=binding.userNameAddUserEtext.getText().toString();
        String passwordEditText=binding.passwordAddUserEtext.getText().toString();
        String startLocationEditText=binding.startLocationAddUserEtext.getText().toString();
        String skilledTradesEditText=binding.skilledTradesAddUserEtext.getText().toString();
        if(userNameEditText!=null && !userNameEditText.isEmpty() && passwordEditText !=null && !passwordEditText.isEmpty() && selectedProfile != null){
            List<CustomField>customFieldList=getCustomFilesList();
            if(customFieldList==null){
                customFieldList=new ArrayList<>();
            }
            if(isValidEmailAddress(emailEditText)){
                initiateApiCall(domainId,authKey,firstNameEditText,lastNameEditText,mobileNumberEditText,emailEditText,userNameEditText,passwordEditText,skilledTradesEditText,customFieldList);
            }else{
                showToast("Please enter valid email address!");
            }
        }else{
            showToast("Please enter username,password,and profile !");
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
                            String formType="users";
                            String typeOfField="Text";
                            CustomField customField=new CustomField();
                            customField.setTextValue(textValue);
                            customField.setFormType(formType);
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
                            String formType="users";
                            String typeOfField="List Of Values";
                            CustomField customField=new CustomField();
                            customField.setTextValue(value);
                            customField.setFormType(formType);
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
                            String formType="users";
                            String typeOfField="Date";
                            CustomField customField=new CustomField();
                            customField.setTextValue(textValue);
                            customField.setFormType(formType);
                            customField.setTypeOfField(typeOfField);
                            customFieldList.add(customField);
                        }
                       break;
                    case "Numeric":
                        if(numericEditText!=null){
                            numericEditText.findViewById(typeWhich.getId());
                            String textValue=numericEditText.getText().toString();
                            String formType="users";
                            String typeOfField="Numeric";
                            CustomField customField=new CustomField();
                            customField.setTextValue(textValue);
                            customField.setFormType(formType);
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
                            String formType="users";
                            String typeOfField="CheckBox";
                            CustomField customField=new CustomField();
                            customField.setTextValue(textValue);
                            customField.setFormType(formType);
                            customField.setTypeOfField(typeOfField);
                            customFieldList.add(customField);
                        }
                        break;
                    case "AutoCompleteBox":
                        if(autoCompleteTextView!=null){
                            autoCompleteTextView.findViewById(typeWhich.getId());
                            String textValue=autoCompleteTextView.getText().toString();
                            String formType="users";
                            String typeOfField="AutoCompleteBox";
                            CustomField customField=new CustomField();
                            customField.setTextValue(textValue);
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

    private void initiateApiCall(String domainId, String authKey, String firstNameEditText, String lastNameEditText, String mobileNumberEditText, String emailEditText, String userNameEditText, String passwordEditText, String skilledTradesEditText, List<CustomField>customFieldList) {
        UserInfo userInfo=new UserInfo();
        userInfo.setUsername(userNameEditText);
        userInfo.setPassword(passwordEditText);
        userInfo.setIdDomain(domainId);
        userInfo.setFirstName(firstNameEditText);
        userInfo.setLastName(lastNameEditText);
        userInfo.setLanguage(selectedLanguage);
        userInfo.setEmail(emailEditText);
        userInfo.setPhone(mobileNumberEditText);
        List<String>teamId=new ArrayList<>();
        List<String>selectedTeamNameListRecycler=new ArrayList<>();
        if(teamsAdapter!=null){
            selectedTeamNameListRecycler=teamsAdapter.getSkilledTradersList();
        }
        for(int i=0;i<teamsTotalList.size();i++){
            for(int j=0;j<selectedTeamNameList.size();j++){
                if(teamsTotalList.get(i).getName().equals(selectedTeamNameListRecycler.get(j))){
                    teamId.add(teamsTotalList.get(i).getId());
                }
            }

        }
        List<String>skilledList=adapter.getSkilledTradersList();
                SkilledTradesModel skilledTradesMode=new SkilledTradesModel();
        skilledTradesMode.setName(skilledList);
        TeamAdd teamAdd=new TeamAdd();
        teamAdd.setId(teamId);
        userInfo.setTeams(teamAdd);
        userInfo.setSkilledTrades(skilledTradesMode);
        userInfo.setCustomFieldValues(customFieldList);
        userInfo.setProfile(selectedProfile);
        if(adapter.getSkilledTradersList().size()!=0){
            callPOSTAPI(authKey,userInfo);
        }else{
            showToast("Please add skilled Traders!");
        }
    }

    private void callPOSTAPI(String authKey, UserInfo userInfo) {
        showDialog();
        Log.d("Add User Input : ",userInfo.toString());
        subscription.add(viewModel.addUserResponseObservable(userInfo,authKey)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                    .subscribe(checkAddUserResponse));



    }
private Subscriber<AddUserResponse>checkAddUserResponse=new Subscriber<AddUserResponse>() {
    @Override
    public void onCompleted() {
dismissDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        dismissDialog();

    }

    @Override
    public void onNext(AddUserResponse addUserResponse) {
        addUserResult(addUserResponse);
    }
};
    private void addUserResult(AddUserResponse addUserResponse) {
        dismissDialog();
        if(addUserResponse!=null){
            Log.d("Add User Response : ",TAG+" / / "+addUserResponse.toString());
            if(addUserResponse.isIsSuccess()){
                showToast("User Added Successfully!");
                AddUserActivity.this.finish();
            }else{
                showToast(" not successfull!");
            }
        }else {
            showToast("response is null!");
        }

    }

    private void callTeamsAPI(String domainId, String authKey) {
        subscription.add(viewModel.getTeamResponse(domainId,authKey)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                    .subscribe(checkTeamsResponse));
    }
    private Subscriber<TeamsResponse>checkTeamsResponse=new Subscriber<TeamsResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e("Error : ",TAG+" / / "+e.getMessage());
        }

        @Override
        public void onNext(TeamsResponse teamsResponse) {
            bindViews(teamsResponse);
        }
    };

    private void bindViews(TeamsResponse teamsResponse) {
        if(teamsResponse!=null){
                Log.d("Team AddUserResponse : ",TAG+" / / "+teamsResponse.toString());
                List<TeamsItem>teamsItemList=teamsResponse.getTeams();
                teamsTotalList=teamsItemList;
                List<String>teamsNameList=new ArrayList<>();
                for (int i=0;i<teamsItemList.size();i++){
                    teamsNameList.add(teamsItemList.get(i).getName());
                }
                setTeamsNameList.addAll(teamsNameList);
                binding.textViewSelectTeam.setOnClickListener(view -> multipleSelectDialogBox("Select Teams ",teamsNameList.toArray(new String[teamsNameList.size()]),new boolean[teamsNameList.size()]));

        }else {
            showToast("response is null!");
        }
    }
    private void multipleSelectDialogBox(String title,String[] values,boolean[] checkedItems){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMultiChoiceItems(values, checkedItems, (dialog, which, isChecked) -> {

            if(isChecked){
                    selectedTeamNameList.add(values[which]);
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
                binding.recyclerTeamsList.setVisibility(View.VISIBLE);
                binding.recyclerTeamsList.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerTeamsList.setItemAnimator(new DefaultItemAnimator());
                 teamsAdapter=new SkilledTradersAdapter(this,selectedTeamNameList);
                binding.recyclerTeamsList.setAdapter(teamsAdapter);
                teamsAdapter.notifyDataSetChanged();
            dialog.cancel();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        multipleSelectDialog = builder.create();
        multipleSelectDialog.show();


    }
    private void dismissMultipleSelectDialogBox(){
        if(multipleSelectDialog!=null && multipleSelectDialog.isShowing()){
            multipleSelectDialog.dismiss();
        }
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddUserActivity.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(AddUserActivity.this);
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
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissDialog();
        dismissSkilledTradesDialog();
        dismissDatePickerDialog();
        dismissMultipleSelectDialogBox();
    }
    public class TypeWhich{
        private int id;
        private String type;
        public TypeWhich(){

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
