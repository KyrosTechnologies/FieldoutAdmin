package com.kyros.technologies.fieldout.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.SkilledTradersAdapter;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityUpdateUserBinding;
import com.kyros.technologies.fieldout.models.CustomField;
import com.kyros.technologies.fieldout.models.CustomFieldResponse;
import com.kyros.technologies.fieldout.models.GetSingleUserResponse;
import com.kyros.technologies.fieldout.models.Result;
import com.kyros.technologies.fieldout.models.SkilledTradesModel;
import com.kyros.technologies.fieldout.models.TeamAdd;
import com.kyros.technologies.fieldout.models.TeamsItem;
import com.kyros.technologies.fieldout.models.TeamsResponse;
import com.kyros.technologies.fieldout.models.UserUpdateResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.AddUserActivityViewModel;
import com.kyros.technologies.fieldout.viewmodel.CustomFieldsFragmentViewModel;
import com.kyros.technologies.fieldout.viewmodel.UpdateUserActivityViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by kyros on 16-12-2017.
 */

public class UpdateUserActivity extends AppCompatActivity {
    private PreferenceManager store;
    private String TAG= UpdateUserActivity.class.getSimpleName();
    private ProgressDialog progressDialog=null;
    private CompositeSubscription subscription;
    private ActivityUpdateUserBinding binding;
    private List<String> timeZoneList=new ArrayList<>();
    private String userId=null;
    private List<TeamsItem> teamsList=new ArrayList<>();
    private AlertDialog generalDialog=null;
    private String[] subContractListString={"Yes","No"};
    private String[] profiles = {"Administrator", "Manager", "Technician"};
    private String[] languages = {"English", "Deutsch", "Portuges", "Polski"};
    private AlertDialog multipleSelectDialog=null;
    private AlertDialog skilledTradersDialog=null;
    private SkilledTradersAdapter adapter;
    private List<String>newNameList=new ArrayList<>();
    private android.support.v7.app.AlertDialog alertDialogDelete;
    @Inject
    CustomFieldsFragmentViewModel customFieldsFragmentViewModel;
    private List<CustomField>usersCustomFieldList=new ArrayList<>();

