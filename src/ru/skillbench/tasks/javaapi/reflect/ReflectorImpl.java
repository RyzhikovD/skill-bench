package ru.skillbench.tasks.javaapi.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ReflectorImpl implements Reflector {
    private Class<?> clazz;

    @Override
    public void setClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Stream<String> getMethodNames(Class<?>... paramTypes) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> Arrays.equals(method.getParameterTypes(), paramTypes))
                .map(Method::getName);
    }

    @Override
    public Stream<Field> getAllDeclaredFields() {
        ArrayList<Class> hierarchy = new ArrayList<>();
        Class<?> hierarchyClass = clazz;
        do {
            hierarchy.add(hierarchyClass);
            hierarchyClass = hierarchyClass.getSuperclass();
        } while(hierarchyClass != null);
        return hierarchy.parallelStream()
                .map(Class::getDeclaredFields)
                .flatMap(Arrays::stream)
                .filter(((Predicate<Field>)field -> Modifier.isStatic(field.getModifiers())).negate());
    }

    @Override
    public Object getFieldValue(Object target, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = (clazz == null ? target.getClass() : clazz).getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    @Override
    public Object getMethodResult(Object constructorParam, String methodName, Object... methodParams)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?>[] parameterTypes = Arrays.stream(methodParams)
                .map(Object::getClass)
                .toArray(Class[]::new);
        try {
            Object newInstance;
            if(constructorParam != null) {
                newInstance = clazz.getConstructor(constructorParam.getClass()).newInstance(constructorParam);
            } else {
                newInstance = clazz.getConstructor().newInstance();
            }
            Method method;
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                method = clazz.getMethod(methodName, parameterTypes);
            }
            method.setAccessible(true);
            return method.invoke(newInstance, methodParams);
        } catch (InvocationTargetException e) {
            if(e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            } else {
                throw e;
            }
        }
    }
}