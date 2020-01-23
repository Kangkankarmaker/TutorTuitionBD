package com.kangkan.developer.tutortuitionbd;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kangkan.developer.tutortuitionbd.DB_Connect.User;

import java.io.IOException;

@SuppressLint("Registered")
public class Sign_In_Teacher_Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone,edit_text_institute;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    ImageView SelectImage;
    ImageView ChooseButton;
    Uri FilePathUri;

    Button button;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    String Storage_Path = "Image/";

    int Image_Request_Code = 7;

    @SuppressLint({"WrongViewCast", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_teacher);

        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPhone = findViewById(R.id.edit_text_phone);
        edit_text_institute=findViewById(R.id.edit_text_institute);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        button=findViewById(R.id.button_register);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_register).setOnClickListener(this);

        /////
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        ChooseButton = findViewById(R.id.ShowImageView);
        SelectImage = findViewById(R.id.ShowImageView);

        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);
                // After selecting image change choose button above text.
               // ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    private void registerUser() {

        button.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String institute = edit_text_institute.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.input_error_name));
            editTextName.requestFocus();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            return;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError(getString(R.string.input_error_phone));
            editTextPhone.requestFocus();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            return;
        }

        if (phone.length() != 11) {
            editTextPhone.setError(getString(R.string.input_error_phone_invalid));
            editTextPhone.requestFocus();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            return;
        }

        if (institute.isEmpty()) {
            edit_text_institute.setError(getString(R.string.input_error_ins));
            edit_text_institute.requestFocus();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            return;
        }
        if (FilePathUri != null) {
            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            final String imageURL=taskSnapshot.getDownloadUrl().toString();

                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            //button.setVisibility(View.VISIBLE);

                                            if (task.isSuccessful()) {

                                                User user = new User(
                                                        name,
                                                        email,
                                                        phone,
                                                        imageURL,
                                                        institute
                                                );

                                                FirebaseDatabase.getInstance().getReference("Teacher")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressBar.setVisibility(View.GONE);
                                                        if (task.isSuccessful()) {

                                                            Toast.makeText(Sign_In_Teacher_Activity.this, "welcome To Tutor & Tuition BD", Toast.LENGTH_LONG).show();
                                                            Intent intent=new Intent(Sign_In_Teacher_Activity.this,DashBoard_teacher_Activity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            //button.setVisibility(View.VISIBLE);
                                                        } else {
                                                            //display a failure message
                                                            progressBar.setVisibility(View.GONE);
                                                            button.setVisibility(View.VISIBLE);


                                                        }
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(Sign_In_Teacher_Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                button.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });


                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Showing exception erro message.
                            Toast.makeText(Sign_In_Teacher_Activity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            button.setVisibility(View.VISIBLE);
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // Setting progressDialog Title.
                           // progressBar.setVisibility(View.GONE);
                            //button.setVisibility(View.VISIBLE);
                        }
                    });
        }
        else {
            Toast.makeText(Sign_In_Teacher_Activity.this, "Please Select Image", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }

        progressBar.setVisibility(View.VISIBLE);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                registerUser();
                break;
        }
    }
}

