package com.example.dictionary.feature_dictionary.data.repository

import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.data.local.WordInfoDAO
import com.example.dictionary.feature_dictionary.data.remote.DictionaryAPI
import com.example.dictionary.feature_dictionary.domain.model.WordInfo
import com.example.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow

class WordInfoRepositoryImpl(
    //this class constructor takes following 2 dependencies
    private val api: DictionaryAPI,
    private val dao: WordInfoDAO
) : WordInfoRepository {

    //Here in this method we put the logic for caching.
    //This will have all the data sources we have. In this case the Remote API and local database. This method has to decide that where
    //the specific data is available and from where it has to forwarded to the viewModel for showing up in the UI.

    //FOR CACHING: we should STICK to the SINGLE SOURCE OF TRUTH principle: the data for the view model should always come from
    //the database.
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> {
        //code for caching will be here
        TODO()
    }
}