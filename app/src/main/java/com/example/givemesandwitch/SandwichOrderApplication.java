package com.example.givemesandwitch;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


public class SandwichOrderApplication extends Application {

    private static SandwichOrderApplication instance = null;
    private String orderId;
    @Override
    public void onCreate() {
        super.onCreate();
//        FirebaseFirestoreSettings.Builder b = new FirebaseFirestoreSettings.Builder();
//        b.setHost("127.0.0.1:8080")
//                .setSslEnabled(false)
//                .setPersistenceEnabled(false)
//                .build();
//        FirebaseFirestore.setLoggingEnabled(true);
        FirebaseApp.initializeApp(this);
        instance = this;

        SharedPreferences sp = this.getSharedPreferences("local_db_orders", Context.MODE_PRIVATE);
        orderId = sp.getString("order_id", "");
    }

    public static SandwichOrderApplication getInstance()
    {
        return instance;
    }
    public String getOrderId() {
        return orderId;
    }
}
