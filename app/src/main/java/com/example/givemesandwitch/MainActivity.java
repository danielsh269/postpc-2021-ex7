package com.example.givemesandwitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    SandwichOrderApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = SandwichOrderApplication.getInstance();
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sp = this.getSharedPreferences("local_db_orders", Context.MODE_PRIVATE);
        String id = sp.getString("order_id", "");

        if(id.equals(""))
        {
            Intent intent = new Intent(this, NewOrderActivity.class);
            startActivity(intent);
            finish();
        }

        else
        {
            Intent editOrderIntent = new Intent(this, EditOrderActivity.class);
            Intent inProgressIntent = new Intent(this, OrderInProgressActivity.class);
            db.collection("orders").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String status = documentSnapshot.getString("status");
                    if (status.equals("waiting"))
                    {
                        startActivity(editOrderIntent);
                        finish();
                    }
                    else if(status.equals("in-progress"))
                    {
                        startActivity(inProgressIntent);
                        finish();
                    }
                    else
                    {
                        //TODO - open "ready" activity
                    }
                }

            });


        }

    }
}