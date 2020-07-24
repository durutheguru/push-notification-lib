package com.julianduru.webpush.data;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * created by julian
 */
public interface DataProvider<T> {


    JpaRepository<T, Long> getRepository();


    default T save() {
        return getRepository().save(provide());
    }


    default T save(T sample) {
        return getRepository().save(provide(sample));
    }


    default List<T> save(int count) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            list.add(save());
        }

        return list;
    }


    default List<T> save(T sample, int count) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            list.add(save(sample));
        }

        return list;
    }


    T provide();


    T provide(T sample);


    default List<T> provide(int count) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            list.add(provide());
        }

        return list;
    }


    default List<T> provide(T sample, int count) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            list.add(provide(sample));
        }

        return list;
    }



}
