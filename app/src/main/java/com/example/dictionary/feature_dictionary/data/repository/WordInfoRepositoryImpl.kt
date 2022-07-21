package com.example.dictionary.feature_dictionary.data.repository

import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.data.local.WordInfoDAO
import com.example.dictionary.feature_dictionary.data.remote.DictionaryAPI
import com.example.dictionary.feature_dictionary.domain.model.WordInfo
import com.example.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    //this class constructor takes following 2 dependencies
    private val api: DictionaryAPI,
    private val dao: WordInfoDAO
) : WordInfoRepository {
    //This app is basically focussing on caching. And the caching logic lies here. This is the most important concept of the project

    //Here in this method we put the logic for caching.
    //This will have all the data sources we have. In this case the Remote API and local database. This method has to decide that where
    //the specific data is available and from where it has to forwarded to the viewModel for showing up in the UI.

    //FOR CACHING: we should STICK to the SINGLE SOURCE OF TRUTH principle: the data for the view model should always come from
    //the database.
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {

        //we use the following line of code when we want to emit a specific value. Like SUCCESS or LOADING.
        //this value will be picked by our view model to trigger specific action, like showing 'progress bar'
        emit(Resource.Loading())


        //now we will try to find the word in our local cache.
        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        //with this variable we can emit the value. If our local cache has the word, it will emit the wordInfos and that will be
        //picked by our View Model
        emit(Resource.Loading(data = wordInfos))


        //code for fetching data from the API
        try {
            val remoteWordInfo = api.getWordInfo(word)
            dao.deleteWordInfos(remoteWordInfo.map { it.word })
            dao.insertWordInfos(remoteWordInfo.map { it.toWordInfoEntity() })

        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "oops, something went wrong",
                    data = wordInfos
                )
            )

        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "oops, internet down or server is not reachable",
                    data = wordInfos
                )
            )
        }

        //till now we have not emitted the information in the UI. We just have fetched the information from
        //the API and have added to the Database.
        val newWordInfo = dao.getWordInfos(word).map { it.toWordInfo() }
        //now we will emit the result fetched from the database through API for the ViewModel
        emit(Resource.Success(newWordInfo))
    }
}