package com.kyros.technologies.fieldout.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Thirunavukkarasu on 28-07-2016.
 */
public class PreferenceManager {
    private static PreferenceManager store;
    private final SharedPreferences sp;

    private PreferenceManager(Context context){
        String filename = "login_credentials";
        sp=context.getApplicationContext().getSharedPreferences(filename,0);
    }
    public static PreferenceManager getInstance(Context context){
        if(store==null){
            Log.v("PreferenceManager","NEW STORE");
            store=new PreferenceManager(context);
        }
        return store;
    }
    public String getImage(){
        return sp.getString("Picture",null);
    }
    public void putImage(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Picture", value);
        editor.commit();
    }

    public void clear(){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.clear();
        editor.commit();
    }

    public String getUsername(){
        return sp.getString("Username",null);
    }
    public void putUsername(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Username", value);
        editor.commit();
    }

    public String getPassword(){
        return sp.getString("Password",null);
    }
    public void putPassword(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Password", value);
        editor.commit();
    }

    public String getUserid(){
        return sp.getString("Userid",null);
    }
    public void putUserid(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Userid", value);
        editor.commit();
    }

    public String getJobOid(){
        return sp.getString("JobOid",null);
    }
    public void putJobOid(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("JobOid", value);
        editor.commit();
    }

    public String getCreatedat(){
        return sp.getString("Createdat",null);
    }
    public void putCreatedat(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Createdat", value);
        editor.commit();
    }

    public String getFCMToken(){
        return sp.getString("FCMToken",null);
    }
    public void putFCMToken(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("FCMToken", value);
        editor.commit();
    }

    public String getHash(){
        return sp.getString("Hash",null);
    }
    public void putHash(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Hash", value);
        editor.commit();
    }


