package com.justice.insight.authenticate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.justice.insight.R;
import com.justice.insight.prisoner.family.Family_Activity;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth authentication;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private HashMap<String, Object> map = new HashMap<>();
    public static String uid;
    private SharedPreferences remember;


    public int password_visiblity;
    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private DatabaseReference login1 = _firebase.getReference("login_saved/username/cases");
    private ChildEventListener _login1_child_listener;
    public Intent prisoner_sub_opener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        blockss();
        FirebaseApp.initializeApp(this);
        authentication = FirebaseAuth.getInstance();
        intialize_login();
        prisoner_sub_opener = new Intent();
        login1.removeEventListener(_login1_child_listener);
        remember = getSharedPreferences("remembee", Activity.MODE_PRIVATE);

    }

     public void intialize_login(){
         TextInputEditText reference_id = (TextInputEditText) findViewById(R.id.reference_id);
         TextInputEditText reference_password = (TextInputEditText) findViewById(R.id.reference_password);
         ProgressBar sign_up_progress = (ProgressBar) findViewById(R.id.sign_up_progress);
         Button sign_in = findViewById(R.id.sign_in);
         TextInputLayout password_layout = (TextInputLayout)findViewById(R.id.password_layout);
         password_layout.setEndIconOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 if (password_visiblity == 0){
                     password_visiblity++;

                    reference_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                     password_layout.setEndIconDrawable(R.drawable.baseline_visibility_24);
                 } else if (password_visiblity ==1){
                     password_visiblity=0;
                     reference_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                     password_layout.setEndIconDrawable(R.drawable.baseline_visibility_off_24);

                 }

             }
         });
        sign_in.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (reference_id.getText().toString().equals("") || reference_password.getText().toString().equals("")) {
                     if (reference_id.getText().toString().equals("") && reference_password.getText().toString().equals("") ) {
                         reference_id.setError("Reference id cannot be empty");
                         reference_password.setError("Reference password cannot be empty");

                     } else {

                         if (reference_id.getText().toString().equals("")) {
                             reference_id.setError("Reference id cannot be empty");
                         }
                         if (reference_password.getText().toString().equals("")) {
                             reference_password.setError("Reference password cannot be empty");
                         }
                     }
                 } else {
                     sign_up_progress.setVisibility(View.VISIBLE);

                     authentication.signInWithEmailAndPassword(reference_id.getText().toString().concat("@gmail.com"), reference_password.getText().toString()).addOnCompleteListener(LoginActivity.this, _auth_sign_in_listener);

                 }
             }
         });
        _auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(Task<AuthResult> _param1) {
                 final boolean _success = _param1.isSuccessful();
                 final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
                 if (_success){
                     login1.addChildEventListener(_login1_child_listener);
                 }else{
                     ProgressBar sign_up_progress = (ProgressBar) findViewById(R.id.sign_up_progress);
                     sign_up_progress.setVisibility(View.GONE);
                     showMessage(getApplicationContext(),_errorMessage);
                 }
             }
         };

        _login1_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    if (_childValue.containsKey("DEVICEID")) {
                        if (_childValue.get("DEVICEID").toString().equals(Build.ID)) {
                            prisoner_sub_opener.setClass(getApplicationContext(), Family_Activity.class);
                            startActivity(prisoner_sub_opener);
                           remember.edit().putString("email", reference_id.getText().toString()).commit();
                            remember.edit().putString("password", reference_password.getText().toString()).commit();
                           uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Toast.makeText(LoginActivity.this, "LOGGED IN SUCCESSFULLY!! ", Toast.LENGTH_LONG);
                        } else {
                            Toast.makeText(LoginActivity.this, "DIFFRENT DEVICE ID! ", Toast.LENGTH_LONG);
                        }
                    } else {
                        map = new HashMap<>();
                        map.put("DEVICEID", Build.ID);
                        login1.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
                        map.clear();
                        prisoner_sub_opener.setClass(getApplicationContext(), Family_Activity.class);
                        startActivity(prisoner_sub_opener);

                        remember.edit().putString("email", reference_id.getText().toString()).commit();
                        remember.edit().putString("password", reference_password.getText().toString()).commit();
                        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Toast.makeText(LoginActivity.this, "THIS DEVICE ID LINKED!! ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    if (_childValue.containsKey("DEVICEID")) {
                        if (_childValue.get("DEVICEID").toString().equals(Build.ID)) {
                            prisoner_sub_opener.setClass(getApplicationContext(), Family_Activity.class);
                            remember.edit().putString("email", reference_id.getText().toString()).commit();
                            remember.edit().putString("password", reference_password.getText().toString()).commit();
                            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Toast.makeText(LoginActivity.this, "LOGGED IN SUCCESSFULLY!! ", Toast.LENGTH_LONG);

                        } else {
                            Toast.makeText(LoginActivity.this, "DIFFRENT DEVICE ID! ", Toast.LENGTH_LONG);
                        }
                    } else {
                        map = new HashMap<>();
                        map.put("DEVICEID", Build.ID);
                        login1.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
                        map.clear();
                        prisoner_sub_opener.setClass(getApplicationContext(), Family_Activity.class);

                        remember.edit().putString("email", reference_id.getText().toString()).commit();
                        remember.edit().putString("password", reference_password.getText().toString()).commit();
                        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Toast.makeText(LoginActivity.this, "THIS DEVICE ID LINKED!! ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
     }
     public void blockss()
     {
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
     }
     public static void showMessage(Context _context, String _s) {
        Toast.makeText(_context, _s, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}