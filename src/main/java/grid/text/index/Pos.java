package grid.text.index;

/**
 * Pos.java 2013-9-9 13:43:53
 * 
 * @Author George Bourne
 */
public class Pos {
	private String target;

	/**
	 * Pos for current matched full target text <br>
	 * 当前文本所在的位置
	 */
	private int pos = -1;

	/**
	 * Index in position array for current matched full target text <br>
	 * 当前文本在位置列表中的索引
	 * 
	 */
	int arrayIndex = -1;

	private boolean found = false;

	public Pos(String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public int getPos() {
		return pos;
	}

	public boolean isFound() {
		return found;
	}

	void setPos(int pos) {
		this.pos = pos;
	}

	void setFound(boolean found) {
		this.found = found;
	}
}
