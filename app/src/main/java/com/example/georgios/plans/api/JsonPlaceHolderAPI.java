package com.example.georgios.plans.api;

import com.example.georgios.plans.model.NumberPair;
import com.example.georgios.plans.model.NumberString;
import com.example.georgios.plans.model.PlanEntity;
import com.example.georgios.plans.model.PreferenciaEntity;
import com.example.georgios.plans.model.Token;
import com.example.georgios.plans.model.UsuarioEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by IkosidekasDesktop on 23/11/2017.
 */

public interface JsonPlaceHolderAPI {

    @POST("user/login")
    Call<Token> login(@Body UsuarioEntity ur);

    @POST("user/users")
    Call<UsuarioEntity> registerNewUser(@Body UsuarioEntity ur);

    @POST("user/users/email")
    Call<UsuarioEntity> getUserbyEmail(@Body UsuarioEntity ur);

    @GET("plan/plans/preferences")
    Call<List<PreferenciaEntity>> getPreferences();

    @POST("user/users/userPreferences")
    Call<NumberString> setUserPreferences(@Body List<NumberString> ns);

    @POST("user/users/updateUserPreferences")
    Call<NumberString> updateUserPreferences(@Body List<NumberString> ns);

    @GET("plan/plans/notuser/{id}")
    Call<List<PlanEntity>> getRecomendedPlans(@Path("id") long usrid);

    @GET("user/users/{id}")
    Call<UsuarioEntity> getUserById(@Path("id") long usrid);

    @GET("user/users/asistentes/{id}")
    Call<List<UsuarioEntity>> getPlanSubscribedUsersById(@Path("id") long planid);

    @POST("plan/plans/subscribe")
    Call<NumberPair> subscribeToPlan(@Body NumberPair np);

    @GET("user/users/preferences/{id}")
    Call<List<PreferenciaEntity>> getUserPreferencesById(@Path("id") long id);

    @POST("user/users/update")
    Call<UsuarioEntity> updateUser(@Body UsuarioEntity ur);

    @GET("plan/plans/{id}")
    Call<List<PlanEntity>> getUserCreatedPlans(@Path("id") long id);

    @GET("plan/plans/subscribelist/{id}")
    Call<List<PlanEntity>> getUserSubscribedPlans(@Path("id") long id);

    @POST("plan/plans/unsubscribe")
    Call<NumberPair> unsubscribePlan(@Body NumberPair np);

    @POST("plan/plans")
    Call<PlanEntity> createPlan(@Body PlanEntity plan);

}
