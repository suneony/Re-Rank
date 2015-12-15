import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MMR {
	private TermVectorSimilarity similarity=null;
	public MMR(){
		similarity=new TermVectorSimilarity();
	}
	/**
	 * 计算当前文本与已经选定的文本集合的相似度
	 * @param rankedDocList 已经选定的文本集合
	 * @param preparedDoc 当前待选择的文本
	 * @return 相似度
	 * */
	private double getMaxValue(ArrayList<String> rankedDocList,String preparedDoc){
		double maxValue=0;
		for(String rankedDoc:rankedDocList){
			double tempValue=similarity.similar(rankedDoc, preparedDoc);
			if(tempValue>maxValue){
				maxValue=tempValue;
			}
		}
		return maxValue;
	}
	/**
	 * @param query 查询词
	 * @param DocList 检索结果集合
	 * @return 重排序后结果集合
	 * */
	public ArrayList<String> rank(String query,ArrayList<String> docList){
		return rank(query,docList,docList.size());
	}
	/**
	 * @param query 查询词
	 * @param DocList 检索结果集合
	 * @param cutOff 之返回前couOff个
	 * @return 重排序后结果集合
	 * */
	public ArrayList<String> rank(String query,ArrayList<String> docList,int cutOff){
		ArrayList<String> rankedDocList=new ArrayList<String>();
		for(int i=0;i<cutOff;i++){
			double minValue=2;
			String minString=null;
			for(String doc:docList){
				//考虑了文本与已选定文本集合的相似度、文本与查询词相似度、文本的长度三个方面。
				double tempValue=getMaxValue(rankedDocList, doc)-similarity.similar(doc, query)+(1/Math.pow(1.1,(doc.length())));
				if(tempValue<minValue){
					minValue=tempValue;
					minString=doc;
				}
			}
			docList.remove(minString);
			rankedDocList.add(minString);
		}
		return rankedDocList;
	}
}
