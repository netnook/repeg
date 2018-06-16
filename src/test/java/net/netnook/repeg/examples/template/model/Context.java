package net.netnook.repeg.examples.template.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Context {

	private final Object data;
	private final Map<String, Object> vars = new HashMap<>();

	public Context(Object data) {
		this.data = data;
	}

	public void set(String var, Object value) {
		vars.put(var, value);
	}

	public void clear(String var) {
		vars.remove(var);
	}

	public <T> T resolve(String ref) {
		String[] parts = ref.split("\\.");

		Object tmp = this;

		for (String part : parts) {
			tmp = resolve(tmp, part);
		}

		return (T) tmp;
	}

	private Object resolve(Object source, String field) {
		if (source == null) {
			return null;
		} else if (source == this) {
			if (vars.containsKey(field)) {
				return vars.get(field);
			} else {
				return resolve(this.data, field);
			}
		} else {
			if (source instanceof Map) {
				return ((Map) source).get(field);
			} else {
				String getter = "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1);

				try {
					Method method = source.getClass().getMethod(getter);
					return method.invoke(source);
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					// ignore
				}

				String iser = "is" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
				try {
					Method method = source.getClass().getMethod(iser);
					return method.invoke(source);
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					// ignore
				}

				throw new IllegalArgumentException("Could not access field '" + field + "' on " + source.getClass());
			}
		}
	}
}
