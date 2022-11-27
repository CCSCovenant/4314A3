package srcMLExtraction;

public class Test {
	public static void main(String[] args){
		ExtractSRCmlData extractor = ExtractSRCmlData.getInstance();
		extractor.parseFunctionDeclaration("srcML_query_decl_result.xml");
		extractor.parseFunctionDeclaration("srcML_query_var_decl_result.xml");
		System.out.println("finish");

	}
}
