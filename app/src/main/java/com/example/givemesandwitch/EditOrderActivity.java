package com.example.givemesandwitch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditOrderActivity extends AppCompatActivity {


    private CheckBox hummusCheckbox, tahiniCheckbox;
    private EditText commentText, picklesText;
    private Button saveButton;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorder);

        hummusCheckbox = findViewById(R.id.hummusCheckbox);
        tahiniCheckbox = findViewById(R.id.tahiniCheckbox);
        commentText = findViewById(R.id.commentText);
        picklesText = findViewById(R.id.picklesText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment = commentText.getText().toString();
                int pickles = Integer.parseInt(picklesText.getText().toString());
                boolean hummus = hummusCheckbox.isChecked();
                boolean tahini = tahiniCheckbox.isChecked();

                //TODO - updated the firestore according to the Views

            }
        });
    }
}
