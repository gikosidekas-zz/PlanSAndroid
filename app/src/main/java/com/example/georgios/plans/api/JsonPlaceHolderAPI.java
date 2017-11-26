package com.example.georgios.plans.api;

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

    @GET("plan/plans/notuser/{id}")
    Call<List<PlanEntity>> getRecomendedPlans(@Path("id") long usrid);



}
