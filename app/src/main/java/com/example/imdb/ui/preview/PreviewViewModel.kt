package com.example.imdb.ui.preview

import com.example.imdb.ui.base.BaseViewModel
import com.example.imdb.utils.field.NullableField

class PreviewViewModel : BaseViewModel() {
    var imageUri = NullableField<String>(null)
}