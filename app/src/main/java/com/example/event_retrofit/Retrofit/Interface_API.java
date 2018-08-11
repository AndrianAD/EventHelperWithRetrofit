package com.example.event_retrofit.Retrofit;

import com.example.event_retrofit.data.App_User;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Interface_API {

    @POST("Register.php")
    @FormUrlEncoded
    Call<String> Register(@Field("name") String name,
                            @Field("lastname") String lastname,
                            @Field("password") String password,
                            @Field("email") String email);


    @POST("Login2.php")
    @FormUrlEncoded
    Call<List<App_User>> Login(@Field("email") String email,
                               @Field("password") String password);

}