    @Inject
    UpdateUserActivityViewModel viewModel;
    @Inject
    AddUserActivityViewModel addUserActivityViewModel;
    private List<TypeWhich>typeWhichList=new ArrayList<>();
    private  List<String>choicesList=new ArrayList<>();
    //custom filed values
    private EditText inputTextView=null;
    private Spinner spinnerCustomField=null;
    private TextView dateTextView=null;
    private EditText numericEditText=null;
    private CheckBox checkBoxCustomField=null;
    private AutoCompleteTextView autoCompleteTextView =null;
    private int spinnerCustomFieldSeletectedChoice=0;
    private int selectedYear=0;
    private int selectedMonth=0;
    private int selectedDay=0;
    private List<CustomField> updateCustomFieldList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_update_user);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectUpdateUsersActivity(this);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getApplicationContext());
        getBundlePackage();

        String[] ids= TimeZone.getAvailableIDs();
        for(String id:ids){
            timeZoneList.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        String[] timeStringArray = new String[timeZoneList.size()];
        timeStringArray = timeZoneList.toArray(timeStringArray);

        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        getTeamsAPI(authKey,domainId);
        getUserAPI(authKey,userId);
        callCustomFieldsAPI(domainId,authKey);

        binding.textViewLanguageAddUser.setOnClickListener(v->generalListDialogBox("Choose an Language",languages,binding.textViewLanguageAddUser,0));
        binding.textViewProfileAddUser.setOnClickListener(v->generalListDialogBox("Choose Profile",profiles,binding.textViewProfileAddUser,0));
        binding.textViewSubcontractorAddUser.setOnClickListener(v->generalListDialogBox("Select subcontract",subContractListString,binding.textViewSubcontractorAddUser,0));
        String[] finalTimeStringArray = timeStringArray;
        binding.textViewTimeZoneAddUser.setOnClickListener(v->generalListDialogBox("Select TimeZone", finalTimeStringArray,binding.textViewTimeZoneAddUser,0));
        binding.buttonSaveAddUsers.setOnClickListener(view -> validateFields());
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
                        case "users":
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
            binding.tableLayoutCustomFields.removeAllViews();
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
                binding.tableLayoutCustomFields.addView(tableRow);

            }
        }

    }


    private void validateFields() {
        showDialog();
        String firstName=binding.firstNameAddUserEtext.getText().toString();
        String lastName=binding.lastNameAddUserEtext.getText().toString();
        String mobileNumber=binding.mobileAddUserEtext.getText().toString();
        String email=binding.emailAddUserEtext.getText().toString();
        String profile=binding.textViewProfileAddUser.getText().toString();
        String language=binding.textViewLanguageAddUser.getText().toString();
        String userName=binding.userNameAddUserEtext.getText().toString();
        String password=binding.passwordAddUserEtext.getText().toString();
        String subContractor=binding.textViewSubcontractorAddUser.getText().toString();
        String timeZone=binding.textViewTimeZoneAddUser.getText().toString();
        String startLocation=binding.startLocationAddUserEtext.getText().toString();

        if(lastName!=null && !lastName.isEmpty() && firstName!=null && !firstName.isEmpty() && mobileNumber!=null && !mobileNumber.isEmpty() &&
                email!=null && !email.isEmpty() && userName!=null && !userName.isEmpty() && password!=null && !password.isEmpty()){
            SkilledTradesModel skilledTradesModel=new SkilledTradesModel();
            List<String>skilledTraders=new ArrayList<>();
            if(adapter!=null){
                skilledTraders=adapter.getSkilledTradersList();
            }
            skilledTradesModel.setName(skilledTraders);
            TeamAdd teamAdd=new TeamAdd();
            teamAdd.setId(newNameList);
            List<CustomField>customFieldList=getCustomFilesList();
            if(customFieldList==null){
                customFieldList=new ArrayList<>();
            }

            Result result=new Result();
            result.setFirstName(firstName);
            result.setLastName(lastName);
            result.setPhone(mobileNumber);
            result.setEmail(email);
            result.setProfile(profile);
            result.setLanguage(language);
            result.setCustomFieldValues(customFieldList);
            result.setUsername(userName);
            result.setSkilledTrades(skilledTradesModel);
            result.setTeams(teamAdd);
            result.setId(userId);
            if(isValidEmailAddress(email)){
                callAPI(result,store.getToken(),userId);
            }else {
                showToast("Please enter valid email address");
            }
        }else{
            showToast("Please enter all fields");
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
                            String formType="users";
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
                            String formType="users";
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
                            String formType="users";
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
                            String formType="users";
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
                            String formType="users";
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

    private void callAPI(Result result, String token, String userId) {
        Log.d("Result : ",""+result.toString());
        if(newNameList.size()!=0){
                subscription.add(viewModel.updateUserObservable(userId,token,result)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {Log.e("Error :",TAG+" / / "+throwable.getMessage());
                    dismissDialog();})
                .subscribe(updateUserResponse));
        }else{
            showToast("Please select teams!");
            dismissDialog();
        }

    }
    private Subscriber<UserUpdateResponse>updateUserResponse=new Subscriber<UserUpdateResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            showToast(""+e.getMessage());
            Log.e("Error : ",TAG+" / / "+e.getMessage());
        }

        @Override
        public void onNext(UserUpdateResponse userUpdateResponse) {
            dismissDialog();
            if(userUpdateResponse!=null)
                if(userUpdateResponse.getIsSuccess())
                showToast("User Updated Successfully!");
            UpdateUserActivity.this.finish();
        }
    };

    private void generalListDialogBox(String title, String [] values, TextView view, int checkedItem){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserActivity.this);
        builder.setTitle(title);
        builder.setSingleChoiceItems(values, checkedItem, (dialogInterface, i) -> view.setText(values[i]));
        builder.setPositiveButton("OK", (dialog, which) -> dismissGeneralDialogBox());
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dismissGeneralDialogBox());
        generalDialog = builder.create();
        generalDialog.show();

    }
    private void dismissGeneralDialogBox(){
        if(generalDialog!=null && generalDialog.isShowing()){
            generalDialog.dismiss();
        }
    }
    private void getTeamsAPI(String authKey, String domainId) {
        addUserActivityViewModel.getTeamResponse(domainId,authKey)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(getTeamResponse);
    }
    private Subscriber<TeamsResponse>getTeamResponse=new Subscriber<TeamsResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            showToast(""+e.getMessage());
            Log.e("Error : ",TAG+" / / "+e.getMessage());
        }

        @Override
        public void onNext(TeamsResponse teamsResponse) {
            bindTeamResponse(teamsResponse);
        }
    };

    private void bindTeamResponse(TeamsResponse teamsResponse) {
        if(teamsResponse!=null){
            teamsList=teamsResponse.getTeams();
        }else{
            showToast("teams is empty or null!");
        }
    }

    private void getUserAPI(String authKey, String userId) {
        if(authKey!=null && userId!=null){
            showDialog();
            getAPI(authKey,userId);
        }else{
            showToast("UserId or authKey is null!");
        }
    }

    private void getAPI(String authKey, String userId) {
        subscription.add(viewModel.getSingleUserResponseObservable(userId,authKey)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                    .subscribe(checkGetOneUserResponse));
    }
