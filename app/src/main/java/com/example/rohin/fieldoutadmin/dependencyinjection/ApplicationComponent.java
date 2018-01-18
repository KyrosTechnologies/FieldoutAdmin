package com.example.rohin.fieldoutadmin.dependencyinjection;

import com.example.rohin.fieldoutadmin.activity.AccountInformationDetailsActivity;
import com.example.rohin.fieldoutadmin.activity.AddAttachmentActivity;
import com.example.rohin.fieldoutadmin.activity.AddCustomFieldsActivity;
import com.example.rohin.fieldoutadmin.activity.AddJobsTypeActivity;
import com.example.rohin.fieldoutadmin.activity.AddProjectTypeActivity;
import com.example.rohin.fieldoutadmin.activity.AddSchedulingWindowsActivity;
import com.example.rohin.fieldoutadmin.activity.AddTaxesActivity;
import com.example.rohin.fieldoutadmin.activity.AddTeamActivity;
import com.example.rohin.fieldoutadmin.activity.AddUserActivity;
import com.example.rohin.fieldoutadmin.activity.ChangePasswordActivity;
import com.example.rohin.fieldoutadmin.activity.PartsAndServicesActivity;
import com.example.rohin.fieldoutadmin.activity.SignUpActivity;
import com.example.rohin.fieldoutadmin.activity.UpdateTeamActivity;
import com.example.rohin.fieldoutadmin.activity.UpdateUserActivity;
import com.example.rohin.fieldoutadmin.common.RetrofitClient;
import com.example.rohin.fieldoutadmin.fragments.AccountInformationFragment;
import com.example.rohin.fieldoutadmin.fragments.ActivityTypeFragment;
import com.example.rohin.fieldoutadmin.fragments.AttachmentFragment;
import com.example.rohin.fieldoutadmin.fragments.CustomFieldsFragment;
import com.example.rohin.fieldoutadmin.fragments.JobTypesFragment;
import com.example.rohin.fieldoutadmin.fragments.PartsAndServicesFragment;
import com.example.rohin.fieldoutadmin.fragments.ProjectTypeFragment;
import com.example.rohin.fieldoutadmin.fragments.RegionalSettingsFragment;
import com.example.rohin.fieldoutadmin.fragments.SchedulingWindowsFragment;
import com.example.rohin.fieldoutadmin.fragments.TagFragment;
import com.example.rohin.fieldoutadmin.fragments.TaxesFragment;
import com.example.rohin.fieldoutadmin.fragments.TeamsFragment;
import com.example.rohin.fieldoutadmin.fragments.ToolsAndResourcesFragment;
import com.example.rohin.fieldoutadmin.fragments.UsersFragment;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by kyros on 12-12-2017.
 */
@Singleton
@Component(modules = {RetrofitClient.class})
public interface ApplicationComponent {
    void inject(SignUpActivity signUpActivity);
    void injectBussiness(AccountInformationFragment accountInformationFragment);
    void injectUserUpdate(AccountInformationDetailsActivity accountInformationDetailsActivity);
    void injectUsersList(UsersFragment usersFragment);
    void injectAddUsersActivity(AddUserActivity addUserActivity);
    void injectUpdateUsersActivity(UpdateUserActivity updateUserActivity);
    void injectTeamsFragment(TeamsFragment teamsFragment);
    void injectAddTeamActivity(AddTeamActivity addTeamActivity);
    void injectUpdateTeamActivity(UpdateTeamActivity updateTeamActivity);
    void injectActivityTypeFragment(ActivityTypeFragment activityTypeFragment);
    void injectToolsAndResourcesFragment(ToolsAndResourcesFragment toolsAndResourcesFragment);
    void injectJobTypesFragment(JobTypesFragment jobTypesFragment);
    void injectAddJobTypesActivity(AddJobsTypeActivity addJobsTypeActivity);
    void injectSchedulingWindowsFragment(SchedulingWindowsFragment schedulingWindowsFragment);
    void injectAddSchedulingWindowsActivity(AddSchedulingWindowsActivity addSchedulingWindowsActivity);
    void injectProjectTypeFragment(ProjectTypeFragment projectTypeFragment);
    void injectAddProjectTypeActivity(AddProjectTypeActivity addProjectTypeActivity);
    void injectAttachmentFragment(AttachmentFragment attachmentFragment);
    void injectAddAttachmentActivity(AddAttachmentActivity addAttachmentActivity);
    void injectRegionalSettingsFragment(RegionalSettingsFragment regionalSettingsFragment);
    void injectTaxesFragment(TaxesFragment taxesFragment);
    void injectAddTaxesActivity(AddTaxesActivity addTaxesActivity);
    void injectPartsAndServicesFragment(PartsAndServicesFragment partsAndServicesFragment);
    void injectPartsAndServicesActivity(PartsAndServicesActivity partsAndServicesActivity);
    void injectCustomFieldsFragment(CustomFieldsFragment customFieldsFragment);
    void injectAddCustomFieldsActivity(AddCustomFieldsActivity addCustomFieldsActivity);
    void injectTagFragment(TagFragment tagFragment);
    void injectChangePasswordActivity(ChangePasswordActivity changePasswordActivity);
}
