

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by szhou on 3/29/17.
 */
/*
*Includes:
*Multi-layer Bloom Filter
*Seed-List
*Crawl List (lvl.1)
*Crawl List (lvl.2)
**/
public class Main {
    public LinkedList <String> crawler_list_lvl1 = new LinkedList<String>(); //Needs to use Collect.synchronizedlist
    public LinkedList <String> crawler_list_lvl2 = new LinkedList<String>(); // After Filtered by Bloom-filt
    public static ConcurrentLinkedQueue <String> bag_of_taks = new ConcurrentLinkedQueue<String>();
    private static ArrayList <String> seed_list = new ArrayList<String>();


    //parameter section
    private static int L = 5;
    private static int K = 20;
    private static int num_of_workers = 5;
    //end of parameter section

    public static void main (String[] args) {
        System.out.println("Hello Idiots");
        read_seed_list();

        ArrayList <Integer> coffA = genRCoff(K);
        ArrayList <Integer> coffB = genRCoff(K);
        //Bit arrays for Bloom Filter
        //n = 1,000,000, p = 1.0E-6 (1 in 1,000,000) → m = 28,755,176 (3.43MB), k = 20
        for(int i = 0; i<=L; i++){
            BitSet BFs = new BitSet(28755176);
            BloomFilter.MLBF.add(BFs);
        }

        for (String link: seed_list) {
            bag_of_taks.add(link);
        }
        //create_workers(num_of_workers,coffA,coffB);
        ExecutorService regulator = Executors.newFixedThreadPool(num_of_workers);

        for(int i=0; i<num_of_workers;i++){
            regulator.execute(new Worker(coffA,coffB));
        }

    }

   // private static void create_workers(int num_of_workers, ArrayList coffA, ArrayList coffB){
   //     for (int i = 0; i <= num_of_workers; i++){
   //         Thread t = new Thread(new Worker(coffA,coffB));  // needs to fix worker class, also naming the worker thread?
   //     }

   //    }

    //Convert the text file into an Arraylist for further processing
    private static void read_seed_list(){
        String filename = "seed-list.csv";
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            int i = 0;
            while((line = br.readLine()) != null){
                seed_list.add(line);
            }
            System.out.println(seed_list);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private static ArrayList<Integer> genRCoff (int n) {
        int max = 2147483647;
        ArrayList <Integer> coff = new ArrayList<Integer>();
        while (n>0) {
            Random r = new Random();
            int a = r.nextInt(max);
            while (coff.contains(a)) {
                a = r.nextInt(max);
            }

            coff.add(a);
            n--;
        }
        //System.out.println(coff);
        return coff;
    }



}
