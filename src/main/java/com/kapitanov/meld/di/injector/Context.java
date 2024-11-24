package com.kapitanov.meld.di.injector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Context {


    private final Map<Class<?>, Class<?>> interfaces = new HashMap<>();
    private final Map<Class<?>, Object> context = new HashMap<>();

    public <T> T get(Class<T> clazz) {
        return (T) context.get(clazz);
    }

    public void addInstance(Class<?> clazz, Object instance) {
        context.put(clazz, instance);
    }

    public Class<?> getInterfaceMapping(Class<?> clazz) {
        return interfaces.get(clazz);
    }

    public void addInterfaceMapping(Class<?> iFace, Class<?> clazz) {
        interfaces.put(iFace, clazz);
    }

}