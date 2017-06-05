package com.segunfamisa.sample.comics.data.local;


import com.segunfamisa.sample.comics.data.ComicDataSource;
import com.segunfamisa.sample.comics.data.local.mappers.Mapper;
import com.segunfamisa.sample.comics.data.local.realm.ComicRealm;
import com.segunfamisa.sample.comics.data.local.realm.Tables;
import com.segunfamisa.sample.comics.data.model.Comic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Fetches comic items from the db (realm db).
 */
@Singleton
public class LocalComicDataSource implements ComicDataSource {

    private final Mapper<ComicRealm, Comic> mapper;

    @Inject
    public LocalComicDataSource(Mapper<ComicRealm, Comic> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Observable<List<Comic>> getComics() {
        return Observable.just(findComics());
    }

    @Override
    public Observable<Comic> getComic(long comicId) {
        return Observable.just(findComicById(comicId));
    }

    private List<Comic> findComics() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ComicRealm> comicRealms = realm.where(ComicRealm.class)
                .findAll();

        List<Comic> comics = new ArrayList<>(mapper.mapMany(comicRealms));
        return comics;
    }

    private Comic findComicById(long comicId) {
        Realm realm = Realm.getDefaultInstance();
        ComicRealm comicRealm = realm.where(ComicRealm.class)
                .equalTo(Tables.Comic.id, comicId)
                .findFirst();
        return mapper.map1(comicRealm);
    }
}
