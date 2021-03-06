package com.example.tandonmedical;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class doctorDetails extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<doctorsModelList> doctorsModelLists;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private String currentUserUid, name, email, password,
            bio, imageUrl, category, address, doctorId, phone,
            experience, status, rating, review, doctorToken,
            education, hospital, degree, timing, fee, tag, sign;
    private GeoPoint geo_point;
    private ImageView doctor_profile_iv, doctorDetail_back_iv;
    private RatingBar rating_and_review_star_rb;
    private CardView doctorDetails_appointment_cv;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GeoPoint doctorGeoPoint, userGeoPoint;
    String userDefaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/test1photographer.appspot.com/o/default%2FuserDefault.png?alt=media&token=0f495f89-caa3-4bcb-b278-97548eb77490";
    private Handler handler1;
    private Runnable runnable1;

    private TextView doctorDetails_doctorName_tv, doctorDetails_category_tv, rating_and_review_star_tv,
            doctor_education, doctor_hospital, doctor_degree, doctor_fee, doctor_detail_eta_tv, doctor_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        doctorDetails_doctorName_tv = findViewById(R.id.doctorDetails_doctorName_tv);
        doctor_profile_iv = findViewById(R.id.doctor_profile_iv);
        doctorDetails_category_tv = findViewById(R.id.doctorDetails_category_tv);
        rating_and_review_star_rb = findViewById(R.id.search_star_rb);
        rating_and_review_star_tv = findViewById(R.id.search_star_tv);
        doctor_education = findViewById(R.id.doctor_education);
        doctor_hospital = findViewById(R.id.doctor_hospital);
        doctor_degree = findViewById(R.id.doctor_degree);
        doctor_fee = findViewById(R.id.doctor_fee);
        doctorDetails_appointment_cv = findViewById(R.id.doctorDetails_appointment_cv);
        doctorDetail_back_iv = findViewById(R.id.doctorDetail_back_iv);
        doctor_detail_eta_tv = findViewById(R.id.doctor_detail_eta_tv);
        doctor_address = findViewById(R.id.doctor_address);

        if (getIntent().getExtras() != null) {
            this.name = (String) getIntent().getExtras().get("name");
            this.email = (String) getIntent().getExtras().get("email");
            this.password = (String) getIntent().getExtras().get("password");
            this.bio = (String) getIntent().getExtras().get("bio");
            this.imageUrl = (String) getIntent().getExtras().get("imageUrl");
            this.category = (String) getIntent().getExtras().get("category");
            this.address = (String) getIntent().getExtras().get("address");
            this.doctorId = (String) getIntent().getExtras().get("doctorId");
            this.phone = (String) getIntent().getExtras().get("phone");
            this.experience = (String) getIntent().getExtras().get("experience");
            this.status = (String) getIntent().getExtras().get("status");
            this.rating = (String) getIntent().getExtras().get("rating");
            this.review = (String) getIntent().getExtras().get("review");
            this.doctorToken = (String) getIntent().getExtras().get("doctorToken");
            this.education = (String) getIntent().getExtras().get("education");
            this.hospital = (String) getIntent().getExtras().get("hospital");
            this.degree = (String) getIntent().getExtras().get("degree");
            this.timing = (String) getIntent().getExtras().get("timing");
            this.fee = (String) getIntent().getExtras().get("fee");
            this.tag = (String) getIntent().getExtras().get("tag");
            this.sign = (String) getIntent().getExtras().get("sign");
            //this.geo_point = (GeoPoint) getIntent().getExtras().get("geo_point");

        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.doctor_detail_map_frag);
        mapFragment.getMapAsync(this);
        setDoctorLocationOnMap();
        getDoctorDetails();
        handler1 = new Handler();
        handler1.postDelayed(runnable1 = new Runnable() {
            @Override
            public void run() {
                if (userGeoPoint != null && doctorGeoPoint != null) {
                    double distance = distanceBetweenUserAndSeller(userGeoPoint.getLatitude(), userGeoPoint.getLongitude(),
                            doctorGeoPoint.getLatitude(), doctorGeoPoint.getLongitude());
                    // 5 is 5meter per second speed and 60 is seconds conversion
                    double timeInSeconds = (distance / 5);
                    doctor_detail_eta_tv.setText(convertSecondsToTime(timeInSeconds) + " Min");

                    doctorDetails_category_tv.setText(category);
                    doctor_education.setText(education);
                    doctor_hospital.setText(hospital);
                    doctor_degree.setText(degree);
                    doctor_fee.setText(fee);
                    doctor_address.setText(address);

                }
                handler1.postDelayed(this, 10000);
            }
        }, 5000);

        doctorDetails_appointment_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Feature Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        doctorDetail_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDoctorDetails() {
        mDb.collection("doctors").document(doctorId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Glide.with(getApplicationContext()).load(imageUrl).into(doctor_profile_iv);
                        doctorDetails_doctorName_tv.setText((String) document.get("name").toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "failed ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void setDoctorLocationOnMap() {

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        map.setMyLocationEnabled(true);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));
                        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        Map<String, Object> updateUserLocation = new HashMap<>();
                        updateUserLocation.put("geo_point", geoPoint);
                        mDb.collection("users").document(currentUserUid).update(updateUserLocation);
                        mDb.collection("users").document(currentUserUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        StorageReference ref = mStorageRef.child("images/" + currentUserUid).child("profilepic.jpg");
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Glide.with(doctorDetails.this)
                                                        .asBitmap()
                                                        .load(uri)
                                                        .into(new CustomTarget<Bitmap>() {
                                                            @Override
                                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                                Bitmap bitmap = getCircularBitmap(resource);
                                                                GeoPoint geoPoint = (GeoPoint) document.get("geo_point");
                                                                userGeoPoint = geoPoint;
                                                                double lat = geoPoint.getLatitude();
                                                                double lng = geoPoint.getLongitude();
                                                                LatLng latLng = new LatLng(lat, lng);
                                                                map.addMarker(new MarkerOptions()
                                                                        .position(latLng)
                                                                        .icon(BitmapDescriptorFactory
                                                                                .fromBitmap(createCustomMarkerForUser
                                                                                        (doctorDetails.this,
                                                                                                bitmap)))
                                                                        .title(String.valueOf(document.get("name")))
                                                                        .zIndex(1.0f));
                                                            }

                                                            @Override
                                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Glide.with(doctorDetails.this)
                                                        .asBitmap()
                                                        .load(userDefaultImageUrl)
                                                        .into(new CustomTarget<Bitmap>() {
                                                            @Override
                                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                                Bitmap bitmap = getCircularBitmap(resource);
                                                                GeoPoint geoPoint = (GeoPoint) document.get("geo_point");
                                                                userGeoPoint = geoPoint;
                                                                double lat = geoPoint.getLatitude();
                                                                double lng = geoPoint.getLongitude();
                                                                LatLng latLng = new LatLng(lat, lng);
                                                                map.addMarker(new MarkerOptions()
                                                                        .position(latLng)
                                                                        .icon(BitmapDescriptorFactory
                                                                                .fromBitmap(createCustomMarkerForUser
                                                                                        (doctorDetails.this,
                                                                                                bitmap)))
                                                                        .title("user")
                                                                        .zIndex(1.0f));
                                                            }

                                                            @Override
                                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                                            }
                                                        });
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "failed ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        mDb.collection("doctors").document(doctorId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        StorageReference ref = mStorageRef.child("images/" + doctorId).child("profilepic.jpg");
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Glide.with(doctorDetails.this)
                                                        .asBitmap()
                                                        .load(uri)
                                                        .into(new CustomTarget<Bitmap>() {
                                                            @Override
                                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                                Bitmap bitmap = getCircularBitmap(resource);
                                                                GeoPoint geoPoint = (GeoPoint) document.get("geo_point");
                                                                doctorGeoPoint = geoPoint;
                                                                double lat = geoPoint.getLatitude();
                                                                double lng = geoPoint.getLongitude();
                                                                LatLng latLng = new LatLng(lat, lng);
                                                                map.addMarker(new MarkerOptions()
                                                                        .position(latLng)
                                                                        .icon(BitmapDescriptorFactory
                                                                                .fromBitmap(createCustomMarkerForUser
                                                                                        (doctorDetails.this,
                                                                                                bitmap)))
                                                                        .title(String.valueOf(document.get("name")))
                                                                        .zIndex(1.0f));
                                                            }

                                                            @Override
                                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Glide.with(doctorDetails.this)
                                                        .asBitmap()
                                                        .load(userDefaultImageUrl)
                                                        .into(new CustomTarget<Bitmap>() {
                                                            @Override
                                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                                Bitmap bitmap = getCircularBitmap(resource);
                                                                GeoPoint geoPoint = (GeoPoint) document.get("geo_point");
                                                                doctorGeoPoint = geoPoint;
                                                                double lat = geoPoint.getLatitude();
                                                                double lng = geoPoint.getLongitude();
                                                                LatLng latLng = new LatLng(lat, lng);
                                                                map.addMarker(new MarkerOptions()
                                                                        .position(latLng)
                                                                        .icon(BitmapDescriptorFactory
                                                                                .fromBitmap(createCustomMarkerForUser
                                                                                        (doctorDetails.this,
                                                                                                bitmap)))
                                                                        .title("Seller")
                                                                        .zIndex(1.0f));
                                                            }

                                                            @Override
                                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                                            }
                                                        });
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "failed ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private String convertSecondsToTime(double timeInSeconds) {
        int hours = (int) (timeInSeconds / 3600);
        int minutes = (int) ((timeInSeconds % 3600) / 60);
        int seconds = (int) (timeInSeconds % 60);
        String timeString = String.format("%02d:%02d", hours, minutes);
        return timeString;
    }

    private double distanceBetweenUserAndSeller(double lat1, double lon1, double lat2, double lon2) {
        Location startPoint = new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lon1);
        Location endPoint = new Location("locationA");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lon2);
        double distance = startPoint.distanceTo(endPoint);
        return distance;
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public Bitmap createCustomMarkerForUser(Context context, Bitmap resource) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_user_marker_layout, null);
        final CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
        roundedBitmapDrawable.setCornerRadius(50.0f);
        roundedBitmapDrawable.setAntiAlias(true);
        markerImage.setImageDrawable(roundedBitmapDrawable);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(),
                marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;
    }

}