package com.kyros.technologies.fieldout.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.SkilledTradersAdapter;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityAddTeamBinding;
import com.kyros.technologies.fieldout.models.AddTeamResponse;
import com.kyros.technologies.fieldout.models.Manager;
import com.kyros.technologies.fieldout.models.ManagersResponse;
import com.kyros.technologies.fieldout.models.Tag;
import com.kyros.technologies.fieldout.models.TagResponse;
import com.kyros.technologies.fieldout.models.TeamsItem;
import com.kyros.technologies.fieldout.models.Technician;
import com.kyros.technologies.fieldout.models.TechniciansResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.AddTeamViewModel;
import com.kyros.technologies.fieldout.viewmodel.TagFragmentViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class AddTeamActivity extends AppCompatActivity {
    private ActivityAddTeamBinding binding;
    private PreferenceManager store;
    private String TAG= AddTeamActivity.class.getSimpleName();
    private ProgressDialog progressDialog=null;
    private ProgressDialog technicianDialog=null;
    private CompositeSubscription subscription;
    private AlertDialog multipleSelectDialog=null;
    private AlertDialog tagsDialog=null;
    private SkilledTradersAdapter adapter;
    private SkilledTradersAdapter selectedAdapter;
    @Inject
    AddTeamViewModel viewModel;
    private List<String>technicianNameList=new ArrayList<>();
    private List<String>technicianIdList=new ArrayList<>();
    private List<String> selectedTechnicianNameList=new ArrayList<>();
    private List<String>selectedManagersNameList=new ArrayList<>();
    private List<String>managersNameList=new ArrayList<>();
    private List<String>managersIdList=new ArrayList<>();
    private List<String>managersNameListRecycler=new ArrayList<>();
    private List<String>tagsList=new ArrayList<>();
    private SkilledTradersAdapter managerListAdapter;
    private SkilledTradersAdapter technicianListAdapter;
    private List<Tag>tagResponpseList=new ArrayList<>();
    private List<Tag>tagIdList=new ArrayList<>();
    private List<Manager> managers=new ArrayList<>();
    private List<Technician> technicians=new ArrayList<>();
    private SkilledTradersAdapter adapterTag;
    private String technicianNameListString=null;
    private String managersNameListString=null;
    @Inject
    TagFragmentViewModel viewModelTag;
    private AlertDialog multipleSelectTagDialog;
    private List<String>selectedTagArrayList=new ArrayList<>();
    private SkilledTradersAdapter tagNameListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_team);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectAddTeamActivity(this);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getApplicationContext());
      //  binding.tagsAddTeamEditText.setOnClickListener(view -> skilledTradesDialog(new ArrayList<>(),binding.tagsAddTeamEditText));
        binding.saveAddTeamButton.setOnClickListener(view -> validateFields());

    }

    private void validateFields() {

        /* Commented for adding tags name list
        * and enabled the initiate add team api **/

//       tagsList=adapter.getSkilledTradersList();
//        addTagAPI();
        initiateTeamAPI();
    }

    private void addTagAPI() {
        for(String tagValue:tagsList){
            Tag tag=new Tag();
            tag.setIdDomain(store.getIdDomain());
            tag.setName(tagValue);
            callTagAPI(store.getToken(),tag);
        }



    }

    private void callTagAPI(String authKey, Tag tag) {
        subscription.add(viewModel.addTagResponseObservable(authKey,tag,store.getIdDomain())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> {Log.e("Error  : ",TAG+" / / "+throwable.getMessage());dismissDialog();})
                    .subscribe(tagResponse->{
                        tagIdList.add(tagResponse.getTag());
                        Log.d("testStringValue",TAG+" / / "+tagIdList.size());
                        if(tagsList.size()==tagIdList.size()){
                            initiateTeamAPI();
                        }
                    },throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage())));
    }

    private void initiateTeamAPI() {
        String teamName=binding.addTeamNameEditText.getText().toString();
        String description=binding.descriptionEditText.getText().toString();
        List<String>tagFinalList=new ArrayList<>();
        if(adapterTag != null){
            tagFinalList=adapterTag.getSkilledTradersList();
        }
        if(tagFinalList == null){
            tagFinalList=new ArrayList<>();
        }
        List<String>technicianFinalList=new ArrayList<>();
        if(technicianListAdapter != null){
            technicianFinalList=technicianListAdapter.getSkilledTradersList();
        }
        if(technicianFinalList == null){
            technicianFinalList=new ArrayList<>();
        }

        List<String>technicianFinalIdList=new ArrayList<>();
        List<String>tagId=new ArrayList<>();
        for(String tagName:tagFinalList){
            for(Tag tag:tagResponpseList){
                if(tagName.toLowerCase().equals(tag.getName().toLowerCase())){
                    tagId.add(tag.getId());
                }
            }
        }

        for(String technicianName:technicianFinalList){
            for(Technician technician:technicians){
                if(technicianName.toLowerCase().equals(technician.getUsername().toLowerCase())){
                    technicianFinalIdList.add(technician.getId());
                }
            }
        }
        List<String>managerFinalList=new ArrayList<>();
        if(managerListAdapter != null){
            managerFinalList=managerListAdapter.getSkilledTradersList();
        }
        if(managerFinalList == null){
            managerFinalList=new ArrayList<>();
        }
        List<String>managerFinalIdList=new ArrayList<>();
        for(String managerName:managerFinalList){
               for(Manager manager:managers){
                   if(managerName.toLowerCase().equals(manager.getUsername().toLowerCase())){
                       managerFinalIdList.add(manager.getId());
                   }
               }
        }

        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        if( managerFinalIdList.size()!=0 &&  technicianFinalIdList.size()!=0 && domainId!=null && teamName!=null){
            TeamsItem teamsItem=new TeamsItem();
            teamsItem.setIdDomain(domainId);
            teamsItem.setManagers(managerFinalIdList);
            teamsItem.setTechnicians(technicianFinalIdList);
            teamsItem.setTags(tagId);
            teamsItem.setName(teamName);
            teamsItem.setDescriptions(description);
            Timber.tag("Input value : ").d(TAG + " / / " + teamsItem.toString());
            callAddTeamAPI(authKey,teamsItem);

        }else{
            showToast("Please select managers, teams and name!");
        }


    }

    private void callAddTeamAPI(String authKey, TeamsItem teamsItem) {
        showDialog();
        subscription.add(viewModel.addTeamResponseObservable(authKey,teamsItem,store.getIdDomain())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                    .subscribe(addTeamResponse));
    }
    private Subscriber<AddTeamResponse>addTeamResponse=new Subscriber<AddTeamResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
        Log.e("Error : ",TAG+" / / "+e.getMessage());
        dismissDialog();
            showToast(""+e.getMessage());
        }

        @Override
        public void onNext(AddTeamResponse addTeamResponse) {
            validateAddTeamResponse(addTeamResponse);
            dismissDialog();
        }
    };

    private void validateAddTeamResponse(AddTeamResponse addTeamResponse) {
        if(addTeamResponse!=null){
            Log.d("AddTeamResponse : ",TAG+" / / "+addTeamResponse.toString());
            AddTeamActivity.this.finish();
        }else {
            showToast("Add team Response value is null !");
        }
    }

    private void skilledTradesDialog(List<String> skilledName,TextView tView) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddTeamActivity.this);
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.adapter_teams_list,null);
        EditText editText=view.findViewById(R.id.et_skilled);
        editText.setHint("Enter tags");
        Button button=view.findViewById(R.id.button_add_skill);
        RecyclerView recyclerView=view.findViewById(R.id.skilled_traders_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new SkilledTradersAdapter(this,skilledName);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
        button.setOnClickListener(views->{
            String edName=editText.getText().toString();
            if(edName!=null && !edName.isEmpty()){
                adapter.setSkilledTradersName(edName);
                editText.setText("");
            }
        });
        builder.setPositiveButton("OK",(dialog,id)->{
            tagsList=adapter.getSkilledTradersList();
            if(tagsList.size()!=0){
                binding.tagsSelectedRecycler.setVisibility(View.VISIBLE);
                selectedAdapter=new SkilledTradersAdapter(this,tagsList);
                binding.tagsSelectedRecycler.setItemAnimator(new DefaultItemAnimator());
                binding.tagsSelectedRecycler.setLayoutManager(new LinearLayoutManager(this));
                binding.tagsSelectedRecycler.setAdapter(selectedAdapter);
                selectedAdapter.notifyDataSetChanged();
            }

        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissTagsDialog();
        });
        tagsDialog=builder.create();
        tagsDialog.show();

    }
    private  void dismissTagsDialog(){
        if(tagsDialog!=null && tagsDialog.isShowing()){
            tagsDialog.dismiss();
        }
    }
    private void showDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(AddTeamActivity.this);
            progressDialog.setMessage("Loading Please wait..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddTeamActivity.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getApplicationContext());
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        Log.d("Token, domain : ","Token : "+authKey+" / / domain : "+domainId);
        initiateTechniciansAPI(domainId,authKey);
        initiateManagersAPI(domainId,authKey);
        initiateTagsAPI(authKey,domainId);
    }

    private void initiateTagsAPI(String authKey, String domainId) {
        subscription.add(viewModelTag.getTags(authKey,domainId,domainId)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::tagResponse,this::tagError,this::tagCompleted));
    }

    private void tagCompleted() {
    }

    private void tagError(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void tagResponse(TagResponse tagResponse) {
        if(tagResponse!=null){
            Log.d("Tag Response : ",TAG+" / / "+tagResponse);
            tagResponpseList =tagResponse.getTags();
            if(tagResponpseList!=null && tagResponpseList.size()!=0){
                String arrayTags[] = new String[tagResponpseList.size()];
                boolean boolTags[]=new boolean[tagResponpseList.size()];
                for(int k=0; k<tagResponpseList.size();k++){
                    boolTags[k]=false;
                }
                for(int j =0;j<tagResponpseList.size();j++){
                    arrayTags[j] = tagResponpseList.get(j).getName();
                }


                binding.tagsAddTeamEditText.setOnClickListener(view -> multipleSelectTagDialogBox("Select Tags",arrayTags,boolTags));
//                    binding.tagsSelectedRecycler.setVisibility(View.VISIBLE);
//                     List<String>tagNameList=new ArrayList<>();
//                     for(Tag tag:tagResponpseList){
//                         tagNameList.add(tag.getName());
//                     }
//                binding.tagsSelectedRecycler.setLayoutManager(new LinearLayoutManager(this));
//                binding.tagsSelectedRecycler.setItemAnimator(new DefaultItemAnimator());
//                adapterTag=new SkilledTradersAdapter(this,tagNameList);
//                binding.tagsSelectedRecycler.setAdapter(adapterTag);
//                adapterTag.notifyDataSetChanged();

            }else{
                binding.tagsSelectedRecycler.setVisibility(View.GONE);
            }
        }else{
            showToast("response is null!");
        }
    }

    private void initiateManagersAPI(String domainId, String authKey) {
        if(domainId != null && authKey != null)
            subscription.add(viewModel.managersResponseObservable(domainId,authKey,domainId)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                        .subscribe(managersResponse));
    }

    private void validateManagerResponse(ManagersResponse managersResponse) {
        if(managersResponse!=null){
            Log.d("Manager Response : ",TAG+" / / "+managersResponse.toString());
            managers=managersResponse.getManagers();
            for(Manager manager:managers){
                managersNameList.add(manager.getUsername());
            }
             managersNameListString=new Gson().toJson(managersNameList);
            managersNameListRecycler=managersNameList;
            binding.checkBoxAddTeamManagers.setOnClickListener(view -> checkBoxTeamManagers(binding.checkBoxAddTeamManagers.isChecked()));
            binding.managerListTextView.setOnClickListener(view ->{
                String[] arrayManager = new String[managersNameList.size()];
                boolean[] boolManager=new boolean[managersNameList.size()];
                for(int j =0;j<managersNameList.size();j++){
                    arrayManager[j] = managersNameList.get(j);
                }

                multipleSelectDialogBox("Select Manager",arrayManager,boolManager,binding.managerListTextView,"manager");
            });


        }else {
            showToast("ManageResponse is null!");
        }
    }
    private void checkBoxTeamManagers(boolean value){
        if(value){
            if(managersNameListString!=null){
                List<String>nameList=new Gson().fromJson(managersNameListString,new TypeToken<List<String>>(){}.getType());
                if(nameList!=null && nameList.size()!=0){
                    binding.recyclerManagersAdd.setVisibility(View.VISIBLE);
                    binding.recyclerManagersAdd.setLayoutManager(new LinearLayoutManager(this));
                    binding.recyclerManagersAdd.setItemAnimator(new DefaultItemAnimator());
                    managerListAdapter=new SkilledTradersAdapter(this,nameList);
                    binding.recyclerManagersAdd.setAdapter(managerListAdapter);
                    managerListAdapter.notifyDataSetChanged();
                }
            }

        }else{
            binding.recyclerManagersAdd.setVisibility(View.GONE);
        }
    }
    private void checkBoxTeamTechnicians(boolean value){
        if(value){
            if(technicianNameListString!=null){
                List<String>nameList=new Gson().fromJson(technicianNameListString,new TypeToken<List<String>>(){}.getType());
                if(nameList!=null && nameList.size()!=0){
                    binding.recyclerTechniciansAdd.setVisibility(View.VISIBLE);
                    binding.recyclerTechniciansAdd.setLayoutManager(new LinearLayoutManager(this));
                    binding.recyclerTechniciansAdd.setItemAnimator(new DefaultItemAnimator());
                    technicianListAdapter=new SkilledTradersAdapter(this,nameList);
                    binding.recyclerTechniciansAdd.setAdapter(technicianListAdapter);
                    technicianListAdapter.notifyDataSetChanged();
                }
            }

        }else{
            binding.recyclerTechniciansAdd.setVisibility(View.GONE);
        }
    }
    private Subscriber<ManagersResponse>managersResponse=new Subscriber<ManagersResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            showToast(""+e.getMessage());
            Log.e("Error : ",TAG+" / / "+e.getMessage());
        }

        @Override
        public void onNext(ManagersResponse managersResponse) {
            validateManagerResponse(managersResponse);
        }
    };

    private void initiateTechniciansAPI(String domainId, String authKey) {
        if(domainId != null && authKey != null)
            subscription.add(viewModel.techniciansResponseObservable(domainId,authKey,domainId)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                        .subscribe(technicianResponse));
    }
    private Subscriber<TechniciansResponse>technicianResponse=new Subscriber<TechniciansResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            showToast(""+e.getMessage());
            Log.e("Error : ",TAG+" / / "+e.getMessage());
        }

        @Override
        public void onNext(TechniciansResponse techniciansResponse) {
            validateTechnicianResponse(techniciansResponse);
        }
    };

    private void validateTechnicianResponse(TechniciansResponse techniciansResponse) {
        if(techniciansResponse!=null){
                Log.d("Technician Response  : ",TAG+" / / "+techniciansResponse.toString());
             technicians=techniciansResponse.getTechnicians();
            for(Technician technician: technicians){
                technicianNameList.add(technician.getUsername());
            }
             technicianNameListString=new Gson().toJson(technicianNameList);
            binding.technicianListTextView.setOnClickListener(view -> {
                String arrayTechnician[] = new String[technicianNameList.size()];
                boolean boolTechnician[]=new boolean[technicianNameList.size()];
                for(int j =0;j<technicianNameList.size();j++){
                    arrayTechnician[j] = technicianNameList.get(j);
                }
                multipleSelectDialogBox("Select Technician",arrayTechnician,boolTechnician,binding.technicianListTextView,"technician");
            });
            binding.checkBoxAddTeam.setOnClickListener(view -> checkBoxTeamTechnicians(binding.checkBoxAddTeam.isChecked()));


        }else {
            showToast("Technician Response is null!");
        }
    }


    private void dismissDialogTechnician(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void dismissDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissDialog();
        dismissMultipleSelectDialogBox();
        dismissTagsDialog();
        dismissMultipleSelectTagDialogBox();

    }

    private void dismissMultipleSelectTagDialogBox() {
        if(multipleSelectTagDialog != null && multipleSelectTagDialog.isShowing()){
            multipleSelectTagDialog.dismiss();
        }
    }

    private void multipleSelectTagDialogBox(String title,String[] values,boolean[] checkedItems){
        selectedTagArrayList.clear();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMultiChoiceItems(values,checkedItems,(dialog,which,isChecked)-> {
            if(isChecked){
                selectedTagArrayList.add(values[which]);
            }
        });
        builder.setPositiveButton("OK",(dialog,which)->{
           binding.tagsSelectedRecycler.setVisibility(View.VISIBLE);
           binding.tagsSelectedRecycler.setLayoutManager(new LinearLayoutManager(this));
           binding.tagsSelectedRecycler.setItemAnimator(new DefaultItemAnimator());
            tagNameListAdapter=new SkilledTradersAdapter(this,new HashSet<>(selectedTagArrayList));
            binding.tagsSelectedRecycler.setAdapter(tagNameListAdapter);
            tagNameListAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel",(dialog,which)-> dialog.dismiss());
        multipleSelectTagDialog=builder.create();
        multipleSelectTagDialog.show();
    }
    private void multipleSelectDialogBox(String title,String[] values,boolean[] checkedItems,TextView view,String whichone){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTeamActivity.this);
        builder.setTitle(title);
        builder.setMultiChoiceItems(values, checkedItems, (dialog, which, isChecked) -> {

            if(isChecked){
                if(whichone.equals("technician")){
                            selectedTechnicianNameList.add(values[which]);
                }else if(whichone.equals("manager")){
                    selectedManagersNameList.add(values[which]);
                }
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            if(whichone.equals("technician")){
                binding.recyclerTechniciansAdd.setVisibility(View.VISIBLE);
                binding.recyclerTechniciansAdd.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerTechniciansAdd.setItemAnimator(new DefaultItemAnimator());
                Set<String> hashSetNameList = new HashSet<>(selectedTechnicianNameList);
                technicianListAdapter=new SkilledTradersAdapter(this,hashSetNameList);
                binding.recyclerTechniciansAdd.setAdapter(technicianListAdapter);
                technicianListAdapter.notifyDataSetChanged();


            }else if(whichone.equals("manager")){
                binding.recyclerManagersAdd.setVisibility(View.VISIBLE);
                binding.recyclerManagersAdd.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerManagersAdd.setItemAnimator(new DefaultItemAnimator());
                Set<String> hashSetNameList = new HashSet<>(selectedManagersNameList);
                managerListAdapter=new SkilledTradersAdapter(this,hashSetNameList);
                binding.recyclerManagersAdd.setAdapter(managerListAdapter);
                managerListAdapter.notifyDataSetChanged();
            }
            dismissMultipleSelectDialogBox();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dismissMultipleSelectDialogBox());

        multipleSelectDialog = builder.create();
        multipleSelectDialog.show();


    }
    private void dismissMultipleSelectDialogBox(){
        if(multipleSelectDialog!=null && multipleSelectDialog.isShowing()){
            multipleSelectDialog.dismiss();
        }
    }

}
