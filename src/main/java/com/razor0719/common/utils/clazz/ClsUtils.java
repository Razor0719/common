package com.razor0719.common.utils.clazz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.Descriptor;
import javassist.bytecode.LineNumberAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.LocalVariableTypeAttribute;
import javassist.bytecode.SignatureAttribute;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @author baoyl
 * @created 2018/11/15
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClsUtils {
    protected static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");
    /**
     * 类和类信息映射缓存
     */
    private static final Map<Class<?>, ClsInfo> CLASS_INFO_CACHE = Maps.newConcurrentMap();
    /**
     * 类继承关系缓存 key:原始类型，value.key:继承类型
     */
    private static final Map<Class<?>, Map<Type, Class<?>[]>> EXTEND_ACTUAL_TYPE_CACHE = Maps.newConcurrentMap();
    /**
     * 类属性与读方法映射缓存
     */
    private static final Map<Class<?>, Map<String, Method>> READING_METHOD_CACHE = Maps.newConcurrentMap();
    /**
     * 类属性与写方法映射缓存
     */
    private static final Map<Class<?>, Map<String, Method>> WRITING_METHOD_CACHE = Maps.newConcurrentMap();
    /**
     * 类的实例属性缓存
     */
    private static final Map<Class<?>, List<Field>> INSTANT_FIELD_CACHE = Maps.newConcurrentMap();
    /**
     * 方法调用栈元素缓存
     */
    private static final Map<StackTraceElement, Method> STACK_TRACE_METHOD_CACHE = Maps.newConcurrentMap();
    /**
     * 方法调用栈元素方法反射对象
     */
    private static final Method GET_STACK_TRACE_ELEMENT_METHOD;
    private static final String ARRAY_TYPE_FORMAT = "[]";
    private static final String IS_REGEX = "^is([a-z_]|[A-Z]).*$";
    private static final String GET_REGEX = "^get([a-z_]|[A-Z]).*$";
    private static final String SET_REGEX = "^set([a-z_]|[A-Z]).*$";
    private static final String CLASS_NAME_REGEX = "^[A-Z]\\w{2,}$";
    private static final String ARRAY_REGEX = "\\[\\]$";
    private static final String GENERIC_REGEX = "^.+<.+>$";
    private static final String WILDCARD = "?";
    private static final String WILDCARD_EXTEND = "?extends";
    private static final String WILDCARD_SUPER = "?super";

    static {
        try {
            //初始化getStackTraceElementMethod
            GET_STACK_TRACE_ELEMENT_METHOD = Throwable.class.getDeclaredMethod("getStackTraceElement", int.class);
            GET_STACK_TRACE_ELEMENT_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new ClassCastException(e.getMessage());
        }

    }

    public static Modifiers getModifiers(Field field) {
        FieldInfo fieldInfo = getInfo(field);
        return fieldInfo == null ? null : fieldInfo.getModifiers();
    }

    public static Modifiers getModifiers(Class<?> clazz) {
        return getInfo(clazz).getModifiers();
    }

    public static Modifiers getModifiers(Constructor<?> constructor) {
        ConstructorInfo constructorInfo = getInfo(constructor);
        return constructorInfo == null ? null : constructorInfo.getModifiers();
    }

    public static Modifiers getModifiers(Method method) {
        MethodInfo methodInfo = getInfo(method);
        return methodInfo == null ? null : methodInfo.getModifiers();
    }

    public static String[] getParameters(Constructor<?> constructor) {
        ConstructorInfo constructorInfo = getInfo(constructor);
        return constructorInfo == null ? null : constructorInfo.getParameters();
    }

    public static String getParameter(Constructor<?> constructor, int index) {
        String[] constructorParameters = getParameters(constructor);
        return constructorParameters == null || constructorParameters.length <= index ? null : constructorParameters[index];
    }

    public static String[] getParameters(Method method) {
        MethodInfo methodInfo = getInfo(method);
        return methodInfo == null ? null : methodInfo.getParameters();
    }

    public static String getMethodParameter(Method method, int index) {
        String[] methodParameters = getParameters(method);
        return methodParameters == null || methodParameters.length <= index ? null : methodParameters[index];
    }

    public static FieldInfo getInfo(Field field) {
        return getInfo(field.getDeclaringClass()).getFieldInfoMap().get(field);
    }

    public static ClsInfo getInfo(Class<?> clazz) {
        ClsInfo clsInfo = CLASS_INFO_CACHE.get(clazz);
        if (clsInfo == null) {
            try {
                clsInfo = buildClassInfo(clazz);
            } catch (Exception e) {
                clsInfo = new ClsInfo(clazz, null, null);
            }
            CLASS_INFO_CACHE.put(clazz, clsInfo);
        }
        return clsInfo;
    }

    public static ConstructorInfo getInfo(Constructor<?> constructor) {
        return getInfo(constructor.getDeclaringClass()).getConstructorInfoMap().get(constructor);
    }

    public static MethodInfo getInfo(Method method) {
        return getInfo(method.getDeclaringClass()).getMethodInfoMap().get(method);
    }

    private static Map<Method, MethodInfo> getStackTraceElementMethodMap(StackTraceElement stackTraceElement) throws ClassNotFoundException {
        Class<?> clazz = ClassUtils.getClass(stackTraceElement.getClassName());
        return getInfo(clazz).getMethodInfoMap();
    }

    /**
     * 获取方法调用栈的第index层栈元素
     *
     * @param index 当前方法为栈底0
     * @return
     */
    public static StackTraceElement getStackTraceElement(int index) {
        try {
            return (StackTraceElement) GET_STACK_TRACE_ELEMENT_METHOD.invoke(new Throwable(), index);
        } catch (Exception e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    /**
     * 获取方法调用栈的第index层方法调用信息
     *
     * @param index 当前方法为栈底0
     * @return
     */
    public static StackTraceMethodInfo getStackTraceMethodInfo(int index) {
        try {
            StackTraceElement stackTraceElement = (StackTraceElement) GET_STACK_TRACE_ELEMENT_METHOD.invoke(new Throwable(), index);
            Map<Method, MethodInfo> methodInfoMap = getStackTraceElementMethodMap(stackTraceElement);
            for (Map.Entry<Method, MethodInfo> entry : methodInfoMap.entrySet()) {
                Method method = entry.getKey();
                MethodInfo methodInfo = entry.getValue();
                if (methodInfo != null && Objects.equal(method.getName(), stackTraceElement.getMethodName())
                        && Range.closed(methodInfo.getStartLine(), methodInfo.getEndLine()).contains(stackTraceElement.getLineNumber())) {
                    return new StackTraceMethodInfo(methodInfo, stackTraceElement.getLineNumber());
                }
            }
        } catch (Exception e) {
            throw new ClassCastException(e.getMessage());
        }
        return null;
    }

    /**
     * 获取方法调用栈中带注解的方法调用信息
     *
     * @param clazz 注解类型
     * @return
     */
    public static StackTraceMethodInfo getStackTraceMethodInfo(Class<? extends Annotation> clazz) {
        List<StackTraceElement> stackTraceElements = Arrays.asList(new Throwable().getStackTrace());
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            try {
                Map<Method, MethodInfo> methodInfoMap = getStackTraceElementMethodMap(stackTraceElement);
                for (Map.Entry<Method, MethodInfo> entry : methodInfoMap.entrySet()) {
                    Method method = entry.getKey();
                    MethodInfo methodInfo = entry.getValue();
                    if (methodInfo != null && Objects.equal(method.getName(), stackTraceElement.getMethodName())
                            && method.getAnnotation(clazz) != null
                            && Range.closed(methodInfo.getStartLine(), methodInfo.getEndLine()).contains(stackTraceElement.getLineNumber())) {
                        return new StackTraceMethodInfo(methodInfo, stackTraceElement.getLineNumber());
                    }
                }
            } catch (Exception e) {
                throw new ClassCastException(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 解析type为字符串
     *
     * @param type
     * @return
     */
    public static String formatType(Type type) {
        Class<?> clazz = (Class) type;
        return type instanceof Class ?
                clazz.isArray() ? formatType(clazz.getComponentType()) + ARRAY_TYPE_FORMAT : clazz.getName()
                : type.toString();
    }

    /**
     * 解析type为字符串
     *
     * @param types
     * @return
     */
    public static List<String> formatType(List<Type> types) {
        List<String> result = Lists.newArrayList();
        for (Type type : types) {
            result.add(formatType(type));
        }
        return result;
    }

    /**
     * 解析type为字符串
     *
     * @param types
     * @return
     */
    public static List<String> formatType(Type... types) {
        List<String> result = Lists.newArrayList();
        for (Type type : types) {
            result.add(formatType(type));
        }
        return result;
    }

    /**
     * 解析字符串type指定的类型
     *
     * @param type
     * @return
     */
    public static Type parseType(String type) {
        return parseType(type, null);
    }

    /**
     * 解析type
     *
     * @param type
     * @param typeMap
     * @return
     */
    public static Type parseType(String type, Map<String, Class<?>> typeMap) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        type = type.replaceAll("\\s", StringUtils.EMPTY);
        try {
            if (type.matches(ARRAY_REGEX)) {
                return getArrayType(parseType(type.substring(0, type.length() - 2), typeMap));
            }
            if (type.matches(GENERIC_REGEX)) {
                int ltIndex = type.indexOf('<');
                ParameterizedTypeImpl parameterizedType = new ParameterizedTypeImpl(getClass(type.substring(0, ltIndex)));
                int stack = 0;
                int start = type.indexOf('<');
                for (int end = start; end < type.length() - 1; end++) {
                    switch (type.charAt(end)) {
                        case '<':
                            stack++;
                            break;
                        case '>':
                            stack--;
                            break;
                        case ',':
                            if (stack == 0 && start < end) {
                                parameterizedType.addActualTypeArguments(parseType(type.substring(start, end), typeMap));
                            }
                            break;
                        default:
                    }
                }
                if (start < type.length() - 1) {
                    parameterizedType.addActualTypeArguments(parseType(type.substring(start, type.length() - 1), typeMap));
                }
                return parameterizedType;
            }
            if (Objects.equal(WILDCARD, type)) {
                return new WildcardTypeImpl();
            }
            if (type.startsWith(WILDCARD_EXTEND)) {
                return new WildcardTypeImpl(parseType(type.substring(8), typeMap));
            }
            if (type.startsWith(WILDCARD_SUPER)) {
                return new WildcardTypeImpl(null, parseType(type.substring(6), typeMap));
            }
            if (typeMap != null && typeMap.keySet().contains(type)) {
                return typeMap.get(type);
            }
            return getClass(type);
        } catch (Exception e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    /**
     * 获取泛型对象
     *
     * @param rawType
     * @param actualTypeArguments
     * @return
     */
    public static ParameterizedType getParameterizedType(Class<?> rawType, Type... actualTypeArguments) {
        return new ParameterizedTypeImpl(rawType, actualTypeArguments);
    }

    /**
     * 获取List泛型对象
     *
     * @param elementType List元素类型
     * @return
     */
    public static ParameterizedType getListType(Type elementType) {
        return getParameterizedType(List.class, elementType);
    }

    /**
     * 获取Map泛型对象，key为String
     *
     * @param valueType Map值类型
     * @return
     */
    public static ParameterizedType getMapType(Type valueType) {
        return getParameterizedType(Map.class, String.class, valueType);
    }

    /**
     * 获取元素类型为elementType的数组类型
     *
     * @param elementType
     * @return
     */
    public static Type getArrayType(Type elementType) {
        try {
            return elementType instanceof Class ?
                    ClassUtils.getClass(formatType(elementType) + ARRAY_TYPE_FORMAT) :
                    new GenericArrayTypeImpl(elementType);
        } catch (Exception e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    /**
     * 获取type的原始类型
     *
     * @param type
     * @param typeProviders
     * @return
     */
    public static Class<?> getRawClass(Type type, Type... typeProviders) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof GenericArrayType) {
            return (Class<?>) getArrayType(getRawClass(((GenericArrayType) type).getGenericComponentType(), typeProviders));
        }
        if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType(), typeProviders);
        }
        if (type instanceof WildcardType) {
            WildcardType wildcard = (WildcardType) type;
            Type[] lowerBounds = wildcard.getLowerBounds();
            return getRawClass(lowerBounds.length == 0 ? wildcard.getUpperBounds()[0] : lowerBounds[0], typeProviders);
        }
        if (type instanceof TypeVariable) {
            TypeVariable<? extends GenericDeclaration> typeVariable = (TypeVariable<?>) type;
            Class<?> rawClass = getRawClass(typeVariable.getBounds()[0], typeProviders);
            if (typeProviders != null && typeProviders.length > 0 && typeVariable.getGenericDeclaration() instanceof Class) {
                Class<?> genericDeclaration = (Class<?>) typeVariable.getGenericDeclaration();
                for (Type typeProvider : typeProviders) {
                    if (genericDeclaration.isAssignableFrom(getRawClass(typeProvider))) {
                        for (int i = 0; i < genericDeclaration.getTypeParameters().length; i++) {
                            if (Objects.equal(typeVariable, genericDeclaration.getTypeParameters()[i])) {
                                Class<?> actualType = getActualTypeArguments(genericDeclaration, typeProvider, typeProviders)[i];
                                if (actualType != rawClass) {
                                    return actualType;
                                }
                                break;
                            }
                        }
                    }
                }
            }
            return rawClass;
        }
        throw new ClassCastException("Unknown Type : " + type);
    }

    public static Class<?>[] getActualTypeArguments(Class<?> rawType, Type extendType, Type... typeProviders) {
        if (!rawType.isAssignableFrom(getRawClass(extendType))) {
            return new Class<?>[0];
        }
        TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
        if (typeParameters.length == 0) {
            return new Class<?>[0];
        }
        Class<?>[] actualTypeArguments = null;
        Map<Type, Class<?>[]> actualTypeArgumentsCache = EXTEND_ACTUAL_TYPE_CACHE.get(rawType);
        if (actualTypeArgumentsCache != null) {
            actualTypeArguments = actualTypeArgumentsCache.get(extendType);
        } else {
            actualTypeArgumentsCache = EXTEND_ACTUAL_TYPE_CACHE.put(rawType, Maps.newHashMap());
        }
        if (actualTypeArguments != null) {
            return actualTypeArguments;
        }
        actualTypeArguments = new Class<?>[typeParameters.length];
        List<Type> extendChain = Lists.newArrayList();
        Type itemType = extendType;
        do {
            extendChain.add(itemType);
        } while ((itemType = checkDirectCompatible(rawType, itemType)) != null);
        extendChain.addAll(getDirectCompatibleExtendChain(rawType, extendChain.remove(extendChain.size() - 1)));
        Map<Integer, Integer> indexMapping = Maps.newHashMap();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            indexMapping.put(i, i);
        }
        for (int i = extendChain.size() - 1; i >= 0; i--) {
            if (indexMapping.isEmpty() || !(extendChain.get(i) instanceof ParameterizedType)) {
                break;
            }
            Type[] params = ((ParameterizedType) extendChain.get(i)).getActualTypeArguments();
            Map<Integer, Integer> nextIndexMapping = Maps.newHashMap();
            for (Map.Entry<Integer, Integer> entry : indexMapping.entrySet()) {
                Integer j = entry.getKey();
                Type param = params[j];
                actualTypeArguments[entry.getValue()] = getRawClass(param, typeProviders);
                if (i > 0 && (param instanceof TypeVariable) && (extendChain.get(i - 1) instanceof ParameterizedType)) {
                    TypeVariable<?>[] vars = getRawClass(extendChain.get(i - 1)).getTypeParameters();
                    for (int k = 0; k < vars.length; k++) {
                        if (Objects.equal(vars[k], param)) {
                            nextIndexMapping.put(k, entry.getValue());
                            break;
                        }
                    }
                }
            }
            indexMapping = nextIndexMapping;
        }
        for (int i = 0; i < actualTypeArguments.length; i++) {
            if (actualTypeArguments[i] == null) {
                actualTypeArguments[i] = getRawClass(typeParameters[i], typeProviders);
            }
        }
        actualTypeArgumentsCache.put(extendType, actualTypeArguments);
        return actualTypeArguments;
    }

    private static void putReadMethodToMap(Map<String, Method> map, String key, Method value) {
        Method last = map.get(key);
        if (last == null || last.getReturnType().isAssignableFrom(value.getReturnType())) {
            map.put(key, value);
        }
    }

    private static void putWriteMethodToMap(Map<String, Method> map, String key, Method value) {
        Method last = map.get(key);
        if (last == null || last.getParameterTypes()[0].isAssignableFrom(value.getParameterTypes()[0])) {
            map.put(key, value);
        }
    }

    public static Map<String, Method> getReadMethodMapping(Class<?> clazz) {
        Map<String, Method> readMap = READING_METHOD_CACHE.get(clazz);
        if (readMap != null) {
            return readMap;
        }
        Map<String, Method> getMap = Maps.newHashMap();
        Map<String, Method> isMap = Maps.newHashMap();
        for (Method method : clazz.getMethods()) {
            if (Modifier.isStatic(method.getModifiers()) || method.getParameterTypes().length > 0) {
                continue;
            }
            String methodName = method.getName();
            if (method.getReturnType() == boolean.class) {
                if (methodName.matches(IS_REGEX)) {
                    putReadMethodToMap(isMap, methodName.substring(2).toLowerCase(), method);
                }
            } else {
                if (methodName.matches(GET_REGEX)) {
                    putReadMethodToMap(getMap, methodName.substring(3).toLowerCase(), method);
                }
            }
        }
        ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
        builder.putAll(getMap);
        builder.putAll(isMap);
        return READING_METHOD_CACHE.put(clazz, builder.build());
    }

    public static Map<String, Method> getWriteMethodMapping(Class<?> clazz) {
        Map<String, Method> writeMap = WRITING_METHOD_CACHE.get(clazz);
        if (writeMap != null) {
            return writeMap;
        }
        Map<String, Method> setMap = Maps.newHashMap();
        for (Method method : clazz.getMethods()) {
            if (Modifier.isStatic(method.getModifiers()) || method.getParameterTypes().length != 1) {
                continue;
            }
            String methodName = method.getName();
            if (methodName.matches(SET_REGEX)) {
                putWriteMethodToMap(setMap, methodName.substring(3).toLowerCase(), method);
            }
        }
        ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
        builder.putAll(setMap);
        return WRITING_METHOD_CACHE.put(clazz, builder.build());
    }

    /**
     * 获取实例属性
     *
     * @param clazz
     * @return
     */
    public static List<Field> getInstantFields(Class<?> clazz) {
        List<Field> fields = INSTANT_FIELD_CACHE.get(clazz);
        if (fields != null) {
            return fields;
        }
        fields = Lists.newArrayList();
        for (Field field : clazz.getDeclaredFields()) {
            Modifiers modifiers = getModifiers(field);
            if (modifiers != null && !modifiers.isStatic()) {
                fields.add(field);
            }
        }
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            fields.addAll(getInstantFields(superClazz));
        }
        return INSTANT_FIELD_CACHE.put(clazz, fields);
    }

    /**
     * 获取类
     *
     * @param name
     * @return
     */
    public static Class<?> getClass(String name) {
        try {
            return ClassUtils.getClass(name);
        } catch (ClassNotFoundException e) {
            if (name.matches(CLASS_NAME_REGEX)) {
                try {
                    return ClassUtils.getClass("java.lang." + name);
                } catch (ClassNotFoundException e2) {
                    try {
                        return ClassUtils.getClass("java.util." + name);
                    } catch (ClassNotFoundException e3) {
                        try {
                            return ClassUtils.getClass("java.math." + name);
                        } catch (ClassNotFoundException e4) {
                            log.warn(name + "not found!");
                        }
                    }
                }
            }
            throw new ClassCastException(e.getMessage());
        }
    }

    /**
     * 获取栈元素所在方法
     *
     * @param stackTraceElement
     * @return
     */
    public static Method getStackTraceMethod(StackTraceElement stackTraceElement) {
        Method method = STACK_TRACE_METHOD_CACHE.get(stackTraceElement);
        if (method == null) {
            Map<Method, MethodInfo> methodInfoMap = getInfo(getClass(stackTraceElement.getClassName())).getMethodInfoMap();
            for (Map.Entry<Method, MethodInfo> entry : methodInfoMap.entrySet()) {
                Method m = entry.getKey();
                if (Objects.equal(m.getName(), stackTraceElement.getMethodName())) {
                    MethodInfo methodInfo = methodInfoMap.get(m);
                    if (Range.closed(methodInfo.getStartLine(), methodInfo.getEndLine()).contains(stackTraceElement.getLineNumber())) {
                        method = STACK_TRACE_METHOD_CACHE.put(stackTraceElement, method);
                        break;
                    }
                }
            }
        }
        return method;
    }

    /**
     * 继承类型是否直接兼容原始类型，不兼容则返回继承类型父类
     *
     * @param rawClass
     * @param extendType
     * @return
     */
    public static Type checkDirectCompatible(Class<?> rawClass, Type extendType) {
        Class<?> extendClass = getRawClass(extendType);
        if (Objects.equal(rawClass, extendClass)) {
            return null;
        }
        if (rawClass.isInterface()) {
            for (Class<?> clazz : extendClass.getInterfaces()) {
                if (Objects.equal(rawClass, clazz)) {
                    return null;
                }
                if (ClassUtils.getAllInterfaces(clazz).contains(rawClass)) {
                    return null;
                }
            }
        }
        return extendClass.getGenericSuperclass();
    }

    /**
     * 获取直接兼容继承链
     *
     * @param rawClass
     * @param extendType
     * @return
     */
    public static List<Type> getDirectCompatibleExtendChain(Class<?> rawClass, Type extendType) {
        List<Type> extendChain = Lists.newArrayList();
        extendChain.add(extendType);
        Class<?> extendClass = getRawClass(extendType);
        if (!Objects.equal(rawClass, extendClass)) {
            for (Type type : extendClass.getGenericInterfaces()) {
                Class<?> clazz = getRawClass(type);
                if (Objects.equal(rawClass, clazz) || ClassUtils.getAllInterfaces(clazz).contains(rawClass)) {
                    extendChain.addAll(getDirectCompatibleExtendChain(rawClass, type));
                    break;
                }
            }
        }
        return extendChain;
    }

    private static ClsInfo buildClassInfo(Class<?> clazz) throws Exception {
        //创建clazz的CtClass
        CtClass ctClass = buildCtClass(clazz);
        ClsInfo clsInfo = new ClsInfo(clazz, ctClass, new SsistModifier(ctClass.getModifiers()));
        for (TypeVariable<?> typeVariable : clazz.getTypeParameters()) {
            buildGeneric(clsInfo, typeVariable);
        }
        //设置泛型变量与上界映射为只读
        ImmutableMap.Builder<String, Class<?>> genericMapBuilder = ImmutableMap.builder();
        clsInfo.setGenericMap(genericMapBuilder.putAll(clsInfo.getGenericMap()).build());

        for (CtField ctField : ctClass.getDeclaredFields()) {
            buildFieldInfo(clsInfo, ctField);
        }
        //设置属性信息映射为只读
        ImmutableMap.Builder<Field, FieldInfo> fieldInfoMapBuilder = ImmutableMap.builder();
        clsInfo.setFieldInfoMap(fieldInfoMapBuilder.putAll(clsInfo.getFieldInfoMap()).build());

        for (CtConstructor ctConstructor : ctClass.getDeclaredConstructors()) {
            ConstructorInfo constructorInfo = buildConstructorInfo(clsInfo, ctConstructor);
            clsInfo.getConstructorInfoMap().put(constructorInfo.getConstructor(), constructorInfo);
        }
        //设置构造方法信息映射为只读
        ImmutableMap.Builder<Constructor<?>, ConstructorInfo> constructorInfoMapBuilder = ImmutableMap.builder();
        clsInfo.setConstructorInfoMap(constructorInfoMapBuilder.putAll(clsInfo.getConstructorInfoMap()).build());

        for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
            MethodInfo methodInfo = buildMethodInfo(clsInfo, ctMethod);
            clsInfo.getMethodInfoMap().put(methodInfo.getMethod(), methodInfo);
        }
        //设置方法信息映射为只读
        ImmutableMap.Builder<Method, MethodInfo> methodInfoMapBuilder = ImmutableMap.builder();
        clsInfo.setMethodInfoMap(methodInfoMapBuilder.putAll(clsInfo.getMethodInfoMap()).build());

        return clsInfo;
    }

    private static void buildGeneric(ClsInfo clsInfo, TypeVariable typeVariable) {
        clsInfo.getGenericMap().put(typeVariable.getName(), getRawClass(typeVariable));
    }

    private static void buildFieldInfo(ClsInfo clsInfo, CtField ctField) throws NoSuchFieldException {
        Field field = clsInfo.getClazz().getDeclaredField(ctField.getName());
        FieldInfo fieldInfo = new FieldInfo(field, ctField, new SsistModifier(ctField.getModifiers()));
        clsInfo.getFieldInfoMap().put(field, fieldInfo);
    }

    private static CtClass buildCtClass(Class<?> clazz) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        String name = clazz.getName();
        try {
            return pool.get(name);
        } catch (Exception e) {
            Class<?> type = clazz;
            while (type.isArray()) {
                type = type.getComponentType();
            }
            try {
                pool.insertClassPath(new ClassClassPath(type));
                return pool.get(name);
            } catch (Exception e2) {
                try {
                    pool.insertClassPath(type.getProtectionDomain().getCodeSource().getLocation().getPath());
                    return pool.get(name);
                } catch (Exception e3) {
                    pool.insertClassPath(new LoaderClassPath(type.getClassLoader()));
                    return pool.get(name);
                }
            }
        }
    }

    private static Class<?>[] convertCtParameterType2ParameterType(CtClass[] ctParameterTypes) throws ClassNotFoundException {
        Class<?>[] parameterTypes = new Class<?>[ctParameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypes[i] = ClassUtils.getClass(ctParameterTypes[i].getName());
        }
        return parameterTypes;
    }

    private static ConstructorInfo buildConstructorInfo(ClsInfo clsInfo, CtConstructor ctConstructor) throws Exception {
        CtClass[] ctParameterTypes = ctConstructor.getParameterTypes();
        Class<?>[] parameterTypes = convertCtParameterType2ParameterType(ctConstructor.getParameterTypes());
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypes[i] = ClassUtils.getClass(ctParameterTypes[i].getName());
        }
        Constructor<?> constructor = clsInfo.getClazz().getDeclaredConstructor(parameterTypes);
        ConstructorInfo constructorInfo = new ConstructorInfo(constructor, ctConstructor, new SsistModifier(ctConstructor.getModifiers()));
        try {
            String[] parameterNames = new String[parameterTypes.length];
            constructorInfo.setParameters(parameterNames);
            constructorInfo.setStartLine(ctConstructor.getMethodInfo().getLineNumber(Integer.MIN_VALUE));
            constructorInfo.setEndLine(ctConstructor.getMethodInfo().getLineNumber(Integer.MAX_VALUE));
            if (ctConstructor.getMethodInfo().getCodeAttribute() == null) {
                return constructorInfo;
            }
            LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute) ctConstructor
                    .getMethodInfo().getCodeAttribute().getAttribute(LocalVariableAttribute.tag);
            if (localVariableAttribute == null) {
                return constructorInfo;
            }
            Map<Integer, String> localVariableNames = new HashMap<>();
            for (int i = 0; i < localVariableAttribute.tableLength(); i++) {
                localVariableNames.put(localVariableAttribute.index(i), localVariableAttribute.variableName(i));
            }
            List<Integer> localVariableNameIndexs = new ArrayList<>(localVariableNames.keySet());
            Collections.sort(localVariableNameIndexs);
            for (int i = 0; i < parameterNames.length && i + 1 < localVariableNameIndexs.size(); i++) {
                parameterNames[i] = localVariableNames.get(localVariableNameIndexs.get(i + 1));
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return constructorInfo;
    }

    private static MethodInfo buildMethodInfo(ClsInfo clsInfo, CtMethod ctMethod) throws Exception {
        CtClass[] ctParameterTypes = ctMethod.getParameterTypes();
        Class<?>[] parameterTypes = convertCtParameterType2ParameterType(ctMethod.getParameterTypes());
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypes[i] = ClassUtils.getClass(ctParameterTypes[i].getName());
        }
        Method method = clsInfo.getClazz().getDeclaredMethod(ctMethod.getName(), parameterTypes);
        MethodInfo methodInfo = new MethodInfo(method, ctMethod, new SsistModifier(ctMethod.getModifiers()));
        try {
            Map<String, Class<?>> typeVariableMapping = Maps.newHashMap();
            typeVariableMapping.putAll(clsInfo.getGenericMap());
            for (TypeVariable<?> typeVariable : method.getTypeParameters()) {
                Class<?> bound = getRawClass(typeVariable);
                methodInfo.getGenericMap().put(typeVariable.getName(), bound);
                typeVariableMapping.put(typeVariable.getName(), bound);
            }
            //设置方法泛型变量与其上界映射为只读
            ImmutableMap.Builder<String, Class<?>> genericMapBuilder = ImmutableMap.builder();
            methodInfo.setGenericMap(genericMapBuilder.putAll(methodInfo.getGenericMap()).build());
            if (typeVariableMapping.isEmpty()) {
                typeVariableMapping = null;
            }
            String[] parameters = new String[parameterTypes.length];
            methodInfo.setParameters(parameters);
            methodInfo.setStartLine(ctMethod.getMethodInfo().getLineNumber(Integer.MIN_VALUE));
            methodInfo.setEndLine(ctMethod.getMethodInfo().getLineNumber(Integer.MAX_VALUE));
            CodeAttribute codeAttribute = ctMethod.getMethodInfo().getCodeAttribute();
            if (codeAttribute == null) {
                return methodInfo;
            }
            LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (localVariableAttribute == null) {
                return methodInfo;
            }
            LineNumberAttribute lineNumberAttribute = (LineNumberAttribute) codeAttribute.getAttribute(LineNumberAttribute.tag);
            List<Integer> lines = Lists.newArrayList();
            if (lineNumberAttribute != null) {
                Map<Integer, Integer> lineNumberMapping = new HashMap<>();
                for (int i = 0; i < lineNumberAttribute.tableLength(); i++) {
                    lineNumberMapping.put(lineNumberAttribute.startPc(i), lineNumberAttribute.lineNumber(i));
                }
                List<Integer> startPcs = new ArrayList<>(lineNumberMapping.keySet());
                Collections.sort(startPcs);
                for (Integer startPc : startPcs) {
                    lines.add(lineNumberMapping.get(startPc));
                }
            }
            Map<String, List<LocalVariableInfo>> localVariableInfoMapping = Maps.newHashMap();
            int maxIndex = 0;
            for (int i = 0; i < localVariableAttribute.tableLength(); i++) {
                LocalVariableInfo localVariableInfo = new LocalVariableInfo();
                localVariableInfo.setName(localVariableAttribute.variableName(i));
                localVariableInfo.setType(parseType(Descriptor.toString(localVariableAttribute.descriptor(i)), typeVariableMapping));
                localVariableInfo.setMethod(method);
                int index = localVariableAttribute.index(i);
                localVariableInfo.setIndex(index);
                if (index > maxIndex) {
                    maxIndex = index;
                }
                if (lineNumberAttribute != null) {
                    int startPc = localVariableAttribute.startPc(i);
                    localVariableInfo.setStartLine(lineNumberAttribute.toLineNumber(startPc));
                    localVariableInfo.setLine(lineNumberAttribute.toLineNumber(startPc - 1));
                    if (localVariableInfo.getLine() < methodInfo.getStartLine()) {
                        localVariableInfo.setLine(methodInfo.getStartLine());
                    }
                    if (localVariableInfo.getLine() < localVariableInfo.getStartLine()) {
                        for (int j = lines.size() - 1; j >= 0; j--) {
                            if (lines.get(j) == localVariableInfo.getLine()) {
                                for (int k = j - 1; k >= 0; k--) {
                                    if (lines.get(k) > localVariableInfo.getLine()) {
                                        localVariableInfo.setInitLine(lines.get(k));
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    if (localVariableInfo.getInitLine() == 0) {
                        localVariableInfo.setInitLine(localVariableInfo.getLine());
                    } else if (localVariableInfo.getInitLine() > localVariableInfo.getStartLine()) {
                        localVariableInfo.setInitLine(localVariableInfo.getStartLine());
                    }
                }
                List<LocalVariableInfo> localVariableInfos = localVariableInfoMapping.get(localVariableInfo.getName());
                if (localVariableInfos == null) {
                    localVariableInfos = Lists.newArrayList();
                    localVariableInfoMapping.put(localVariableInfo.getName(), localVariableInfos);
                }
                localVariableInfos.add(localVariableInfo);
            }
            ImmutableMap.Builder<String, List<LocalVariableInfo>> localVariableInfoMapBuilder = ImmutableMap.builder();
            methodInfo.setLocalVariableInfoMap(localVariableInfoMapBuilder.putAll(localVariableInfoMapping).build());
            Map<Integer, LocalVariableInfo> localVariableMapping = Maps.newHashMap();
            for (List<LocalVariableInfo> localVariableInfos : localVariableInfoMapping.values()) {
                for (LocalVariableInfo localVariableInfo : localVariableInfos) {
                    localVariableMapping.put(localVariableInfo.getLine() * (maxIndex + 1) + localVariableInfo.getIndex(),
                            localVariableInfo);
                }
            }
            List<Integer> localVariableKeys = Lists.newArrayList(localVariableMapping.keySet());
            Collections.sort(localVariableKeys);
            int offset = methodInfo.getModifiers().isStatic() ? 0 : 1;
            for (int i = 0; i < parameters.length && i + offset < localVariableKeys.size(); i++) {
                parameters[i] = localVariableMapping.get(localVariableKeys.get(i + offset)).getName();
            }
            if (lineNumberAttribute == null) {
                return methodInfo;
            }
            Map<Integer, List<LocalVariableInfo>> localVariableLineMapping = Maps.newHashMap();
            for (int i = offset + parameterTypes.length; i < localVariableKeys.size(); i++) {
                LocalVariableInfo localVariableInfo = localVariableMapping.get(localVariableKeys.get(i));
                List<LocalVariableInfo> localVariableInfos = localVariableLineMapping.get(localVariableInfo.getLine());
                if (localVariableInfos == null) {
                    localVariableInfos = new ArrayList<>();
                    localVariableLineMapping.put(localVariableInfo.getLine(), localVariableInfos);
                }
                localVariableInfos.add(localVariableInfo);
            }
            methodInfo.setLocalVariableInfoLineMap(Collections.unmodifiableMap(localVariableLineMapping));
            LocalVariableTypeAttribute localVariableTypeAttribute = (LocalVariableTypeAttribute) codeAttribute.getAttribute(LocalVariableTypeAttribute.tag);
            if (localVariableTypeAttribute == null) {
                return methodInfo;
            }
            for (int i = 0; i < localVariableTypeAttribute.tableLength(); i++) {
                List<LocalVariableInfo> localVariableInfos = localVariableInfoMapping.get(localVariableTypeAttribute.variableName(i));
                if (CollectionUtils.isEmpty(localVariableInfos)) {
                    continue;
                }
                int lineNumber = lineNumberAttribute.toLineNumber(localVariableTypeAttribute.startPc(i) - 1);
                if (lineNumber < methodInfo.getStartLine()) {
                    lineNumber = methodInfo.getStartLine();
                }
                for (LocalVariableInfo localVariableInfo : localVariableInfos) {
                    if (localVariableInfo.getLine() == lineNumber) {
                        localVariableInfo.setType(parseType(SignatureAttribute.toFieldSignature(
                                localVariableTypeAttribute.descriptor(i)).toString(), typeVariableMapping));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return methodInfo;
    }

}
