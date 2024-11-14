package com.kapitanov.meld.di.test;

import com.kapitanov.meld.di.annotations.Wired;
import com.kapitanov.meld.di.annotations.Service;

@Service
public class FirstTest {

    private final Second second;

    @Wired
    public FirstTest(Second second) {
        this.second = second;
        System.out.println(second);
    }
}
