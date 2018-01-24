package com.kyros.technologies.fieldout.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kyros.technologies.fieldout.R;

/**
 * Created by kyros on 12-12-2017.
 */

public class ConfigurationFragment extends Fragment {
    private View view;
    private LinearLayout sheduling_linear,job_type_linear,projects_type_linear,job_report_templates_linear,attachments_linear,my_account_linear,regional_settings_linear,taxes_linear,
            options_linear,customer_email_linear,footer_linear,trash_linear,users_linear,teams_id,activity_types_linear,tools_and_resources_linear,parts_services_linear,custom_fields_linear,import_export_linear,reporting_linear,tags_linear,authentication_key_linear,
            integrations_linear,mobile_client_download_linear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_configuration,container,false);
        sheduling_linear=view.findViewById(R.id.sheduling_linear);
        job_type_linear=view.findViewById(R.id.job_type_linear);
        projects_type_linear=view.findViewById(R.id.projects_type_linear);
        job_report_templates_linear=view.findViewById(R.id.job_report_templates_linear);
        attachments_linear=view.findViewById(R.id.attachments_linear);
        my_account_linear=view.findViewById(R.id.my_account_linear);
        regional_settings_linear=view.findViewById(R.id.regional_settings_linear);
        taxes_linear=view.findViewById(R.id.taxes_linear);
        options_linear=view.findViewById(R.id.options_linear);
        customer_email_linear=view.findViewById(R.id.customer_email_linear);
        footer_linear=view.findViewById(R.id.footer_linear);
        trash_linear=view.findViewById(R.id.trash_linear);
        users_linear=view.findViewById(R.id.users_linear);
        teams_id=view.findViewById(R.id.teams_id);
        activity_types_linear=view.findViewById(R.id.activity_types_linear);
        tools_and_resources_linear=view.findViewById(R.id.tools_and_resources_linear);
        parts_services_linear=view.findViewById(R.id.parts_services_linear);
        custom_fields_linear=view.findViewById(R.id.custom_fields_linear);
        import_export_linear=view.findViewById(R.id.import_export_linear);
        reporting_linear=view.findViewById(R.id.reporting_linear);
        tags_linear=view.findViewById(R.id.tags_linear);
        authentication_key_linear=view.findViewById(R.id.authentication_key_linear);
        integrations_linear=view.findViewById(R.id.integrations_linear);
        mobile_client_download_linear=view.findViewById(R.id.mobile_client_download_linear);
        my_account_linear.setOnClickListener(view-> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new AccountInformationFragment()).commit());
        users_linear.setOnClickListener(view-> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new UsersFragment()).commit());
        teams_id.setOnClickListener(view-> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new TeamsFragment()).commit());
        activity_types_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new ActivityTypeFragment()).commit());
        tools_and_resources_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new ToolsAndResourcesFragment()).commit());
        job_type_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new JobTypesFragment()).commit());
        //mobile_client_download_linear.setOnClickListener(view -> startActivity(new Intent(getContext(), RandTetActvitiy.class)));
        sheduling_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new SchedulingWindowsFragment()).commit());
        projects_type_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new ProjectTypeFragment()).commit());
        attachments_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new AttachmentFragment()).commit());
        regional_settings_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new RegionalSettingsFragment()).commit());
        taxes_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new TaxesFragment()).commit());
        options_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new OptionsFragment()).commit());
        customer_email_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new CustomerEmailsFragment()).commit());
        footer_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new FooterFragment()).commit());
        parts_services_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new PartsAndServicesFragment()).commit());
        custom_fields_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new CustomFieldsFragment()).commit());
        tags_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new TagFragment()).commit());
        job_report_templates_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new JobReportTemplateFragment()).commit());
        authentication_key_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new AuthenticationKeyFragment()).commit());
        import_export_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new ImportExportFragment()).commit());
        integrations_linear.setOnClickListener(view1 -> getFragmentManager().beginTransaction().replace(R.id.container_fragments,new IntegrationsFragment()).commit());
        return view;
    }

}
