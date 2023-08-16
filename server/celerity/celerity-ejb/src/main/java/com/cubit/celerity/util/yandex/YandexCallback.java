package com.cubit.celerity.util.yandex;

public interface YandexCallback<T> {
    void onResponse(T result);

    void onFailure(Throwable t);
}
