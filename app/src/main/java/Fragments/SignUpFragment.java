package Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.metzy.InfoActivity;
import com.example.metzy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpFragment extends Fragment {

    private EditText mName, mAge, mPhoneNum, mEmail, mPass;
    private Button mCreateAccountBtn;
    private ProgressDialog mProgressDialog;
    private FirebaseFirestore db;
    private View view;
    private FirebaseAuth mAuth;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_signup, container, false);

        init();

        return view;
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mName = view.findViewById(R.id.nameEditText);
        mAge = view.findViewById(R.id.ageEditText);
        mPhoneNum = view.findViewById(R.id.phoneEditText);
        mEmail = view.findViewById(R.id.emailEditText);
        mPass = view.findViewById(R.id.passwordEditText);
        mCreateAccountBtn = view.findViewById(R.id.createAc);
        mProgressDialog = new ProgressDialog(getContext());
        mCreateAccountBtn.setOnClickListener(view1 -> {
            registerUser();
        });
    }

    private void registerUser() {
        if (TextUtils.isEmpty(mName.getText())
                || TextUtils.isEmpty(mAge.getText())
                || TextUtils.isEmpty(mPhoneNum.getText())
                || TextUtils.isEmpty(mEmail.getText())
                || TextUtils.isEmpty(mPass.getText())){
            Toast.makeText(getContext(), "Please provide all the information", Toast.LENGTH_SHORT).show();
        }else {
            mProgressDialog.setMessage("Creating Account, Please wait...");
            mProgressDialog.show();
            String email = mEmail.getText().toString();
            String password = mPass.getText().toString();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){

                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("name", mName.getText().toString());
                    userMap.put("age", mAge.getText().toString());
                    userMap.put("number", mPhoneNum.getText().toString());
                    userMap.put("email", email);

                    db.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).set(userMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){
                            mProgressDialog.dismiss();
                            Intent intent = new Intent(getActivity(), InfoActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}