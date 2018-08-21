package com.example.event_retrofit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class App_User {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("lastname")
    @Expose
    private String lastName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("role")
    @Expose
    String role;



    public App_User(String userName, String userLastName, String email, String password) {
        this.name = userName;
        this.lastName = userLastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }


        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
