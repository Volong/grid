package grid.common;

/**
 * TextUtils.java 2013-9-9 14:32:12
 * 
 * @Author George Bourne
 */
public class TextUtils {

	/**
	 *  https://www.qqxiuzi.cn/zh/hanzi-unicode-bianma.php
	 *   
	 *  \u4E00 = 一    \u9FCB = 熕
	 *  
	 * @param c
	 * @return
	 */
	public static boolean isCnLetter(char c) {
		return c >= 0x4E00 && c <= 0x9FCB;
	}

	public static boolean isNumeric(char c) {
		return c >= '0' && c <= '9';
	}

	public static boolean isEnLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	/**
	 *  
	 *  dest 是否在 src 中的 off 位置出现过
	 *
	 * @param src 源字符串
	 * @param off  
	 * @param dest 需要查找的字符串
	 * @return
	 */
	public static boolean match(String src, int off, String dest) {
		int destLen = dest.length();
		int srcLen = src.length();
		for (int i = 0; i < destLen; i++) {
			if (srcLen <= off + i) {
				return false;
			}
			// 判断 dest 是不是在 src 中作为一个整体出现
			if (dest.charAt(i) != src.charAt(off + i)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isBlank(String str) {
		return null == str || str.isEmpty() || str.trim().isEmpty();
	}
}
