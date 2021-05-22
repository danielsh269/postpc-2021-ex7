package com.example.givemesandwitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Button makeOrderButton = findViewById(R.id.makeOrderButton);
        Intent intent = new Intent(this, NewOrderActivity.class);
        makeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                finish();
            }
        });

        if (!id.equals(""))
        {
            Intent editOrderIntent = new Intent(this, EditOrderActivity.class);
            Intent inProgressIntent = new Intent(this, OrderInProgressActivity.class);
            Intent readyIntent = new Intent(this, OrderReadyActivity.class);

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
                    else if(status.equals("ready"))
                    {
                        startActivity(readyIntent);
                        finish();
                    }
                }
            });
        }

    }

}