package com.kangkan.developer.tutortuitionbd.DB_Connect;


public class User {

    public String  name, email, phone,imageURL,institute;

    public User() {
    }

    public User(String name, String email, String phone, String imageURL, String institute) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imageURL = imageURL;
        this.institute = institute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }
}
