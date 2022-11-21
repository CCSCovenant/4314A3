package UnderstandExtractData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExtractUnderstand {

    public static PrintStream log = System.out;

    public static void main(String[] args) throws FileNotFoundException {
        writeToFile(extractUnderstand("understand.raw.ta"),"understand.ta");


    }

    public static List<String> extractUnderstand(String path) throws FileNotFoundException{
        List<String> list = new ArrayList<>();
        File file = new File(path);
        Scanner myReader = new Scanner(file);

        while(myReader.hasNextLine()) {
            String line = myReader.nextLine();

            //cLink file_path other_path
            String[] split = line.split(" ");
            String[] other_file = split[2].split("/");

            String curr = "cLinks " + split[1] +" " +other_file[other_file.length-1];
            list.add(curr);

        }
        myReader.close();
        return list;

    }

    public static void writeToFile(List<String> list, String path) {
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

}