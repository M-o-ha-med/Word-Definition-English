package com.pam.Dictionary.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pam.Dictionary.core.util.Resource
import com.pam.Dictionary.feature_dictionary.data.local.FavoriteWordDAO
import com.pam.Dictionary.feature_dictionary.data.local.WordInfoDao
import com.pam.Dictionary.feature_dictionary.data.local.entity.FavoriteWordEntity
import com.pam.Dictionary.feature_dictionary.data.local.entity.WordInfoEntity
import com.pam.Dictionary.feature_dictionary.domain.model.Meaning
import com.pam.Dictionary.feature_dictionary.domain.usecase.GetWordInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfoUseCase: GetWordInfoUseCase,
    private var favDao : FavoriteWordDAO,
    private var wordDao : WordInfoDao
) : ViewModel() {

    private val _searchQuery = mutableStateOf<String>("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf<WordInfoState>(WordInfoState())
    val state: State<WordInfoState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    private val _history = MutableStateFlow<List<WordInfoEntity>>(emptyList())
    val history: StateFlow<List<WordInfoEntity>> get() = _history

    private val _favorite = MutableStateFlow<List<WordInfoEntity>>(emptyList())
    val favorite: StateFlow<List<WordInfoEntity>> get() = _favorite



    fun toggleFavorite(word: String, meaning : List<Meaning>) {
        viewModelScope.launch {
            val wordID = wordDao.getWordIDbyWordAndMeaning(word = word, meaning = meaning)
            val favWord = FavoriteWordEntity(wordID = wordID)
            favDao.addFavorite(favWord)
        }
    }

    fun loadFavorite(){
        viewModelScope.launch {
            _favorite.value = favDao.getFavoriteWords()
        }
    }



    fun loadHistory() {
        viewModelScope.launch {
            _history.value = wordDao.getAllWordInfo()
        }
    }

    fun deleteFavoriteItem(wordID : Int){
        viewModelScope.launch {
            favDao.removeFavorite(wordID)
            loadFavorite()
        }
    }

    fun deleteHistoryItem(word : String) {
        viewModelScope.launch {
            wordDao.deleteWordInfos(word)
            loadHistory()
        }
    }


    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(250L)
            getWordInfoUseCase(query)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                wordInfoItems = resource.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                wordInfoItems = resource.data ?: emptyList(),
                                isLoading = false
                            )
                            UIEvent.ShowSnackBar(
                                resource.message ?: "${query} successfully found!"
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                wordInfoItems = resource.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(
                                UIEvent.ShowSnackBar(
                                    resource.message ?: "Network Offline Or Unknown Word"
                                )
                            )
                        }
                    }
                }.launchIn(this)
        }
    }


    sealed class UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent()
    }

}