package com.kyros.technologies.fieldout.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.models.CustomField;
import com.kyros.technologies.fieldout.models.TypeWhich;
import com.kyros.technologies.fieldout.views.CustomFieldsViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kyros on 23-01-2018.
 */

public class CustomFieldsAdapter extends RecyclerView.Adapter<CustomFieldsViewModel> {
    private List<CustomField> customFieldList;
    List<CustomField>customFieldListOutput=new ArrayList<>();
    Set<CustomField>set=new HashSet<>();
    private Context mContext;
    private int spinnerCustomFieldSeletectedChoice=0;
    public  int selectedYear=0;
    public  int selectedMonth=0;
    public int selectedDay=0;
    private List<TypeWhich>typeWhichList=new ArrayList<>();
    private CustomFieldsViewModel mHolder;
    int check = 0;
    private String whichOne;
    private HashMap<Integer, String> hMapDate = new HashMap<>();
    private HashMap<Integer, String> hMapNumeric = new HashMap<>();
    private HashMap<Integer, String> hMapBoolean = new HashMap<>();
    private HashMap<Integer, String> hMapText = new HashMap<>();
    private HashMap<Integer, String> hMapActv = new HashMap<>();

    public CustomFieldsAdapter(){

    }

    public void setCustomFieldData(List<CustomField> customFieldList, Context mContext,String whichOne){
        this.customFieldList = customFieldList;
        this.mContext = mContext;
        this.whichOne=whichOne;
        notifyDataSetChanged();
    }
    @Override
    public CustomFieldsViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_customfields, parent, false);
        return new CustomFieldsViewModel(view);
    }

    @Override
    public void onBindViewHolder(CustomFieldsViewModel holder, int position) {
        mHolder=holder;

        CustomField customField=customFieldList.get(position);
        String typeOfField=customField.getTypeOfField();
        List<String>choicesList=customField.getChoices();
        String updateText="";
            if(whichOne.equals("update")){
               String vl=customField.getTextValue();
               if(vl!=null){
                   updateText=vl;
               }
                customFieldListOutput=customFieldList;
            }
        if(choicesList==null){
            choicesList=new ArrayList<>();
        }
        String name=customField.getName();
        if(name==null){
            name="";
        }
        String id=customField.getId();
        TypeWhich typeWhich=new TypeWhich();
        try{
            holder.linear_custom_fields_parent.removeAllViews();
        }catch (Exception e){
            e.printStackTrace();
        }
        switch (typeOfField){
            case "Text":
                holder.linear_custom_fields_parent.addView(holder.inputTextView);

                String valueInput="Enter user input "+name;
                holder.inputTextView.setHint(valueInput);
                if(whichOne.equals("update")){
                    hMapText.put(position,updateText);
                    holder.inputTextView.setText(updateText);
                    for(CustomField customF:customFieldListOutput){

                        if(customF.getPosition()==holder.inputTextView.getId()){
                            if(customF.getTextValue().equals(holder.inputTextView.getText().toString())){
                                customF.setTextValue( holder.inputTextView.getText().toString());
                                return;
                            }
                        }

                    }

                        holder.inputTextView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                    String textValue= holder.inputTextView.getText().toString();
                    CustomField customField_text=new CustomField();
                    customField_text.setTextValue(textValue);
                    customField_text.setFormType("equipments");
                    customField_text.setId(id);
                    customField_text.setPosition(position);
                    customField_text.setTypeOfField("Text");
                    customFieldListOutput.add(customField_text);


                }
                else if (whichOne.equals("add")){

                    holder.inputTextView.setId(position);
                    typeWhich.setId(position);
                    typeWhich.setType("Text");
                    typeWhich.setCustomFieldId(id);
                    holder.inputTextView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            for(CustomField customF:customFieldListOutput){
                                if(customF.getPosition()==holder.inputTextView.getId()){
                                    customF.setTextValue( holder.inputTextView.getText().toString());
                                    return;
                                }

                            }
                            String textValue= holder.inputTextView.getText().toString();
                            CustomField customField_text=new CustomField();
                            customField_text.setTextValue(textValue);
                            customField_text.setFormType("equipments");
                            customField_text.setId(id);
                            customField_text.setPosition(position);
                            customField_text.setTypeOfField("Text");
                            customFieldListOutput.add(customField_text);
                        }
                    });

                }


                break;
            case "List Of Values":
                holder.linear_custom_fields_parent.addView(holder.spinnerCustomField);
                holder.spinnerCustomField.setPrompt(""+name);
                holder.spinnerCustomField.setId(position);
                ArrayAdapter<String> adapterSpinner=new  ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item,
                        choicesList);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_1);
                holder.spinnerCustomField.setAdapter(adapterSpinner);
                List<String> finalChoicesList = choicesList;

                    holder. spinnerCustomField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                          //  if(++check > 1) {
                                spinnerCustomFieldSeletectedChoice=i;
                                String str = (String) adapterView.getItemAtPosition(i);
                                for(CustomField customF:customFieldListOutput){
                                    if(customF.getPosition()==holder.spinnerCustomField.getId()){
                                        customF.setTextValue(str);
                                        return;
                                    }

                                }
                                CustomField customField_lov=new CustomField();
                                customField_lov.setTextValue(str);
                                customField_lov.setFormType("equipments");
                                customField_lov.setId(id);
                                customField_lov.setPosition(position);
                                customField_lov.setChoices(finalChoicesList);
                                customField_lov.setTypeOfField("List Of Values");
                                customFieldListOutput.add(customField_lov);
                          //  }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                typeWhich.setId(position);
                        typeWhich.setType("List Of Values");
                        typeWhich.setCustomFieldId(id);
                String values=holder.spinnerCustomField.getSelectedItem().toString();

                break;
            case "Date":
                holder.linear_custom_fields_parent.addView(holder.dateTextView);
                String valueDate="Date "+name;
                holder.dateTextView.setText(valueDate);
                holder.dateTextView.setId((position));
                if(whichOne.equals("update")){
                    holder.dateTextView.setText(updateText);
                    hMapDate.put(position,updateText);

                }
                if(whichOne.equals("update")){
                    for(CustomField customF:customFieldListOutput){
                        if(customF.getPosition()==holder.dateTextView.getId()){
                            if(customF.getTextValue().equals(holder.dateTextView.getText().toString()))
                            customF.setTextValue(holder.dateTextView.getText().toString());
                            return;
                        }
                    }
                    CustomField customField1=new CustomField();
                    customField1.setTextValue(holder.dateTextView.getText().toString());
                    customField1.setId(id);
                    customField1.setPosition(position);
                    customField1.setFormType("equipments");
                    customField1.setTypeOfField("Date");
                    customFieldListOutput.add(customField1);
                }else if(whichOne.equals("add")){
                    holder.dateTextView.setOnClickListener(view ->{
                        Calendar mcurrentDate=Calendar.getInstance();
                        final int mYear = mcurrentDate.get(Calendar.YEAR);
                        final int mMonth=mcurrentDate.get(Calendar.MONTH);
                        final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog mDatePicker=new DatePickerDialog(mContext, (datepicker, selectedyear, selectedmonth, selectedday) -> {
                            selectedMonth=selectedmonth+1;
                            selectedDay=selectedday;
                            selectedYear=selectedyear;
                            String finalTimeDay=String.format("%02d",selectedDay);
                            String finalTimeMonth=String.format("%02d",selectedMonth);

                            String value =selectedYear+"-"+finalTimeMonth+"-"+finalTimeDay;

                            holder.dateTextView.setText(value);
                            for(CustomField customF:customFieldListOutput){
                                if(customF.getPosition()==holder.dateTextView.getId()){
                                    customF.setTextValue(holder.dateTextView.getText().toString());
                                    return;
                                }

                            }
                            CustomField customField1=new CustomField();
                            customField1.setTextValue(holder.dateTextView.getText().toString());
                            customField1.setId(id);
                            customField1.setPosition(position);
                            customField1.setFormType("equipments");
                            customField1.setTypeOfField("Date");
                            customFieldListOutput.add(customField1);

                        },mYear, mMonth, mDay);
                        mDatePicker.show();
                    });
                }

                typeWhich.setId(position);
                        typeWhich.setType("Date");
                        typeWhich.setCustomFieldId(id);

                break;
            case "Numeric":
                holder.linear_custom_fields_parent.addView(holder.numericEditText);
                String valueNumeric="Input "+name;
                holder.numericEditText.setHint(valueNumeric);
                holder.numericEditText.setId(position);
                typeWhich.setId(position);
                        typeWhich.setType("Numeric");
                        typeWhich.setCustomFieldId(id);
                if(whichOne.equals("update")){
                    holder.numericEditText.setText(updateText);
                    hMapNumeric.put(position,updateText);

                }
                holder.numericEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        for(CustomField customF:customFieldListOutput){
                            if(customF.getPosition()==holder.numericEditText.getId()){
                                customF.setTextValue( holder.numericEditText.getText().toString());
                                return;
                            }

                        }

                        CustomField customField_numeric=new CustomField();
                        String tValue=holder.numericEditText.getText().toString();
                        customField_numeric.setTextValue(tValue);
                        customField_numeric.setFormType("equipments");
                        customField_numeric.setId(id);
                        customField_numeric.setPosition(position);
                        customField_numeric.setTypeOfField("Numeric");
                        customFieldListOutput.add(customField_numeric);

                    }
                });


                break;
            case "CheckBox":
                holder.linear_custom_fields_parent.addView(holder.checkBoxCustomField);
                String valueCheckBox="Select "+name;
                holder.checkBoxCustomField.setText(valueCheckBox);
                holder.checkBoxCustomField.setId(position);
                if(whichOne.equals("update")){
                    hMapBoolean.put(position,updateText);
                    holder.checkBoxCustomField.setText(updateText);
                    if(updateText.equals("True")){
                        holder.checkBoxCustomField.setChecked(true);
                    }else{
                        holder.checkBoxCustomField.setChecked(false);
                    }
                }
                typeWhich.setId(position);
                        typeWhich.setType("CheckBox");
                        typeWhich.setCustomFieldId(id);
                        boolean isChecked=holder.checkBoxCustomField.isChecked();
                        String teValue="";
                if(isChecked){
                    teValue="True";
                }else{
                    teValue="False";
                }
                holder.checkBoxCustomField.setOnCheckedChangeListener((compoundButton, b) -> {
                    String chValue="";
                    if(b){
                        chValue="True";
                    }else{
                        chValue="False";
                    }
                    for(CustomField customF:customFieldListOutput){
                        if(customF.getPosition()==holder.checkBoxCustomField.getId()){
                            customF.setTextValue( chValue);
                            return;
                        }

                    }
                    CustomField customField_checkBox=new CustomField();
                    customField_checkBox.setTextValue(chValue);
                    customField_checkBox.setId(id);
                    customField_checkBox.setFormType("equipments");
                    customField_checkBox.setTypeOfField("CheckBox");
                    customField_checkBox.setPosition(position);
                    customFieldListOutput.add(customField_checkBox);

                });

                break;
            case "AutoCompleteBox":
                holder.linear_custom_fields_parent.addView(holder.autoCompleteTextView);
                String valueACTV=""+name;
                holder.autoCompleteTextView.setHint(valueACTV);
                holder.autoCompleteTextView.setId(position);
                if(whichOne.equals("update")) {
                    holder.autoCompleteTextView.setText(updateText);
                    hMapActv.put(position,updateText);

                }
                typeWhich.setId(position);
                        typeWhich.setType("AutoCompleteBox");
                        typeWhich.setCustomFieldId(id);
                        String texValue=holder.autoCompleteTextView.getText().toString();

                holder.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        for(CustomField customF:customFieldListOutput){
                            if(customF.getPosition()==holder.autoCompleteTextView.getId()){
                                customF.setTextValue( holder.autoCompleteTextView.getText().toString());
                                return;
                            }

                        }
                        CustomField customField_actv=new CustomField();
                        customField_actv.setTextValue(texValue);
                        customField_actv.setId(id);
                        customField_actv.setPosition(position);
                        customField_actv.setFormType("equipments");
                        customField_actv.setTypeOfField("AutoCompleteBox");
                        customFieldListOutput.add(customField_actv);
                    }
                });

                break;


        }
        typeWhichList.add(typeWhich);



    }

    @Override
    public int getItemCount() {
        if (customFieldList == null) {
            return 0;
        }
        return customFieldList.size();
    }
    public List<CustomField>getCustomFieldListOutput(){
        return customFieldListOutput;
    }

}


