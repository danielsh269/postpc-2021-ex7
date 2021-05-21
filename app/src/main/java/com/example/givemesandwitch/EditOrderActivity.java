package com.example.givemesandwitch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class EditOrderActivity extends AppCompatActivity {


    private CheckBox hummusCheckbox, tahiniCheckbox;
    private EditText commentText, picklesText;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListenerRegistration listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorder);

        hummusCheckbox = findViewById(R.id.hummusCheckbox);
        tahiniCheckbox = findViewById(R.id.tahiniCheckbox);
        commentText = findViewById(R.id.commentText);
        picklesText = findViewById(R.id.picklesText);
        Button saveButton = findViewById(R.id.saveButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        SharedPreferences sp = this.getSharedPreferences("local_db_orders", Context.MODE_PRIVATE);
        String orderId = sp.getString("order_id", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (savedInstanceState != null)
        {
            commentText.setText(savedInstanceState.getString("comment"));
            picklesText.setText(savedInstanceState.getString("pickles"));
            hummusCheckbox.setChecked(Boolean.parseBoolean(savedInstanceState.getString("hummus")));
            tahiniCheckbox.setChecked(Boolean.parseBoolean(savedInstanceState.getString("tahini")));
        }
        else
        {
            db.collection("orders").document(orderId).get().
                    addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String comment = documentSnapshot.getString("comment");
                            Number pickles = (Number)documentSnapshot.get("pickles");
                            Boolean hummus = documentSnapshot.getBoolean("hummus");
                            Boolean tahini = documentSnapshot.getBoolean("tahini");

                            hummusCheckbox.setChecked(hummus);
                            tahiniCheckbox.setChecked(tahini);
                            commentText.setText(comment);
                            picklesText.setText(pickles.toString());

                        }
                    });
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment = commentText.getText().toString();
                int pickles = Integer.parseInt(picklesText.getText().toString());
                boolean hummus = hummusCheckbox.isChecked();
                boolean tahini = tahiniCheckbox.isChecked();

                db.collection("orders").document(orderId).update("comment", comment);
                db.collection("orders").document(orderId).update("pickles", pickles);
                db.collection("orders").document(orderId).update("hummus", hummus);
                db.collection("orders").document(orderId).update("tahini", tahini);


            }
        });

        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("order_id");
                editor.apply();
                db.collection("orders").document(orderId).delete();
                startActivity(mainActivityIntent);
                finish();
            }
        });

        Intent inProgressIntent = new Intent(this, OrderInProgressActivity.class);
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
                            if (status != null && status.equals("in-progress"))
                            {
                                startActivity(inProgressIntent);
                                finish();

                            }
                            else if (status != null && status.equals("ready"))
                            {
                                startActivity(readyIntent);
                                finish();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listener.remove();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("comment", commentText.getText().toString());
        outState.putString("pickles", picklesText.getText().toString());
        boolean hummus = hummusCheckbox.isChecked();
        boolean tahini = tahiniCheckbox.isChecked();
        outState.putString("hummus", String.valueOf(hummus));
        outState.putString("tahini", String.valueOf(tahini));
    }
}
