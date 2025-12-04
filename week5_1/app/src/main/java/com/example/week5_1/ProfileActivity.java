package com.example.week5_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView imgProfile;
    TextView tvId, tvUsername, tvFullname, tvEmail, tvGender;
    Button btnLogout;
    ImageView btnBack;

    private ActivityResultLauncher<Intent> uploadLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newUrl = result.getData().getStringExtra("new_avatar_url");
                    if (newUrl != null) {
                        Glide.with(ProfileActivity.this)
                                .load(newUrl)
                                .into(imgProfile);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AnhXa();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UploadImageActivity.class);
                String userId = tvId.getText().toString().trim();
                intent.putExtra("id", userId);
//              intent.putExtra("username", tvUsername.getText().toString());
                uploadLauncher.launch(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        imgProfile = findViewById(R.id.profile_image);
        tvId = findViewById(R.id.tvId);
        tvUsername = findViewById(R.id.tvUsername);
        tvFullname = findViewById(R.id.tvFullname);
        tvEmail = findViewById(R.id.tvEmail);
        tvGender = findViewById(R.id.tvGender);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);
    }
}