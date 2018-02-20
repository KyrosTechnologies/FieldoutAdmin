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

public class SiteDetails extends AppCompatActivity {

    private PreferenceManager store;
    private TextView job_number_details,main_address_details,lat_lng_details,name_details,mobile_details,phone_details,path_text;
    private ImageView cancel_attachment;
    private LinearLayout add_attachments,add_attachments_linear;
    private Button save_jobs_details;
    private String siteid=null;
    private String sitname=null;
    private String siteaddress=null;
    private String sitecompaddress=null;
    private String sitemyid=null;
    private String sitefirstname=null;
    private String sitelastname=null;
    private String sitemobile=null;
    private String sitephone=null;
    private String sitefax=null;
    private String siteemail=null;
    private String cusid=null;
    private String cusname=null;
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
        ((ServiceHandler)getApplication()).getApplicationComponent().injectSiteDetails(this);
        subscription=new CompositeSubscription();
        rxPermissions = new RxPermissions(this);
        add_attachments.setOnClickListener(view -> checkReadPermission());

        try {

            Bundle bundle = getIntent().getExtras();
            siteid=bundle.getString("siteid");
            cusid=bundle.getString("customerid");
            sitname=bundle.getString("sitename");
            cusname=bundle.getString("customername");
            siteaddress=bundle.getString("address");
            sitecompaddress=bundle.getString("compaddress");
            sitemyid=bundle.getString("myid");
            sitefirstname=bundle.getString("firstname");
            sitelastname=bundle.getString("lastname");
            sitemobile=bundle.getString("mobile");
            sitephone=bundle.getString("phone");
            sitefax=bundle.getString("fax");
            siteemail=bundle.getString("email");
            latlng=bundle.getString("latlng");
            taginfo=bundle.getString("tags");
            customFields=bundle.getString("customFields");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sitemyid != null) {
            job_number_details.setText(sitemyid);
        }
        if (siteaddress != null) {
            main_address_details.setText(siteaddress);
        }
        if (sitefirstname==null){
            sitefirstname="";
        }
        if (sitelastname==null){
            sitelastname="";
        }
        if (siteemail==null){
            siteemail="";
        }
        name_details.setText(sitefirstname+" "+sitelastname+" ("+siteemail+")");

        if (sitemobile != null) {
            mobile_details.setText(sitemobile);
        }
        if (sitephone != null) {
            phone_details.setText(sitephone);
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
                SiteDetails.this.finish();
                return true;
            case R.id.action_edit:
                Intent i=new Intent(SiteDetails.this,SitesUpdateDelete.class);
                i.putExtra("siteid",siteid);
                i.putExtra("customerid",cusid);
                i.putExtra("customername",cusname);
                i.putExtra("sitename",sitname);
                i.putExtra("address",siteaddress);
                i.putExtra("compaddress",sitecompaddress);
                i.putExtra("myid",sitemyid);
                i.putExtra("firstname",sitefirstname);
                i.putExtra("lastname",sitelastname);
                i.putExtra("phone",sitephone);
                i.putExtra("mobile",sitemobile);
                i.putExtra("fax",sitefax);
                i.putExtra("email",siteemail);
                i.putExtra("tags",taginfo.toString());
                i.putExtra("customFields",customFields);
                startActivity(i);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(SiteDetails.this);
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
                SiteDetails.this.finish();
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