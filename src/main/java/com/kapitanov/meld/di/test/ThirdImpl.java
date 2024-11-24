package com.kapitanov.meld.di.test;

import com.kapitanov.meld.di.annotations.Service;
import com.kapitanov.meld.di.annotations.Wired;

@Service
public class ThirdImpl implements Third {

    @Override
    public String toString() {
        return "ThirdImpl []";
    }

    
}
