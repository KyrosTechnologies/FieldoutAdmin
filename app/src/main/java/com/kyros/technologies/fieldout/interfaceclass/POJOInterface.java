package com.kyros.technologies.fieldout.interfaceclass;

import com.kyros.technologies.fieldout.models.ActivityType;
import com.kyros.technologies.fieldout.models.ActivityTypeAddResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeDeleteResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeUpdateResponse;
import com.kyros.technologies.fieldout.models.AddJobsTypeResponse;
import com.kyros.technologies.fieldout.models.AddProjectTypeResponse;
import com.kyros.technologies.fieldout.models.AddSchedulingResponse;
import com.kyros.technologies.fieldout.models.AddTagResponse;
import com.kyros.technologies.fieldout.models.AddTeamResponse;
import com.kyros.technologies.fieldout.models.AddToolsResourceResponse;
import com.kyros.technologies.fieldout.models.AddUserResponse;
import com.kyros.technologies.fieldout.models.BussinessHoursModel;
import com.kyros.technologies.fieldout.models.BussinessHoursResponse;
import com.kyros.technologies.fieldout.models.CustomField;
import com.kyros.technologies.fieldout.models.CustomFieldResponse;
import com.kyros.technologies.fieldout.models.DeleteCustomFieldResponse;
import com.kyros.technologies.fieldout.models.DeleteJobsTypeResponse;
import com.kyros.technologies.fieldout.models.DeletePartsAndServicesResponse;
import com.kyros.technologies.fieldout.models.DeleteProjectTypeResponse;
import com.kyros.technologies.fieldout.models.DeleteScheduleWindowsResponse;
import com.kyros.technologies.fieldout.models.DeleteTagResponse;
import com.kyros.technologies.fieldout.models.DeleteToolsAndResourcesResponse;
import com.kyros.technologies.fieldout.models.DomainResponse;
import com.kyros.technologies.fieldout.models.GetToolsAndResourcesResponse;
import com.kyros.technologies.fieldout.models.JobType;
import com.kyros.technologies.fieldout.models.JobsTypeResponse;
import com.kyros.technologies.fieldout.models.ManagersResponse;
import com.kyros.technologies.fieldout.models.PartsAndServicesResponse;
import com.kyros.technologies.fieldout.models.ProjectType;
import com.kyros.technologies.fieldout.models.ProjectTypeResponse;
import com.kyros.technologies.fieldout.models.RegionalSettings;
import com.kyros.technologies.fieldout.models.RegionalSettingsResponse;
import com.kyros.technologies.fieldout.models.Resource;
import com.kyros.technologies.fieldout.models.ScheduleResponse;
import com.kyros.technologies.fieldout.models.SchedulingWindow;
import com.kyros.technologies.fieldout.models.SignUpModel;
import com.kyros.technologies.fieldout.models.Result;
import com.kyros.technologies.fieldout.models.SingleTeamResponse;
import com.kyros.technologies.fieldout.models.StockPart;
import com.kyros.technologies.fieldout.models.Tag;
import com.kyros.technologies.fieldout.models.TagResponse;
import com.kyros.technologies.fieldout.models.Tax;
import com.kyros.technologies.fieldout.models.TaxDeleteResponse;
import com.kyros.technologies.fieldout.models.TaxResponse;
import com.kyros.technologies.fieldout.models.TeamDeleteResponse;
import com.kyros.technologies.fieldout.models.TeamsItem;
import com.kyros.technologies.fieldout.models.TeamsResponse;
import com.kyros.technologies.fieldout.models.GetSingleUserResponse;
import com.kyros.technologies.fieldout.models.TechniciansResponse;
import com.kyros.technologies.fieldout.models.UpdateTeamResponse;
import com.kyros.technologies.fieldout.models.UpdateToolsAndResourceResponse;
import com.kyros.technologies.fieldout.models.User;
import com.kyros.technologies.fieldout.models.UserInfo;
import com.kyros.technologies.fieldout.models.UserUpdateResponse;
import com.kyros.technologies.fieldout.models.UsersResponse;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by kyros on 12-12-2017.
 */

