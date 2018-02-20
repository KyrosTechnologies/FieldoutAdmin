package com.kyros.technologies.fieldout.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.FilePath;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.models.AddAttachments;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.AddAttachmentsViewModel;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rohin on 26-12-2017.
 */

public class CustomerDetails extends AppCompatActivity {

    private PreferenceManager store;
    private TextView job_number_details,main_address_details,lat_lng_details,name_details,mobile_details,phone_details,path_text;
    private ImageView cancel_attachment;
    private LinearLayout add_attachments,add_attachments_linear;
    private Button save_jobs_details;
    private String customerid=null;
    private String customername=null;
    private String cusaddress=null;
    private String compaddress=null;
    private String cusmyid=null;
    private String cusfirstname=null;
    private String cuslastname=null;
    private String cusmobile=null;
    private String cusphone=null;
    private String cusfax=null;
    private String cusemail=null;
    private String latlng=null;
    private String taginfo=null;
    private String customFields=null;
    private CompositeSubscription subscription;
    private String TAG=AddAttachmentActivity.class.getSimpleName();
    private int FILE_PICKER=3;
    private RxPermissions rxPermissions;
    private final int READ_EXTERNAL_STORAGE=4;
    @Inject
    AddAttachmentsViewModel viewModel;
    private ProgressDialog pDialog;
    private byte[] fileByte;
    private String fileName=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.customer_details);
        store= PreferenceManager.getInstance(getApplicationContext());
        job_number_details = findViewById(R.id.job_number_details);
        main_address_details = findViewById(R.id.main_address_details);
        lat_lng_details = findViewById(R.id.lat_lng_details);
        name_details = findViewById(R.id.name_details);
        mobile_details = findViewById(R.id.mobile_details);
        phone_details = findViewById(R.id.phone_details);
        save_jobs_details=findViewById(R.id.save_jobs_details);
        path_text=findViewById(R.id.path_text);
        cancel_attachment=findViewById(R.id.cancel_attachment);
        add_attachments=findViewById(R.id.add_attachments);
        add_attachments_linear=findViewById(R.id.add_attachments_linear);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectCustomerDetails(this);
        subscription=new CompositeSubscription();
        rxPermissions = new RxPermissions(this);
        add_attachments.setOnClickListener(view -> checkReadPermission());

        try {

            Bundle bundle = getIntent().getExtras();
            customerid = bundle.getString("customerid");
            customername = bundle.getString("customername");
            cusaddress = bundle.getString("address");
            compaddress = bundle.getString("compaddress");
            cusmyid = bundle.getString("myid");
            cusfirstname = bundle.getString("firstname");
            cuslastname = bundle.getString("lastname");
            cusmobile = bundle.getString("mobile");
            cusphone = bundle.getString("phone");
            cusfax = bundle.getString("fax");
            cusemail = bundle.getString("email");
            latlng=bundle.getString("latlng");
            taginfo=bundle.getString("tags");
            customFields=bundle.getString("customFields");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cusmyid != null) {
            job_number_details.setText(cusmyid);
        }
        if (cusaddress != null) {
            main_address_details.setText(cusaddress);
        }
        if (cusfirstname==null){
            cusfirstname="";
        }
        if (cuslastname==null){
            cuslastname="";
        }
        if (cusemail==null){
            cusemail="";
        }
            name_details.setText(cusfirstname+" "+cuslastname+" ("+cusemail+")");

        if (cusmobile != null) {
            mobile_details.setText(cusmobile);
        }
        if (cusphone != null) {
            phone_details.setText(cusphone);
        }
        if (latlng!=null){
            lat_lng_details.setText(latlng);
        }

        cancel_attachment.setOnClickListener(view -> {
            add_attachments_linear.setVisibility(View.GONE);
        });

        save_jobs_details.setOnClickListener(view -> {
            initiateAddAttachmentAPIByteStream(fileName,fileByte);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                CustomerDetails.this.finish();
                return true;
            case R.id.action_edit:
                Intent i=new Intent(CustomerDetails.this,CustomerUpdateDelete.class);
                i.putExtra("customerid",customerid);
                i.putExtra("customername",customername);
                i.putExtra("address",cusaddress);
                i.putExtra("compaddress",compaddress);
                i.putExtra("myid",cusmyid);
                i.putExtra("firstname",cusfirstname);
                i.putExtra("lastname",cuslastname);
                i.putExtra("mobile",cusmobile);
                i.putExtra("phone",cusphone);
                i.putExtra("fax",cusfax);
                i.putExtra("email",cusemail);
                try {
                    i.putExtra("tags",taginfo.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                i.putExtra("customFields",customFields);
                startActivity(i);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(CustomerDetails.this);
            pDialog.setTitle("Please Wait");
            pDialog.setMessage("Synchronization in progress...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
        }
        pDialog.show();
    }

    private void dismissProgressDialog() {
        try{
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FILE_PICKER) {
                if (data != null) {
                    Uri selectedFileUri = data.getData();
                    String selectedFilePath = FilePath.getPath(this, selectedFileUri);
                    Log.i(TAG, "Selected File Path:" + selectedFilePath);

                    if (selectedFilePath != null && !selectedFilePath.equals("")) {
                        Log.d("File Path : ", TAG + " / / " + selectedFilePath);
                        File file = new File(selectedFilePath);
                        fileByte = getBytes(file);
                        fileName = file.getName();
                        if (fileName != null) {
                            add_attachments_linear.setVisibility(View.VISIBLE);
                            path_text.setText(fileName);

                            try {
                                Log.d("fileByte : ", TAG + " / / " + fileByte.toString());
                                Log.d("fileName : ", TAG + " / / " + fileName);
                                Log.d("fileSize : ", TAG + " / / " + file.length());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showToast("File path is empty!");
                        }
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
                CustomerDetails.this.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            showToast("response is null !");
        }
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

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    @NonNull
    private MultipartBody.Part attachmentFilePart(String partName, Uri fileUri, String fileName, File file){
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