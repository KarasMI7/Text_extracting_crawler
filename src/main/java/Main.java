import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.Random;

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
    public LinkedList <String> crawler_list_lvl2 = new LinkedList<String>(); // After Filtered by Bloom-filter
    private static ArrayList <String> seed_list = new ArrayList<String>();
    public static ArrayList<BitSet> MLBF = new ArrayList<BitSet>(); // keeps all BF in a list, layer is defined by the list cell position.


    //parameter section
    private static int L = 5;
    private static int K = 20;
    private static int num_of_workers = 1;
    //end of parameter section

    public static void main (String[] args) {
        System.out.println("Hello Idiots");
        read_seed_list();

        ArrayList <Integer> coffA = genRCoff(K);
        ArrayList <Integer> coffB = genRCoff(K);
        create_workers(num_of_workers,coffA,coffB,L,K);

        //Bit arrays for Bloom Filter
        //n = 1,000,000, p = 1.0E-6 (1 in 1,000,000) → m = 28,755,176 (3.43MB), k = 20
        for(int i = 0; i<=L; i++){
            BitSet BFs = new BitSet(28755176);
            MLBF.add(BFs);
        }

    }

    private static void create_workers(int num_of_workers, ArrayList coffA, ArrayList coffB, int L, int K){
        for (int i = 0; i <= num_of_workers; i++){
            Thread t = new Thread(new Worker(MLBF,coffA,coffB,L,K));  // needs to fix worker class, also naming the worker thread?
            t.start();
        }

    }

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
