package com.segunfamisa.sample.comics.data.local.mappers;


import com.segunfamisa.sample.comics.data.local.realm.ComicCreatorsRealm;
import com.segunfamisa.sample.comics.data.model.ComicCreators;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Maps from {@link ComicCreatorsRealm} to {@link ComicCreators}.
 */
public class ComicCreatorsRealmMapper implements Mapper<ComicCreatorsRealm, ComicCreators> {

    private final CreatorSummaryRealmMapper summaryRealmMapper;

    public ComicCreatorsRealmMapper(CreatorSummaryRealmMapper summaryRealmMapper) {
        this.summaryRealmMapper = summaryRealmMapper;
    }

    @Override
    public ComicCreators map1(ComicCreatorsRealm from) {
        return new ComicCreators.Builder()
                .setAvailable(from.getAvailable())
                .setCollectionUri(from.getCollectionUri())
                .setItems(new ArrayList<>(summaryRealmMapper.mapMany(from.getItems())))
                .build();
    }

    @Override
    public Collection<ComicCreators> mapMany(Collection<ComicCreatorsRealm> fromCollection) {
        return null;
    }
}
