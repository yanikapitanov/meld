package com.kapitanov.meld.di.test;

import com.kapitanov.meld.di.annotations.Wired;
import com.kapitanov.meld.di.annotations.Service;

@Service
public class Second {

    private final Third third;

    @Wired
    public Second(Third third) {
        this.third = third;
    }

    @Override
    public String toString() {
        return "Second [third=" + third + "]";
    }
}
