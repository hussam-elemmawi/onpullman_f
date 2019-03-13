package io.hussam.westarmy.onpullman.pullmans;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.hussam.westarmy.onpullman.data.model.Pullman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.hussam.westarmy.onpullman.R;
import io.hussam.westarmy.onpullman.days.DaysFragmentDialog;
import io.hussam.westarmy.onpullman.util.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Pullman}s. User can choose to view all, active or completed tasks.
 */
public class PullmansFragment extends Fragment implements PullmansContract.View {

    private PullmansContract.Presenter presenter;

    private PullmanAdapter listAdapter;

    private AppCompatActivity activity;

    public PullmansFragment() {
        // Requires empty public constructor
    }

    public static PullmansFragment newInstance() {
        return new PullmansFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        listAdapter = new PullmanAdapter(new ArrayList<Pullman>(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull PullmansContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pullmans_frag, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.pullmansRecyclerView);
        recyclerView.setAdapter(listAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showPullmans(List<Pullman> pullmanList) {
        if (pullmanList != null) {
            listAdapter.replaceData(pullmanList);
        }
    }

    @Override
    public void showPullmanDaysUi(int pullmanId) {
        DaysFragmentDialog dialog = new DaysFragmentDialog();
        Bundle args = new Bundle();
        args.putInt("pullmanId", pullmanId);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "DaysFragmentDialog");
    }

    @Override
    public void showLoadingPullmansError() {
        ActivityUtils.showMessage(getContext(), "Can't load pullmans");
    }

    private PullmanItemListener itemListener = new PullmanItemListener() {
        @Override
        public void onPullmanClick(Pullman clickedPullman) {
            presenter.openPullmanDays(clickedPullman);
        }
    };

    private class PullmanAdapter extends RecyclerView.Adapter<PullmanViewHolder> {

        private List<Pullman> pullmanList;

        PullmanAdapter(List<Pullman> pullmans) {
            setList(pullmans);
        }

        void replaceData(List<Pullman> pullmans) {
            setList(pullmans);
            notifyDataSetChanged();
        }

        void setList(List<Pullman> pullmans) {
            pullmanList = checkNotNull(pullmans);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return pullmanList.size();
        }

        @Override
        public PullmanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = activity.getLayoutInflater().inflate(R.layout.pullman_item, parent, false);
            final PullmanViewHolder vh = new PullmanViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(final PullmanViewHolder holder, final int position) {

            final Pullman pullman = pullmanList.get(position);

            holder.title.setText(pullman.getTitle());
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onPullmanClick(pullman);
                }
            });
        }
    }

    public class PullmanViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public PullmanViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    private interface PullmanItemListener {
        void onPullmanClick(Pullman selectedPullman);
    }
}
