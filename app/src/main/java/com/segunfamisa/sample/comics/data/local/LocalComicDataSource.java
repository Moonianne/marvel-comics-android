package com.segunfamisa.sample.comics.data.local;


import com.segunfamisa.sample.comics.data.ComicDataSource;
import com.segunfamisa.sample.comics.data.model.Comic;

import java.util.List;

import io.reactivex.Observable;

public class LocalComicDataSource implements ComicDataSource {

    @Override
    public Observable<List<Comic>> getComics() {
        return null;
    }

    @Override
    public Observable<Comic> getComic(long comicId) {
        return null;
    }

}
