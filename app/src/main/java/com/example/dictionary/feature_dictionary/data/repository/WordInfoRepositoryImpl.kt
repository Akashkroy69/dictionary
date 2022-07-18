package com.example.dictionary.feature_dictionary.data.repository

import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.data.local.WordInfoDAO
import com.example.dictionary.feature_dictionary.data.remote.DictionaryAPI
import com.example.dictionary.feature_dictionary.domain.model.WordInfo
import com.example.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow

class WordInfoRepositoryImpl(
    private val api: DictionaryAPI,
    private val dao: WordInfoDAO
) : WordInfoRepository {

    //Here only we will have the code for caching.
    //here we will keep the single source of truth in mind: All the data comes from database. It means that even we get
    // the data from our API then also we will put this in DB first. Then from there itself we will access to show in the UI.
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> {
        //code for caching will be here
        TODO()
    }
}