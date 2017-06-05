package com.segunfamisa.sample.comics.data.local.mappers;


import com.segunfamisa.sample.comics.data.local.realm.ImageRealm;
import com.segunfamisa.sample.comics.data.model.Image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Mapper from {@link com.segunfamisa.sample.comics.data.local.realm.ImageRealm} to
 *      {@link com.segunfamisa.sample.comics.data.model.Image}.
 */
public class ImageRealmMapper implements Mapper<ImageRealm, Image> {

    public ImageRealmMapper() {
    }

    @Override
    public Image map1(ImageRealm imageRealm) {
        return new Image.Builder()
                .setExtension(imageRealm.getExtension())
                .setPath(imageRealm.getPath())
                .build();
    }

    @Override
    public Collection<Image> mapMany(Collection<ImageRealm> imageRealms) {
        List<Image> images = new ArrayList<>();
        for (ImageRealm imageRealm : imageRealms) {
            images.add(map1(imageRealm));
        }
        return images;
    }

}
