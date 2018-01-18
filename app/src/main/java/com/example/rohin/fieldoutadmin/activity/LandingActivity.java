package com.example.rohin.fieldoutadmin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.fragments.ActivitiesFragment;
import com.example.rohin.fieldoutadmin.fragments.ConfigurationFragment;
import com.example.rohin.fieldoutadmin.fragments.CustomerFragment;
import com.example.rohin.fieldoutadmin.fragments.InvoicesFragment;
import com.example.rohin.fieldoutadmin.fragments.JobsDayFragment;
import com.example.rohin.fieldoutadmin.fragments.JobsMonthFragment;
import com.example.rohin.fieldoutadmin.fragments.JobsToScheduleFragment;
import com.example.rohin.fieldoutadmin.fragments.JobsWeekFragment;
import com.example.rohin.fieldoutadmin.fragments.LateJobsFragment;
import com.example.rohin.fieldoutadmin.fragments.PartsAndServicesFragment;
import com.example.rohin.fieldoutadmin.fragments.ProjectsFragment;
import com.example.rohin.fieldoutadmin.fragments.QuotationsFragment;
import com.example.rohin.fieldoutadmin.fragments.ScheduleDayFragment;
import com.example.rohin.fieldoutadmin.fragments.EquipmentFragment;
import com.example.rohin.fieldoutadmin.fragments.ScheduleMonthFragment;
import com.example.rohin.fieldoutadmin.fragments.ResourcesFragment;
import com.example.rohin.fieldoutadmin.fragments.SiteFragment;
import com.example.rohin.fieldoutadmin.fragments.ScheduleWeekFragment;
import com.example.rohin.fieldoutadmin.fragments.UpcomingJobsFragment;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.sharedpreference.SessionManager;

/**
 * Created by Rohin on 12-12-2017.
 */

public class LandingActivity extends AppCompatActivity {

