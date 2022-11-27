package DependencyExtraction;

import srcMLExtraction.ExtractSRCmlData;

import java.util.ArrayList;

public class IncludeExtraction_scrML {
	public static void main(String[] args){
		ExtractSRCmlData extractor = ExtractSRCmlData.getInstance();
		extractor.parseInclude("srcML_query_result.xml");
		extractor.ExtractionInclude();

		ArrayList<String> TA = new ArrayList<>();
		TA.add("FACT TUPLE :");
		for (String file:extractor.getLinkMap().keySet()){
			TA.add("$INSTANCE "+file+" cFile");
		}
		for (String file:extractor.getLinkMap().keySet()){
			for (String target:extractor.getLinkMap().get(file)){
				TA.add("cLinks "+file+" "+target);
			}
		}


		extractor.writeToFile(TA,"include.ta");
	}
}
