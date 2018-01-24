package com.kyros.technologies.fieldout.fragments;

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
import com.kyros.technologies.fieldout.databinding.FragmentOptionsBinding;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

/**
 * Created by kyros on 03-01-2018.
 */

public class OptionsFragment extends Fragment {
    private PreferenceManager store;
    private String TAG=OptionsFragment.class.getSimpleName();
    private FragmentOptionsBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_options,container,false);
        View view=binding.getRoot();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