    private PreferenceManager store;
    private SessionManager session;
    private LinearLayout linear_name_down,name_visible_linear,linear_team,linear_schedule_down,map_linear,
            linear_customers_down,linear_projects,linear_jobs_down,linear_invoicing_down,linear_reports_down,
            schedule_visible_linear,cutomer_visible_linear,jobs_visible_linear,invoice_visible_linear,reports_visible_linear;
    private ImageView settings_fragment;
    private TextView week_text,month_text,day_text,activity_text,resources_text,list_text,site_text,equipment_text,jobs_month_text,
            jobs_day_text,jobs_week_text,jobs_late_text,upcoming_jobs_text,to_schedule_job_text,invoices_text,quotations_text,
            company_name,project_text,parts_text,first_last_name;
    private Boolean namearrowclicked=true;
    private Boolean techarrowclicked=true;
    private Boolean schedulearrowclicked=true;
    private Boolean customerarrowclicked=true;
    private Boolean jobsarrowclicked=true;
    private Boolean invoicearrowclicked=true;
    private Boolean reportsarrowclicked=true;
    private RecyclerView recycler_tech;
    private String companyname=null;
    private String firstname=null;
    private String lastname=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_acticity);

        session = new SessionManager(getApplicationContext());
        store=PreferenceManager.getInstance(getApplicationContext());
        session.checkLogin();
        linear_name_down=findViewById(R.id.linear_name_down);
        name_visible_linear=findViewById(R.id.name_visible_linear);
        linear_team=findViewById(R.id.linear_team);
        recycler_tech=findViewById(R.id.recycler_tech);
        linear_schedule_down=findViewById(R.id.linear_schedule_down);
        map_linear=findViewById(R.id.map_linear);
        linear_customers_down=findViewById(R.id.linear_customers_down);
        linear_projects=findViewById(R.id.linear_projects);
        linear_jobs_down=findViewById(R.id.linear_jobs_down);
        linear_invoicing_down=findViewById(R.id.linear_invoicing_down);
        linear_reports_down=findViewById(R.id.linear_reports_down);
        schedule_visible_linear=findViewById(R.id.schedule_visible_linear);
        cutomer_visible_linear=findViewById(R.id.cutomer_visible_linear);
        jobs_visible_linear=findViewById(R.id.jobs_visible_linear);
        invoice_visible_linear=findViewById(R.id.invoice_visible_linear);
        reports_visible_linear=findViewById(R.id.reports_visible_linear);
        settings_fragment=findViewById(R.id.settings_fragment);
        week_text=findViewById(R.id.week_text);
        month_text=findViewById(R.id.month_text);
        day_text=findViewById(R.id.day_text);
        activity_text=findViewById(R.id.activity_text);
        resources_text=findViewById(R.id.resources_text);
        list_text=findViewById(R.id.list_text);
        site_text=findViewById(R.id.site_text);
        equipment_text=findViewById(R.id.equipment_text);
        jobs_month_text=findViewById(R.id.jobs_month_text);
        jobs_day_text=findViewById(R.id.jobs_day_text);
        jobs_week_text=findViewById(R.id.jobs_week_text);
        jobs_late_text=findViewById(R.id.jobs_late_text);
        upcoming_jobs_text=findViewById(R.id.upcoming_jobs_text);
        to_schedule_job_text=findViewById(R.id.to_schedule_job_text);
        invoices_text=findViewById(R.id.invoices_text);
        quotations_text=findViewById(R.id.quotations_text);
        company_name=findViewById(R.id.company_name);
        project_text=findViewById(R.id.project_text);
        parts_text=findViewById(R.id.parts_text);
        first_last_name=findViewById(R.id.first_last_name);
        companyname=store.getCompanyName();
        firstname=store.getFirstName();
        lastname=store.getLastName();
        ScheduleMonthFragment i = new ScheduleMonthFragment();
        android.support.v4.app.FragmentTransaction j =
                getSupportFragmentManager().beginTransaction();
        j.replace(R.id.container_fragments, i);
        j.commit();

        if (companyname!=null){
            company_name.setText(companyname);
        }
        if (firstname!=null&&lastname!=null){
            first_last_name.setText(firstname+" "+lastname);
        }

        linear_name_down.setOnClickListener(view -> {
            recycler_tech.setVisibility(View.GONE);
            schedule_visible_linear.setVisibility(View.GONE);
            cutomer_visible_linear.setVisibility(View.GONE);
            jobs_visible_linear.setVisibility(View.GONE);
            invoice_visible_linear.setVisibility(View.GONE);
            reports_visible_linear.setVisibility(View.GONE);
            if (namearrowclicked){
                namearrowclicked=false;
                techarrowclicked=true;
                schedulearrowclicked=true;
                customerarrowclicked=true;
                jobsarrowclicked=true;
                invoicearrowclicked=true;
                reportsarrowclicked=true;
                name_visible_linear.setVisibility(View.VISIBLE);
            }else {
                namearrowclicked=true;
                name_visible_linear.setVisibility(View.GONE);
            }
        });

        linear_team.setOnClickListener(view -> {
            name_visible_linear.setVisibility(View.GONE);
            schedule_visible_linear.setVisibility(View.GONE);
            cutomer_visible_linear.setVisibility(View.GONE);
            jobs_visible_linear.setVisibility(View.GONE);
            invoice_visible_linear.setVisibility(View.GONE);
            reports_visible_linear.setVisibility(View.GONE);
            if (techarrowclicked){
                techarrowclicked=false;
                namearrowclicked=true;
                schedulearrowclicked=true;
                customerarrowclicked=true;
                jobsarrowclicked=true;
                invoicearrowclicked=true;
                reportsarrowclicked=true;
                recycler_tech.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Recyclerview Visible",Toast.LENGTH_SHORT).show();
            }else {
                techarrowclicked=true;
                recycler_tech.setVisibility(View.GONE);
            }
        });

        linear_schedule_down.setOnClickListener(view -> {
            name_visible_linear.setVisibility(View.GONE);
            recycler_tech.setVisibility(View.GONE);
            cutomer_visible_linear.setVisibility(View.GONE);
            jobs_visible_linear.setVisibility(View.GONE);
            invoice_visible_linear.setVisibility(View.GONE);
            reports_visible_linear.setVisibility(View.GONE);
            linear_schedule_down.setBackgroundColor(getResources().getColor(R.color.button));
            map_linear.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_customers_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_projects.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_jobs_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_invoicing_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_reports_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            if (schedulearrowclicked){
                schedulearrowclicked=false;
                namearrowclicked=true;
                techarrowclicked=true;
                customerarrowclicked=true;
                jobsarrowclicked=true;
                invoicearrowclicked=true;
                reportsarrowclicked=true;
                schedule_visible_linear.setVisibility(View.VISIBLE);
            }else {
                schedulearrowclicked=true;
                schedule_visible_linear.setVisibility(View.GONE);
            }
        });

        map_linear.setOnClickListener(view -> {
            linear_schedule_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            map_linear.setBackgroundColor(getResources().getColor(R.color.button));
            linear_customers_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_projects.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_jobs_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_invoicing_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_reports_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
        });

        linear_customers_down.setOnClickListener(view -> {
            name_visible_linear.setVisibility(View.GONE);
            recycler_tech.setVisibility(View.GONE);
            schedule_visible_linear.setVisibility(View.GONE);
            jobs_visible_linear.setVisibility(View.GONE);
            invoice_visible_linear.setVisibility(View.GONE);
            reports_visible_linear.setVisibility(View.GONE);
            linear_schedule_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            map_linear.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_customers_down.setBackgroundColor(getResources().getColor(R.color.button));
            linear_projects.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_jobs_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_invoicing_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_reports_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            if (customerarrowclicked){
                customerarrowclicked=false;
                schedulearrowclicked=true;
                namearrowclicked=true;
                techarrowclicked=true;
                jobsarrowclicked=true;
                invoicearrowclicked=true;
                reportsarrowclicked=true;
                cutomer_visible_linear.setVisibility(View.VISIBLE);
            }else {
                customerarrowclicked=true;
                cutomer_visible_linear.setVisibility(View.GONE);
            }
        });

        linear_projects.setOnClickListener(view -> {
            linear_schedule_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            map_linear.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_customers_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_projects.setBackgroundColor(getResources().getColor(R.color.button));
            linear_jobs_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_invoicing_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_reports_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
        });

        linear_jobs_down.setOnClickListener(view -> {
            name_visible_linear.setVisibility(View.GONE);
            recycler_tech.setVisibility(View.GONE);
            schedule_visible_linear.setVisibility(View.GONE);
            cutomer_visible_linear.setVisibility(View.GONE);
            invoice_visible_linear.setVisibility(View.GONE);
            reports_visible_linear.setVisibility(View.GONE);
            linear_schedule_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            map_linear.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_customers_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_projects.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_jobs_down.setBackgroundColor(getResources().getColor(R.color.button));
            linear_invoicing_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_reports_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            if (jobsarrowclicked){
                jobsarrowclicked=false;
                customerarrowclicked=false;
                schedulearrowclicked=true;
                namearrowclicked=true;
                techarrowclicked=true;
                invoicearrowclicked=true;
                reportsarrowclicked=true;
                jobs_visible_linear.setVisibility(View.VISIBLE);
            }else {
                jobsarrowclicked=true;
                jobs_visible_linear.setVisibility(View.GONE);
            }
        });

        linear_invoicing_down.setOnClickListener(view -> {
            name_visible_linear.setVisibility(View.GONE);
            recycler_tech.setVisibility(View.GONE);
            schedule_visible_linear.setVisibility(View.GONE);
            cutomer_visible_linear.setVisibility(View.GONE);
            jobs_visible_linear.setVisibility(View.GONE);
            reports_visible_linear.setVisibility(View.GONE);
            linear_schedule_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            map_linear.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_customers_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_projects.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_jobs_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_invoicing_down.setBackgroundColor(getResources().getColor(R.color.button));
            linear_reports_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            if (invoicearrowclicked){
                invoicearrowclicked=false;
                customerarrowclicked=false;
                schedulearrowclicked=true;
                namearrowclicked=true;
                techarrowclicked=true;
                jobsarrowclicked=true;
                reportsarrowclicked=true;
                invoice_visible_linear.setVisibility(View.VISIBLE);
            }else {
                invoicearrowclicked=true;
                invoice_visible_linear.setVisibility(View.GONE);
            }
        });

        linear_reports_down.setOnClickListener(view -> {
            name_visible_linear.setVisibility(View.GONE);
            recycler_tech.setVisibility(View.GONE);
            schedule_visible_linear.setVisibility(View.GONE);
            cutomer_visible_linear.setVisibility(View.GONE);
            jobs_visible_linear.setVisibility(View.GONE);
            invoice_visible_linear.setVisibility(View.GONE);
            linear_schedule_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            map_linear.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_customers_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_projects.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_jobs_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_invoicing_down.setBackgroundColor(getResources().getColor(R.color.action_bar));
            linear_reports_down.setBackgroundColor(getResources().getColor(R.color.button));
            if (reportsarrowclicked){
                reportsarrowclicked=false;
                customerarrowclicked=false;
                schedulearrowclicked=true;
                namearrowclicked=true;
                techarrowclicked=true;
                jobsarrowclicked=true;
                invoicearrowclicked=true;
                reports_visible_linear.setVisibility(View.VISIBLE);
            }else {
                reportsarrowclicked=true;
                reports_visible_linear.setVisibility(View.GONE);
            }
        });

        settings_fragment.setOnClickListener(view -> {
            ConfigurationFragment h = new ConfigurationFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        week_text.setOnClickListener(view -> {
            schedule_visible_linear.setVisibility(View.GONE);
            ScheduleWeekFragment h = new ScheduleWeekFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        month_text.setOnClickListener(view -> {
            schedule_visible_linear.setVisibility(View.GONE);
            ScheduleMonthFragment h = new ScheduleMonthFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        day_text.setOnClickListener(view -> {
            schedule_visible_linear.setVisibility(View.GONE);
            ScheduleDayFragment h = new ScheduleDayFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        activity_text.setOnClickListener(view -> {
            schedule_visible_linear.setVisibility(View.GONE);
            ActivitiesFragment h = new ActivitiesFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        resources_text.setOnClickListener(view -> {
            schedule_visible_linear.setVisibility(View.GONE);
            ResourcesFragment h = new ResourcesFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        list_text.setOnClickListener(view -> {
            cutomer_visible_linear.setVisibility(View.GONE);
            CustomerFragment h = new CustomerFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        site_text.setOnClickListener(view -> {
            cutomer_visible_linear.setVisibility(View.GONE);
            SiteFragment h = new SiteFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        equipment_text.setOnClickListener(view -> {
            cutomer_visible_linear.setVisibility(View.GONE);
            EquipmentFragment h = new EquipmentFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        jobs_month_text.setOnClickListener(view -> {
            jobs_visible_linear.setVisibility(View.GONE);
            JobsMonthFragment h = new JobsMonthFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        jobs_day_text.setOnClickListener(view -> {
            jobs_visible_linear.setVisibility(View.GONE);
            JobsDayFragment h = new JobsDayFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        jobs_week_text.setOnClickListener(view -> {
            jobs_visible_linear.setVisibility(View.GONE);
            JobsWeekFragment h = new JobsWeekFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        jobs_late_text.setOnClickListener(view -> {
            jobs_visible_linear.setVisibility(View.GONE);
            LateJobsFragment h = new LateJobsFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        upcoming_jobs_text.setOnClickListener(view -> {
            jobs_visible_linear.setVisibility(View.GONE);
            UpcomingJobsFragment h = new UpcomingJobsFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        to_schedule_job_text.setOnClickListener(view -> {
            jobs_visible_linear.setVisibility(View.GONE);
            JobsToScheduleFragment h = new JobsToScheduleFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        invoices_text.setOnClickListener(view -> {
            invoice_visible_linear.setVisibility(View.GONE);
            InvoicesFragment h = new InvoicesFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        quotations_text.setOnClickListener(view -> {
            invoice_visible_linear.setVisibility(View.GONE);
            QuotationsFragment h = new QuotationsFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

        project_text.setOnClickListener(view -> {
            ProjectsFragment h = new ProjectsFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });
        parts_text.setOnClickListener(view -> {
            reports_visible_linear.setVisibility(View.GONE);
            PartsAndServicesFragment h = new PartsAndServicesFragment();
            android.support.v4.app.FragmentTransaction k =
                    getSupportFragmentManager().beginTransaction();
            k.replace(R.id.container_fragments, h);
            k.commit();
        });

    }

}
