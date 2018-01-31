package com.kyros.technologies.fieldout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityPdfCsvBinding;
import com.kyros.technologies.fieldout.models.InvoiceCustomerResponse;
import com.kyros.technologies.fieldout.models.PdfTotalList;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.PDF_CSV_ActivityViewModel;
import com.opencsv.CSVWriter;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class PDF_CSV_Activity extends AppCompatActivity {
    private CompositeSubscription subscription;
    private String TAG=PDF_CSV_Activity.class.getSimpleName();
    @Inject
    PDF_CSV_ActivityViewModel viewModel;
    private ActivityPdfCsvBinding binding;
    private PreferenceManager store;
    private String invoiceId="5a57650c94d2954827b1ce0c";
    private List<PdfTotalList>pdfTotalListList=new ArrayList<>();
    private List<String[]> data = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE = 1;
    private AlertDialog showChooseDialog;
    private String companyName;
    private String customerName;
    private String customerAddress;
    private String jrxmlPath=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_pdf__csv_);
        store=PreferenceManager.getInstance(getApplicationContext());
        ((ServiceHandler)getApplication()).getApplicationComponent().injectPDF_CSV_Activity(this);
        subscription=new CompositeSubscription();

        subscription.add(viewModel.invoiceCustomerResponseObservable(store.getToken(),invoiceId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(this::response,this::error,this::completed));
        data.add(new String[]{"ITEM", "DESCRIPTION","UNIT PRICE","QUANTITY","DISCOUNT","TOTAL","TAX"});
        customerName=store.getCustomerName();
        customerAddress=store.getCustomerAddress();
        companyName=store.getCompanyName();
        if(companyName == null){
            companyName="Harrison";
        } if(customerAddress == null){
            customerAddress="3rd main road kasthuri bhai nagar, adyar, chennai-20.";
        } if(customerName == null){
            customerName="Harrison Ford";
        }
        binding.buttonWriteToDisk.setOnClickListener(view -> checkPermission());
        Uri path = Uri.parse("file:///android_asset/invoice.jrxml");

      //  jrxmlPath= path.toString();
        jrxmlPath= getAssets()+"invoice.jrxml";
    }
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)&&ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Toast.makeText(getApplicationContext(),"Please allow read,write permission for storage",Toast.LENGTH_SHORT).show();


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }else{
            try {
                fileWriteCSV(store.getIdDomain()+"_csv_");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                writePDF(pdfTotalListList,store.getIdDomain()+"_pdf_");
            } catch (JRException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void completed() {

    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void error(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        fileWriteCSV(store.getIdDomain()+"_csv_");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        writePDF(pdfTotalListList,store.getIdDomain()+"_pdf_");
                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {


                    Toast.makeText(getApplicationContext(),"Please allow read,write permission for storage",Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }
    private void response(InvoiceCustomerResponse invoiceCustomerResponse) {
        if(invoiceCustomerResponse != null){
            List<String>itemList=invoiceCustomerResponse.getInvoice().getItem();
            List<String>descriptionList=invoiceCustomerResponse.getInvoice().getDescription();
            List<Integer>unitPriceList=invoiceCustomerResponse.getInvoice().getUnitPrice();
            List<Integer>quantityList=invoiceCustomerResponse.getInvoice().getQuantity();
            List<Integer>discountList=invoiceCustomerResponse.getInvoice().getDiscount();
            List<Integer>totalList=invoiceCustomerResponse.getInvoice().getTotal();
            List<Integer>taxList=invoiceCustomerResponse.getInvoice().getTax();
            if(itemList.size() != 0 && itemList != null && descriptionList.size() != 0 && descriptionList != null && unitPriceList.size() != 0 &&
                    unitPriceList != null && quantityList.size() != 0 && quantityList != null && discountList.size() !=0 && discountList != null &&
                    totalList.size() != 0 && totalList != null && taxList.size() != 0 && taxList != null){
                if((itemList.size() == descriptionList.size()) && (descriptionList.size() == unitPriceList.size()) && (unitPriceList.size() == quantityList.size()) && (quantityList.size() == discountList.size()) && (discountList.size() == totalList.size()) && (totalList.size() == taxList.size()) && (taxList.size() == itemList.size())){
                        for(int i=0; i<itemList.size(); i++){
                            PdfTotalList pdfTotalList=new PdfTotalList();
                            pdfTotalList.setItem(itemList.get(i));
                            pdfTotalList.setDescription(descriptionList.get(i));
                            pdfTotalList.setUnitPrice(unitPriceList.get(i));
                            pdfTotalList.setQuantity(quantityList.get(i));
                            pdfTotalList.setDiscount(discountList.get(i));
                            pdfTotalList.setTotal(totalList.get(i));
                            pdfTotalList.setTax(taxList.get(i));
                            data.add(new String[]{itemList.get(i),descriptionList.get(i),String.valueOf(unitPriceList.get(i)),String.valueOf(quantityList.get(i)),String.valueOf(discountList.get(i)),String.valueOf(totalList.get(i)),String.valueOf(taxList.get(i))});
                            pdfTotalListList.add(pdfTotalList);
                        }
                        Log.d("Pdf Total List : ",TAG+" / / "+new Gson().toJson(pdfTotalListList));
                }else{
                    showToast("list or not same!");
                }
            }else{
                showToast("some list is null or empty!");
            }


        }else {
            showToast("response is null!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissChooseDialog();
    }
    private  void fileWriteCSV(String title)throws Exception{
        File newFolder = new File(Environment.getExternalStorageDirectory(), "FieldOut");
        if (!newFolder.exists()) {
            newFolder.mkdir();
        }
        File textFile=new File(newFolder,File.separator+title+".csv");
        CSVWriter csvWriter=new CSVWriter(new FileWriter(textFile));
        csvWriter.writeAll(data);
        csvWriter.close();
      showToast("File write successfully");
      showDialogChoose(textFile);
    }

    private void showDialogChoose(File textFile) {
       AlertDialog.Builder builder=new AlertDialog.Builder(this);
       builder.setTitle("Do you want to open ?");
       builder.setMessage("File stored under : "+textFile.toString());
       builder.setPositiveButton("Yes", (dialogInterface, i) -> {
           Log.d("Path : ",textFile.toString());
           Uri path= Uri.parse(textFile.toString());
           Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
           intent.setDataAndType(path, "resource/folder");
           startActivity(Intent.createChooser(intent, "Open folder"));
       });
       builder.setNegativeButton("Cancel",((dialogInterface, i) -> dialogInterface.cancel()));
       showChooseDialog=builder.create();
       showChooseDialog.show();

    }
    private void dismissChooseDialog(){
        if(showChooseDialog != null && showChooseDialog.isShowing()){
            showChooseDialog.dismiss();
        }
    }
    private void writePDF(List<PdfTotalList>pdfTotalListList,String title)throws JRException,FileNotFoundException,IOException{
        File newFolder = new File(Environment.getExternalStorageDirectory(), "FieldOut");
        if (!newFolder.exists()) {
            newFolder.mkdir();
        }
        File textFile=new File(newFolder,File.separator+title+".pdf");
        JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(pdfTotalListList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("invoicedataset", itemsJRBean);
        parameters.put("companyName",companyName );
        parameters.put("customerName", customerName);
        parameters.put("customerAddress", customerAddress);
        String[] imgPath = getAssets().list("invoice.jrxml");
        try{
            for(String value: imgPath){
                Log.d("VALUE : ",value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(jrxmlPath, parameters, new JREmptyDataSource());
//        OutputStream outputStream = new FileOutputStream(textFile);
//            /* Write content to PDF file */
//        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        exportPDF(jasperPrint,textFile.toString());

    }
    private void exportPDF(JasperPrint print,String path)throws JRException{
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        System.out.println("Document Exported Successfully!");
        showToast("Pdf Generated successfully");
    }
}
