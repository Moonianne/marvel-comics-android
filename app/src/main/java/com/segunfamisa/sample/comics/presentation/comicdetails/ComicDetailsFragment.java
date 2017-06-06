package com.segunfamisa.sample.comics.presentation.comicdetails;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.segunfamisa.sample.comics.App;
import com.segunfamisa.sample.comics.R;
import com.segunfamisa.sample.comics.data.model.ComicCreators;
import com.segunfamisa.sample.comics.databinding.ComicDetailsBinding;
import com.segunfamisa.sample.comics.presentation.comicdetails.di.ComicDetailsPresenterModule;

import javax.inject.Inject;

/**
 * Fragment for comic details.
 */
public class ComicDetailsFragment extends Fragment implements ComicDetailsContract.View {

    private static final String ARG_COMIC_ID = "arg_comic_id";

    private ComicDetailsBinding binding;
    private long comicId;

    @Inject
    ComicDetailsPresenter presenter;

    /**
     * Creates a new instance of {@link ComicDetailsFragment}.
     *
     * @param comicId - comic id
     * @return a fragment instance with comic id set in the arguments;
     */
    public static Fragment newInstance(long comicId) {
        Fragment frag = new ComicDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_COMIC_ID, comicId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectComponents();

        presenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_comic_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar();

        comicId = retrieveComicId(savedInstanceState);

        fetchComic(comicId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(ARG_COMIC_ID, comicId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    private void injectComponents() {
        ((App) getActivity().getApplication()).getAppComponent()
                .plus(new ComicDetailsPresenterModule(this))
                .inject(this);
    }

    private long retrieveComicId(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getArguments().getLong(ARG_COMIC_ID, -1);
        } else {
            return savedInstanceState.getLong(ARG_COMIC_ID, -1);
        }
    }

    private void fetchComic(long comicId) {
        presenter.fetchComicDetails(comicId);
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_nav_back);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void showTitle(String title) {
        // TODO: 06/06/2017 implement
    }

    @Override
    public void showDescription(String description) {
        // TODO: 06/06/2017 implement
    }

    @Override
    public void showPageCount(String pageCount) {
        // TODO: 06/06/2017 implement
    }

    @Override
    public void showPrice(double price) {
        // TODO: 06/06/2017 implement
    }

    @Override
    public void showAuthors(ComicCreators authors) {
        // TODO: 06/06/2017 implement
    }

    @Override
    public void showComicImage(String imageUrl) {
        // TODO: 06/06/2017 implement
    }

    @Override
    public void showLoading(boolean isLoading) {
        // TODO: 06/06/2017 implement
    }

    @Override
    public void showError(String error) {
        // TODO: 06/06/2017 implement
    }
}
