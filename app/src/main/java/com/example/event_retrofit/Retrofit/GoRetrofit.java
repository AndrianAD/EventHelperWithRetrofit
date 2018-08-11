package com.example.event_retrofit.Retrofit;

public class GoRetrofit {
    private final static String BASE_URL="http://uncroptv.000webhostapp.com/";


    public static Interface_API getAPI(){
        return RetrofitSingleton.getInstance(BASE_URL).create(Interface_API.class);
    }


}
