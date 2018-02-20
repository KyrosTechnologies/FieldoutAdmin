package com.kyros.technologies.fieldout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.kyros.technologies.fieldout.models.InvoiceResponse;
import com.kyros.technologies.fieldout.models.Item;
import com.kyros.technologies.fieldout.models.PdfTotalList;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.PDF_CSV_ActivityViewModel;
import com.opencsv.CSVWriter;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
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
    private List<PdfTotalList>pdfTotalListList=new ArrayList<>();
    private List<String[]> data = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE = 1;
    private AlertDialog showChooseDialog;
  //  private String companyName;
    private String customerName;
    private String customerAddress;
    private String jrxmlPath=null;
    String reportSrcFile = "src/main/java/data/static_template.jrxml";
    String outputPath="C:\\Users\\kyros\\Desktop\\FieldoutReport\\";
    String companyName="Rohin pvt ltd";
    String address="2nd main road,Anna nagar west, Chennai 40.";
    String jobTypeName="Standard Job one";
    String scheduleTime="2018-01-06 11:30 AM";
    String scheduleDuration="02h00";
    String technicianName="Amalan";
    String jobCompleted="Completed";
    String completedDuration="03h00";
    String description="Description";
    String jobComplete="Job Completed";
    String notes="This is a note from the field out admin rohin.  Thank you";
    private String domainId="5a7c2a4f599b312e5f634637";
    private String invoiceId="5a81237f599b3129ed02e4e8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_pdf__csv_);
        store=PreferenceManager.getInstance(getApplicationContext());
        ((ServiceHandler)getApplication()).getApplicationComponent().injectPDF_CSV_Activity(this);
        subscription=new CompositeSubscription();

        subscription.add(viewModel.invoiceCustomerResponseObservable(store.getToken(),invoiceId,domainId)
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
            Async async=new Async();
            async.execute();
//            try {
//                fileWriteCSV(store.getIdDomain()+"_csv_");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

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

//                    try {
//                        fileWriteCSV(store.getIdDomain()+"_csv_");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    Async async=new Async();
                    async.execute();

                } else {


                    Toast.makeText(getApplicationContext(),"Please allow read,write permission for storage",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            //new Upload//

        }
    }
    private void response(InvoiceResponse invoiceCustomerResponse) {
        Log.d("Response : ",""+new Gson().toJson(invoiceCustomerResponse));
        if(invoiceCustomerResponse != null){
            List<com.kyros.technologies.fieldout.models.Item>itemList=invoiceCustomerResponse.getInvoice().getItems();
            if(itemList != null && itemList.size() != 0){
                for(int i=0; i<itemList.size(); i++){
                    PdfTotalList pdfTotalList=new PdfTotalList();
                    pdfTotalList.setItem(itemList.get(i).getItem());
                    pdfTotalList.setDescription(itemList.get(i).getDescription());
                    pdfTotalList.setUnitPrice(itemList.get(i).getUnitPrice());
                    pdfTotalList.setQuantity(itemList.get(i).getQuantity());
                    pdfTotalList.setDiscount(itemList.get(i).getDiscount());
                    pdfTotalList.setTotal(itemList.get(i).getTotal());
                    pdfTotalList.setTax(itemList.get(i).getTax());
                    data.add(new String[]{itemList.get(i).getItem(),itemList.get(i).getDescription(),String.valueOf(itemList.get(i).getUnitPrice()),String.valueOf(itemList.get(i).getQuantity()),String.valueOf(itemList.get(i).getDiscount()),String.valueOf(itemList.get(i).getTotal()),String.valueOf(itemList.get(i).getTax())});
                    pdfTotalListList.add(pdfTotalList);
                }
                Log.d("Pdf Total List : ",TAG+" / / "+new Gson().toJson(pdfTotalListList));
            }

                }else{
                    showToast("list or not same!");
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
           intent.setDataAndType(path, "resource_type_value/folder");
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
    private void printPDF() throws IOException, ClassNotFoundException, JRException, SQLException {
        File newFolder = new File(Environment.getExternalStorageDirectory(), "FieldOut");
        if (!newFolder.exists()) {
            newFolder.mkdir();
        }
        File textFile=new File(newFolder,File.separator+"title"+".pdf");
        Uri path = Uri.parse("assets://static_template.jrxml");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new BufferedInputStream(getAssets().open("static_template.jrxml")));
      //  JasperReport jasperReport = JasperCompileManager.compileReport(getAssets().open("static_template.jrxml"));
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("company_address",address );
        parameters.put("company_name", companyName);
        parameters.put("domain_name", companyName);
        parameters.put("job_type_name", jobTypeName);
        parameters.put("schedule_time", scheduleTime);
        parameters.put("scheduled_duration", scheduleDuration);
        parameters.put("technician_name", technicianName);
        parameters.put("job_completed", jobCompleted);
        parameters.put("completed_duration", completedDuration);
        parameters.put("description_text", description);
        parameters.put("job_complete_text", jobComplete);
        parameters.put("notes_text", notes);
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        list.add(parameters);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
        exportPDF(print,textFile.toString());
    }

    private void writePDF(List<PdfTotalList>pdfTotalListList,String title) throws Exception {
        Log.d("Write Pdf  : ","Started : ");

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
        InputStream is = getAssets().open("invoice.jrxml");
        String path="FieldoutAdmin\\app\\src\\main\\java\\com\\kyros\\technologies\\fieldout\\Tes\\invoice.jrxml";
        //URL fileURL = getClass().getClassLoader().getResource("invoice.jrxml");
//        String fileName = fileURL.getFile();
      //  String filePath = fileURL.getPath();
      ///  Log.d("Path : "," / filePath: "+filePath);
        JasperReport jasperReport=JasperCompileManager.compileReport(getAssets().open("invoice.jrxml"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
//        OutputStream outputStream = new FileOutputStream(textFile);
//            /* Write content to PDF file */
//        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        Log.d("Write Pdf : ","Write Pdf Finished : ");

        exportPDFV1(textFile.toString(),jasperPrint);

    }
    private void exportPDFV1(String path,JasperPrint jasperPrint) throws Exception{
        Log.d("exportPDFV1 : ","exportPDFV1 Started : ");
        OutputStream outputStream= new FileOutputStream(new File(path));
        JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
        System.out.println("Document Exported Successfully!");
        showToast("Pdf Generated successfully");
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
    private class Async extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Async Background : ","Async Background : ");

            try {
                //printPDF();
//                writePDF(pdfTotalListList,domainId+"_pdf_");
                jQueryMethod();
            } catch (JRException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Async Running : ","Async Running : ");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Async Finished : ","Async Finished : ");

        }
        private  void jQueryMethod() throws Exception {
            System.out.println("creating file");
            File newFolder = new File(Environment.getExternalStorageDirectory(), "FieldOut");
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
            File outputFile=new File(newFolder,File.separator+"_query_"+".pdf");
            List<Item> listItems = new ArrayList<>();
            Item iPhone = new Item("iPhone 6s",65000.00);
            Item iPad = new Item("iPad Pro",70000.00);
            Item onePlus = new Item("one plus 3t",29999.00);
            listItems.add(iPad);
            listItems.add(iPhone);
            listItems.add(onePlus);
            JRBeanCollectionDataSource itemsJrBean= new JRBeanCollectionDataSource(listItems);
            Map<String,Object> parameters= new HashMap<>();
            parameters.put("ItemDataSource",itemsJrBean);
            JasperReport jasperReport=JasperCompileManager.compileReport(getAssets().open("invoice.jrxml"));
            JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parameters, new JREmptyDataSource());
            OutputStream outputStream= new FileOutputStream(outputFile);
            JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
            System.out.println("file is generated");


        }

    }
    public static class Item {
        private String name;
        private Double price;
        public Item(){

        }

        public Item(String name, Double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }
}
