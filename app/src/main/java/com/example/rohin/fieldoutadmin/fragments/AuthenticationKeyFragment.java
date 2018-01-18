package com.example.rohin.fieldoutadmin.fragments;

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
import com.example.rohin.fieldoutadmin.databinding.FragmentAuthenticationKeyBinding;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

/**
 * Created by kyros on 16-01-2018.
 */

public class AuthenticationKeyFragment extends Fragment {
    private FragmentAuthenticationKeyBinding binding;
    private PreferenceManager store;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_authentication_key,container,false);
        View view=binding.getRoot();
        store=PreferenceManager.getInstance(getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
        String authKey=store.getToken();
        if(authKey!=null){
            binding.textViewTokenId.setText(authKey);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

}
