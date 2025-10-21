package com.example.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.whatsapp.databinding.ActivitySettingsBinding;
import com.example.whatsapp.databinding.ActivitySignInBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySettingsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database.getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("userName")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            String userName = task.getResult().getValue(String.class);
                            binding.uName.setText(userName);
                        } else {
                            binding.uName.setText("No username found");
                        }
                    } else {
                        binding.uName.setText("Failed to load");
                    }
                });
        database.getInstance()
                .getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("status")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            String statss = task.getResult().getValue(String.class);
                            binding.status.setText(statss);
                        } else {
                            binding.status.setText("No status found");
                        }
                    } else {
                        binding.status.setText("Failed to load");
                    }
                });

        binding.backBtnImg.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finishAffinity();
        });
        binding.addSignToPicImg.setOnClickListener(v->{
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,101);
        });
        binding.btnSave.setOnClickListener(v->{
            String userName=binding.uName.getText().toString();
            String about=binding.status.getText().toString();

            HashMap<String ,Object> obj=new HashMap<>();
            obj.put("userName",userName);
            obj.put("status",about);

            database.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                    .updateChildren(obj);
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
            binding.status.setText(about);
            binding.uName.setText(userName);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK){
            binding.userProfilePic.setImageURI(data.getData());

        }else{
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }
}