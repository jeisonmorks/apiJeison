package com.texo.filme;

public final class IntervaloUtils {

	/**
	 * Calcula um intervalo de anos
	 * @param followingWin
	 * @param previousWin
	 * @return
	 */
	public static Long calculaIntervalo(Long followingWin, Long previousWin) {
		if (followingWin == null || previousWin == null) {
			return null;
		}
		return followingWin - previousWin;
	}
	
}
