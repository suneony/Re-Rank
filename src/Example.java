import java.util.ArrayList;

public class Example {
	public static void main(String[] args){
		MMR mmr=new MMR();
		ArrayList<String> docs=new ArrayList<>();
		docs.add("The Lucene PMC is pleased to announce the release of the Apache Solr Reference Guide for Solr 4.4.");
		docs.add("The Lucene PMC is pleased to announce the release of the Apache Solr Reference Guide for Solr 4.4.");
		docs.add("The Lucene PMC is pleased to announce the release of the Apache Solr Reference Guide for Slr 4.4.");
		docs.add("The Lucene PMC   is is pleased to announce the release of the Apache Solr Reference Guide for Solr 4.4.");
		docs.add("abc");
		for(String doc:docs){
			System.out.println(doc);
		}
		ArrayList<String> rankedDocs=mmr.rank("is",docs);
		for(String doc:rankedDocs){
			System.out.println(doc);
		}
		
	}
}
