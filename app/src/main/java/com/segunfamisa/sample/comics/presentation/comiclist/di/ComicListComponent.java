package com.segunfamisa.sample.comics.presentation.comiclist.di;


import com.segunfamisa.sample.comics.data.di.ScreenScoped;
import com.segunfamisa.sample.comics.di.AppComponent;
import com.segunfamisa.sample.comics.presentation.comiclist.ComicListFragment;

import dagger.Component;

/**
 * ComicList dagger component.
 */
@ScreenScoped
@Component(dependencies = AppComponent.class, modules = ComicListPresenterModule.class)
public interface ComicListComponent {

    void inject(ComicListFragment fragment);

}
