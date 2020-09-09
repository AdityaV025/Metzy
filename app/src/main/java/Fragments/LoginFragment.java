package Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.metzy.InfoActivity;
import com.example.metzy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private EditText mEmail, mPass;
    private Button mLoginBtn;
    private ProgressDialog mProgressDialog;
    private View view;
    private FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        init();

        return view;
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mEmail = view.findViewById(R.id.emailEditTextLogin);
        mPass = view.findViewById(R.id.passwordEditTextLogin);
        mLoginBtn = view.findViewById(R.id.loginBtn);
        mProgressDialog = new ProgressDialog(getContext());
        mLoginBtn.setOnClickListener(view1 -> {
            loginUser();
        });
    }

    private void loginUser() {
        if (TextUtils.isEmpty(mEmail.getText()) || TextUtils.isEmpty(mPass.getText())){
            Toast.makeText(getContext(), "Please provide all the information", Toast.LENGTH_SHORT).show();
        }else {
            mProgressDialog.setMessage("Logging, Please wait...");
            mProgressDialog.show();
            String email = mEmail.getText().toString();
            String password = mPass.getText().toString();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(getActivity(), InfoActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }else {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}