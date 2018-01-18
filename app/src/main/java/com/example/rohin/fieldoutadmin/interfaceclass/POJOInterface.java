package com.example.rohin.fieldoutadmin.interfaceclass;

import com.example.rohin.fieldoutadmin.models.ActivityType;
import com.example.rohin.fieldoutadmin.models.ActivityTypeAddResponse;
import com.example.rohin.fieldoutadmin.models.ActivityTypeDeleteResponse;
import com.example.rohin.fieldoutadmin.models.ActivityTypeResponse;
import com.example.rohin.fieldoutadmin.models.ActivityTypeUpdateResponse;
import com.example.rohin.fieldoutadmin.models.AddJobsTypeResponse;
import com.example.rohin.fieldoutadmin.models.AddProjectTypeResponse;
import com.example.rohin.fieldoutadmin.models.AddSchedulingResponse;
import com.example.rohin.fieldoutadmin.models.AddTagResponse;
import com.example.rohin.fieldoutadmin.models.AddTeamResponse;
import com.example.rohin.fieldoutadmin.models.AddToolsResourceResponse;
import com.example.rohin.fieldoutadmin.models.AddUserResponse;
import com.example.rohin.fieldoutadmin.models.BussinessHoursModel;
import com.example.rohin.fieldoutadmin.models.BussinessHoursResponse;
import com.example.rohin.fieldoutadmin.models.CustomField;
import com.example.rohin.fieldoutadmin.models.CustomFieldResponse;
import com.example.rohin.fieldoutadmin.models.DeleteCustomFieldResponse;
import com.example.rohin.fieldoutadmin.models.DeleteJobsTypeResponse;
import com.example.rohin.fieldoutadmin.models.DeletePartsAndServicesResponse;
import com.example.rohin.fieldoutadmin.models.DeleteProjectTypeResponse;
import com.example.rohin.fieldoutadmin.models.DeleteScheduleWindowsResponse;
import com.example.rohin.fieldoutadmin.models.DeleteTagResponse;
import com.example.rohin.fieldoutadmin.models.DeleteToolsAndResourcesResponse;
import com.example.rohin.fieldoutadmin.models.DomainResponse;
import com.example.rohin.fieldoutadmin.models.GetToolsAndResourcesResponse;
import com.example.rohin.fieldoutadmin.models.JobType;
import com.example.rohin.fieldoutadmin.models.JobsTypeResponse;
import com.example.rohin.fieldoutadmin.models.ManagersResponse;
import com.example.rohin.fieldoutadmin.models.PartsAndServicesResponse;
import com.example.rohin.fieldoutadmin.models.ProjectType;
import com.example.rohin.fieldoutadmin.models.ProjectTypeResponse;
import com.example.rohin.fieldoutadmin.models.RegionalSettings;
import com.example.rohin.fieldoutadmin.models.RegionalSettingsResponse;
import com.example.rohin.fieldoutadmin.models.Resource;
import com.example.rohin.fieldoutadmin.models.ScheduleResponse;
import com.example.rohin.fieldoutadmin.models.SchedulingWindow;
import com.example.rohin.fieldoutadmin.models.SignUpModel;
import com.example.rohin.fieldoutadmin.models.Result;
import com.example.rohin.fieldoutadmin.models.SingleTeamResponse;
import com.example.rohin.fieldoutadmin.models.StockPart;
import com.example.rohin.fieldoutadmin.models.Tag;
import com.example.rohin.fieldoutadmin.models.TagResponse;
import com.example.rohin.fieldoutadmin.models.Tax;
import com.example.rohin.fieldoutadmin.models.TaxDeleteResponse;
import com.example.rohin.fieldoutadmin.models.TaxResponse;
import com.example.rohin.fieldoutadmin.models.TeamAdd;
import com.example.rohin.fieldoutadmin.models.TeamDeleteResponse;
import com.example.rohin.fieldoutadmin.models.TeamsItem;
import com.example.rohin.fieldoutadmin.models.TeamsResponse;
import com.example.rohin.fieldoutadmin.models.GetSingleUserResponse;
import com.example.rohin.fieldoutadmin.models.TechniciansResponse;
import com.example.rohin.fieldoutadmin.models.UpdateTeamResponse;
import com.example.rohin.fieldoutadmin.models.UpdateToolsAndResourceResponse;
import com.example.rohin.fieldoutadmin.models.User;
import com.example.rohin.fieldoutadmin.models.UserInfo;
import com.example.rohin.fieldoutadmin.models.UserUpdateResponse;
import com.example.rohin.fieldoutadmin.models.UsersResponse;

import lombok.Getter;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
