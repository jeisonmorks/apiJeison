package com.texo.filme;

public final class CompareUtils {
	
	/**
	 * Comparação entre maior e menor
	 * 
	 * @param <T>
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static <T> int compare(Comparable<T> o1, T o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		return o1.compareTo(o2);
	}
	
}
