package com.example.byoungeun_kim.exrefarm;

/**
 * Created by sumbu on 2017-11-19.
 */

public class  Friend {
    public String email;

    public String photo;

    public String key;

    public Friend(){

    }


    public String getEmail()
    {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }
}
