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

        HashSet<String> set1 = new HashSet<>();
        HashSet<String> set2 = new HashSet<>();

        Scanner scan1 = new Scanner(file1);
        Scanner scan2 = new Scanner(file2);

        while (scan1.hasNextLine()) {
            set1.add(scan1.nextLine());
        }
        scan1.close();

        while (scan2.hasNextLine()) {
            set2.add(scan2.nextLine());
        }
        scan2.close();

        log.println("--Extracted depenendcies--");
        log.println(file1.getName()+": "+set1.size());
        log.println(file2.getName()+": "+set2.size());


        log.println("--Dependency breakdown--");
        HashSet<String> set1_unique = new HashSet<>(set1);
        HashSet<String> set2_unique = new HashSet<>(set2);
        HashSet<String> common = new HashSet<>(set1);

        //Unique
        set1_unique.removeAll(set2);
        set2_unique.removeAll(set1);

        //Common
        common.retainAll(set2);

        log.println(file1.getName()+": "+set1_unique.size());
        log.println(file2.getName()+": "+set2_unique.size());
        log.println("Common: "+common.size());

        log.println("--Recall/Precision stats--");

        //Precision
        double set1_precision = (set1.size()/(double)(set1_unique.size()+set2_unique.size()+ common.size()));
        double set2_precision = (set2.size()/(double)(set1_unique.size()+set2_unique.size()+ common.size()));

        log.format("%s: %.2f%%\n", file1.getName(), set1_precision*100);
        log.format("%s: %.2f%%\n", file2.getName(), set2_precision*100);



    }


}