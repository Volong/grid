package grid.text.evolution;

import grid.common.CountMap;
import grid.common.TextUtils;
import grid.text.index.Pos;
import grid.text.index.TextIndexer;

/**
 * EntropyJudger.java 2013-9-10 11:06:24
 * 
 * @Author George Bourne
 */
public class EntropyJudger {

	private TextIndexer indexer;

	/**
	 * A word least appeared count <br>
	 * 一个词至少出现的次数
	 */
	private static int LEAST_COUNT_THRESHOLD = 3;

	/**
	 * Threshold for solid rate calculated by word appeared count and every
	 * single letter. <br>
	 * 通过字数以及每个字母计算凝合度的阀值 <br>
	 * 
	 * The smaller this values is, more new words you will get, but with less
	 * accuracy. The greater this value is, less new words you will get, but
	 * with high accuracy.
	 * 
	 * 这个值越低，会得到更多的新词，但是准确度比较低 <br>
	 * 这个值越高，会得到更少的新词，但是准确度比较高
	 * 
	 * 默认为： 0.018
	 */
	private static double SOLID_RATE_THRESHOLD = 0.018;

	/**
	 * Threshold for entropy value calculated by candidate word prefix character
	 * count and suffix character count <br>
	 * 熵值的阀值通过候选词前面的词以及后面的词的数量计算得到 <br>
	 * 
	 * The smaller this values is, more new words you will get, but with less
	 * accuracy. The greater this value is, less new words you will get, but
	 * with high accuracy. <br>
	 * 
	 * 这个值越低，会得到更多的新词，但是准确度比较低 <br>
     * 这个值越高，会得到更少的新词，但是准确度比较高
	 * 
	 * 默认为 1.92
	 */
	private static double ENTROPY_THRESHOL = 1.0;

	public EntropyJudger(TextIndexer indexer) {
		this.indexer = indexer;
	}

	public boolean judge(String candidate) {
	    
		double solidRate = getSolidRate(candidate);

		if (solidRate < SOLID_RATE_THRESHOLD) {
			return false;
		}

		double entropy = getEntropy(candidate);
		
		if (entropy < ENTROPY_THRESHOL) {
			return false;
		}
		return true;
	}

	private double getEntropy(String candidate) {
		Pos pos = new Pos(candidate);
		CountMap<Character> frontCountMap = new CountMap<>();
		CountMap<Character> backCountMap = new CountMap<>();
		final int candidateLen = candidate.length();
		int off = 0;
		char c;
		double rate;
		double frontEntropy = 0;
		double backEntropy = 0;

		while (indexer.find(pos).isFound()) {
		    
		    // 候选词所出现的位置
			off = pos.getPos();
			
			// 查找前一个词
			c = indexer.charAt(off - 1);
			if (TextUtils.isCnLetter(c)) {
				frontCountMap.increase(c);
			}
			
			// 查找后一个词
			c = indexer.charAt(off + candidateLen);
			if (TextUtils.isCnLetter(c)) {
				backCountMap.increase(c);
			}

		}

		for (char key : frontCountMap.keySet()) {
		    // 当前 key 出现的次数 / 所有 key 出现的次数
			rate = (double) frontCountMap.get(key) / frontCountMap.count();
			// 熵 = -p * log(p)
			frontEntropy -= rate * Math.log(rate);
		}
		for (char key : backCountMap.keySet()) {
			rate = (double) backCountMap.get(key) / backCountMap.count();
			backEntropy -= rate * Math.log(rate);
		}
		
		// 熵越小，可能性越大 
		return frontEntropy > backEntropy ? backEntropy : frontEntropy;

	}

	/**
	 * 
	 * 凝合度
	 * @param candidate
	 * @return
	 */
	public double getSolidRate(String candidate) {

		final int candidateLen = candidate.length();
		double rate = 1;
		
		// 为什么词的长度 < 2 的凝合度确实 1，而出现的频次小于 LEAST_COUNT_THRESHOLD 的词凝合度却是 0？
		if (candidateLen < 2) {
			return 0;
		}

		final int count = indexer.count(candidate);

		if (count < LEAST_COUNT_THRESHOLD) {
//			return 0;
		    return 0;
		}
		
		/*
		 *  理想的情况: rate = 1
		 *  假设词 [电影院] 在一段文本中出现了 100 次，这个词里面的字都是以同样的顺序一起出现，没有单独出现，或者跟其它的字搭配出现
		 *  那么这个三个字出现的总次数就是 300 次，也就是理想情况下 rate = 1
		 *  但是如果这三个字还出现在了其它的词当中，那么这三个字加起来的次数会 > 300，也就是 rate < 1
		 */
		for (int i = 0; i < candidateLen; i++) {
			rate *= (double) count / indexer.count("" + candidate.charAt(i));
		}

//		return Math.pow(rate, 1D / candidateLen) * Math.sqrt(candidateLen);
		return rate;
	}

	public void setIndexer(TextIndexer indexer) {
		this.indexer = indexer;
	}

}
