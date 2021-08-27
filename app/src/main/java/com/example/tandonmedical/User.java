package com.example.tandonmedical;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class User implements Parcelable {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    private String user_id;
    private String username;
    private String phone;
    private String password;
    private GeoPoint geo_point;
    private @ServerTimestamp
    Date timestamp;

    public User(String email, String user_id, String username, String phone, String password, GeoPoint geo_point, Date timestamp) {
        this.email = email;
        this.user_id = user_id;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.geo_point = geo_point;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                '}';
    }

    public User() {

    }


    protected User(Parcel in) {
        email = in.readString();
        user_id = in.readString();
        username = in.readString();
        phone = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(phone);
        dest.writeString(password);
    }
}
