package com.example.givemesandwitch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class NewOrderActivity extends AppCompatActivity {

    private CheckBox hummusCheckbox, tahiniCheckbox;
    private EditText nameText, commentText, picklesText;
    private Button saveButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neworder);

        hummusCheckbox = findViewById(R.id.hummusCheckbox);
        tahiniCheckbox = findViewById(R.id.tahiniCheckbox);
        nameText = findViewById(R.id.customerNameText);
        commentText = findViewById(R.id.commentText);
        picklesText = findViewById(R.id.picklesText);
        saveButton = findViewById(R.id.saveButton);

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

                //TODO - save the id of the order in the phone with SP
                //TODO - start activity of edit order

            }
        });



    }
}
