package com.ximpl.lib.db.utils;

public class XcDbUtils {
	/**
	 * Calculate from
	 * @param page
	 * @param size
	 * @return
	 */
	public static int from(int page, int size){
		return page*size;
	}
	
	/**
	 * Calculate to
	 * @param page
	 * @param size
	 * @param total
	 * @return
	 */
	public static int to(int page, int size, int total){
		final int max = ++page*size;
		return total - max  > 0 ? max : total;
	}
}
