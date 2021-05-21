package com.example.givemesandwitch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class OrderInProgressActivity extends AppCompatActivity {

    ListenerRegistration listener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderinprogress);
        ProgressBar progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sp = this.getSharedPreferences("local_db_orders", Context.MODE_PRIVATE);
        String orderId = sp.getString("order_id", "");
        db.collection("orders").document(orderId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("customerName");
                TextView textView = findViewById(R.id.textInProgress);
                String txt = name + ", " + textView.getText().toString();
                textView.setText(txt);
            }
        });

        Intent readyIntent = new Intent(this, OrderReadyActivity.class);
        listener = db.collection("orders").document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error)
                    {
                        if (error != null || value == null)
                        {
                            System.out.println("error in the snapshot listener in editOrderActivity");
                        }
                        else
                        {
                            String status = value.getString("status");
                            if (status != null && status.equals("ready"))
                            {
                                startActivity(readyIntent);
                                finish();

                            }
                        }
                    }
                });
    } // end of onCreate()

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listener.remove();
    }
}
