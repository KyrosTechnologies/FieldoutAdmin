package com.kyros.technologies.fieldout.fragments;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.databinding.FragmentFooterBinding;

/**
 * Created by kyros on 04-01-2018.
 */

public class FooterFragment extends Fragment{
    private FragmentFooterBinding binding;
    private AlertDialog singleSelectBox;
    private String title="Which type do you want to select?";
    private String[] zoneArray=new String []{"left","central","right"};
    private int checkedItem=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_footer,container,false);
        View view=binding.getRoot();
        bindViews();
        return view;
    }

    private void bindViews() {
        binding.textViewJobHashFragment.setOnClickListener(view -> singleSelectDialogBox(title,zoneArray,checkedItem,"<@JobId@>"));
        binding.textViewDateScheduledStartedFragment.setOnClickListener(view -> singleSelectDialogBox(title,zoneArray,checkedItem,"<@StartedScheduledDate@>"));
        binding.textViewDateCarriedOutStarted.setOnClickListener(view -> singleSelectDialogBox(title,zoneArray,checkedItem,"<@StartedRealdDate@>"));
        binding.textViewDate.setOnClickListener(view -> singleSelectDialogBox(title,zoneArray,checkedItem,"<@GenerationDate@>"));
        binding.textViewCurrentPageNumber.setOnClickListener(view -> singleSelectDialogBox(title,zoneArray,checkedItem,"<@CurrentPage@>"));
        binding.textViewTotalPageNumber.setOnClickListener(view -> singleSelectDialogBox(title,zoneArray,checkedItem,"<@ToPage@>"));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissSingleSelectDialogBox();
    }
    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }


    private void bindData(String value, String whichOne) {
        switch (whichOne){
            case "left":
                String oldValueLeft=(binding.editTextLeftZone.getText().toString())+" "+value;
                if(oldValueLeft!=null){
                    binding.editTextLeftZone.setText(oldValueLeft);
                }

                break;
            case "central":
                String oldValueCentral=(binding.editTextCentralZone.getText().toString())+" "+value;
                if(oldValueCentral!=null){
                    binding.editTextCentralZone.setText(oldValueCentral);
                }
                break;
            case "right":
                String oldValueRight=(binding.editTextRightZone.getText().toString())+" "+value;
                if(oldValueRight!=null){
                    binding.editTextRightZone.setText(oldValueRight);
                }
                break;
        }
    }

    private void singleSelectDialogBox(String title, String[] values,int checkItem,String data){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setSingleChoiceItems(values, checkItem, (dialogInterface, i) -> bindData(data,zoneArray[i]));
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        singleSelectBox = builder.create();
        singleSelectBox.show();


    }
    private void dismissSingleSelectDialogBox(){
        if(singleSelectBox!=null && singleSelectBox.isShowing()){
            singleSelectBox.dismiss();
        }
    }
}
