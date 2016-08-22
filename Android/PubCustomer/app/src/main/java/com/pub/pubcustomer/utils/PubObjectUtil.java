package com.pub.pubcustomer.utils;

import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class PubObjectUtil {

	public static boolean isEmpty(Object object) {
		boolean empty;

		if (object instanceof String) {
			empty = !StringUtils.hasText((String) object);
		} else if (object instanceof StringBuffer) {
			empty = ((StringBuffer) object).length() == 0;
		} else if (object instanceof StringBuilder) {
			empty = ((StringBuilder) object).length() == 0;
		} else if (object instanceof Collection && ((Collection<?>) object).isEmpty()) {
			empty = true;
		} else if (object instanceof Map && ((Map<?, ?>) object).isEmpty()) {
			empty = true;
		} else if (object != null && object.getClass().isArray() && Array.getLength(object) == 0) {
			empty = true;
		} else {
			empty = (object == null);
		}
		return empty;
	}

	public static <T> T ifNull(T obj, T objDefault) {
		if (isEmpty(obj)) {
			return objDefault;
		}
		return obj;
	}
}