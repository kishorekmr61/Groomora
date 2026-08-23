package com.groomora.feature.beauty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BeautyViewModel(
    private val beautyRepository: BeautyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BeautyState())
    val state: StateFlow<BeautyState> = _state.asStateFlow()

    init {
        onIntent(BeautyIntent.LoadBeautyData)
    }

    fun onIntent(intent: BeautyIntent) {
        when (intent) {
            BeautyIntent.LoadBeautyData -> loadInitialData()
            is BeautyIntent.SelectCategory -> selectCategory(intent.categoryId)
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            beautyRepository.getBeautyCategories().collect { categories ->
                _state.update { it.copy(categories = categories) }
                val initialCat = categories.firstOrNull()?.id ?: "facial"
                selectCategory(initialCat)
            }
        }
    }

    private fun selectCategory(categoryId: String) {
        viewModelScope.launch {
            _state.update { it.copy(selectedCategoryId = categoryId, isLoading = true) }
            beautyRepository.getBeautyServices(categoryId).collect { services ->
                beautyRepository.getBeautyPackages().collect { packages ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            services = services,
                            packages = packages
                        )
                    }
                }
            }
        }
    }
}
