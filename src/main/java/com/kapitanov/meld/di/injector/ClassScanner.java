package com.kapitanov.meld.di.injector;

import com.kapitanov.meld.di.annotations.Service;
import org.reflections.Reflections;

import java.util.Set;

public class ClassScanner {

    private ClassScanner() {
        // Should not be instantiated
    }

    public static Set<Class<?>> findClasses(Class<?> mainClass) {
        String name = mainClass.getPackage().getName();
        Reflections reflections = new Reflections(name);
        return reflections.getTypesAnnotatedWith(Service.class);
    }
}
