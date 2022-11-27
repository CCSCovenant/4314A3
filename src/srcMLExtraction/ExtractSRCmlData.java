package srcMLExtraction;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class ExtractSRCmlData {

    HashMap<String, String> functionDeclMap = new HashMap();
    // key: function, value: file that decl this function
    HashMap<String, String> varDeclMap = new HashMap();
    // key: var, value: file that decl this var


    HashMap<String, HashSet<String>> externFunctionMap = new HashMap();
    // key: file, value: all extern function that decl in this file

    HashMap<String, HashSet<String>> externVarMap = new HashMap();
    // key: file, value: all extern var that decl in this file



    HashMap<String, HashSet<String>> functionCallMap = new HashMap<>();
    HashMap<String,HashSet<String>> includeMap = new HashMap<>();

    HashMap<String,HashSet<String>> linkMap = new HashMap<>();
    // HashMap<String,String> globalTable = new HashMap();

    public static PrintStream log = System.out;

    static ExtractSRCmlData extractSRCmlData = new ExtractSRCmlData();
    public static ExtractSRCmlData getInstance() {
        return extractSRCmlData;
    }
    public  void CountLink(){
        long count = 0;
        for (String s:linkMap.keySet()){
            HashSet<String> link = linkMap.get(s);
            count += link.size();
        }
        System.out.println(count);
    }
    public  void CountInclude(){
        long count = 0;
        for (String s:includeMap.keySet()){
            HashSet<String> link = includeMap.get(s);
            count += link.size();
        }
        System.out.println(count);
    }

    public HashMap<String,HashSet<String>> getLinkMap(){
        return linkMap;
    }
    public void addFunctionDecl(boolean isExtern,String file,String function){
        if (function==null||function==""||file==null||file==""){
            return;
        }
        if (isExtern){
            if (externFunctionMap.get(file)==null){
                externFunctionMap.put(file,new HashSet<>());
            }
            externFunctionMap.get(file).add(function);
        }else {
            functionDeclMap.put(function,file);
        }
    }

    public void addVarDecl(boolean isExtern,String file,String var){
        if (var==null||var==""||file==null||file==""){
            return;
        }
        if (isExtern){
            if (externVarMap.get(file)==null){
                externVarMap.put(file,new HashSet<>());
            }
            externVarMap.get(file).add(var);
        }else {
            varDeclMap.put(var,file);
        }
    }


    public void addFunctionCall(String file,String function){
        if (functionCallMap.get(file)==null){
            functionCallMap.put(file,new HashSet<>());
        }
        functionCallMap.get(file).add(function);

    }
    public void parseCalls(String path) {

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

                            addFunctionCall(file_name, temp);

                        }

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void ExtractionFunction(){
        for (String file:externFunctionMap.keySet()){
            HashSet<String> functions = externFunctionMap.get(file);
            for (String function:functions){
                String target = functionDeclMap.get(function);
                if (target==null){
                    continue;
                }
                if (linkMap.get(file)==null){
                    linkMap.put(file,new HashSet<>());
                }
                linkMap.get(file).add(target);
            }
        }
        for (String file:functionCallMap.keySet()){
            HashSet<String> functions = functionCallMap.get(file);
            for (String function:functions){
                String target = functionDeclMap.get(function);
                if (target==null){
                    continue;
                }
                if (linkMap.get(file)==null){
                    linkMap.put(file,new HashSet<>());
                }
                linkMap.get(file).add(target);
            }
        }
    }
    public void ExtractionVar(){
        for (String file:externVarMap.keySet()){
            HashSet<String> vars = externVarMap.get(file);
            for (String var:vars){
                String target = varDeclMap.get(var);
                if (target==null){
                    continue;
                }
                if (linkMap.get(file)==null){
                    linkMap.put(file,new HashSet<>());
                }
                linkMap.get(file).add(target);
            }
        }
    }
    public void ExtractionInclude(){
        for (String file:includeMap.keySet()){
            HashSet<String> files = includeMap.get(file);
            for (String target:files){
                if (linkMap.get(file)==null){
                    linkMap.put(file,new HashSet<>());
                }
                linkMap.get(file).add(target);
            }
        }
    }
    public void parseFunctionDeclaration(String path) {

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
                    if (decl==null){
                        continue;
                    }
                    if (decl.getAttribute("type").equals("operator")) {
                        continue;
                    }
                    int args = decl.getElementsByTagName("argument").getLength();
                    String file_name = eElement.getAttribute("filename");
                    String function_name = "";
                    String typename = "";
                    NodeList nl = decl.getElementsByTagName("name");
                    NodeList specifier = decl.getElementsByTagName("specifier");

                    for (int j = 0; j < nl.getLength(); j++) {
                        Node curr = nl.item(j);
                        Element el = (Element) curr.getParentNode();

                        if (el.getTagName().equals("function_decl")) {
                            function_name = curr.getTextContent();
                        }
                        if (el.getTagName().equals("type")){
                            typename = curr.getTextContent();
                        }

                    }
                    boolean STATIC = false;
                    boolean externDecl = false;
                    for (int j =0;j<specifier.getLength();j++){
                        Node curr = specifier.item(j);
                        if (curr.getTextContent().equals("extern")){
                            externDecl = true;
                        }
                        if (curr.getTextContent().equals("static")){
                            STATIC = true;
                        }
                    }
                    if (STATIC){
                        continue;
                    }
                    String entity_name = typename+" "+function_name+" "+args;
                    addFunctionDecl(externDecl,file_name,function_name);

//                    int argumentSize = decl.getElementsByTagName("parameter_list").getLength();
//                    System.out.println(argumentSize);
//


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void parseVarDeclaration(String path) {

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
                    Element decl = (Element) eElement.getElementsByTagName("decl").item(0);

                    String file_name = eElement.getAttribute("filename");
                    String varname = "";
                    String typename = "";
                    NodeList nl = decl.getElementsByTagName("name");
                    NodeList specifier = decl.getElementsByTagName("specifier");

                    for (int j = 0; j < nl.getLength(); j++) {
                        Node curr = nl.item(j);
                        Element el = (Element) curr.getParentNode();

                        if (el.getTagName().equals("function_decl")) {
                            varname = curr.getTextContent();
                        }
                        if (el.getTagName().equals("type")){
                            typename = curr.getTextContent();
                        }

                    }
                    boolean externDecl = false;
                    boolean STATIC = false;

                    for (int j =0;j<specifier.getLength();j++){
                        Node curr = specifier.item(j);
                        if (curr.getTextContent().equals("extern")){
                            externDecl = true;
                        }
                        if (curr.getTextContent().equals("static")){
                            STATIC = true;
                        }
                    }
                    if (STATIC){
                        continue;
                    }
                    String entity_name = typename+" "+varname;
                    addVarDecl(externDecl,file_name,varname);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void parseInclude(String path) {
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

    public void writeToFile(ArrayList<String> list, String path) {
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

    public String cleanString(String dependency_name) {
        String result = dependency_name;
        result = result.replaceAll("\"", "");
        result = result.replaceAll("<", "");
        result = result.replaceAll(">", "");

        return result;

    }

}