package com.kyros.technologies.fieldout.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.FilePath;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityAddAttachmentBinding;
import com.kyros.technologies.fieldout.models.AddAttachments;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.AddAttachmentsViewModel;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddAttachmentActivity extends AppCompatActivity {
    private ActivityAddAttachmentBinding binding;
    private PreferenceManager store;
    private CompositeSubscription subscription;
    private String TAG=AddAttachmentActivity.class.getSimpleName();
    private int FILE_PICKER=3;
    private RxPermissions rxPermissions;
    private final int READ_EXTERNAL_STORAGE=4;
    @Inject
    AddAttachmentsViewModel viewModel;
    private ProgressDialog progressDialog;
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
                        File file=new File(selectedFilePath);
                        byte[] fileByte=getBytes(file);
                        String fileName=file.getName();
                        if(fileName != null){
                            binding.addAttachmentTypeNameEditText.setText(fileName);
                            int fileSizeMb = Integer.parseInt(String.valueOf(file.length()/1024));
                            binding.addAttachmentTypeSizeTextView.setText(String.valueOf(fileSizeMb+" KB."));

                        }
                        binding.saveAddAttachmentTypeButton.setOnClickListener(view -> {
                            if(fileByte.length != 0 && fileName != null ){
                           //     initiateAddAttachmentAPI(file,"fileData",selectedFileUri,fileName);
                                initiateAddAttachmentAPIByteStream(fileName,fileByte);
                            }else{
                                showToast("file is empty!");
                            }
                        });

                        try {
                            Log.d("fileByte : ",TAG+" / / "+fileByte.toString());
                            Log.d("fileName : ",TAG+" / / "+fileName);
                            Log.d("fileSize : ",TAG+" / / "+file.length());
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

    private void initiateAddAttachmentAPIByteStream(String fileName, byte[] fileByte) {
        showProgressDialog();
        AddAttachments addAttachments=new AddAttachments();
        addAttachments.setFileData( encodeBase64(fileByte));
        addAttachments.setFileName(fileName);
        addAttachments.setFileType(fileName);
        StringTokenizer stringTokenizer=new StringTokenizer(fileName,".");
        String ext=stringTokenizer.nextToken();
        addAttachments.setFileExtension(ext);
        Log.d("InputAddAttach : ",""+new Gson().toJson(addAttachments));
        subscription.add(viewModel.addResponseBodyObservableByteStream(store.getToken(),addAttachments,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::addByteResponse,this::addByteError,this::addByteCompleted));
    }

    private void addByteCompleted() {
        dismissProgressDialog();
    }

    private void addByteError(Throwable throwable) {
        dismissProgressDialog();
        Log.d("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());
    }

    private void addByteResponse(ResponseBody responseBody) {
        dismissProgressDialog();
        if(responseBody != null){
            try {
                Log.d("Response Byte : ",TAG+" / / "+responseBody.string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            showToast("response is null !");
        }
    }

    private void initiateAddAttachmentAPI(File file, String fileDataKey, Uri selectedFileUri, String fileName) {
            showProgressDialog();
     MultipartBody.Part fileData=attachmentFilePart(fileDataKey,selectedFileUri,fileName,file);
        RequestBody fileNameInput=createPartFromString(fileName);
        HashMap<String,RequestBody>map=new HashMap<>();
        map.put("fileName",fileNameInput);
        subscription.add(viewModel.addResponseBodyObservable(store.getToken(),map,fileData,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::addResponse,this::addError,this::addCompleted));
    }


    private void addCompleted() {
    dismissProgressDialog();
    }

    private void addError(Throwable throwable) {
    Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    showToast(""+throwable.getMessage());
    dismissProgressDialog();
    }

    private void addResponse(ResponseBody responseBody) {
    dismissProgressDialog();
    if(responseBody != null){
        try {
            String response=responseBody.string();
            Log.d("Response : ",TAG+" / / "+response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }else {
        showToast("add response is null!");
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
        dismissProgressDialog();
    }

    private void dismissProgressDialog() {
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading.....!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
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
    @NonNull
    private MultipartBody.Part attachmentFilePart(String partName,Uri fileUri,String fileName,File file){
        String mimeType = null;
        if (fileUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(fileUri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        RequestBody requestFile=RequestBody.create(MediaType.parse(mimeType),file);
        return MultipartBody.Part.createFormData(partName,fileName,requestFile);
    }
    @NonNull
    private RequestBody createPartFromString (String value){
        return RequestBody.create(MultipartBody.FORM,value);
    }

    private String encodeBase64(byte[] bytes){
        byte[] encodeValue = Base64.encode(bytes, Base64.DEFAULT);
        return new String(encodeValue);
    }
    private String decodeBase64(byte[] bytes){
        byte[] decodeValue = Base64.decode(bytes, Base64.DEFAULT);
        return new String(decodeValue);

    }

}
