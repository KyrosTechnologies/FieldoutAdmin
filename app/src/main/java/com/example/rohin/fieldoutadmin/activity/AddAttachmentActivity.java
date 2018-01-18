package com.example.rohin.fieldoutadmin.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.common.FilePath;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.ActivityAddAttachmentBinding;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.FileInputStream;

import rx.subscriptions.CompositeSubscription;

public class AddAttachmentActivity extends AppCompatActivity {
    private ActivityAddAttachmentBinding binding;
    private PreferenceManager store;
    private CompositeSubscription subscription;
    private String TAG=AddAttachmentActivity.class.getSimpleName();
    private int FILE_PICKER=3;
    private RxPermissions rxPermissions;
    private final int READ_EXTERNAL_STORAGE=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectAddAttachmentActivity(this);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_attachment);
        store=PreferenceManager.getInstance(getApplicationContext());
        subscription=new CompositeSubscription();
         rxPermissions = new RxPermissions(this);
        binding.clickToAddAttachment.setOnClickListener(view -> checkReadPermission());
    }

    private void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast("Please allow permission for attachments!");

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE);

            }
        }else{
            getAttachment();
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getAttachment();

                } else {

                  showToast("Please enable permission for attachment");
                }
                return;
            }

        }
    }
    private void getAttachment() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload"),FILE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == FILE_PICKER){
                if(data != null){
                    Uri selectedFileUri = data.getData();
                String selectedFilePath = FilePath.getPath(this,selectedFileUri);
                    Log.i(TAG,"Selected File Path:" + selectedFilePath);

                    if(selectedFilePath != null && !selectedFilePath.equals("")){
                        Log.d("File Path : ",TAG+" / / "+selectedFilePath);
                        byte[] fileByte=getBytes(new File(selectedFilePath));
                        try {
                            Log.d("fileByte : ",TAG+" / / "+fileByte.toString());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                       showToast("File path is empty!");
                    }
                }
            }
        }
    }

    private byte[] getBytes (File file){
        FileInputStream input = null;
        if (file.exists())
            try{
            input = new FileInputStream (file);
            int len = (int) file.length();
            byte[] data = new byte[len];
            int count, total = 0;
            while ((count = input.read (data, total, len - total)) > 0) total += count;
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            if (input != null)
                try{
                input.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
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
                this.finish();
                return true;
            case R.id.action_delete:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
