package com.example.week5_1;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageActivity extends AppCompatActivity {


    private static final String TAG = UploadImageActivity.class.getName();
    public static final int MY_REQUEST_CODE = 100;

    Button btnChoose, btnUpload;
    ImageView imageViewChoose;
    String mId;
    EditText editTextUserName;
    TextView textViewUsername;
    TextView tvIdDisplay, tvUsernameDisplay;
    private Uri mUri;
    private ProgressDialog mProgressDialog;

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) return;
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageViewChoose.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        AnhXa();

        Intent intent = getIntent();
        if (intent != null) {
            String idReceived = intent.getStringExtra("id");
            String usernameReceived = intent.getStringExtra("username");

            tvIdDisplay.setText(idReceived);
            tvUsernameDisplay.setText(usernameReceived);

            mId = idReceived;
        }
        mProgressDialog = new ProgressDialog(UploadImageActivity.this);
        mProgressDialog.setMessage("Please wait upload...");
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
            }
        });
        btnUpload.setOnClickListener(v -> {
            if (mUri != null) {
                UploadImage1();
            }
        });
    }

    private void AnhXa() {
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageViewChoose = findViewById(R.id.imgChoose);
        tvIdDisplay = findViewById(R.id.tvIdDisplay);
        tvUsernameDisplay = findViewById(R.id.tvUsernameDisplay);
    }

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= 33) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(permissions()[0]) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            requestPermissions(permissions(), MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    public void UploadImage1() {
        mProgressDialog.show();


        String username = editTextUserName.getText().toString().trim();
//        RequestBody requestUsername = RequestBody.create(MediaType.parse("multipart/form-data"), username);
        RequestBody requestId = RequestBody.create(MediaType.parse("multipart/form-data"), mId);
        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
        Log.e("Upload", "Image Path: " + IMAGE_PATH);

        File file = new File(IMAGE_PATH);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part partbodyavatar = MultipartBody.Part.createFormData(Const.MY_IMAGES, file.getName(), requestFile);

//        APIService.servieapi.upload(requestUsername, partbodyavatar).enqueue(new Callback<List<ImageUpload>>() {
        APIService.servieapi.upload(requestId, partbodyavatar).enqueue(new Callback<List<ImageUpload>>() {
        @Override
            public void onResponse(Call<List<ImageUpload>> call, Response<List<ImageUpload>> response) {
                mProgressDialog.dismiss();
                List<ImageUpload> imageUpload = response.body();
                if (imageUpload != null && imageUpload.size() > 0) {
//                    String newAvatarUrl = imageUpload.get(0).getAvatar();

//                    textViewUsername.setText(imageUpload.get(0).getUsername());
//                    Glide.with(UploadImageActivity.this).load(newAvatarUrl).into(imageViewChoose);
                    Toast.makeText(UploadImageActivity.this, "Thành công", Toast.LENGTH_LONG).show();
                    Intent returnIntent = new Intent();
//                    returnIntent.putExtra("new_avatar_url", newAvatarUrl);
                    returnIntent.putExtra("new_avatar_url", imageUpload.get(0).getAvatar());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(UploadImageActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ImageUpload>> call, Throwable t) {
                mProgressDialog.dismiss();
                Log.e("TAG", t.toString());
                Toast.makeText(UploadImageActivity.this, "Gọi API thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }
}