import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;
/**
 * @author suneony
 * 空间向量模型文本相似度
 * */
public class TermVectorSimilarity {
	public TermVectorSimilarity(){}
	/**
	 * @param str1 第一个文本
	 * @param str2 第二个文本
	 * @return 两个文本的相似度
	 * */
	public double similar(String str1,String str2){
		Map<String, Integer> tf1=null;
		Map<String, Integer> tf2=null;
		double similarity=0;
		Map<String, MutablePair<Integer, Integer>> tfs=new HashMap<String,MutablePair<Integer,Integer>>();
		try {
			tf1=getTF(str1);
			tf2=getTF(str2);
			for(String key:tf1.keySet()){
				MutablePair<Integer, Integer> pair=new MutablePair<Integer,Integer>(tf1.get(key),0);
				tfs.put(key, pair);
			}
			for(String key:tf2.keySet()){
				MutablePair<Integer, Integer> pair=tfs.get(key);
				if(null==pair){
					pair=new MutablePair<Integer,Integer>(0,tf2.get(key));
				}else{
					pair.setRight(tf2.get(key));
				}
				tfs.put(key, pair);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		similarity=getIDF(tfs);
		return similarity;
	}
	private Map<String, Integer> getTF(String str) throws IOException{
		Map<String, Integer> map=new HashMap<String,Integer>();
		//读取配置文件，加载停用词
		String stopWordString=Config.getProperty("STOP_WORD");
		String[] stopWords=stopWordString.split(" ");
		CharArraySet cas=new CharArraySet(0,true);
		for(String word:stopWords){
			cas.add(word);
		}
		//加载系统停用词
//		Iterator<Object> iterator=StandardAnalyzer.STOP_WORDS_SET.iterator();
//		while(iterator.hasNext()){
//			cas.add(iterator.next());
//		}
		//分词
		StandardAnalyzer sa=new StandardAnalyzer(cas);
		TokenStream tokenStream=sa.tokenStream("field", str);
		CharTermAttribute charTermAttribute=tokenStream.addAttribute(CharTermAttribute.class);
		tokenStream.reset();
		while(tokenStream.incrementToken()){
			String token=charTermAttribute.toString();
			if(!map.containsKey(token)){
				map.put(token, 1);
			}else{
				int count=map.get(token);
				count=count+1;
				map.put(token, count);
			}
		}
		tokenStream.end();
		tokenStream.close();
		
		return map;
	}
	private double getIDF(Map<String,MutablePair<Integer, Integer>> tf){
		double d = 0;  
        if (MapUtils.isEmpty(tf)) {  
            return d;  
        }  
        double denominator = 0;  
        double sqdoc1 = 0;  
        double sqdoc2 = 0;  
        Pair<Integer, Integer> count = null;  
        for (String key : tf.keySet()) {  
            count = tf.get(key);  
            denominator += count.getLeft() * count.getRight();  
            sqdoc1 += count.getLeft() * count.getLeft();  
            sqdoc2 += count.getRight() * count.getRight();  
        }  
        d = denominator / (Math.sqrt(sqdoc1) * Math.sqrt(sqdoc2));  
        return d; 
	}

}
