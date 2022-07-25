package com.example.dictionary.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.core.util.Resource
import com.example.dictionary.feature_dictionary.domain.use_case.GetWordInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

//ViewModel always calls the use case from domain layer then forwards the result to your UI/map the result to corresponding compose
//state & provide that for the ui
@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo
) : ViewModel() {
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf(WordInfoState())
    val state: State<WordInfoState> = _state

    //we will create a flow which is of type shared flow to send the event
    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //onSearch() makes query request to the db and API for the characters typed in the search field. But we want that
    //it does after we pause for sometime while typing otherwise onSearch() will try to make the query for each character
    private var searchJob: Job? = null


    //we need a function which is triggered when in the search field some characters are typed.
    //the function will make a query request in the db and api to set the result from the cache.
    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            getWordInfo(query)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(
                                UIEvent.SnackBarEvent(
                                    result.message ?: "Unknown Error"
                                )
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }
                }
        }


    }


    //We need to make An Event Flow which is used to send one time event to UI. Ex; Showing a snack bar for error message.
    //The event we are going to create hare will be created in a Sealed Class( as we have made in the Calculator app)
    sealed class UIEvent {
        //an event for showing message in a snack bar. This needs message data, so the event will be of type data class
        data class SnackBarEvent(val errorMessage: String) : UIEvent()
    }
}