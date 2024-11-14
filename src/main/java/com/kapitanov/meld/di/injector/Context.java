package com.kapitanov.meld.di.injector;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private final Map<Class<?>, Object> context = new HashMap<>();

    public <T> T get(Class<T> clazz) {
        return (T) context.get(clazz);
    }

    public void addInstance(Class<?> clazz, Object instance) {
        context.put(clazz, instance);
    }
}
