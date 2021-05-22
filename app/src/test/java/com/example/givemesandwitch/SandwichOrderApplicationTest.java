package com.example.givemesandwitch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.StructuredQuery;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class SandwichOrderApplicationTest extends TestCase {

    @Test
    public void test_whenFirstLaunchTheAppAndPressMakeOrderButton_Then_OpenNewOrderActivity()
    {
        MainActivity activity = Robolectric.buildActivity(MainActivity.class).create().visible().get();
        Button b = activity.findViewById(R.id.makeOrderButton);
        b.performClick();

        Intent expected = new Intent(activity, NewOrderActivity.class);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expected.getComponent(), actual.getComponent());
    }

    @Test
    public void whenAddingNewOrder_Then_CheckThatTheOrderStoredInFirestoreAsExpected()
    {
        NewOrderActivity activity = Robolectric.buildActivity(NewOrderActivity.class).create().visible().get();
        CheckBox hummusCheckbox = activity.findViewById(R.id.hummusCheckbox);
        CheckBox tahiniCheckbox = activity.findViewById(R.id.tahiniCheckbox);
        EditText nameText = activity.findViewById(R.id.customerNameText);
        EditText commentText = activity.findViewById(R.id.commentText);
        EditText picklesText = activity.findViewById(R.id.picklesText);
        Button saveButton = activity.findViewById(R.id.saveButton);
        hummusCheckbox.setChecked(true);
        tahiniCheckbox.setChecked(true);
        nameText.setText("test customer");
        commentText.setText("test comment");
        picklesText.setText("5");
        saveButton.performClick();
        SharedPreferences sp;
        sp = SandwichOrderApplication.getInstance().getSharedPreferences("local_db_orders", Context.MODE_PRIVATE);
        String id = sp.getString("order_id", "");
        assertFalse(id.equals(""));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertEquals("test customer", documentSnapshot.getString("name"));
                assertEquals("test comment", documentSnapshot.getString("comment"));
                assertEquals(5, documentSnapshot.get("pickles"));
                assertTrue(documentSnapshot.getBoolean("tahini"));
                assertTrue(documentSnapshot.getBoolean("hummus"));
            }
        });
    }

    @Test
    public void whenAdminMarkOrderAsInProgress_Then_EditOrderActivityOpen()
    {
        NewOrderActivity activity = Robolectric.buildActivity(NewOrderActivity.class).create().visible().get();
        CheckBox hummusCheckbox = activity.findViewById(R.id.hummusCheckbox);
        CheckBox tahiniCheckbox = activity.findViewById(R.id.tahiniCheckbox);
        EditText nameText = activity.findViewById(R.id.customerNameText);
        EditText commentText = activity.findViewById(R.id.commentText);
        EditText picklesText = activity.findViewById(R.id.picklesText);
        Button saveButton = activity.findViewById(R.id.saveButton);
        hummusCheckbox.setChecked(true);
        tahiniCheckbox.setChecked(true);
        nameText.setText("test customer");
        commentText.setText("test comment");
        picklesText.setText("5");
        saveButton.performClick();
        SharedPreferences sp;
        sp = SandwichOrderApplication.getInstance().getSharedPreferences("local_db_orders", Context.MODE_PRIVATE);
        String id = sp.getString("order_id", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                db.collection("orders").document(id).update("status", "in-progress");
                Intent expected = new Intent(activity, EditOrderActivity.class);
                Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
                assertEquals(expected.getComponent(), actual.getComponent());
            }
        });
    }
}
