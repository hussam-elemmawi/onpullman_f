package io.hussam.westarmy.onpullman.pullmans;

import androidx.annotation.NonNull;

import io.hussam.westarmy.onpullman.BaseView;
import io.hussam.westarmy.onpullman.BasePresenter;
import io.hussam.westarmy.onpullman.data.model.Pullman;

import java.util.Date;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface PullmansContract {

    interface View extends BaseView<Presenter> {

        void showPullmans(List<Pullman> pullmanList);

        void showPullmanDaysUi(int pullmanId);

        void showLoadingPullmansError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadPullmans();

        void openPullmanDays(@NonNull Pullman requestedPullmanId);
    }
}
