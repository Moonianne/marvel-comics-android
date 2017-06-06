package com.segunfamisa.sample.comics.presentation.comicdetails;


import com.segunfamisa.sample.comics.common.scheduler.SchedulerProvider;
import com.segunfamisa.sample.comics.data.ComicRepository;
import com.segunfamisa.sample.comics.data.model.Comic;
import com.segunfamisa.sample.comics.data.model.ComicPrice;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Presenter for comic details.
 */
public class ComicDetailsPresenter implements ComicDetailsContract.Presenter {

    private final ComicRepository comicRepository;
    private final SchedulerProvider schedulerProvider;
    private ComicDetailsContract.View view;
    private CompositeDisposable compositeDisposable;

    /**
     * Comic details presenter.
     *
     * @param comicRepository - comic repository
     * @param schedulerProvider - scheduler provider
     */
    @Inject
    public ComicDetailsPresenter(ComicRepository comicRepository,
                                 SchedulerProvider schedulerProvider) {
        this.comicRepository = comicRepository;
        this.schedulerProvider = schedulerProvider;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onAttach(ComicDetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        compositeDisposable.clear();
    }

    @Override
    public void fetchComicDetails(long comicId) {
        view.showLoading(true);
        Disposable disposable = comicRepository.getComic(comicId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(new Consumer<Comic>() {
                    @Override
                    public void accept(Comic comic) throws Exception {
                        // on next
                        view.showLoading(false);
                        showComicDetails(comic);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // on error
                        view.showLoading(false);
                        view.showError(throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void showComicDetails(Comic comic) {
        view.showTitle(comic.getTitle());
        view.showDescription(comic.getDescription());
        view.showPageCount(String.valueOf(comic.getPageCount()));
        view.showPrice(resolvePrice(comic.getPrices()));
        view.showAuthors(comic.getCreators());
        view.showComicImage(comic.getThumbnail().getUrl());
    }

    private double resolvePrice(List<ComicPrice> prices) {
        return prices.get(0).getPrice();
    }
}
