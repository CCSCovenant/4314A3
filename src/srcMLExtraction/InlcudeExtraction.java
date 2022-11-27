package srcMLExtraction;

import java.util.ArrayList;

public class InlcudeExtraction {
	public static void main(String[] args){
		ExtractSRCmlData extractor = ExtractSRCmlData.getInstance();
		extractor.parseInclude("srcML_query_result.xml");
		extractor.ExtractionInclude();

		ArrayList<String> links = new ArrayList<>();
		for (String file:extractor.linkMap.keySet()){
			for (String target:extractor.linkMap.get(file)){
				links.add("cLinks "+file+" "+target);
			}
		}
		extractor.writeToFile(links,"include.ta");
	}
}
