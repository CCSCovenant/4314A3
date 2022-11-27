package srcMLExtraction;

import java.util.ArrayList;

public class srcMLExtraction {
	public static void main(String[] args){
		ExtractSRCmlData extractor = ExtractSRCmlData.getInstance();
		extractor.parseFunctionDeclaration("srcML_query_decl_result.xml");
		extractor.parseVarDeclaration("srcML_query_var_decl_result.xml");
		extractor.parseInclude("srcML_query_result.xml");
		extractor.parseCalls("srcML_query_call_result.xml");
		extractor.ExtractionFunction();
		extractor.ExtractionVar();
		extractor.ExtractionInclude();

		ArrayList<String> links = new ArrayList<>();
		for (String file:extractor.linkMap.keySet()){
			for (String target:extractor.linkMap.get(file)){
				links.add("cLinks "+file+" "+target);
			}
		}
		extractor.writeToFile(links,"srcML.ta");
	}

}
