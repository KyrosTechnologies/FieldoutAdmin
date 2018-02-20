package com.kyros.technologies.fieldout.dependencyinjection;

import com.kyros.technologies.fieldout.PDF_CSV_Activity;
import com.kyros.technologies.fieldout.activity.AccountInformationDetailsActivity;
import com.kyros.technologies.fieldout.activity.AddAttachmentActivity;
import com.kyros.technologies.fieldout.activity.AddCustomFieldsActivity;
import com.kyros.technologies.fieldout.activity.AddCustomer;
import com.kyros.technologies.fieldout.activity.AddEquipment;
import com.kyros.technologies.fieldout.activity.AddJobsTypeActivity;
import com.kyros.technologies.fieldout.activity.AddProjectTypeActivity;
import com.kyros.technologies.fieldout.activity.AddSchedulingWindowsActivity;
import com.kyros.technologies.fieldout.activity.AddSite;
import com.kyros.technologies.fieldout.activity.AddTaxesActivity;
import com.kyros.technologies.fieldout.activity.AddTeamActivity;
import com.kyros.technologies.fieldout.activity.AddUserActivity;
import com.kyros.technologies.fieldout.activity.ChangePasswordActivity;
import com.kyros.technologies.fieldout.activity.CustomerDetails;
import com.kyros.technologies.fieldout.activity.CustomerUpdateDelete;
import com.kyros.technologies.fieldout.activity.EquipmentUpdateDelete;
import com.kyros.technologies.fieldout.activity.JobsDayDetails;
import com.kyros.technologies.fieldout.activity.JobsMonthDetails;
import com.kyros.technologies.fieldout.activity.JobsWeekDetails;
import com.kyros.technologies.fieldout.activity.PartsAndServicesActivity;
import com.kyros.technologies.fieldout.activity.ScheduleDayDetails;
import com.kyros.technologies.fieldout.activity.ScheduleMonthDetails;
import com.kyros.technologies.fieldout.activity.ScheduleWeekDetails;
import com.kyros.technologies.fieldout.activity.SignUpActivity;
import com.kyros.technologies.fieldout.activity.SiteDetails;
import com.kyros.technologies.fieldout.activity.SitesUpdateDelete;
import com.kyros.technologies.fieldout.activity.UpdateTeamActivity;
import com.kyros.technologies.fieldout.activity.UpdateUserActivity;
import com.kyros.technologies.fieldout.common.RetrofitClient;
import com.kyros.technologies.fieldout.fragments.AccountInformationFragment;
import com.kyros.technologies.fieldout.fragments.ActivityTypeFragment;
import com.kyros.technologies.fieldout.fragments.AttachmentFragment;
import com.kyros.technologies.fieldout.fragments.CustomFieldsFragment;
import com.kyros.technologies.fieldout.fragments.JobTypesFragment;
import com.kyros.technologies.fieldout.fragments.PartsAndServicesFragment;
import com.kyros.technologies.fieldout.fragments.ProjectTypeFragment;
import com.kyros.technologies.fieldout.fragments.RegionalSettingsFragment;
import com.kyros.technologies.fieldout.fragments.SchedulingWindowsFragment;
import com.kyros.technologies.fieldout.fragments.TagFragment;
import com.kyros.technologies.fieldout.fragments.TaxesFragment;
import com.kyros.technologies.fieldout.fragments.TeamsFragment;
import com.kyros.technologies.fieldout.fragments.ToolsAndResourcesFragment;
import com.kyros.technologies.fieldout.fragments.UsersFragment;

import javax.inject.Singleton;

import dagger.Component;

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
    void injectAddCustomer(AddCustomer addCustomer);
    void injectCustomerUpdateDelete(CustomerUpdateDelete customerUpdateDelete);
    void injectAddSite(AddSite addSite);
    void injectSitesUpdateDelete(SitesUpdateDelete sitesUpdateDelete);
    void injectAddEquipment(AddEquipment addEquipment);
    void injectEquipmentUpdateDelete(EquipmentUpdateDelete equipmentUpdateDelete);
    void injectPDF_CSV_Activity(PDF_CSV_Activity pdf_csv_activity);
    void injectSchedulemonthDetails(ScheduleMonthDetails scheduleMonthDetails);
    void injectJobsmonthDetails(JobsMonthDetails jobsMonthDetails);
    void injectScheduledayDetails(ScheduleDayDetails scheduleDayDetails);
    void injectJobsdayDetails(JobsDayDetails jobsDayDetails);
    void injectScheduleweekDetails(ScheduleWeekDetails scheduleWeekDetails);
    void injectJobsweekDetails(JobsWeekDetails jobsWeekDetails);
    void injectCustomerDetails(CustomerDetails customerDetails);
    void injectSiteDetails(SiteDetails siteDetails);
}