private Subscriber<GetSingleUserResponse>checkGetOneUserResponse=new Subscriber<GetSingleUserResponse>() {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        dismissDialog();
        showToast(""+throwable.getMessage());
        Timber.tag("Error : ").e(TAG + " / / " + throwable.getMessage());
    }

    @Override
    public void onNext(GetSingleUserResponse getSingleUserResponse) {
        dismissDialog();
        Timber.d(TAG + " / / " + getSingleUserResponse.toString());
        if(getSingleUserResponse!=null) bindViews(getSingleUserResponse);
    }
};

    private void bindViews(GetSingleUserResponse getSingleUserResponse) {
        updateCustomFieldList=getSingleUserResponse.getUser().getCustomFieldValues();
        String firstName=getSingleUserResponse.getUser().getFirstName();
        if(firstName!=null){
            binding.firstNameAddUserEtext.setText(firstName);
        }
        String lastName=getSingleUserResponse.getUser().getLastName();
        if(lastName!=null){
            binding.lastNameAddUserEtext.setText(lastName);
        }
        String email=getSingleUserResponse.getUser().getEmail();
        if(email!=null){
            binding.emailAddUserEtext.setText(email);
        }
        String phone=getSingleUserResponse.getUser().getPhone();
        if(phone!=null){
            binding.mobileAddUserEtext.setText(phone);
        }
        String profile=getSingleUserResponse.getUser().getProfile();
        if(profile!=null){
            store.putProfile(profile);
            binding.textViewProfileAddUser.setText(profile);
        }
        String language=getSingleUserResponse.getUser().getLanguage();
        if(language!=null){
            store.putLanguage(language);
            binding.textViewLanguageAddUser.setText(language);
        }
        String userName=getSingleUserResponse.getUser().getUsername();
        if(userName!=null){
            binding.userNameAddUserEtext.setText(userName);
        }
        String password=getSingleUserResponse.getUser().getPassword();
        if(password!=null){
            binding.passwordAddUserEtext.setText(password);
        }
        SkilledTradesModel skilledTrades=getSingleUserResponse.getUser().getSkilledTrades();
        List<String>stringList=  skilledTrades.getName();
        if(stringList!=null && stringList.size()!=0){
            binding.recyclerSkilledList.setVisibility(View.VISIBLE);
            binding.recyclerSkilledList.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerSkilledList.setItemAnimator(new DefaultItemAnimator());
            SkilledTradersAdapter  adapterSkilled=new SkilledTradersAdapter(this,stringList);
            binding.recyclerSkilledList.setAdapter(adapterSkilled);
            adapterSkilled.notifyDataSetChanged();
        }


        TeamAdd teams=getSingleUserResponse.getUser().getTeams();
        List<String>teamId=teams.getId();
        List<String>teamNameList=new ArrayList<>();
        if(teamsList!=null && teamsList.size()!=0){
            for(int i=0;i<teamsList.size();i++){
                for(int j=0;j<teamId.size();j++){
                    if(teamsList.get(i).getId().equals(teamId.get(j))){
                        teamNameList.add(teamsList.get(i).getName());
                    }
                }
            }
        }
        if(teamNameList.size()!=0){
            binding.recyclerTeamList.setVisibility(View.VISIBLE);
            binding.recyclerTeamList.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerTeamList.setItemAnimator(new DefaultItemAnimator());
            SkilledTradersAdapter  adapterSkilled=new SkilledTradersAdapter(this,teamNameList);
            binding.recyclerTeamList.setAdapter(adapterSkilled);
            adapterSkilled.notifyDataSetChanged();
        }


        binding.teamsSpinnerAddUser.setOnClickListener(view -> checkTeam(binding.teamsSpinnerAddUser));
        binding.skilledTradesAddUserEtext.setOnClickListener(view -> skilledTradesDialog(stringList,binding.skilledTradesAddUserEtext));
        List<CustomField>customFieldList=getSingleUserResponse.getUser().getCustomFieldValues();
        if(customFieldList!=null && customFieldList.size()!=0){
            bindCustomFieldsView(customFieldList);
        }


    }

    private void bindCustomFieldsView(List<CustomField> customFieldList) {
        binding.tableLayoutCustomFields.removeAllViews();
        typeWhichList.clear();
        int position=0;
        for(CustomField customField:customFieldList){
            TypeWhich typeWhich=new TypeWhich();
            position++;
            choicesList=customField.getChoices();
            if(choicesList==null){
                choicesList=new ArrayList<>();
            }


            String typeOfField=customField.getTypeOfField();
            String textValueResponse=customField.getTextValue();

            //Tables Rows
            TableRow tableRow=new TableRow(this);
            tableRow.setBackground(getResources().getDrawable(R.color.bg));
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            switch (typeOfField){
                case "Text":
                    typeWhich.setId(position);
                    typeWhich.setType("Text");
                    inputTextView =new EditText(this);
                    if(textValueResponse!=null){
                        inputTextView.setText(textValueResponse);
                    }else{
                        inputTextView.setHint("enter here...");
                    }
                    inputTextView.setTextSize(20);
                    inputTextView.setId(position);
                    TableRow.LayoutParams tableRowInputTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowInputTextParams.setMargins(10,10,0,10);
                    inputTextView.setLayoutParams(tableRowInputTextParams);
                    inputTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    inputTextView.setTextColor(getResources().getColor(R.color.light_black));
                    inputTextView.setPadding(5, 5, 5, 15);
                    tableRow.addView(inputTextView);
                    break;
                case "List Of Values":
                    typeWhich.setId(position);
                    typeWhich.setType("List Of Values");
                    spinnerCustomField=new Spinner(this);
                    TableRow.LayoutParams tableRowSpinnerParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowSpinnerParams.setMargins(10,10,0,10);
                    spinnerCustomField.setLayoutParams(tableRowSpinnerParams);
                    spinnerCustomField.setPrompt("List Of Values");
                    spinnerCustomField.setGravity(Gravity.LEFT|Gravity.CENTER);
                    spinnerCustomField.setId(position);
                    spinnerCustomField.setPadding(5, 5, 5, 5);
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
                    dateTextView=new TextView(this);
                    if(textValueResponse!=null){
                        dateTextView.setText(textValueResponse);
                    }else{
                        dateTextView.setText("Select date here..");
                    }
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
                    if(textValueResponse!=null){
                        numericEditText.setText(textValueResponse);
                    }else{
                        numericEditText.setHint("enter here..");
                    }
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
                    if(textValueResponse!=null){
                        switch (textValueResponse){
                            case "True":
                                checkBoxCustomField.setChecked(true);
                                break;
                            case "False":
                                checkBoxCustomField.setChecked(false);
                                break;
                        }
                    }else{
                        checkBoxCustomField.setChecked(true);
                    }
                    checkBoxCustomField.setText("Select one");
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
                    if(textValueResponse!=null){
                        autoCompleteTextView.setText(textValueResponse);

                    }else{
                        autoCompleteTextView.setHint("enter here..");

                    }
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

    private void skilledTradesDialog(List<String> skilledName,TextView tView) {
        AlertDialog.Builder builder=new AlertDialog.Builder(UpdateUserActivity.this);
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
                    adapter.setSkilledTradersName(edName);
                editText.setText("");
            }
        });
        builder.setPositiveButton("OK",(dialog,id)->{
        List<String>stringListValue=adapter.getSkilledTradersList();
            if(stringListValue!=null && stringListValue.size()!=0){
                binding.recyclerSkilledList.setVisibility(View.VISIBLE);
                binding.recyclerSkilledList.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerSkilledList.setItemAnimator(new DefaultItemAnimator());
                SkilledTradersAdapter  adapterSkilled=new SkilledTradersAdapter(this,stringListValue);
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


    private void checkTeam(TextView view) {
        if(teamsList!=null && teamsList.size()!=0){
            String[]teamNameArray=new String[teamsList.size()];
            boolean[]checkedItems=new boolean[teamsList.size()];
            List<String>teamStringList=new ArrayList<>();
            for (TeamsItem teamsItem:teamsList){
                teamStringList.add(teamsItem.getName());
            }
            teamNameArray=teamStringList.toArray(teamNameArray);
           multipleSelectDialogBox("Select Teams",teamNameArray,checkedItems,view);

        }else{
            showToast("Teams is not loaded!");
        }
    }
    private void multipleSelectDialogBox(String title,String[] values,boolean[] checkedItems,TextView view){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserActivity.this);
        builder.setTitle(title);
        builder.setMultiChoiceItems(values, checkedItems, (dialog, which, isChecked) -> {
           if(isChecked){
              newNameList.add(values[which]);
           }
        });

        builder.setPositiveButton("OK", (dialog, which) ->{ dismissMultipleSelectDialogBox();

            if(newNameList!=null && newNameList.size()!=0){
                StringBuilder builder1=new StringBuilder();
                int position=0;
                for(String name:newNameList){
                    position++;
                    if(name!=null){
                       if(newNameList.size()==position){
                           builder1.append(name);
                       }else{
                           builder1.append(name+" / ");
                       }


                    }

                }
                String finalValue=builder1.toString();
                if(finalValue!=null){
                    view.setText(finalValue);
                }
            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dismissMultipleSelectDialogBox());

         multipleSelectDialog = builder.create();
        multipleSelectDialog.show();


    }
    private void dismissMultipleSelectDialogBox(){
        if(multipleSelectDialog!=null && multipleSelectDialog.isShowing()){
            multipleSelectDialog.dismiss();
        }
    }





    private void getBundlePackage() {
        try{
            Bundle bundle=getIntent().getExtras();
            userId=bundle.getString("userId");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissDialog();
        dismissGeneralDialogBox();
        dismissMultipleSelectDialogBox();
        dismissSkilledTradesDialog();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_delete, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                UpdateUserActivity.this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(UpdateUserActivity.this);
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
