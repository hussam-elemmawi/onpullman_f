package io.hussam.westarmy.onpullman.photos;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import io.hussam.westarmy.onpullman.R;

import io.hussam.westarmy.onpullman.util.StorageUtil;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_viewer_act);

        ImageView imageView = findViewById(R.id.fullImageView);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String photoPath = bundle.getString("photoPath");

            Uri photoUri = StorageUtil.buildPhotoUri(PhotoViewerActivity.this,
                    R.string.file_provider_authority, new File(photoPath));
            if (photoUri != null) {
                Glide.with(PhotoViewerActivity.this).load(photoUri).into(imageView);
            }
        }
    }
}
