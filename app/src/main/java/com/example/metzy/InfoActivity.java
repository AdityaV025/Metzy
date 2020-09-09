package com.example.metzy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InfoActivity extends AppCompatActivity {

    private EditText mName, mAge, mPhoneNum, mEmail;
    private Button mUpdateBtn;
    private ProgressDialog mProgressDialog;
    private TextView mLogOutBtn;
    private FirebaseFirestore db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
        fetchInfo();
    }

    private void init() {
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db = FirebaseFirestore.getInstance();
        mName = findViewById(R.id.nameEditTextUpdate);
        mAge = findViewById(R.id.ageEditTextUpdate);
        mPhoneNum = findViewById(R.id.phoneEditTextUpdate);
        mEmail = findViewById(R.id.emailEditTextUpdate);
        mLogOutBtn = findViewById(R.id.logoutBtn);
        mUpdateBtn = findViewById(R.id.UpdateBtn);
        mProgressDialog = new ProgressDialog(this);
        mLogOutBtn.setOnClickListener(view -> {
            mProgressDialog.setMessage("Logging Out...");
            mProgressDialog.show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            mProgressDialog.dismiss();
        });
        mUpdateBtn.setOnClickListener(view1 -> {
            updateInfo();
        });
    }

    private void fetchInfo() {
        mProgressDialog.setMessage("Fetching Info, Please wait...");
        mProgressDialog.show();
        DocumentReference documentReference = db.collection("Users").document(uid);
        Log.d("ahsdkja", uid);
        documentReference.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               DocumentSnapshot docRef = task.getResult();
               mName.setText(Objects.requireNonNull(Objects.requireNonNull(docRef).get("name")).toString());
               mAge.setText(Objects.requireNonNull(docRef.get("age")).toString());
               mPhoneNum.setText(Objects.requireNonNull(docRef.get("number")).toString());
               mEmail.setText(Objects.requireNonNull(docRef.get("email")).toString());
               mProgressDialog.dismiss();
           }
        });
    }

    private void updateInfo() {
        if (TextUtils.isEmpty(mName.getText())
                || TextUtils.isEmpty(mAge.getText())
                || TextUtils.isEmpty(mPhoneNum.getText())
                || TextUtils.isEmpty(mEmail.getText())){
            Toast.makeText(this, "Please provide all the information", Toast.LENGTH_SHORT).show();
        }else {
            mProgressDialog.setMessage("Updating Info, Please wait...");
            mProgressDialog.show();

            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("name", mName.getText().toString());
            updateMap.put("age", mAge.getText().toString());
            updateMap.put("number", mPhoneNum.getText().toString());
            updateMap.put("email", mEmail.getText().toString());
            db.collection("Users").document(uid).update(updateMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    mProgressDialog.dismiss();
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}