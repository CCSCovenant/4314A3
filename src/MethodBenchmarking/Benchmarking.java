package MethodBenchmarking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;

public class Benchmarking {

    public static PrintStream log = System.out;

    public static void main(String[] args) throws FileNotFoundException {

        File file1 = new File("srcML.ta");
        File file2 = new File("understand.ta");

        compareData(file1,file2);


    }

    public static void compareData(File file1, File file2) throws FileNotFoundException {

        HashSet<String> Set1 = new HashSet<>();
        HashSet<String> Set2 = new HashSet<>();

        Scanner scanner1 = new Scanner(file1);
        Scanner scanner2 = new Scanner(file2);

        while(scanner1.hasNextLine()) {
            String line = scanner1.nextLine();
            Set1.add(line);
        }
        scanner1.close();

        while(scanner2.hasNextLine()) {
            String line = scanner2.nextLine();
            Set2.add(line);
        }
        scanner2.close();


        System.out.println(file1.getName() + " Dependancies extracted ... " + Set1.size());
        System.out.println(file2.getName() + " Dependancies extracted ... " + Set2.size());

        log.println("--------------");


        HashSet<String> setdiff1;

        setdiff1 = new HashSet<>(Set1);
        setdiff1.removeAll(Set2);

        System.out.println(file1.getName() + " Unique Dependancies " + setdiff1.size());

        HashSet<String> setdiff2 = new HashSet<>(Set2);
        setdiff2.removeAll(Set1);

        System.out.println(file2.getName() + " Unique Dependancies " + setdiff2.size());
        HashSet<String> common = new HashSet<>(Set2);
        common.retainAll(Set2);

        log.println("--------------");


        System.out.println("Common Dependancies " + common.size());

        double precision1 = ((Set1.size() / (double)(Set1.size() + Set2.size() - common.size())));

        System.out.println("Precision " + file1.getName() + " : " + precision1);

        double precision2 = ((Set2.size() / (double)(Set1.size() + Set2.size() - common.size())));

        System.out.println("Precision " + file2.getName() + " : " + precision2);




    }


}