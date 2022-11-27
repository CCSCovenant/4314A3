package srcMLExtraction;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class ExtractSRCmlData {
    static HashMap<Integer,HashMap<String, HashSet<String>>> globalTable = new HashMap();
    static HashMap<Integer, HashMap<String, HashSet<String>>> callTable = new HashMap<>();
    static HashMap<String,HashSet<String>> includeMap = new HashMap<>();
    // HashMap<String,String> globalTable = new HashMap();

    public static PrintStream log = System.out;

    public static void main(String[] args) {

        getCInclude("srcML_query_result.xml");
        //parseDeclaration("test.xml");
        //parseCalls("calltest.xml");
        System.out.println(callTable);


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
                    int args = decl.getElementsByTagName("argument").getLength();
                    String file_name = eElement.getAttribute("filename");

                    NodeList nl = decl.getElementsByTagName("name");

//                    int argumentSize = decl.getElementsByTagName("parameter_list").getLength();
//                    System.out.println(argumentSize);
//
                    for (int j = 0; j < nl.getLength(); j++) {
                        Node curr = nl.item(j);
                        Element el = (Element) curr.getParentNode();

                        if (el.getTagName().equals("function_decl")) {

                            addFunctionDeclare(file_name,curr.getTextContent(),args);
                        }

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getCInclude(String path) {
        try {
            File inputFile = new File(path);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            StringBuilder xmlStringBuilder = new StringBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("unit");


            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);


                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    if (eElement.getElementsByTagName("cpp:file").item(0) != null) {
                        if (eElement.getAttribute("filename").length()!=0) {
                            String dependency_name = eElement.getElementsByTagName("cpp:file").item(0).getTextContent();
                            if (dependency_name.contains("\"")){
                                dependency_name = cleanString(dependency_name);
                                String file_path_o = eElement.getAttribute("filename");
                                ArrayList<String>file_path = new ArrayList<>(Arrays.asList(eElement.getAttribute("filename").split("/")));
                                ArrayList<String>target_path = new ArrayList<>(Arrays.asList(dependency_name.split("/")));

                                file_path.remove(file_path.size()-1);

                                for (int c = 0;c<target_path.size();c++){
                                    if (target_path.get(c).equals("..")){
                                        file_path.remove(file_path.size()-1);
                                    }else {
                                        file_path.add(target_path.get(c));
                                    }
                                }
                                StringBuilder target_path_o  = new StringBuilder();
                                for (String s:file_path){
                                    target_path_o.append(s+"/");
                                }
                                target_path_o.deleteCharAt(target_path_o.length()-1);
                                if (includeMap.get(file_path_o)==null){
                                    includeMap.put(file_path_o,new HashSet<>());
                                }
                                includeMap.get(file_path_o).add(target_path_o.toString());
                            }
                        }

                    } else {

                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

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