package com.example.givemesandwitch;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;


public class SandwichOrderApplication extends Application {

    private static String orderId;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        SharedPreferences sp = this.getSharedPreferences("local_db_orders", Context.MODE_PRIVATE);
        orderId = sp.getString("order_id", "");
    }

    public static String getOrderId() {
        return orderId;
    }
}
