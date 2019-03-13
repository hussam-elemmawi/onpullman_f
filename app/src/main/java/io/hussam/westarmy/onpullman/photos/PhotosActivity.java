package io.hussam.westarmy.onpullman.photos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import io.hussam.westarmy.onpullman.R;
import io.hussam.westarmy.onpullman.data.model.DayInfo;
import io.hussam.westarmy.onpullman.util.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class PhotosActivity extends AppCompatActivity {

    private PhotoAdapter listAdapter;
    private DayInfo dayInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_act);

        init();
        initViews();
    }

    private void init() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            dayInfo = new DayInfo(bundle.getString("date"), bundle.getInt("pullmanId"));
        }
        listAdapter = new PhotoAdapter(new ArrayList<String>());
    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.photosRecyclerView);
        recyclerView.setAdapter(listAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listAdapter.replaceData(StorageUtil.getAllPullmanDayPhotosPaths(dayInfo));
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

        private List<String> photosUriList;

        PhotoAdapter(List<String> photos) {
            setList(photos);
        }

        void replaceData(List<String> photos) {
            if (photos != null) {
                setList(photos);
                notifyDataSetChanged();
            }
        }

        void setList(List<String> photos) {
            photosUriList = checkNotNull(photos);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return photosUriList.size();
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.photo_item, parent, false);
            final PhotoViewHolder vh = new PhotoViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

            final String photoPath = photosUriList.get(position);
            Uri photoUri = null;
            photoUri = StorageUtil.buildPhotoUri(PhotosActivity.this,
                    R.string.file_provider_authority, new File(photoPath));
            if (photoUri != null) {
                Glide.with(PhotosActivity.this).load(photoUri).into(holder.imageView);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PhotosActivity.this, PhotoViewerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("photoPath", photoPath);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.pic);
        }
    }
}
