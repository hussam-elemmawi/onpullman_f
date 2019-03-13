package io.hussam.westarmy.onpullman.days;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.hussam.westarmy.onpullman.data.model.DayInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.hussam.westarmy.onpullman.R;
import io.hussam.westarmy.onpullman.photos.PhotosActivity;
import io.hussam.westarmy.onpullman.util.ActivityUtils;
import io.hussam.westarmy.onpullman.util.StorageUtil;

import static android.app.Activity.RESULT_CANCELED;

import static com.google.common.base.Preconditions.checkNotNull;

public class DaysFragmentDialog extends DialogFragment {

    private DaysInfoAdapter listAdapter;
    private int pullmanId;
    private String lastPhotoFilePath;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            pullmanId = getArguments().getInt("pullmanId");
        }
        listAdapter = new DaysInfoAdapter(new ArrayList<DayInfo>());

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.days_dialog_frag, null);
        initViews(view);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAdapterData();
    }

    private void initViews(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.daysRecyclerView);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAdapterData();
        Button addNewPhotoButton = view.findViewById(R.id.addPhotoForPullman);
        addNewPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void loadAdapterData() {
        List<DayInfo> daysDir = StorageUtil.getPullmanPhotoDirectories(pullmanId);
        listAdapter.replaceData(daysDir);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            Uri photoURI = null;
            try {
                File photoFile = StorageUtil.createImageFile(pullmanId);
                photoURI = StorageUtil.buildPhotoUri(requireActivity(),
                        R.string.file_provider_authority, photoFile);
                // Continue only if the File was successfully created
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra(Intent.ACTION_VIEW, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                lastPhotoFilePath = photoFile.getAbsolutePath();
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                // Error occurred while creating the File
                ActivityUtils.showMessage(getContext(), "حدث خطآ، برجاء المراجعه مع المطور\n" + e.getMessage());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_CANCELED) {
                try {
                    ActivityUtils.showMessage(getContext(), lastPhotoFilePath);
                    boolean deleted = StorageUtil.deleteFile(lastPhotoFilePath);
                    String message = "تم حفظ صوره فارغة، برجاء اعادة المحاولة";
                    if (deleted) {
                        message = "تم الغاء الصورة";
                    }
                    ActivityUtils.showMessage(getContext(), message);
                } catch (Exception e) {
                    ActivityUtils.showMessage(getContext(), "حدث خطآ، برجاء المراجعه مع المطور\n" + e.getMessage());
                }
            }
        }
    }

    private DayInfoItemListener itemListener = new DayInfoItemListener() {
        @Override
        public void onDayInfoClicked(DayInfo dayInfo) {
            Intent intent = new Intent(requireActivity(), PhotosActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("pullmanId", dayInfo.getPullmanId());
            bundle.putString("date", dayInfo.getDate());
            intent.putExtras(bundle);
            requireActivity().startActivity(intent);
        }
    };

    private class DaysInfoAdapter extends RecyclerView.Adapter<DayInfoViewHolder> {

        private List<DayInfo> daysInfoList;

        DaysInfoAdapter(List<DayInfo> dayInfos) {
            setList(dayInfos);
        }

        void replaceData(List<DayInfo> dayInfos) {
            setList(dayInfos);
            notifyDataSetChanged();
        }

        void setList(List<DayInfo> dayInfos) {
            daysInfoList = checkNotNull(dayInfos);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return daysInfoList.size();
        }

        @Override
        public DayInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = requireActivity().getLayoutInflater().inflate(R.layout.days_info_item, parent, false);
            final DayInfoViewHolder vh = new DayInfoViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(final DayInfoViewHolder holder, final int position) {

            final DayInfo dayInfo = daysInfoList.get(position);

            holder.name.setText(dayInfo.getDate());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onDayInfoClicked(dayInfo);
                }
            });
        }
    }

    public class DayInfoViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public DayInfoViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
        }
    }

    private interface DayInfoItemListener {
        void onDayInfoClicked(DayInfo day);
    }
}