public interface POJOInterface {
    @Headers("Content-Type: application/json")
    @POST("/domain/register")
    Observable<SignUpModel>getSignUpResponse(@Body SignUpModel model);
    @POST("/business_hours/save")
    Observable<BussinessHoursResponse>postBussinessHours(@Header("Authorization")String authKey,@Body BussinessHoursModel bussinessHoursModel);
    @PUT("/users/update/{userId}")
    Observable<UserUpdateResponse>updateUserProfile(@Header("Authorization")String authKey, @Path("userId")String userId, @Body Result result);
    @GET("/domain/getById/{domainId}")
    Observable<DomainResponse>getDomain(@Path("domainId")String domainId,@Header("Authorization")String authKey);
    @GET("/business_hours/getByDomainId/{domainId}")
    Observable<BussinessHoursResponse>getBussinessHours(@Path("domainId")String domainId, @Header("Authorization")String authKey);
    @GET("/users/getByDomainId/{domainId}")
    Observable<UsersResponse>getUsers(@Path("domainId")String domainId,@Header("Authorization")String authKey);
    @GET("/teams/getByDomainId/{domainId}")
    Observable<TeamsResponse>getTeams(@Path("domainId")String domainId, @Header("Authorization")String authKey);
    @POST("/users/add")
    Observable<AddUserResponse>addUser(@Body UserInfo userInfo,@Header("Authorization")String authKey);
    @GET("/users/getById/{userId}")
    Observable<GetSingleUserResponse>getOneUserResponse(@Path("userId")String userId,@Header("Authorization")String authKey);
    @GET("/users/getTechnicians/{domainId}")
    Observable<TechniciansResponse>getTechniciansResponse(@Path("domainId")String domainId,@Header("Authorization")String authKey);
    @GET("/users/getManagers/{domainId}")
    Observable<ManagersResponse>getManagersResponse(@Path("domainId")String domainId,@Header("Authorization")String authKey);
    @POST("/tags/add")
    Observable<AddTagResponse>addTag(@Header("Authorization")String authKey, @Body Tag tag);
    @POST("/teams/add")
    Observable<AddTeamResponse>addTeam(@Body TeamsItem teamsItem,@Header("Authorization")String authKey);
    @GET("/teams/getById/{teamId}")
    Observable<SingleTeamResponse>getSingleTeam(@Path("teamId")String teamId, @Header("Authorization")String authKey);
    @PUT("/teams/update/{teamId}")
    Observable<UpdateTeamResponse>updateTeam(@Path("teamId")String teamId, @Header("Authorization")String authKey,@Body TeamsItem teamsItem);
    @GET("/tags/getByDomainId/{domainId}")
    Observable<TagResponse>getTags(@Path("domainId")String domainId,@Header("Authorization")String authKey);
    @DELETE("/teams/delete/{teamId}")
    Observable<TeamDeleteResponse>deleteTeam(@Path("teamId")String teamId,@Header("Authorization")String authKey);
    @GET("/activity_types/getByDomainId/{domainId}")
    Observable<ActivityTypeResponse>getActivityType(@Path("domainId")String domainId,@Header("Authorization")String authKey);
    @POST("/activity_types/send")
    Observable<ActivityTypeAddResponse>addActivityType(@Header("Authorization")String authKey, @Body ActivityType activityType);
    @PUT("/activity_types/update/{activityTypeId}")
    Observable<ActivityTypeUpdateResponse>updateActivityType(@Header("Authorization")String authKey, @Body ActivityType activityType,@Path("activityTypeId")String activityTypeId);
    @DELETE("/activity_types/delete/{activityTypeId}")
    Observable<ActivityTypeDeleteResponse>deleteActivityType(@Header("Authorization")String authKey,@Path("activityTypeId")String activityTypeId);
    @POST("/tools_and_resources/add")
    Observable<AddToolsResourceResponse>addToolsResource(@Header("Authorization")String authKey, @Body Resource resource);
    @GET("/tools_and_resources/getByDomainId/{domainId}")
    Observable<GetToolsAndResourcesResponse>getToolsResources(@Header("Authorization")String authKey,@Path("domainId")String domainId);
    @PUT("/tools_and_resources/update/{resourceId}")
    Observable<UpdateToolsAndResourceResponse>updateToolsResource(@Header("Authorization")String authKey, @Body Resource resource,@Path("resourceId")String resourceId);
    @DELETE("/tools_and_resources/delete/{resourceId}")
    Observable<DeleteToolsAndResourcesResponse>deleteToolsResource(@Header("Authorization")String authKey,@Path("resourceId")String resourceId);
    @POST("/job_types/add")
    Observable<AddJobsTypeResponse>addJobsType(@Header("Authorization")String authKey, @Body JobType jobType);
    @GET("/job_types/getByDomainId/{domainId}")
    Observable<JobsTypeResponse>getJobsType(@Header("Authorization")String authKey,@Path("domainId")String domainId);
    @PUT("/job_types/update/{jobTypeId}")
    Observable<AddJobsTypeResponse>updateJobsType(@Header("Authorization")String authKey,@Path("jobTypeId")String jobTypeId,@Body JobType jobType);
    @DELETE("/job_types/delete/{jobTypeId}")
    Observable<DeleteJobsTypeResponse>deleteJobType(@Header("Authorization")String authKey,@Path("jobTypeId")String jobTypeId);
    @POST("/scheduling_window/add")
    Observable<AddSchedulingResponse>addSchedule(@Header("Authorization")String authKey, @Body SchedulingWindow schedulingWindow);
    @GET("/scheduling_window/getByDomainId/{domainId}")
    Observable<ScheduleResponse>getSchedule(@Header("Authorization")String authKey,@Path("domainId")String domainId);
    @DELETE("/scheduling_window/delete/{schedulingId}")
    Observable<DeleteScheduleWindowsResponse>deleteSchedule(@Header("Authorization")String authKey,@Path("schedulingId")String schedulingId);
    @POST("/project_types/add")
    Observable<AddProjectTypeResponse>addProjecType(@Header("Authorization")String authKey, @Body ProjectType projectType);
    @GET("/project_types/getByDomainId/{domainId}")
    Observable<ProjectTypeResponse>getProjectType(@Header("Authorization")String authKey,@Path("domainId")String domainId);
    @DELETE("/project_types/delete/{projectTypeId}")
    Observable<DeleteProjectTypeResponse>deleteProjectType(@Header("Authorization")String authKey,@Path("projectTypeId")String projectTypeId);
    @PUT("/project_types/update/{projectTypeId}")
    Observable<AddProjectTypeResponse>updateProjectType(@Header("Authorization")String authKey,@Path("projectTypeId")String projectTypeId,@Body ProjectType projectType);
    @PUT("/scheduling_window/update/{schedulingId}")
    Observable<AddSchedulingResponse>updateScheduling(@Header("Authorization")String authKey,@Path("schedulingId")String schedulingId,@Body SchedulingWindow schedulingWindow);
    @GET("/regional_settings/getByDomainId/{domainId}")
    Observable<RegionalSettingsResponse>getRegionalSettings(@Header("Authorization")String authKey,@Path("domainId")String domainId);
    @POST("/regional_settings/add")
    Observable<RegionalSettingsResponse>addRegionalSettings(@Header("Authorization")String authKey, @Body RegionalSettings regionalSettings);
    @GET("/taxes/getByDomainId/{domainId}")
    Observable<TaxResponse>getTax(@Header("Authorization")String authKey,@Path("domainId")String domainId);
    @POST("/taxes/add")
    Observable<TaxResponse>addTax(@Header("Authorization")String authKey, @Body Tax tax);
    @PUT("/taxes/update/{taxId}")
    Observable<TaxResponse>updateTax(@Header("Authorization")String authKey,@Path("taxId")String taxId, @Body Tax tax);
    @DELETE("/taxes/delete/{taxId}")
    Observable<TaxDeleteResponse>deleteTaxResponse(@Header("Authorization")String authKey,@Path("taxId")String taxId);
    @GET("/stock_parts/getByDomainId/{domainId}")
    Observable<PartsAndServicesResponse>getPartsAndServices(@Header("Authorization")String authKey,@Path("domainId")String domainId);
    @POST("/stock_parts/send")
    Observable<PartsAndServicesResponse>addPartsAndServices(@Header("Authorization")String authKey, @Body StockPart stockPart);
    @PUT("/stock_parts/update/{stockId}")
    Observable<PartsAndServicesResponse>updatePartsAndServices(@Header("Authorization")String authKey,@Path("stockId")String stockId,@Body StockPart stockPart);
    @DELETE("/stock_parts/delete/{stockId}")
    Observable<DeletePartsAndServicesResponse>deleteParsAndServices(@Header("Authorization")String authKey,@Path("stockId")String stockId);
    @GET("/custom_fields/getByDomainId/{domainId}")
    Observable<CustomFieldResponse>getCustomFields(@Header("Authorization")String authKey,@Path("domainId")String domainId);
    @POST("/custom_fields/add")
    Observable<CustomFieldResponse>addCustomFields(@Header("Authorization")String authKey, @Body CustomField customField);
    @PUT("/custom_fields/update/{customFieldId}")
    Observable<CustomFieldResponse>updateCustomFields(@Header("Authorization")String authKey, @Body CustomField customField, @Path("customFieldId")String customFieldId);
    @DELETE("/custom_fields/delete/{customFieldId}")
    Observable<DeleteCustomFieldResponse>deleteCustomFields(@Header("Authorization")String authKey,@Path("customFieldId")String customFieldId);
    @PUT("/tags/update/{tagId}")
    Observable<TagResponse>updateTag(@Header("Authorization")String authKey,@Path("tagId")String tagId,@Body Tag tag);
    @DELETE("/tags/delete/{tagId}")
    Observable<DeleteTagResponse>deleteTag(@Header("Authorization")String authKey, @Path("tagId")String tagId);
    @PUT("/users/changePassword/{userId}")
    Observable<User>changePassword(@Header("Authorization")String authKey,@Path("userId")String userId,@Body User user);

}
