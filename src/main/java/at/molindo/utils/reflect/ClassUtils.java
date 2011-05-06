package at.molindo.utils.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import at.molindo.utils.collections.ArrayUtils;

public class ClassUtils {
	private ClassUtils() {
	}

	/**
	 * @param type
	 * @param genericCls
	 * @return first actual type arguments of gernicCls in type hierarchy of cls
	 */
	public static Class<?> getTypeArgument(Class<?> cls, Class<?> genericCls) {
		return ArrayUtils.first(getTypeArguments(cls, genericCls));
	}

	/**
	 * @param type
	 * @param genericCls
	 * @return actual type arguments of gernicCls in type hierarchy of cls
	 */
	public static Class<?>[] getTypeArguments(Class<?> cls, Class<?> genericCls) {
		if (genericCls.isAssignableFrom(cls)) {
			while (cls != Object.class) {
				Class<?>[] classes;

				classes = getTypeArguments(cls.getGenericSuperclass(), genericCls);
				if (classes != null) {
					return classes;
				}

				for (Type type : cls.getGenericInterfaces()) {
					classes = getTypeArguments(type, genericCls);
					if (classes != null) {
						return classes;
					}
				}

				cls = cls.getSuperclass();
			}
		}
		return null;
	}

	/**
	 * @param type
	 * @param genericCls
	 * @return actual type arguments of type to gernicCls
	 */
	private static Class<?>[] getTypeArguments(Type type, Class<?> genericCls) {
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			if (genericCls == pt.getRawType()) {
				Type[] typeArgs = pt.getActualTypeArguments();
				Class<?>[] classes = new Class<?>[typeArgs.length];
				for (int i = 0; i < typeArgs.length; i++) {
					Type typeArg = typeArgs[i];
					if (typeArg instanceof Class<?>) {
						classes[i] = (Class<?>) typeArg;
					} else if (typeArg instanceof ParameterizedType) {
						classes[i] = (Class<?>) ((ParameterizedType) typeArg).getRawType();
					}
				}
				return classes;
			}
		}
		return null;
	}
}
