package com.example.rongbuzz.citicitichat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.R.attr.button;
import static android.R.attr.colorPrimary;
import static android.R.attr.drawable;
import static android.R.attr.visibility;
import static android.R.attr.visible;

public class Signup extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText emailT, passwordT, nameT;
    Button signupBtn , loginBtn;
    CheckBox ihaachackbox;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //firebase initilation
        mAuth = FirebaseAuth.getInstance();
        //ui initilation
        emailT = (EditText) findViewById(R.id.emailTi);
        passwordT = (EditText) findViewById(R.id.passwordTi);
        nameT = (EditText) findViewById(R.id.usernameTi);

        String userName = nameT.getText().toString().trim();

        signupBtn = (Button) findViewById(R.id.signupBt);
        loginBtn = (Button) findViewById(R.id.loginBt);
        ihaachackbox = (CheckBox) findViewById(R.id.iArhacheck);

        ihaachackbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ihaachackbox.isChecked()){
                    signupBtn.setVisibility(View.INVISIBLE);
                    nameT.setVisibility(View.INVISIBLE);
                }else {
                    signupBtn.setVisibility(View.VISIBLE);
                    nameT.setVisibility(View.VISIBLE);
                }
            }
        });

        //signUp function added here
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize signup method
                signup();
            }
        });

        //login method added here
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize login method
                login();
            }
        });

    }//onCreate end here

    //signup method
    private void signup(){

        //getting email and password from user
        String email = emailT.getText().toString().trim();
        String password = passwordT.getText().toString().trim();

        //checking textInput are not empty
        if (emailT !=null && passwordT !=null) {

            //sign up with email password
            //checking edit field are empty or not
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(), "Authintication is not sucsessfull !", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            emailT.setHint("email are empty");
            passwordT.setHint("password are empty");
        }
    }

    //login method
    private void login(){
        //getting email and password from user
        String email = emailT.getText().toString().trim();
        String password = passwordT.getText().toString().trim();

        if (emailT !=null && passwordT !=null) {

            //sign up with email password
            //checking edit field are empty or not
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(), "login problem !", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            emailT.setHint("email are empty");
            passwordT.setHint("password are empty");
        }
    }

}
