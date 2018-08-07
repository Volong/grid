package grid.text.selector;

/**
 * CnTextSelector.java 2013-9-20 
 * 
 * @Author George Bourne
 */
public class CommonTextSelector implements TextSelector {

	protected String document;

	protected int pos = 0;

	protected int maxSelectLen = 5;

	protected int minSelectLen = 2;

	protected int curLen;

	protected final int docLen;

	public CommonTextSelector(String document, int minSelectLen,
			int maxSelectLen) {
		this.document = document;
		this.minSelectLen = minSelectLen;
		this.maxSelectLen = maxSelectLen;
		docLen = document.length();
		adjustCurLen();
	}

	public void select() {
	    // 当前词的位置往后移一位
		pos += ++curLen;
		// 重新调整当前位置，也就是重新选取统计区间
		adjustCurLen();
	}

	protected void adjustCurLen() {
	    // 如果指定的最大长度大于文本的长度，那么当前长度为：当前词的位置 + (文本长度 - 当前词的位置)
	    // 如果指定的最大长度小于文本的长度，那么当前长度为：当前词的位置 + 最大长度
	    // 一般情况下，最大长度不会大于文本长度，所以当前长度为：当前词的位置 + 最大长度
		curLen = pos + maxSelectLen > docLen ? (docLen - pos) : maxSelectLen;
	}

	public String next() {
	    // 如果当前长度小于最小长度
		if (curLen < minSelectLen) {
			pos++;
			adjustCurLen();
		}
		// 
		if (pos + curLen <= docLen && curLen >= minSelectLen) {
			return document.substring(pos, pos + curLen--);
		} else {
			curLen--;
			// return document.substring(pos, docLen);
			return "";
		}
	}

	public boolean end() {
		return curLen < minSelectLen && curLen + pos >= docLen - 1;
	}

	@Override
	public int getCurPos() {
		return pos;
	}
}
