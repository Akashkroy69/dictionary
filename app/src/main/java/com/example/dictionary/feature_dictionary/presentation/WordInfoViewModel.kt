package com.example.dictionary.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dictionary.feature_dictionary.domain.use_case.GetWordInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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





    //We need to make An Event Flow which is used to send one time event to UI. Ex; Showing a snack bar for error message.
    //The event we are going to create hare will be created in a Sealed Class( as we have made in the Calculator app)
    sealed class UIEvent {
        //an event for showing message in a snack bar. This needs message data, so the event will be of type data class
        data class SnackBarEvent(val errorMessage: String) : UIEvent()
    }
}