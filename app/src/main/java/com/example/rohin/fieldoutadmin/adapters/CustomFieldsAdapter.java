package com.example.rohin.fieldoutadmin.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.AddEquipment;
import com.example.rohin.fieldoutadmin.models.CustomField;
import com.example.rohin.fieldoutadmin.models.TypeWhich;
import com.example.rohin.fieldoutadmin.views.CustomFieldsViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by kyros on 23-01-2018.
 */

public class CustomFieldsAdapter extends RecyclerView.Adapter<CustomFieldsViewModel> {
    private List<CustomField> customFieldList;
    List<CustomField>customFieldListOutput=new ArrayList<>();
    private Context mContext;
    private int spinnerCustomFieldSeletectedChoice=0;
    public  int selectedYear=0;
    public  int selectedMonth=0;
    public int selectedDay=0;
    private List<TypeWhich>typeWhichList=new ArrayList<>();
    private String inputTextViewResult=null;
    private String spinnerCustomFieldResult=null;
    private String dateTextViewResult=null;
    private String numericEditTextResult=null;
    private boolean checkBoxCustomFieldResult;
    private String autoCompleteTextViewResult;
    private CustomFieldsViewModel mHolder;
    public CustomFieldsAdapter(){

    }

    public void setCustomFieldData(List<CustomField> customFieldList, Context mContext){
        this.customFieldList = customFieldList;
        this.mContext = mContext;
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
        if(choicesList==null){
            choicesList=new ArrayList<>();
        }
        String name=customField.getName();
        if(name==null){
            name="";
        }
        String id=customField.getId();
        TypeWhich typeWhich=new TypeWhich();
        switch (typeOfField){
            case "Text":
                holder.linear_custom_fields_parent.addView(holder.inputTextView);
                String valueInput="Enter user input "+name;
                holder.inputTextView.setHint(valueInput);
                holder.inputTextView.setId(position);
                         typeWhich.setId(position);
                        typeWhich.setType("Text");
                        typeWhich.setCustomFieldId(id);
                break;
            case "List Of Values":
                holder.linear_custom_fields_parent.addView(holder.spinnerCustomField);
                holder.spinnerCustomField.setPrompt(""+name);
                holder.spinnerCustomField.setId(position);
                ArrayAdapter<String> adapterSpinner=new  ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item,
                        choicesList);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_1);
                holder.spinnerCustomField.setAdapter(adapterSpinner);
                holder. spinnerCustomField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        spinnerCustomFieldSeletectedChoice=i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                typeWhich.setId(position);
                        typeWhich.setType("List Of Values");
                        typeWhich.setCustomFieldId(id);
                break;
            case "Date":
                holder.linear_custom_fields_parent.addView(holder.dateTextView);
                String valueDate="Date "+name;
                holder.dateTextView.setText(valueDate);
                holder.dateTextView.setId((position+1));
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


                    },mYear, mMonth, mDay);
                    mDatePicker.show();
                });
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
                break;
            case "CheckBox":
                holder.linear_custom_fields_parent.addView(holder.checkBoxCustomField);
                String valueCheckBox="Select "+name;
                holder.checkBoxCustomField.setText(valueCheckBox);
                holder.checkBoxCustomField.setId(position);
                typeWhich.setId(position);
                        typeWhich.setType("CheckBox");
                        typeWhich.setCustomFieldId(id);
                break;
            case "AutoCompleteBox":
                holder.linear_custom_fields_parent.addView(holder.autoCompleteTextView);
                String valueACTV=""+name;
                holder.autoCompleteTextView.setHint(valueACTV);
                holder.autoCompleteTextView.setId(position);
                typeWhich.setId(position);
                        typeWhich.setType("AutoCompleteBox");
                        typeWhich.setCustomFieldId(id);
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
    public List<CustomField> getCustomFilesList(){
        List<CustomField>customFieldList=new ArrayList<>();
        Log.d("TypeWhich size : ",""+typeWhichList.size());
        Log.d("Type Which List : ",""+new Gson().toJson(typeWhichList));
        if(typeWhichList!=null && typeWhichList.size()!=0){
            int position=0;
            for(TypeWhich typeWhich:typeWhichList){
                CustomField customField=new CustomField();

                position++;
                switch (typeWhich.getType()){
                    case "Text":
                      TextView textView= mHolder.inputTextView.findViewById(typeWhich.getId());
                            String textValue=textView.getText().toString();
                            String formType="equipments";
                            String typeOfField="Text";
                            customField.setTextValue(textValue);
                            customField.setFormType(formType);
                            customField.setId(typeWhich.getCustomFieldId());
                            customField.setTypeOfField(typeOfField);
                            customFieldList.add(customField);

                        break;
                    case "List Of Values":
                        String value ="";
                            Spinner spinner=mHolder.spinnerCustomField.findViewById(typeWhich.getId());
                            value=spinner.getSelectedItem().toString();
                            CustomField customFieldTemp =(CustomField)mHolder.spinnerCustomField.getTag();
                            List<String>choiceList=customFieldTemp.getChoices();
                            String formType_lov="equipments";
                            String typeOfField_lov="List Of Values";
                        customField.setTextValue(value);
                        customField.setFormType(formType_lov);
                        customField.setId(typeWhich.getCustomFieldId());
                        customField.setChoices(choiceList);
                        customField.setTypeOfField(typeOfField_lov);
                            customFieldList.add(customField);


                        break;
                    case "Date":

                        String textValue_date ="";
                            TextView textView1=mHolder.dateTextView.findViewById((typeWhich.getId()));
                            //textValue_date=textView1.getText().toString();
                        Log.d("Date ID : ",""+mHolder.dateTextView.getId());
                            String formType_date="equipments";
                            String typeOfField_date="Date";
                        customField.setTextValue(textValue_date);
                        customField.setId(typeWhich.getCustomFieldId());
                        customField.setFormType(formType_date);
                        customField.setTypeOfField(typeOfField_date);
                            customFieldList.add(customField);

                        break;
                    case "Numeric":

                       EditText editText= mHolder.numericEditText.findViewById(typeWhich.getId());
                            String textValue_numeric=editText.getText().toString();
                            String formType_numeric="equipments";
                            String typeOfField_numeric="Numeric";
                        customField.setTextValue(textValue_numeric);
                        customField.setFormType(formType_numeric);
                        customField.setId(typeWhich.getCustomFieldId());
                        customField.setTypeOfField(typeOfField_numeric);
                            customFieldList.add(customField);

                        break;
                    case "CheckBox":

                        String textValue_checkbox="";
                           CheckBox checkBox= mHolder.checkBoxCustomField.findViewById(typeWhich.getId());
                            boolean isChecked=checkBox.isChecked();
                            if(isChecked){
                                textValue_checkbox="True";
                            }else{
                                textValue_checkbox="False";
                            }
                            String formType_checkbox="equipments";
                            String typeOfField_checkbox="CheckBox";
                        customField.setTextValue(textValue_checkbox);
                        customField.setId(typeWhich.getCustomFieldId());
                        customField.setFormType(formType_checkbox);
                        customField.setTypeOfField(typeOfField_checkbox);
                            customFieldList.add(customField);

                        break;
                    case "AutoCompleteBox":

                       AutoCompleteTextView autoCompleteTextView= mHolder.autoCompleteTextView.findViewById(typeWhich.getId());
                            String textValue_actv=autoCompleteTextView.getText().toString();
                            String formType_actv="equipments";
                            String typeOfField_actv="AutoCompleteBox";
                        customField.setTextValue(textValue_actv);
                        customField.setId(typeWhich.getCustomFieldId());
                        customField.setFormType(formType_actv);
                        customField.setTypeOfField(typeOfField_actv);
                            customFieldList.add(customField);

                        break;

                }
            }
        }
        return customFieldList;
    }


}


