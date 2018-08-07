package grid.text.index;

import grid.common.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * CnPreviewTextIndexer.java 2013-9-9 14:07:20
 * 
 * @Author George Bourne
 */
public class CnPreviewTextIndexer implements TextIndexer {

	private static final int CN_LETTER_COUNT = 5021;

	private String document;

	/**
	 *  统计每一个词出现的位置
	 */
	private Map<Character, Vector<Integer>> posMap;

	public CnPreviewTextIndexer(String document) {
		this.document = document;
		init();
	}

	/**
	 *  将 document 中的字符放到 posMap 中
	 *
	 */
	private void init() {
		final int len = document.length();

		final int supposedMinCount = 1 + (int) Math.log(len / CN_LETTER_COUNT + 1);

		char c;

		Vector<Integer> posVector;

		posMap = new HashMap<>(CN_LETTER_COUNT);

		for (int i = 0; i < len; i++) {
			c = document.charAt(i);
			if (!TextUtils.isCnLetter(c)) {
				continue;
			}
			posVector = posMap.get(c);
			if (null == posVector) {
				posVector = new Vector<>(supposedMinCount);
				posMap.put(c, posVector);
			}
			posVector.add(i);
		}
	}

	@Override
	public int count(String text) {

		if (TextUtils.isBlank(text)) {
			return 0;
		}

		Vector<Integer> vector = posMap.get(text.charAt(0));

		if (null == vector) {
			return 0;
		}

		if (1 == text.length()) {
			return vector.size();
		}

		final int size = vector.size();
		int count = 0;

		for (int i = 0; i < size; i++) {
			if (TextUtils.match(document, vector.get(i), text)) {
				count++;
			}
		}

		return count;
	}

	@Override
	public Pos find(Pos pos) {
		String text = pos.getTarget();

		pos.setFound(false);

		if (TextUtils.isBlank(text)) {
			return pos;
		}

		Vector<Integer> vector = posMap.get(text.charAt(0));

		if (null == vector) {
			return pos;
		}

		final int arraySize = vector.size();
		// arrayIndex 默认为 -1，所以第一次查找时，值为 0
		final int arrayIndex = pos.arrayIndex + 1;

		for (int i = arrayIndex; i < arraySize; i++) {
			if (TextUtils.match(document, vector.get(i), text)) {
				pos.setFound(true);
				pos.setPos(vector.get(i));
				// 找到了当前文本，则将当前文本所在位置列表的索引赋值给 arrayIndex 
				pos.arrayIndex = i;
				break;
			}
		}

		return pos;
	}

	@Override
	public int len() {
		return document.length();
	}

	@Override
	public String sub(int off, int len) {
		if (off < 0 || off + len >= document.length()) {
			return "";
		}
		return document.substring(off, off + len);
	}

	@Override
	public char charAt(int index) {
		if (index < 0 || index >= document.length()) {
			return 0;
		}
		return document.charAt(index);
	}
}