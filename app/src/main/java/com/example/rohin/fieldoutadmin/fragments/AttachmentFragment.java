package com.example.rohin.fieldoutadmin.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.AddAttachmentActivity;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.FragmentAttachmentBinding;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 29-12-2017.
 */

public class AttachmentFragment extends Fragment {
    private CompositeSubscription subscription;
    private PreferenceManager store;
    private String TAG=AttachmentFragment.class.getSimpleName();
    private FragmentAttachmentBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectAttachmentFragment(this);
       binding= DataBindingUtil.inflate(inflater,R.layout.fragment_attachment,container,false);
       store=PreferenceManager.getInstance(getContext());
       subscription=new CompositeSubscription();
       View view=binding.getRoot();
       binding.addAttachmentButton.setOnClickListener(view1 -> startActivity(new Intent(getContext(), AddAttachmentActivity.class)));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.clear();
    }
    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
