package srcMLExtraction;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {
	HashMap<Integer,HashMap<String,String>> globalTable = new HashMap();

	//HashMap<String,String> globalTable = new HashMap();

	public static PrintStream log = System.out;

	public static void main(String[] args) {



		//  ArrayList<String> list = createClinks("C:\\Users\\Hamza\\Desktop\\querytest.xml");

		//writeToFile(parseMacro("srcML_macro_query_result.xml"), "test_macro.ta"); //First line is weird

		parseDeclaration("test.xml");

	}

	public static void parseDeclaration(String path) {

		try {
			File inputFile = new File(path);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append("<?xml version=1.0?> <class> </class>");
			Document doc = builder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("unit");

			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Element decl = (Element)eElement.getElementsByTagName("function_decl").item(0);

					System.out.println(decl.getChildNodes().getLength());
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<String> createClinks(String path) {
		ArrayList<String> list = new ArrayList<>();

		try {
			File inputFile = new File(path);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append("<?xml version=1.0?> <class> </class>");
			Document doc = builder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("unit");




			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);


				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;


					for (int k = 0 ; k < eElement.getElementsByTagName("cpp:include").getLength(); k++) {
						if (eElement.getElementsByTagName("cpp:file").item(0) != null) {
							String file_name = eElement.getAttribute("filename");
							String dependency_name = cleanString(eElement.getElementsByTagName("cpp:file").item(k).getTextContent()) ;
							String template = "cLinks "+file_name+" "+ dependency_name;
							list.add(template);
						}
					}


				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static void writeToFile(ArrayList<String> list, String path) {
		try {
			FileWriter myWriter = new FileWriter(path);

			for (String temp : list) {
				myWriter.write(temp + "\n");
			}
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static String cleanString(String dependency_name) {
		String result = dependency_name;
		result = result.replaceAll("\"","");
		result = result.replaceAll("<","");
		result = result.replaceAll(">","");

		return result;

	}


}