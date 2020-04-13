package com.example.imdb.utils.field;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

public interface NullableObserver<T> extends Observer<T> {
    @MainThread
    @Override
    void onChanged(@Nullable T value);
}
