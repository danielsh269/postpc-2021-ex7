package com.example.givemesandwitch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderReadyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderready);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sp = this.getSharedPreferences("local_db_orders", Context.MODE_PRIVATE);
        String orderId = sp.getString("order_id", "");
        db.collection("orders").document(orderId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("customerName");
                TextView textView = findViewById(R.id.readyText);
                String txt = name + ", " + textView.getText().toString();
                textView.setText(txt);
            }
        });

        Button doneButton = findViewById(R.id.doneButton);
        Intent intent = new Intent(this, MainActivity.class);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(orderId);
                editor.apply();
                db.collection("orders").document(orderId).update("status", "done");
                startActivity(intent);
                finish();
            }
        });
    }
}
