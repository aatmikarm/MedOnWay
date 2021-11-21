package com.example.tandonmedical;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class payment extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    User currentUser;
    private FirebaseFirestore mDb;
    private FirebaseAuth firebaseAuth;
    private GoogleMap mMap;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private LatLngBounds mMapBoundary;
    private EditText payment_address_et;
    private TextView paymentActivity_totalPayment;
    private CardView onlinePayment_cv, CASH_ON_DELIVERY_cv, payment_confirm_cv;
    private Float totalAmount;
    private String currentUserUid,productId, dateandtimepattern = "ssmmHHddMMyyyy";
    private productModelList productModelLists;

    GeoPoint currentUserGeoPoints;
    private StorageReference mStorageRef;
    List<Marker> allMapMarkers = new ArrayList<Marker>();
    String userDefaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/test1photographer.appspot.com/o/default%2FuserDefault.png?alt=media&token=0f495f89-caa3-4bcb-b278-97548eb77490";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().hide();

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        currentUserUid = firebaseAuth.getUid();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.paymentMap);
        assert mapFragment != null;
        mapFragment.getMapAsync((OnMapReadyCallback) payment.this);

        if (getIntent().getExtras() != null) {
            this.totalAmount = (Float) getIntent().getExtras().get("totalAmount");
            this.productId = (String) getIntent().getExtras().get("productId");
        }

        payment_address_et = findViewById(R.id.payment_address_et);
        paymentActivity_totalPayment = findViewById(R.id.paymentActivity_totalPayment);
        payment_confirm_cv = findViewById(R.id.payment_confirm_cv);

        paymentActivity_totalPayment.setText(String.valueOf(totalAmount));
        //Toast.makeText(payment.this, String.valueOf(paymentAmount), Toast.LENGTH_SHORT).show();

        payment_confirm_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalAmount == 0) {

                    Toast.makeText(payment.this, "CART IS EMPTY", Toast.LENGTH_SHORT).show();
                    finish();

                } else if (totalAmount != 0) {

                    updateUserProductStatus();
                    finish();
                    startActivity(new Intent(getApplicationContext(), orders.class));

                }

            }
        });


    }

    private void updateUserProductStatus() {
        mDb.collection("users").document(currentUserUid).collection("orders")
                .whereEqualTo("status","in cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int otp = generateRandomOTP();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        SimpleDateFormat sdf = new SimpleDateFormat(dateandtimepattern);
                        final String productOrderPlacedTime = sdf.format(new Date());
                        Map<String, Object> updateUserInfo = new HashMap<>();
                        updateUserInfo.put("status", "on the way");
                        updateUserInfo.put("productOrderPlacedTime", productOrderPlacedTime);
                        updateUserInfo.put("otp", String.valueOf(otp));
                        mDb.collection("users").document(currentUserUid)
                                .collection("orders").document((String) document.get("productOrderId"))
                                .update(updateUserInfo);
                        Map<String, Object> updateSellerInfo = new HashMap<>();
                        updateSellerInfo.put("name", (String) document.get("name"));
                        updateSellerInfo.put("userId", currentUserUid);
                        updateSellerInfo.put("otp", String.valueOf(otp));
                        updateSellerInfo.put("imageUrl", (String) document.get("imageUrl"));
                        updateSellerInfo.put("mrp", (String) document.get("mrp"));
                        updateSellerInfo.put("price", (String) document.get("price"));
                        updateSellerInfo.put("discount", (String) document.get("discount"));
                        updateSellerInfo.put("description", (String) document.get("description"));
                        updateSellerInfo.put("productId", (String) document.get("productId"));
                        updateSellerInfo.put("productOrderId", (String) document.get("productOrderId"));
                        updateSellerInfo.put("category", (String) document.get("category"));
                        updateSellerInfo.put("sellerId", (String) document.get("sellerId"));
                        updateSellerInfo.put("quantity", "1");
                        updateSellerInfo.put("status", "on the way");
                        mDb.collection("seller").document((String) document.get("sellerId"))
                                .collection("orders").document((String) document.get("productOrderId"))
                                .set(updateSellerInfo);
                    }
                }
            }
        });
    }

    int range = 9;  // to generate a single number with this range, by default its 0..9
    int length = 4; // by default length is 4
    public int generateRandomOTP() {
        int randomOTP;
        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }
        randomOTP = Integer.parseInt(s);
        return randomOTP;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_style);
        mMap.setMapStyle(mapStyleOptions);
        enableMyLocationIfPermitted();
        setCameraView();
        setUserCurrentLocationOnMap();
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //location automatically whn activity launches
    private void setCameraView() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mfusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    //this retrive current user lat lon positions for map to show from firebase
                    Location location = task.getResult();
                    final GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    //put 0.1 if you want full city map
                    //put 0.01 if you want zoomed in map
                    double bottomBoundary = geoPoint.getLatitude() - 0.1;
                    double leftBoundary = geoPoint.getLongitude() - 0.1;
                    double topBoundary = geoPoint.getLatitude() + 0.1;
                    double rightBoundary = geoPoint.getLongitude() + 0.1;
                    mMapBoundary = new LatLngBounds(new LatLng(bottomBoundary, leftBoundary),
                            new LatLng(topBoundary, rightBoundary));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));
                }
            }
        });
    }

    private void setUserCurrentLocationOnMap() {
        if (mMap != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mfusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {

                        Location location = task.getResult();
                        final GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        currentUserGeoPoints = geoPoint;
                        final Date timestamp = new Date();
                        final String uid = firebaseAuth.getUid();
                        //updated user location and details on firestore
                        mDb.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                final User user = documentSnapshot.toObject(User.class);
                                //this sets marker with user details like icon name etc


                                StorageReference ref = mStorageRef.child("images/" + uid).child("profilepic.jpg");
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Glide.with(payment.this)
                                                .asBitmap()
                                                .load(uri)
                                                .into(new CustomTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                        Bitmap bitmap = getCircularBitmap(resource);

                                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                                .position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()))
                                                                .icon(BitmapDescriptorFactory
                                                                        .fromBitmap(createCustomMarkerForUser
                                                                                (payment.this,
                                                                                        bitmap)))
                                                                .title(user.getUsername())
                                                                .zIndex(1.0f)//this zIndex makes marker to be on top of other markers
                                                                .snippet(user.getEmail()));

                                                        allMapMarkers.add(marker);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    }
                                                });


                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Glide.with(payment.this)
                                                .asBitmap()
                                                .load(userDefaultImageUrl)
                                                .into(new CustomTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                        Bitmap bitmap = getCircularBitmap(resource);

                                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                                .position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()))
                                                                .icon(BitmapDescriptorFactory
                                                                        .fromBitmap(createCustomMarkerForUser
                                                                                (payment.this,
                                                                                        bitmap)))
                                                                .title(user.getUsername())
                                                                .zIndex(1.0f)//this zIndex makes marker to be on top of other markers
                                                                .snippet(user.getEmail()));

                                                        allMapMarkers.add(marker);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    }
                                                });
                                    }
                                });


                                // ... get a map.//////////////////////////////////////////////////////////////////////////////////////
                                // Add a thin red line from London to New York.
                                //polyline
//                                Polyline line = mMap.addPolyline(new PolylineOptions()
//                                        .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
//                                        .width(5)
//                                        .color(Color.RED));


                            }
                        });

                        updateLocationAndTimeInFirestore(geoPoint, timestamp);
                    }
                }
            });
        }

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
        //markerImage.setImageResource(resource);


        //TextView txt_name = (TextView) marker.findViewById(R.id.name);
        //txt_name.setText(_name);
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


    private void updateLocationAndTimeInFirestore(GeoPoint geoPoint, Date timestamp) {
        final String uid = firebaseAuth.getUid();
        Map<String, Object> locationAndTime = new HashMap<>();
        locationAndTime.put("geo_point", geoPoint);
        locationAndTime.put("timestamp", timestamp);
        //note this set commont in firestore overright exiting user data
        // set() delet data and write new user data
        //whereas update only change exixting data and dont delet everything
        mDb.collection("users").document(uid).update(locationAndTime);

    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_user_marker_layout, null);
        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
        //TextView txt_name = (TextView) marker.findViewById(R.id.name);
        //txt_name.setText(_name);
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

    public static double distanceBetweenTwoCoordinates(double lat1, double lon1, double lat2, double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result in KM
        return (c * r);
    }


}