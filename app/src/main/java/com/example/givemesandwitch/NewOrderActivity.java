package com.example.givemesandwitch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class NewOrderActivity extends AppCompatActivity {

    private CheckBox hummusCheckbox, tahiniCheckbox;
    private EditText nameText, commentText, picklesText;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neworder);
        sp = this.getSharedPreferences("local_db_orders", Context.MODE_PRIVATE);;
        hummusCheckbox = findViewById(R.id.hummusCheckbox);
        tahiniCheckbox = findViewById(R.id.tahiniCheckbox);
        nameText = findViewById(R.id.customerNameText);
        commentText = findViewById(R.id.commentText);
        picklesText = findViewById(R.id.picklesText);
        Button saveButton = findViewById(R.id.saveButton);

        if (savedInstanceState != null)
        {
            nameText.setText(savedInstanceState.getString("name"));
            commentText.setText(savedInstanceState.getString("comment"));
            picklesText.setText(savedInstanceState.getString("pickles"));
            hummusCheckbox.setChecked(Boolean.parseBoolean(savedInstanceState.getString("hummus")));
            tahiniCheckbox.setChecked(Boolean.parseBoolean(savedInstanceState.getString("tahini")));
        }
        Intent intent = new Intent(this, EditOrderActivity.class);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String comment = commentText.getText().toString();
                int pickles = Integer.parseInt(picklesText.getText().toString());
                boolean hummus = hummusCheckbox.isChecked();
                boolean tahini = tahiniCheckbox.isChecked();

                SandwichOrder so = new SandwichOrder(name,pickles,hummus,tahini,comment);
                db.collection("orders").document(so.getId()).set(so);

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("order_id", so.getId());
                editor.apply();

                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", nameText.getText().toString());
        outState.putString("comment", commentText.getText().toString());
        outState.putString("pickles", picklesText.getText().toString());
        boolean hummus = hummusCheckbox.isChecked();
        boolean tahini = tahiniCheckbox.isChecked();
        outState.putString("hummus", String.valueOf(hummus));
        outState.putString("tahini", String.valueOf(tahini));
    }
}
