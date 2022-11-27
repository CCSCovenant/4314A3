package MethodBenchmarking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Benchmarking {

    public static PrintStream log = System.out;

    public static void main(String[] args) throws FileNotFoundException {

        File file1 = new File("LocalTA/understand.local.ta");
        File file2 = new File("LocalTA/include.local.ta");
        File file3 = new File("LocalTA/srcML.local.ta");
        File file4 = new File("ground_truth/GT.local.ta");

        //compareData(file2,file3);
        HashSet<String> SetR = getSample(file4);
        HashSet<String> SetU = getSample(file1);
        HashSet<String> SetI = getSample(file2);
        HashSet<String> SetS =  getSample(file3);
        System.out.println("size of gt sample"+SetR.size());
        System.out.println("size of understand sample"+SetU.size());
        System.out.println("size of include sample"+SetI.size());
        System.out.println("size of srcML sample"+SetS.size());

        HashSet<String> valid = valid(SetI);
        System.out.println("understand");
        PR(SetR,SetU);
        System.out.println("include");
        PR(SetR,SetI);
        System.out.println("scrMl");
        PR(SetR,SetS);


    }
    public static void PR(HashSet<String> GT,HashSet<String> OB){
        HashSet<String> set1_unique = new HashSet<>(GT);
        HashSet<String> set2_unique = new HashSet<>(OB);
        HashSet<String> common = new HashSet<>(GT);

        set1_unique.removeAll(OB);
        set2_unique.removeAll(GT);
        common.retainAll(OB);

        double precision = (common.size()/(double)(OB.size()));
        double recall = (common.size()/(double)(GT.size()));
        log.format("%s: %.2f%%\n","precision",precision*100);
        log.format("%s: %.2f%%\n","recall", recall*100);
    }
    public static HashSet<String> getSample(File file) throws FileNotFoundException{
        Scanner scanner = new Scanner(file);
        HashSet<String> set = new HashSet<>();
        while (scanner.hasNextLine()){
            String line = scanner.nextLine().trim();
            String name = line.split("\\s+")[0];
            if (name.equals("cLinks")){
                set.add(line);
            }
        }
        return set;
    }
    public static HashSet<String>  valid(HashSet<String> validfile) throws FileNotFoundException{

        HashSet<String> set = new HashSet<>();
        for (String dependence:validfile){
            set.add(dependence.split("\\s+")[1]);
        }
        return set;
    }
    public static void compareData(File file1, File file2) throws FileNotFoundException {

        HashSet<String> set1 = new HashSet<>();
        HashSet<String> set2 = new HashSet<>();

        Scanner scan1 = new Scanner(file1);
        Scanner scan2 = new Scanner(file2);

        while (scan1.hasNextLine()) {
            set1.add(scan1.nextLine().trim());
        }
        scan1.close();

        while (scan2.hasNextLine()) {
            set2.add(scan2.nextLine().trim());
        }
        scan2.close();

        log.println("--Extracted depenendcies--");
        log.println(file1.getName()+": "+set1.size());
        log.println(file2.getName()+": "+set2.size());


        log.println("--Unique Dependency breakdown--");
        HashSet<String> set1_unique = new HashSet<>(set1);
        HashSet<String> set2_unique = new HashSet<>(set2);
        HashSet<String> common = new HashSet<>(set1);

//        log.println(set1_unique);
//        log.println(set2.contains("cLinks libcp1.cc names.hh"));
//        log.println(set1.contains("cLinks libcp1.cc names.hh"));

        //Unique
        set1_unique.removeAll(set2);
        set2_unique.removeAll(set1);

        log.println(set1_unique);

        //Common
        common.retainAll(set2);

        log.println(file1.getName()+": "+set1_unique.size());
        log.println(file2.getName()+": "+set2_unique.size());
        log.println("Common: "+common.size());

        log.println("--Precision stats--");

        //Precision
        double set1_precision = (common.size()/(double)(set2_unique.size()+ common.size()));
//        double set2_precision = (set2.size()/(double)(set1_unique.size()+set2_unique.size()+ common.size()));

        log.format("%s: %.2f%%\n", file1.getName(), set1_precision*100);
//        log.format("%s: %.2f%%\n", file2.getName(), set2_precision*100);
    }




}