package com.example.event_retrofit.Retrofit;

import com.example.event_retrofit.data.App_User;
import com.example.event_retrofit.data.Event;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Interface_API {

    @POST("Register.php")
    @FormUrlEncoded
    Call<String> Register(@Field("name") String name,
                          @Field("lastname") String lastname,
                          @Field("password") String password,
                          @Field("email") String email);


    @POST("Login.php")
    @FormUrlEncoded
    Call<List<App_User>> Login(@Field("email") String email,
                               @Field("password") String password);


    @POST("Create_Event.php")
    @FormUrlEncoded
    Call<String> createEvent(@Field("name") String name,
                             @Field("description") String description,
                             @Field("time") String time,
                             @Field("sortOrder") int sortOrder,
                             @Field("id") int user_id);


    @POST("Edit_Event.php")
    @FormUrlEncoded
    Call<String> editEvent(@Field("name") String name,
                           @Field("description") String description,
                           @Field("id") int user_id);


    @POST("Read.php")
    @FormUrlEncoded
    Call<ArrayList<Event>> read(@Field("id") int user_id);


    @POST("Delete_Event(json).php")
    @FormUrlEncoded
    Call<String> delete(@Field("event_id") int event_id);


    @POST("ChangeOrder.php")
    @FormUrlEncoded
    Call<String>changeOrder
            (@Field("event_id") int event_id,
             @Field("order") int order);

}
