package com.kangkan.developer.tutortuitionbd;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Log_In_Activity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin;
    EditText input_email,input_password;
    //TextView textView;
    private ProgressBar progressBar;

    LinearLayout activity_main;

    Dialog myDialog;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);

        myDialog = new Dialog(this);


        btnLogin = findViewById(R.id.login_btn_login);
        input_email = findViewById(R.id.login_email);
        input_password=findViewById(R.id.login_password);
       // textView=findViewById(R.id.txt_Sign_In);

        progressBar = findViewById(R.id.progressbar1);
        progressBar.setVisibility(View.GONE);

       // myDialog = new Dialog(this);

        btnLogin.setOnClickListener(this);
       // textView.setOnClickListener(this);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Check already session , if ok-> DashBoard
        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(Log_In_Activity.this,DashBoard_teacher_Activity.class));
            finish();
        }
        else if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Log_In_Activity.this,DashBoard_student_Activity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_btn_login)
        {

            if (input_email.length() == 0) {
                Toast.makeText(getApplicationContext(), "Enter your Email first", Toast.LENGTH_SHORT).show();
            }
            else if (input_password.length() == 0) {
                Toast.makeText(getApplicationContext(), "Enter your Password first", Toast.LENGTH_SHORT).show();
            }
            else {
                loginUser(input_email.getText().toString(),input_password.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);
            }
        }
    }


    private void loginUser(final String email, final String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (email.isEmpty()) {
                            input_email.setError(getString(R.string.input_error_email));
                            input_email.requestFocus();
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            return;
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            input_email.setError(getString(R.string.input_error_email_invalid));
                            input_email.requestFocus();
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            return;
                        }

                        if (password.isEmpty()) {
                            input_password.setError(getString(R.string.input_error_password));
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            return;
                        }

                        if (password.length() < 6) {
                            input_password.setError(getString(R.string.input_error_password_length));
                        input_password.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                        return;
                    }

                        if(!task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Log In Error", Toast.LENGTH_SHORT).show();
                            btnLogin.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            //startActivity(new Intent(log_in.this,DashBoard.class));
                            Intent intent=new Intent(Log_In_Activity.this,DashBoard_teacher_Activity.class);
                            startActivity(intent);
                            finish();
                        }


                    }
                });
    }

    public void ShowPopup(View v) {
        myDialog.setContentView(R.layout.pop_up);

        Button button1,button2;

        button1 = myDialog.findViewById(R.id.btn_teacher_lonin);

        button2 = myDialog.findViewById(R.id.btn_Student_lonin);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Log_In_Activity.this,Sign_In_Teacher_Activity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Log_In_Activity.this,Sign_In_Student_Activity.class);
                startActivity(intent);
            }
        });



        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


}
