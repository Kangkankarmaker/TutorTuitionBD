package com.kangkan.developer.tutortuitionbd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashBoard_student_Activity extends AppCompatActivity {

    TextView textView1,textView2,textView3,wl;
    Button logout;
    ImageView imageView;
    private FirebaseAuth auth;
    DatabaseReference ref;

    FirebaseUser firebaseUser;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_student);
        textView1=findViewById(R.id.txt1);
        textView2=findViewById(R.id.txt2);
        textView3=findViewById(R.id.txt3);
        wl=findViewById(R.id.wel);
        imageView=findViewById(R.id.img);
        logout=findViewById(R.id.logout);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                if(auth.getCurrentUser() == null)
                {
                    startActivity(new Intent(DashBoard_student_Activity.this,Log_In_Activity.class));
                    finish();
                }
            }
        });

        //Init Firebase
        auth = FirebaseAuth.getInstance();

        //Session check
        if(auth.getCurrentUser() != null) wl.setText("Welcome , "+auth.getCurrentUser().getEmail());


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        uid=firebaseUser.getUid();

        ref= FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    Glide.with(getApplicationContext()).load(ds.child(uid).child("imageURL").getValue().toString()).into(imageView);

                    String name=ds.child(uid).child("name").getValue(String.class);
                    String email=ds.child(uid).child("email").getValue(String.class);
                    String phn=ds.child(uid).child("phone").getValue(String.class);

                    String ins=ds.child(uid).child("institute").getValue(String.class);

                    textView1.setText(name);
                    textView2.setText(email);
                    textView3.setText(phn+"        "+ins);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(DashBoard_student_Activity.this, "DB Error", Toast.LENGTH_LONG).show();

            }
        });

    }
}



