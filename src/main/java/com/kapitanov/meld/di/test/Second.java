package com.kapitanov.meld.di.test;

import com.kapitanov.meld.di.annotations.Wired;
import com.kapitanov.meld.di.annotations.Service;

@Service
public class Second {

    @Wired
    public Second() {}
}
