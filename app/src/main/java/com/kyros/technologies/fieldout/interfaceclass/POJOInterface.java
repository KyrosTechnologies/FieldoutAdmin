package com.kyros.technologies.fieldout.interfaceclass;

import com.kyros.technologies.fieldout.models.ActivityType;
import com.kyros.technologies.fieldout.models.ActivityTypeAddResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeDeleteResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeUpdateResponse;
import com.kyros.technologies.fieldout.models.AddAttachments;
import com.kyros.technologies.fieldout.models.AddJobsTypeResponse;
import com.kyros.technologies.fieldout.models.AddProjectTypeResponse;
import com.kyros.technologies.fieldout.models.AddSchedulingResponse;
import com.kyros.technologies.fieldout.models.AddTagResponse;
import com.kyros.technologies.fieldout.models.AddTeamResponse;
import com.kyros.technologies.fieldout.models.AddToolsResourceResponse;
import com.kyros.technologies.fieldout.models.AddUserResponse;
import com.kyros.technologies.fieldout.models.BussinessHoursModel;
import com.kyros.technologies.fieldout.models.BussinessHoursResponse;
import com.kyros.technologies.fieldout.models.ChangePasswordResponse;
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
import com.kyros.technologies.fieldout.models.InvoiceCustomerResponse;
import com.kyros.technologies.fieldout.models.InvoiceResponse;
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
import com.kyros.technologies.fieldout.models.UsersItem;
import com.kyros.technologies.fieldout.models.UsersResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
    Observable<BussinessHoursResponse>postBussinessHours(@Header("Authorization")String authKey,@Body BussinessHoursModel bussinessHoursModel,@Header("idDomain")String idDomain);
    @PUT("/users/update/{userId}")
    Observable<UserUpdateResponse>updateUserProfile(@Header("Authorization")String authKey, @Path("userId")String userId, @Body Result result,@Header("idDomain")String idDomain);
    @GET("/domain/getById/{domainId}")
    Observable<DomainResponse>getDomain(@Path("domainId")String domainId,@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @GET("/business_hours/get")
    Observable<BussinessHoursResponse>getBussinessHours(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @GET("/users/getAll")
    Observable<List<UsersItem>>getUsers(@Header("Authorization")String authKey, @Header("idDomain")String idDomain);
    @GET("/teams/getAll")
    Observable<TeamsResponse>getTeams(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @POST("/users/add")
    Observable<AddUserResponse>addUser(@Body UserInfo userInfo,@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @GET("/users/getById/{userId}")
    Observable<GetSingleUserResponse>getOneUserResponse(@Path("userId")String userId,@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @GET("/users/getTechnicians")
    Observable<TechniciansResponse>getTechniciansResponse(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @GET("/users/getManagers")
    Observable<ManagersResponse>getManagersResponse(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @POST("/tags/add")
    Observable<AddTagResponse>addTag(@Header("Authorization")String authKey, @Body Tag tag,@Header("idDomain")String idDomain);
    @POST("/teams/add")
    Observable<AddTeamResponse>addTeam(@Body TeamsItem teamsItem,@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @GET("/teams/getById/{teamId}")
    Observable<SingleTeamResponse>getSingleTeam(@Path("teamId")String teamId, @Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @PUT("/teams/update/{teamId}")
    Observable<UpdateTeamResponse>updateTeam(@Path("teamId")String teamId, @Header("Authorization")String authKey,@Body TeamsItem teamsItem,@Header("idDomain")String idDomain);
    @GET("/tags/getAll")
    Observable<TagResponse>getTags(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @DELETE("/teams/delete/{teamId}")
    Observable<TeamDeleteResponse>deleteTeam(@Path("teamId")String teamId,@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @GET("/activity_types/getAll")
    Observable<ActivityTypeResponse>getActivityType(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @POST("/activity_types/send")
    Observable<ActivityTypeAddResponse>addActivityType(@Header("Authorization")String authKey, @Body ActivityType activityType,@Header("idDomain")String idDomain);
    @PUT("/activity_types/update/{activityTypeId}")
    Observable<ActivityTypeUpdateResponse>updateActivityType(@Header("Authorization")String authKey, @Body ActivityType activityType,@Path("activityTypeId")String activityTypeId,@Header("idDomain")String idDomain);
    @DELETE("/activity_types/delete/{activityTypeId}")
    Observable<ActivityTypeDeleteResponse>deleteActivityType(@Header("Authorization")String authKey,@Path("activityTypeId")String activityTypeId,@Header("idDomain")String idDomain);
    @POST("/tools_and_resources/add")
    Observable<AddToolsResourceResponse>addToolsResource(@Header("Authorization")String authKey, @Body Resource resource,@Header("idDomain")String idDomain);
    @GET("/tools_and_resources/getAll")
    Observable<GetToolsAndResourcesResponse>getToolsResources(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @PUT("/tools_and_resources/update/{resourceId}")
    Observable<UpdateToolsAndResourceResponse>updateToolsResource(@Header("Authorization")String authKey, @Body Resource resource,@Path("resourceId")String resourceId,@Header("idDomain")String idDomain);
    @DELETE("/tools_and_resources/delete/{resourceId}")
    Observable<DeleteToolsAndResourcesResponse>deleteToolsResource(@Header("Authorization")String authKey,@Path("resourceId")String resourceId,@Header("idDomain")String idDomain);
    @POST("/job_types/add")
    Observable<AddJobsTypeResponse>addJobsType(@Header("Authorization")String authKey, @Body JobType jobType,@Header("idDomain")String idDomain);
    @GET("/job_types/getAll")
    Observable<JobsTypeResponse>getJobsType(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @PUT("/job_types/update/{jobTypeId}")
    Observable<AddJobsTypeResponse>updateJobsType(@Header("Authorization")String authKey,@Path("jobTypeId")String jobTypeId,@Body JobType jobType,@Header("idDomain")String idDomain);
    @DELETE("/job_types/delete/{jobTypeId}")
    Observable<DeleteJobsTypeResponse>deleteJobType(@Header("Authorization")String authKey,@Path("jobTypeId")String jobTypeId,@Header("idDomain")String idDomain);
    @POST("/scheduling_window/add")
    Observable<AddSchedulingResponse>addSchedule(@Header("Authorization")String authKey, @Body SchedulingWindow schedulingWindow,@Header("idDomain")String idDomain);
    @GET("/scheduling_window/getByDomainId/{domainId}")
    Observable<ScheduleResponse>getSchedule(@Header("Authorization")String authKey,@Path("domainId")String domainId,@Header("idDomain")String idDomain);
    @DELETE("/scheduling_window/delete/{schedulingId}")
    Observable<DeleteScheduleWindowsResponse>deleteSchedule(@Header("Authorization")String authKey,@Path("schedulingId")String schedulingId,@Header("idDomain")String idDomain);
    @POST("/project_types/add")
    Observable<AddProjectTypeResponse>addProjecType(@Header("Authorization")String authKey, @Body ProjectType projectType,@Header("idDomain")String idDomain);
    @GET("/project_types/getByDomainId/{domainId}")
    Observable<ProjectTypeResponse>getProjectType(@Header("Authorization")String authKey,@Path("domainId")String domainId,@Header("idDomain")String idDomain);
    @DELETE("/project_types/delete/{projectTypeId}")
    Observable<DeleteProjectTypeResponse>deleteProjectType(@Header("Authorization")String authKey,@Path("projectTypeId")String projectTypeId,@Header("idDomain")String idDomain);
    @PUT("/project_types/update/{projectTypeId}")
    Observable<AddProjectTypeResponse>updateProjectType(@Header("Authorization")String authKey,@Path("projectTypeId")String projectTypeId,@Body ProjectType projectType,@Header("idDomain")String idDomain);
    @PUT("/scheduling_window/update/{schedulingId}")
    Observable<AddSchedulingResponse>updateScheduling(@Header("Authorization")String authKey,@Path("schedulingId")String schedulingId,@Body SchedulingWindow schedulingWindow,@Header("idDomain")String idDomain);
    @GET("/regional_settings/getByDomainId/{domainId}")
    Observable<RegionalSettingsResponse>getRegionalSettings(@Header("Authorization")String authKey,@Path("domainId")String domainId,@Header("idDomain")String idDomain);
    @POST("/regional_settings/add")
    Observable<RegionalSettingsResponse>addRegionalSettings(@Header("Authorization")String authKey, @Body RegionalSettings regionalSettings,@Header("idDomain")String idDomain);
    @GET("/taxes/getAll")
    Observable<TaxResponse>getTax(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @POST("/taxes/add")
    Observable<TaxResponse>addTax(@Header("Authorization")String authKey, @Body Tax tax,@Header("idDomain")String idDomain);
    @PUT("/taxes/update/{taxId}")
    Observable<TaxResponse>updateTax(@Header("Authorization")String authKey,@Path("taxId")String taxId, @Body Tax tax,@Header("idDomain")String idDomain);
    @DELETE("/taxes/delete/{taxId}")
    Observable<TaxDeleteResponse>deleteTaxResponse(@Header("Authorization")String authKey,@Path("taxId")String taxId,@Header("idDomain")String idDomain);
    @GET("/stock_parts/getAll")
    Observable<PartsAndServicesResponse>getPartsAndServices(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @POST("/stock_parts/send")
    Observable<PartsAndServicesResponse>addPartsAndServices(@Header("Authorization")String authKey, @Body StockPart stockPart,@Header("idDomain")String idDomain);
    @PUT("/stock_parts/update/{stockId}")
    Observable<PartsAndServicesResponse>updatePartsAndServices(@Header("Authorization")String authKey,@Path("stockId")String stockId,@Body StockPart stockPart,@Header("idDomain")String idDomain);
    @DELETE("/stock_parts/delete/{stockId}")
    Observable<DeletePartsAndServicesResponse>deleteParsAndServices(@Header("Authorization")String authKey,@Path("stockId")String stockId,@Header("idDomain")String idDomain);
    @GET("/custom_fields/getAll")
    Observable<CustomFieldResponse>getCustomFields(@Header("Authorization")String authKey,@Header("idDomain")String idDomain);
    @POST("/custom_fields/add")
    Observable<CustomFieldResponse>addCustomFields(@Header("Authorization")String authKey, @Body CustomField customField,@Header("idDomain")String idDomain);
    @PUT("/custom_fields/update/{customFieldId}")
    Observable<CustomFieldResponse>updateCustomFields(@Header("Authorization")String authKey, @Body CustomField customField, @Path("customFieldId")String customFieldId,@Header("idDomain")String idDomain);
    @DELETE("/custom_fields/delete/{customFieldId}")
    Observable<DeleteCustomFieldResponse>deleteCustomFields(@Header("Authorization")String authKey,@Path("customFieldId")String customFieldId,@Header("idDomain")String idDomain);
    @PUT("/tags/update/{tagId}")
    Observable<TagResponse>updateTag(@Header("Authorization")String authKey,@Path("tagId")String tagId,@Body Tag tag,@Header("idDomain")String idDomain);
    @DELETE("/tags/delete/{tagId}")
    Observable<DeleteTagResponse>deleteTag(@Header("Authorization")String authKey, @Path("tagId")String tagId,@Header("idDomain")String idDomain);
    @PUT("/users/changePassword/{userId}")
    Observable<ChangePasswordResponse>changePassword(@Header("Authorization")String authKey, @Path("userId")String userId, @Body User user,@Header("idDomain")String idDomain);
    @GET("/invoices/getById/{invoiceId}")
    Observable<InvoiceResponse>getCustomerInvoice(@Header("Authorization")String authKey, @Path("invoiceId")String invoiceId, @Header("idDomain")String idDomain);
    @Multipart
    @POST("/attachments/add")
    Observable<ResponseBody>addAttachments(@Header("Authorization")String authKey, @PartMap()Map<String, RequestBody> partMap, @Part MultipartBody.Part file,@Header("idDomain")String idDomain);
    @POST("/attachments/add")
    Observable<ResponseBody>addAttachmentsByteStream(@Header("Authorization")String authKey, @Body AddAttachments addAttachments,@Header("idDomain")String idDomain);

}
