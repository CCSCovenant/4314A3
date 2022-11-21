package srcMLExtraction;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;

public class ExtractSRCmlData {

    public static PrintStream log = System.out;

    public static void main(String[] args) {

        ArrayList<String> list = createClinks("srcML_query_result.xml");
        writeToFile(list, "srcML.ta"); //First line is weird


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

                    if (eElement.getElementsByTagName("cpp:file").item(0) != null) {
                        String file_name = eElement.getAttribute("filename");
                        String dependency_name = cleanString(eElement.getElementsByTagName("cpp:file").item(0).getTextContent()) ;
                        String template = "cLinks "+file_name+" "+ dependency_name;
                        list.add(template);
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