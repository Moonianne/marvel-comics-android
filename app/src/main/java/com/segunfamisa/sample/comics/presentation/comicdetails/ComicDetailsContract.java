package com.segunfamisa.sample.comics.presentation.comicdetails;


import com.segunfamisa.sample.comics.data.model.ComicCreators;
import com.segunfamisa.sample.comics.presentation.base.BasePresenter;
import com.segunfamisa.sample.comics.presentation.base.BaseView;

/**
 * Contract between the view and presenter of the Comic details screen.
 */
public interface ComicDetailsContract {

    interface Presenter extends BasePresenter<View> {

        void fetchComicDetails(long comicId);

    }

    interface View extends BaseView {

        void showTitle(String title);

        void showDescription(String description);

        void showPageCount(String pageCount);

        void showPrice(double price);

        void showAuthors(ComicCreators authors);

        void showLoading(boolean isLoading);

        void showError(String error);

    }

}
