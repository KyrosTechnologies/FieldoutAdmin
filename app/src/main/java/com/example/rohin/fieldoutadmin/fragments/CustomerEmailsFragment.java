package com.example.rohin.fieldoutadmin.fragments;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.databinding.FragmentCustomerEmailBinding;

/**
 * Created by kyros on 04-01-2018.
 */

public class CustomerEmailsFragment extends Fragment{
    private FragmentCustomerEmailBinding binding;
    private AlertDialog dialogWhichOne;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_customer_email,container,false);
        View view=binding.getRoot();
        bindViews();
        return view;
    }

    private void bindViews() {
        binding.textViewDateScheduledStarted.setOnClickListener(view -> showDialogWhichOne("<@StartedScheduledDate@>"));
        binding.textViewDateScheduledCompleted.setOnClickListener(view -> showDialogWhichOne("<@CompletedScheduledDate@>"));
        binding.textViewDateTimeStarted.setOnClickListener(view -> showDialogWhichOne("<@StartedScheduledDateTime@>"));
        binding.textViewDateTimeCompleted.setOnClickListener(view -> showDialogWhichOne("<@CompletedScheduledDateTime@>"));
        binding.textViewDateCarriedStarted.setOnClickListener(view -> showDialogWhichOne("<@StartedRealisedDate@>"));
        binding.textViewDateCarriedCompleted.setOnClickListener(view -> showDialogWhichOne("<@CompletedRealisedDate@>"));
        binding.textViewDateTimeCarriedStarted.setOnClickListener(view -> showDialogWhichOne("<@StartedRealisedDateTime@>"));
        binding.textViewDateTimeCarriedCompleted.setOnClickListener(view -> showDialogWhichOne("<@CompletedRealisedDateTime@>"));
        binding.textViewJobTypes.setOnClickListener(view -> showDialogWhichOne("<@JobType@>"));
        binding.textViewJobHash.setOnClickListener(view -> showDialogWhichOne("<@JobNumber@>"));
        binding.textViewDescription.setOnClickListener(view -> showDialogWhichOne("<@JobDescription@>"));
        binding.textViewContact.setOnClickListener(view -> showDialogWhichOne("<@ContactName@>"));
        binding.textViewJobCusf.setOnClickListener(view -> showDialogWhichOne("<@Job.CustomField.JobCusF@>"));
        binding.textViewTechnicianName.setOnClickListener(view -> showDialogWhichOne("<@TechnicianName@>"));
        binding.textViewCustomer.setOnClickListener(view -> showDialogWhichOne("<@CustomerName@>"));
        binding.textViewSage.setOnClickListener(view -> showDialogWhichOne("<@Customer.CustomField.Sage #@>"));
        binding.textViewSite.setOnClickListener(view -> showDialogWhichOne("<@SiteName@>"));
        binding.textViewEquipment.setOnClickListener(view -> showDialogWhichOne("<@EquipmentName@>"));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissDialogWhichOne();
    }
    private void showDialogWhichOne(String value){
        if(dialogWhichOne==null){
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setMessage("Which Type do you want to insert?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Subject",
                    (dialog, id) ->{
                        bindData(value,"subject");
                        dialog.cancel();
                    });

            builder.setNegativeButton(
                    "Body",
                    (dialog, id) -> {
                        bindData(value,"body");
                        dialog.cancel();});
            dialogWhichOne=builder.create();
        }
        dialogWhichOne.show();
    }

    private void bindData(String value, String whichOne) {
        switch (whichOne){
            case "subject":
                String oldValueSubject=(binding.editTextSubject.getText().toString())+" "+value;
                if(oldValueSubject!=null){
                    binding.editTextSubject.setText(oldValueSubject);
                }

                break;
            case "body":
                String oldValueBody=(binding.editTextBody.getText().toString())+" "+value;
                if(oldValueBody!=null){
                    binding.editTextBody.setText(oldValueBody);
                }
                break;
        }
    }

    private void dismissDialogWhichOne() {
        if(dialogWhichOne!=null && dialogWhichOne.isShowing()){
            dialogWhichOne.dismiss();
        }
    }

    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
