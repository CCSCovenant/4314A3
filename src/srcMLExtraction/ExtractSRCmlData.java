package srcMLExtraction;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ExtractSRCmlData {
    static HashMap<Integer,HashMap<String, HashSet<String>>> globalTable = new HashMap();
    static HashMap<Integer, HashMap<String, HashSet<String>>> callTable = new HashMap<>();

    // HashMap<String,String> globalTable = new HashMap();

    public static PrintStream log = System.out;

    public static void main(String[] args) {

        // ArrayList<String> list =
        // createClinks("C:\\Users\\Hamza\\Desktop\\querytest.xml");

        // writeToFile(parseMacro("srcML_macro_query_result.xml"), "test_macro.ta");
        // //First line is weird

        // parseDeclaration("srcML_declaration.xml");
        parseCalls("calltest.xml");

        System.out.println(callTable);

        // System.out.println(globalTable);
//    	for(String key : globalTable.keySet()) {
//    		System.out.println(key);
//    		System.out.print("----> " + globalTable.get(key));
//
//
//    	}

    }

    public static void addFunctionCall(String filename, String functionName, int arg) {
        if (callTable.get(arg) == null) {
            callTable.put(arg, new HashMap<String, HashSet<String>>());
        }
        HashMap<String, HashSet<String>> Map = callTable.get(arg);
        if (Map.get(filename) == null) {
            Map.put(filename, new HashSet<String>());
        }
        Map.get(filename).add(functionName);
    }

    public static void addFunctionDeclare(String filename, String functionName, int arg) {
        if (globalTable.get(arg) == null) {
            globalTable.put(arg, new HashMap<String, HashSet<String>>());
        }
        HashMap<String, HashSet<String>> Map = globalTable.get(arg);
        if (Map.get(functionName) == null) {
            Map.put(functionName, new HashSet<String>());
        }
        Map.get(functionName).add(filename);
    }

    public static void parseCalls(String path) {

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
                    Element decl = (Element) eElement.getElementsByTagName("call").item(0);

                    int args = decl.getElementsByTagName("argument").getLength();

                    String file_name = eElement.getAttribute("filename");

                    NodeList nl = decl.getElementsByTagName("name");

                    for (int j = 0; j < nl.getLength(); j++) {
                        Node curr = nl.item(j);
                        Element el = (Element) curr.getParentNode();
                        String temp = curr.getTextContent();

                        if (el.getTagName().equals("call")) {
                            if (temp.contains("::")) {
                                temp = temp.split("::")[1];
                            }
                            if (temp.contains(".")) {
                                temp = temp.split("[.]")[1];
                            }

                            addFunctionCall(file_name, temp, args);

                        }

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    Element decl = (Element) eElement.getElementsByTagName("function_decl").item(0);
                    if (decl.getAttribute("type").equals("operator")) {
                        continue;
                    }
                    String file_name = eElement.getAttribute("filename");

                    NodeList nl = decl.getElementsByTagName("name");

//                    int argumentSize = decl.getElementsByTagName("parameter_list").getLength();
//                    System.out.println(argumentSize);
//
                    for (int j = 0; j < nl.getLength(); j++) {
                        Node curr = nl.item(j);
                        Element el = (Element) curr.getParentNode();

                        if (el.getTagName().equals("function_decl")) {

                          //TODO fix add
                        }

                    }

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

                    for (int k = 0; k < eElement.getElementsByTagName("cpp:include").getLength(); k++) {
                        if (eElement.getElementsByTagName("cpp:file").item(0) != null) {
                            String file_name = eElement.getAttribute("filename");
                            String dependency_name = cleanString(
                                    eElement.getElementsByTagName("cpp:file").item(k).getTextContent());
                            String template = "cLinks " + file_name + " " + dependency_name;
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
        result = result.replaceAll("\"", "");
        result = result.replaceAll("<", "");
        result = result.replaceAll(">", "");

        return result;

    }

}