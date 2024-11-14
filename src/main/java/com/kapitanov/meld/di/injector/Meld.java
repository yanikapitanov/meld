package com.kapitanov.meld.di.injector;

import com.kapitanov.meld.di.annotations.Wired;

import java.lang.reflect.Constructor;
import java.util.Set;

public class Meld {

    private Meld() {

    }

    public static void run(Class<?> cls) {
        Set<Class<?>> classes = ClassScanner.findClasses(cls);
        Context context = new Context();

        for (Class<?> clazz : classes) {
            try {
                Constructor<?> constructor = findAutowiredConstructor(clazz);
                Object instance = createInstance(context, constructor);
                context.addInstance(clazz, instance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Object createInstance(Context context, Constructor<?> constructor) throws Exception {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = getInstance(context, parameterTypes[i]);
        }
        return constructor.newInstance(parameters);
    }

    private static Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Wired.class)) {
                return constructor;
            }
        }
        throw new RuntimeException("No @Inject constructor found for class " + clazz);
    }


    public static <T> T getInstance(Context context, Class<T> clazz) {
        return clazz.cast(context.get(clazz));
    }
}
