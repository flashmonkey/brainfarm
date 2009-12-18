package org.flashmonkey.util;

public class ArrayUtils {

	public static boolean contains(Object[] array, Object o) {
		for (Object e : array) {
			if (e.equals(o)) {
				return true;
			}
		}
		
		return false;
	}
}
