package com.segunfamisa.sample.comics.data;

import com.segunfamisa.sample.comics.data.model.Comic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ComicRepositoryTest {

    private ComicRepository comicRepository;

    @Mock
    private ComicDataSource localDataSource;

    @Mock
    private ComicDataSource remoteDataSource;

    @Mock
    private ComicDataPersistence localDataPersistence;

    private final List<Comic> comicList = TestDataGenerator.getComics();

    /**
     * Set Up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        comicRepository = new ComicRepository(localDataSource, remoteDataSource,
                localDataPersistence);
    }

    @Test
    public void getComics_fromLocalDataSource_noInMemory() {
        // given that the in memory is empty
        comicRepository.inMemoryDataSource.clear();

        // given that the local data source returns comic list
        when(localDataSource.getComics()).thenReturn(Observable.just(comicList));

        // given that the remote source returns an empty list
        when(remoteDataSource.getComics()).thenReturn(Observable.<List<Comic>>empty());

        TestObserver<List<Comic>> testObserver = new TestObserver<>();

        // when we get comics from comic repository
        comicRepository.getComics().subscribe(testObserver);

        // then verify that the local data source is queried
        verify(localDataSource).getComics();

        // then verify that the returned values are those from the local data source (comicList)
        testObserver.assertValue(comicList);
    }

    @Test
    public void getComics_fromRemoteDataSource_noInMemory() {
        // given that the in memory is empty
        comicRepository.inMemoryDataSource.clear();

        // given that the local data source is unavailable
        when(localDataSource.getComics()).thenReturn(Observable.<List<Comic>>empty());

        // given that the remote data source returns comic list
        when(remoteDataSource.getComics()).thenReturn(Observable.just(comicList));

        TestObserver<List<Comic>> testObserver = new TestObserver<>();

        // when we get comics from comic repository
        comicRepository.getComics().subscribe(testObserver);

        // then verify that the remote data source is queried
        verify(remoteDataSource).getComics();

        // then verify that the returned values are those from the remote data source
        testObserver.assertValue(comicList);
    }

    @Test
    public void getComics_fromRemoteDataSource_savesToLocal() {
        // given that the local data source is unavailable
        when(localDataSource.getComics()).thenReturn(Observable.<List<Comic>>empty());

        // given that the remote data source returns comic list
        when(remoteDataSource.getComics()).thenReturn(Observable.just(comicList));

        // when we get comics from the repository
        comicRepository.getComics().subscribe();

        // verify that we call save to local for each item in the list
        verify(localDataPersistence, times(comicList.size())).save(any(Comic.class));
    }

    @Test
    public void getComics_remoteDataSourceFails_returnsLocalData() {
        // given that the local data source returns comic list
        when(localDataSource.getComics()).thenReturn(Observable.just(comicList));

        // given that the remote source fails with an error
        when(remoteDataSource.getComics()).thenReturn(
                Observable.<List<Comic>>error(new Throwable("Network Error")));

        TestObserver<List<Comic>> testObserver = new TestObserver<>();

        // when we get comics from the repository
        comicRepository.getComics().subscribe(testObserver);

        // then verify that the values returned are those from the local source
        testObserver.assertValue(comicList);
    }

    @Test
    public void getComics_fetchFromRemote_emptyLocal_emptyCache_updateLocal_updateCache() {
        // given that the in memory cache is empty
        comicRepository.inMemoryDataSource.clear();

        // given that the local data source is not available
        when(localDataSource.getComics()).thenReturn(Observable.<List<Comic>>empty());

        // given that the remote data source returns comic list
        when(remoteDataSource.getComics()).thenReturn(Observable.just(comicList));

        TestObserver<List<Comic>> testObserver = new TestObserver<>();

        // when we get comics from the repository
        comicRepository.getComics().subscribe(testObserver);

        // then assert that the values are the same as emitted by the remote data source
        testObserver.assertValue(comicList);

        // then verify that we try to update the local source
        verify(localDataPersistence, times(comicList.size())).save(any(Comic.class));

        // then verify that we update the in memory cache
        assertFalse(comicRepository.inMemoryDataSource.isEmpty());
    }

    @Test
    public void getComics_fromMemory() {
        // given that we already have comic list items in memory
        for (Comic comic : comicList) {
            comicRepository.inMemoryDataSource.put(comic.getId(), comic);
        }

        // given that we are not due to refresh yet.
        comicRepository.forceRefresh = false;

        // given that the local data source returns only half of comicList
        when(localDataSource.getComics()).thenReturn(
                Observable.just(comicList.subList(0, comicList.size() / 2)));

        // given that the remote data source returns the other half
        when(remoteDataSource.getComics()).thenReturn(
                Observable.just(comicList.subList(comicList.size() / 2, comicList.size() - 1)));

        TestObserver<List<Comic>> testObserver = new TestObserver<>();

        // when we call get comics
        comicRepository.getComics().subscribe(testObserver);

        // then verify that the emiited comics are the ones from the memory.
        // we can do that by verifying the number of items in the list is the same as that in memory
        assertEquals(comicList.size(), testObserver.values().get(0).size());
    }
}