package grid.text.evolution;

import grid.common.TextUtils;
import grid.text.dic.CnDictionary;
import grid.text.index.CnPreviewTextIndexer;
import grid.text.index.TextIndexer;
import grid.text.selector.CnTextSelector;
import grid.text.selector.TextSelector;

import java.util.HashSet;
import java.util.Set;

/**
 * NewWordDiscover.java 2013-9-20 13:48:04
 * 
 * @Author George Bourne
 */
public class NewWordDiscover {

    private CnDictionary dictionary;

    /**
     * Minimum word length
     * 候选词的最小长度
     */
    private final static int MIN_CANDIDATE_LEN = 2;

    /** 
     * Maximum word length
     * 候选词的最大长度
     */
    private final static int MAX_CANDIDATE_LEN = 6;

    private static Set<Character> structuralLetterSet = new HashSet<>();

    /**
     *  停顿词
     */
    private static char[] structuralLetters = { '我', '你', '您', '他', '她', '谁',
            '哪', '那', '这', '的', '了', '着', '也', '是', '有', '不', '在', '与', '呢',
            '啊', '呀', '吧', '嗯', '哦', '哈', '呐' };

    static {
        for (char c : structuralLetters) {
            structuralLetterSet.add(c);
        }
    }

    public NewWordDiscover() {
        dictionary = CnDictionary.instance();
    }

    /**
     * New word discover is based on statistic and entropy, better to sure
     * document size is in 100kb level, or you may get a unsatisfied result.
     * 
     * 新词发现基于统计以及熵，最后确保 document 的大小在 100kb 左右，否则你可能会得到一个不太满意的结果
     * 
     * @param document
     * @return
     */
    public Set<String> discover(String document) {

        Set<String> set = new HashSet<>();
        TextIndexer indexer = new CnPreviewTextIndexer(document);
        TextSelector selector = new CnTextSelector(document, MIN_CANDIDATE_LEN, MAX_CANDIDATE_LEN);
        EntropyJudger judger = new EntropyJudger(indexer);
        String candidate;
        while (!selector.end()) {
            candidate = selector.next();
            if (TextUtils.isBlank(candidate)) {
                continue;
            }
            // 如果是停顿词
            if (structuralLetterSet.contains(candidate.charAt(0))
                    || structuralLetterSet.contains(candidate.charAt(candidate
                            .length() - 1))) {
                continue;
            }
            // Replace IF clause with "set.contains(candidate)" if you want to
            // find new word without any dictionary
            // 如果你想在没有词典的情况下发现新词，可以用 "set.contains(candidate)" 提到 if 语句
            // 如果词典中包含这个词，或者这个词已经被当作新词找到
            if (dictionary.contains(candidate) || set.contains(candidate)) {
                // 位置往后移
                selector.select();
            } else if (judger.judge(candidate)) { // 找到了一个新词
                set.add(candidate);
            }
        }
        return set;
    }
}
