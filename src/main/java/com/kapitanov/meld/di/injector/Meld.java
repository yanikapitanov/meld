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
        try {
            linkInterfaces(classes);
            for (Class<?> clazz : classes) {
                if (APP.context.get(clazz) != null) {
                    continue;
                }
                Constructor<?> constructor = APP.findAutowiredConstructor(clazz);
                Object instance = APP.createInstance(constructor);
                APP.context.addInstance(clazz, instance);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    private static Optional<ClassInterfaceLinker> findInterface(Class<?> clazz) {
        if (clazz.getInterfaces().length == 0) {
            return Optional.empty();
        }
        if (clazz.getInterfaces().length > 1) {
            throw new RuntimeException("More than one interfaces found for service: " + clazz);
        }
        ClassInterfaceLinker linker = new ClassInterfaceLinker(clazz.getInterfaces()[0], clazz);
        return Optional.of(linker);
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        Class<?> toBeChecked = clazz.isInterface() ? context.getInterfaceMapping(clazz) : clazz;

        for (Constructor<?> constructor : toBeChecked.getDeclaredConstructors()) {
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

    private static void linkInterfaces(Set<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            findInterface(aClass)
                    .ifPresent(linker -> APP.context.addInterfaceMapping(
                            linker.interFace(),
                            linker.clazz())
                    );
        }
    }
}
