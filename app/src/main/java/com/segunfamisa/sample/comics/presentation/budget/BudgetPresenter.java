package com.segunfamisa.sample.comics.presentation.budget;


import com.segunfamisa.sample.comics.common.scheduler.SchedulerProvider;
import com.segunfamisa.sample.comics.data.ComicRepository;
import com.segunfamisa.sample.comics.data.model.Comic;
import com.segunfamisa.sample.comics.util.BudgetCalculator;
import com.segunfamisa.sample.comics.util.ListUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Budget presenter.
 */
public class BudgetPresenter implements BudgetContract.Presenter {

    private final ComicRepository comicRepository;
    private final BudgetCalculator budgetCalculator;
    private final SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;

    private BudgetContract.View view;

    /**
     * Create a new budget presenter.
     *
     * @param comicRepository   - comic repository
     * @param budgetCalculator  - budget calculator
     * @param schedulerProvider -
     */
    public BudgetPresenter(ComicRepository comicRepository,
                           BudgetCalculator budgetCalculator,
                           SchedulerProvider schedulerProvider) {
        this.comicRepository = comicRepository;
        this.budgetCalculator = budgetCalculator;
        this.schedulerProvider = schedulerProvider;
        compositeDisposable = new CompositeDisposable();
    }


    @Override
    public void onAttach(BudgetContract.View view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        compositeDisposable.clear();
    }

    @Override
    public void findComics(final int budget) {
        view.showLoading(true);
        Disposable disposable = comicRepository.getComics()
                .observeOn(schedulerProvider.mainThread())
                .subscribeOn(schedulerProvider.io())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comicList) throws Exception {
                        // on next
                        view.showLoading(false);
                        Map<Integer, List<Comic>> budgetResults =
                                budgetCalculator.calculate(budget, comicList);

                        handleBudgetResults(budgetResults);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // on error
                        view.showLoading(false);
                        view.showError(throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void handleBudgetResults(Map<Integer, List<Comic>> budgetResults) {
        int budgetPages = (int) budgetResults.keySet().toArray()[0];
        List<Comic> comics = budgetResults.get(budgetPages);

        view.showTotalComicPagesCountForBudget(budgetPages);
        view.showMatchingComicList(comics);
        view.showNoMatchingComic(ListUtils.isNullOrEmpty(comics));
    }

    @Override
    public void navigateToComicDetails(Comic comic) {
        view.showComicDetails(comic.getId());
    }
}
