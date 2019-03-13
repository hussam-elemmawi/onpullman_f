package io.hussam.westarmy.onpullman.pullmans;

import androidx.annotation.NonNull;

import io.hussam.westarmy.onpullman.data.model.Pullman;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link PullmansFragment}), retrieves the data and updates the
 * UI as required.
 */
public class PullmansPresenter implements PullmansContract.Presenter {

    private final PullmansContract.View pullmansView;

    public PullmansPresenter(@NonNull PullmansContract.View pullmansView) {
        this.pullmansView = checkNotNull(pullmansView, "pullmansView cannot be null!");
        this.pullmansView.setPresenter(this);
    }

    @Override
    public void start() {
        pullmansView.showPullmans(Pullman.getPullmanList());
    }

    @Override
    public void loadPullmans() {
        pullmansView.showPullmans(Pullman.getPullmanList());
    }

    @Override
    public void openPullmanDays(@NonNull Pullman requestedPullman) {
        if (pullmansView != null && pullmansView.isActive()) {
            pullmansView.showPullmanDaysUi(requestedPullman.getId());
        }
    }
}
