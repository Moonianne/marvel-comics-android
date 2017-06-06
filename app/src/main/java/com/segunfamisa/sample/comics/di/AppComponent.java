package com.segunfamisa.sample.comics.di;

import com.segunfamisa.sample.comics.data.di.ApiModule;
import com.segunfamisa.sample.comics.data.di.ComicRepositoryModule;
import com.segunfamisa.sample.comics.presentation.comicdetails.di.ComicDetailsComponent;
import com.segunfamisa.sample.comics.presentation.comicdetails.di.ComicDetailsPresenterModule;
import com.segunfamisa.sample.comics.presentation.comiclist.di.ComicListComponent;
import com.segunfamisa.sample.comics.presentation.comiclist.di.ComicListPresenterModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = { ComicRepositoryModule.class, ApiModule.class, AppModule.class})
public interface AppComponent {

    ComicDetailsComponent plus(ComicDetailsPresenterModule module);

    ComicListComponent plus(ComicListPresenterModule module);

}
