package com.kapitanov.meld.di.second;

import com.kapitanov.meld.di.annotations.Service;
import com.kapitanov.meld.di.test.Third;

@Service
public class ThirdImpl implements Third {

    @Override
    public String toString() {
        return "ThirdImpl []";
    }

    
}
