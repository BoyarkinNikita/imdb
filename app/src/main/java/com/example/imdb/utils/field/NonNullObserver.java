package com.example.imdb.utils.field;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

public interface NonNullObserver<T> extends Observer<T> {
    @MainThread
    @Override
    void onChanged(@NonNull T value);
}
