package com.kapitanov.meld.di.injector;

import com.kapitanov.meld.di.annotations.Wired;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.Set;

public class Meld {

    private static final Meld APP = new Meld();
    private final Context context = new Context();

    private Meld() {

    }

    public static void run(Class<?> cls) {
        Set<Class<?>> classes = ClassScanner.findClasses(cls);
        for (Class<?> aClass : classes) {
            findInterface(aClass).ifPresent(f -> APP.context.addInterfaceMapping(f[0], f[1]));
        }

        for (Class<?> clazz : classes) {
            try {
                Constructor<?> constructor = APP.findAutowiredConstructor(clazz);
                Object instance = APP.createInstance(constructor);
                APP.context.addInstance(clazz, instance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object createInstance(Constructor<?> constructor) throws Exception {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Object instance = getInstance(context, parameterTypes[i]);
            if (instance == null) {
                Constructor<?> autowiredConstructor = findAutowiredConstructor(parameterTypes[i]);
                parameters[i] = createInstance(autowiredConstructor);
            } else {
                parameters[i] = instance;
            }
        }
        return constructor.newInstance(parameters);
    }

    private static Optional<Class<?>[]> findInterface(Class<?> clazz) {
        if (clazz.getInterfaces().length == 0) {
            return Optional.empty();
        }
        if (clazz.getInterfaces().length > 1) {
            throw new RuntimeException("More than one interfaces found for service: " + clazz);
        }
        Class<?>[] classes = {clazz.getInterfaces()[0], clazz};
        return Optional.of(classes);
    }
  
    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        if (clazz.isInterface()) {
            clazz = context.getInterfaceMapping(clazz);
        }
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Wired.class)) {
                return constructor;
            }
            if (constructor.getParameterTypes().length == 0) {
                return constructor;
            }
        }
        throw new RuntimeException("More than one constructor found for service: " + clazz);
    }

    public static <T> T getInstance(Context context, Class<T> clazz) {
        return clazz.cast(context.get(clazz));
    }
}