    public String getToken(){
        return sp.getString("Token",null);
    }
    public void putToken(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Token", value);
        editor.commit();
    }
    public String getJobId(){
        return sp.getString("JobId",null);
    }
    public void putJobId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("JobId", value);
        editor.commit();
    }

    public String getStatus(){
        return sp.getString("Status",null);
    }
    public void putStatus(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Status", value);
        editor.commit();
    }

    public String getPriority(){
        return sp.getString("Priority",null);
    }
    public void putPriority(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Priority", value);
        editor.commit();
    }

    public String getProfile(){
        return sp.getString("Profile",null);
    }
    public void putProfile(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Profile", value);
        editor.commit();
    }

    public String getmyId(){
        return sp.getString("MyId",null);
    }
    public void putmyId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("MyId", value);
        editor.commit();
    }

    public String getdescription(){
        return sp.getString("Description",null);
    }
    public void putdescription(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Description", value);
        editor.commit();
    }

    public String getscheduledBeginDate(){
        return sp.getString("scheduledBeginDate",null);
    }
    public void putscheduledBeginDate(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("scheduledBeginDate", value);
        editor.commit();
    }

    public String getscheduledEndDate(){
        return sp.getString("scheduledEndDate",null);
    }
    public void putscheduledEndDate(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("scheduledEndDate", value);
        editor.commit();
    }

    public String getDtEnd(){
        return sp.getString("DtEnd",null);
    }
    public void putDtEnd(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("DtEnd", value);
        editor.commit();
    }

    public String getDtStart(){
        return sp.getString("DtStart",null);
    }
    public void putDtStart(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("DtStart", value);
        editor.commit();
    }

    public String getIdUser(){
        return sp.getString("IdUser",null);
    }
    public void putIdUser(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("IdUser", value);
        editor.commit();
    }

    public String getnmActivity(){
        return sp.getString("NmActivity",null);
    }
    public void putnmActivity(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("NmActivity", value);
        editor.commit();
    }

    public String getNoteActivity(){
        return sp.getString("NoteActivity",null);
    }
    public void putNoteActivity(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("NoteActivity", value);
        editor.commit();
    }

    public String getMessageBody(){
        return sp.getString("MessageBody",null);
    }
    public void putMessageBody(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("MessageBody", value);
        editor.commit();
    }

    public String getMessageTitle(){
        return sp.getString("MessageTitle",null);
    }
    public void putMessageTitle(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("MessageTitle", value);
        editor.commit();
    }

    public String getCustomerName(){
        return sp.getString("CustomerName",null);
    }
    public void putCustomerName(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("CustomerName", value);
        editor.commit();
    }

    public String getCustomerAddress(){
        return sp.getString("CustomerAddress",null);
    }
    public void putCustomerAddress(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("CustomerAddress", value);
        editor.commit();
    }

    public String getEquipmentName(){
        return sp.getString("EquipmentName",null);
    }
    public void putEquipmentName(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("EquipmentName", value);
        editor.commit();
    }

    public String getSiteName(){
        return sp.getString("SiteName",null);
    }
    public void putSiteName(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("SiteName", value);
        editor.commit();
    }

    public String getContactNumber(){
        return sp.getString("ContactNumber",null);
    }
    public void putContactNumber(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ContactNumber", value);
        editor.commit();
    }

    public String getContactName(){
        return sp.getString("ContactName",null);
    }
    public void putContactName(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ContactName", value);
        editor.commit();
    }

    public String getContactEmail(){
        return sp.getString("ContactEmail",null);
    }
    public void putContactEmail(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ContactEmail", value);
        editor.commit();
    }

    public String getComplementAddress(){
        return sp.getString("ComplementAddress",null);
    }
    public void putComplementAddress(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ComplementAddress", value);
        editor.commit();
    }

    public String getContactFirstName(){
        return sp.getString("ContactFirstName",null);
    }
    public void putContactFirstName(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ContactFirstName", value);
        editor.commit();
    }

    public String getContactPhone(){
        return sp.getString("ContactPhone",null);
    }
    public void putContactPhone(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ContactPhone", value);
        editor.commit();
    }

    public String getCustomFieldValues(){
        return sp.getString("CustomFieldValues",null);
    }
    public void putCustomFieldValues(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("CustomFieldValues", value);
        editor.commit();
    }

    public String getGlobalAddress(){
        return sp.getString("GlobalAddress",null);
    }
    public void putGlobalAddress(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("GlobalAddress", value);
        editor.commit();
    }

    public String getIdCreator(){
        return sp.getString("IdCreator",null);
    }
    public void putIdCreator(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("GlobalAddress", value);
        editor.commit();
    }

    public String getIdCustomer(){
        return sp.getString("IdCustomer",null);
    }
    public void putIdCustomer(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("IdCustomer", value);
        editor.commit();
    }

    public String getIdDomain(){
        return sp.getString("IdDomain",null);
    }
    public void putIdDomain(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("IdDomain", value);
        editor.commit();
    }

    public String getCompanyName(){
        return sp.getString("CompanyName",null);
    }
    public void putCompanyName(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("CompanyName", value);
        editor.commit();
    }

    public String getIdEquipment(){
        return sp.getString("IdEquipment",null);
    }
    public void putIdEquipment(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("IdEquipment", value);
        editor.commit();
    }

    public String getIdJobType(){
        return sp.getString("IdJobType",null);
    }
    public void putIdJobType(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("IdJobType", value);
        editor.commit();
    }

    public int getIdReport(){
        return sp.getInt("IdReport",0);
    }
    public void putIdReport(int value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putInt("IdReport", value);
        editor.commit();
    }

    public String getIdSite(){
        return sp.getString("IdSite",null);
    }
    public void putIdSite(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("IdSite", value);
        editor.commit();
    }

    public String getNum(){
        return sp.getString("Num",null);
    }
    public void putNum(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Num", value);
        editor.commit();
    }

    public String getPublicLink(){
        return sp.getString("PublicLink",null);
    }
    public void putPublicLink(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("PublicLink", value);
        editor.commit();
    }

    public String getSchedullingPreferredDate(){
        return sp.getString("SchedullingPreferredDate",null);
    }
    public void putSchedullingPreferredDate(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("SchedullingPreferredDate", value);
        editor.commit();
    }

    public String getSchedullingPreferredTeam(){
        return sp.getString("SchedullingPreferredTeam",null);
    }
    public void putSchedullingPreferredTeam(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("SchedullingPreferredTeam", value);
        editor.commit();
    }

    public String getTags(){
        return sp.getString("Tags",null);
    }
    public void putTags(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Tags", value);
        editor.commit();
    }

    public String getWindow(){
        return sp.getString("Window",null);
    }
    public void putWindow(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Window", value);
        editor.commit();
    }

    public String getJobTypeName(){
        return sp.getString("JobTypeName",null);
    }
    public void putJobTypeName(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("JobTypeName", value);
        editor.commit();
    }

    public String getJobIdType(){
        return sp.getString("JobIdType",null);
    }
    public void putJobIdType(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("JobIdType", value);
        editor.commit();
    }

    public String getAmount(){
        return sp.getString("Amount",null);
    }
    public void putAmount(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Amount", value);
        editor.commit();
    }

    public String getQuantity(){
        return sp.getString("Quantity",null);
    }
    public void putQuantity(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Quantity", value);
        editor.commit();
    }

    public String getInvoiceId(){
        return sp.getString("InvoiceId",null);
    }
    public void putInvoiceId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("InvoiceId", value);
        editor.commit();
    }

    public String getQuotationId(){
        return sp.getString("QuotationId",null);
    }
    public void putQuotationId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("QuotationId", value);
        editor.commit();
    }

    public String getProjectId(){
        return sp.getString("ProjectId",null);
    }
    public void putProjectId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ProjectId", value);
        editor.commit();
    }

    public String getTotal(){
        return sp.getString("Total",null);
    }
    public void putTotal(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Total", value);
        editor.commit();
    }

    public String getUnitPrice(){
        return sp.getString("UnitPrice",null);
    }
    public void putUnitPrice(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("UnitPrice", value);
        editor.commit();
    }

    public String getItem(){
        return sp.getString("Item",null);
    }
    public void putItem(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Item", value);
        editor.commit();
    }

    public String getDiscount(){
        return sp.getString("Discount",null);
    }
    public void putDiscount(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Discount", value);
        editor.commit();
    }

    public String getTax(){
        return sp.getString("Tax",null);
    }
    public void putTax(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Tax", value);
        editor.commit();
    }

    public String getDateStart(){
        return sp.getString("DateStart",null);
    }
    public void putDateStart(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("DateStart", value);
        editor.commit();
    }

    public String getCategoryName(){
        return sp.getString("CategoryName",null);
    }
    public void putCategoryName(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("CategoryName", value);
        editor.commit();
    }

    public String getPartName(){
        return sp.getString("PartName",null);
    }
    public void putPartName(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("PartName", value);
        editor.commit();
    }

    public String getReference(){
        return sp.getString("Reference",null);
    }
    public void putReference(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Reference", value);
        editor.commit();
    }

    public String getPrice(){
        return sp.getString("Price",null);
    }
    public void putPrice(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Price", value);
        editor.commit();
    }

    public String getDuration(){
        return sp.getString("Duration",null);
    }
    public void putDuration(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Duration", value);
        editor.commit();
    }

    public String getPausedDate(){
        return sp.getString("PausedDate",null);
    }
    public void putPausedDate(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("PausedDate", value);
        editor.commit();
    }

    public String getResumedDate(){
        return sp.getString("ResumedDate",null);
    }
    public void putResumedDate(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ResumedDate", value);
        editor.commit();
    }

    public String getActivitiesCount(){
        return sp.getString("ActivitiesCount",null);
    }
    public void putActivitiesCount(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ActivitiesCount", value);
        editor.commit();
    }

    public String getAllJobsCount(){
        return sp.getString("AllJobsCount",null);
    }
    public void putAllJobsCount(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("AllJobsCount", value);
        editor.commit();
    }

    public String getLateJobsCount(){
        return sp.getString("LateJobsCount",null);
    }
    public void putLateJobsCount(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("LateJobsCount", value);
        editor.commit();
    }

    public String getMessagesCount(){
        return sp.getString("MessagesCount",null);
    }
    public void putMessagesCount(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("MessagesCount", value);
        editor.commit();
    }

    public String getTodayJobsCount(){
        return sp.getString("TodayJobsCount",null);
    }
    public void putTodayJobsCount(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("TodayJobsCount", value);
        editor.commit();
    }

    public String getUpComingJobsCount(){
        return sp.getString("UpComingJobsCount",null);
    }
    public void putUpComingJobsCount(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("UpComingJobsCount", value);
        editor.commit();
    }

    public String getResourceId(){
        return sp.getString("ResourceId",null);
    }
    public void putResourceId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ResourceId", value);
        editor.commit();
    }

    public String getTagId(){
        return sp.getString("TagId",null);
    }
    public void putTagId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("TagId", value);
        editor.commit();
    }

    public String getSiteId(){
        return sp.getString("SiteId",null);
    }
    public void putSiteId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("SiteId", value);
        editor.commit();
    }

    public String getStockPartsId(){
        return sp.getString("StockPartsId",null);
    }
    public void putStockPartsId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("StockPartsId", value);
        editor.commit();
    }

    public String getCustomerId(){
        return sp.getString("CustomerId",null);
    }
    public void putCustomerId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("CustomerId", value);
        editor.commit();
    }

    public String getEquipmentId(){
        return sp.getString("EquipmentId",null);
    }
    public void putEquipmentId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("EquipmentId", value);
        editor.commit();
    }

    public String getTeamId(){
        return sp.getString("TeamId",null);
    }
    public void putTeamId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("TeamId", value);
        editor.commit();
    }

    public String getPhone(){
        return sp.getString("Phone",null);
    }
    public void putPhone(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("Phone", value);
        editor.commit();
    }

    public String getEmail() {
        return sp.getString("Email", null);
    }

    public boolean putEmail(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("Email", value);
      return   editor.commit();
    }

    public String getFirstName() {
        return sp.getString("FirstName", null);
    }

    public void putFirstName(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("FirstName", value);
        editor.commit();
    }

    public String getLastName() {
        return sp.getString("LastName", null);
    }

    public void putLastName(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("LastName", value);
        editor.commit();
    }

    public String getLanguage() {
        return sp.getString("Language", null);
    }

    public void putLanguage(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("Language", value);
        editor.commit();
    }

    public String getSkilledTrades() {
        return sp.getString("SkilledTradesModel", null);
    }

    public void putSkilledTrades(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("SkilledTradesModel", value);
        editor.commit();
    }
    public String getDomainName() {
        return sp.getString("DomainName", null);
    }

    public void putDomainName(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("DomainName", value);
        editor.commit();
    }

    public String getTechnicianId() {
        return sp.getString("TechnicianId", null);
    }

    public void putTechnicianId(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("TechnicianId", value);
        editor.commit();
    }

    public String getMessageId() {
        return sp.getString("MessageId", null);
    }

    public void putMessageId(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("MessageId", value);
        editor.commit();
    }

    public String getTaxId() {
        return sp.getString("TaxId", null);
    }

    public void putTaxId(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("TaxId", value);
        editor.commit();
    }

    public String getSchedulingId() {
        return sp.getString("SchedulingId", null);
    }

    public void putSchedulingId(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("SchedulingId", value);
        editor.commit();
    }

    public String getActivityTypeId() {
        return sp.getString("ActivityTypeId", null);
    }

    public void putActivityTypeId(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("ActivityTypeId", value);
        editor.commit();
    }

    public String getProjectTypeId() {
        return sp.getString("ProjectTypeId", null);
    }

    public void putProjectTypeId(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("ProjectTypeId", value);
        editor.commit();
    }

    public String getActivitiesId() {
        return sp.getString("ActivitiesId", null);
    }

    public void putActivitiesId(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("ActivitiesId", value);
        editor.commit();
    }

    public String getTechSpinner() {
        return sp.getString("TechSpinner", null);
    }

    public void putTechSpinner(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("TechSpinner", value);
        editor.commit();
    }

    public String getActivityTypeSpinner() {
        return sp.getString("ActivityTypeSpinner", null);
    }

    public void putActivityTypeSpinner(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("ActivityTypeSpinner", value);
        editor.commit();
    }

    public String getResourceid() {
        return sp.getString("Resourceid", null);
    }

    public void putResourceid(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("Resourceid", value);
        editor.commit();
    }

    public String getResourceNameSpinner() {
        return sp.getString("ResourceName", null);
    }

    public void putResourceNameSpinner(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("ResourceName", value);
        editor.commit();
    }

    public String getIdToolsResources() {
        return sp.getString("IdToolsResources", null);
    }

    public void putIdToolsResources(String value) {
        SharedPreferences.Editor editor;
        editor = sp.edit();
        editor.putString("IdToolsResources", value);
        editor.commit();
    }

}