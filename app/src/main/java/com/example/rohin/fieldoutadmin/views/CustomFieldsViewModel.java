package com.example.rohin.fieldoutadmin.views;

import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rohin.fieldoutadmin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyros on 23-01-2018.
 */

public class CustomFieldsViewModel extends RecyclerView.ViewHolder {
  public EditText inputTextView=null;
    public  Spinner spinnerCustomField=null;
    public TextView dateTextView=null;
    public  EditText numericEditText=null;
    public  CheckBox checkBoxCustomField=null;
    public  AutoCompleteTextView autoCompleteTextView =null;
    public  List<String> choicesList=new ArrayList<>();
    public    LinearLayout linear_custom_fields_parent;

    public CustomFieldsViewModel(View itemView) {
        super(itemView);
        linear_custom_fields_parent=itemView.findViewById(R.id.linear_custom_fields_parent);
        validateCustomField();
    }
    private void validateCustomField() {
        inputTextView =new EditText(itemView.getContext());
        String valueInput="Enter user input ";
        inputTextView.setHint(valueInput);
        inputTextView.setTextSize(20);
        LinearLayout.LayoutParams tableRowInputTextParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120,50);
        tableRowInputTextParams.setMargins(10,20,0,10);
        inputTextView.setLayoutParams(tableRowInputTextParams);
        inputTextView.setSingleLine(true);
        inputTextView.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.button_bg));
        inputTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
        inputTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.light_black));
        inputTextView.setPadding(15, 15, 5, 15);

        spinnerCustomField=new Spinner(itemView.getContext());
        LinearLayout.LayoutParams tableRowSpinnerParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120,50);
        tableRowSpinnerParams.setMargins(10,20,0,10);
        spinnerCustomField.setLayoutParams(tableRowSpinnerParams);
        spinnerCustomField.setPrompt("");
        spinnerCustomField.setGravity(Gravity.LEFT|Gravity.CENTER);
        spinnerCustomField.setPadding(15, 15, 5, 15);


        String valueDate="Date ";
        dateTextView=new TextView(itemView.getContext());
        dateTextView.setText(valueDate);
        dateTextView.setTextSize(20);
        LinearLayout.LayoutParams tableRowuserNameParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120,50);
        tableRowuserNameParams.setMargins(10,20,0,10);
        dateTextView.setLayoutParams(tableRowuserNameParams);
        dateTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
        dateTextView.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.default_text_view_background));
        dateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
        dateTextView.setPadding(15, 15, 5, 15);


        numericEditText=new EditText(itemView.getContext());
        String valueNumeric="Input ";
        numericEditText.setHint(valueNumeric);
        numericEditText.setTextSize(20);
        numericEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams tableRowNumericParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120,50);
        tableRowNumericParams.setMargins(10,20,0,10);
        numericEditText.setLayoutParams(tableRowNumericParams);
        numericEditText.setSingleLine(true);
        numericEditText.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.button_bg));
        numericEditText.setGravity(Gravity.LEFT|Gravity.CENTER);
        numericEditText.setTextColor(itemView.getContext().getResources().getColor(R.color.light_black));
        numericEditText.setPadding(15, 15, 5, 15);

        checkBoxCustomField=new CheckBox(itemView.getContext());
        checkBoxCustomField.setChecked(true);
        String valueCheckBox="Select ";
        checkBoxCustomField.setText(valueCheckBox);
        LinearLayout.LayoutParams tableRowCheckBoxParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120,50);
        tableRowCheckBoxParams.setMargins(10,20,0,10);
        checkBoxCustomField.setLayoutParams(tableRowCheckBoxParams);
        checkBoxCustomField.setGravity(Gravity.LEFT|Gravity.CENTER);
        checkBoxCustomField.setTextColor(itemView.getContext().getResources().getColor(R.color.light_black));
        checkBoxCustomField.setPadding(15, 15, 5, 15);

        autoCompleteTextView=new AutoCompleteTextView(itemView.getContext());
        String valueACTV="";
        autoCompleteTextView.setHint(valueACTV);
        autoCompleteTextView.setTextSize(20);
        LinearLayout.LayoutParams tableRowACTVParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120,50);
        tableRowACTVParams.setMargins(10,20,0,10);
        autoCompleteTextView.setLayoutParams(tableRowACTVParams);
        autoCompleteTextView.setSingleLine(true);
        autoCompleteTextView.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.button_bg));
        autoCompleteTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
        autoCompleteTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
        autoCompleteTextView.setPadding(15, 15, 5, 15);



}}
