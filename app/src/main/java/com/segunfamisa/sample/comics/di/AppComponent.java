package com.segunfamisa.sample.comics.di;

import com.segunfamisa.sample.comics.MainActivity;
import com.segunfamisa.sample.comics.data.di.ApiModule;
import com.segunfamisa.sample.comics.data.di.ComicRepositoryModule;
import com.segunfamisa.sample.comics.di.AppModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = { ComicRepositoryModule.class, ApiModule.class, AppModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

}
