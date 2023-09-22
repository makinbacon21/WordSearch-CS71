import java.io.*;
import java.util.*;

/*
 * Implements a text search engine for a collection of documents in the same directory.
 */

public class WordSearch {
    
    public static Map<String, Set<String>> buildMap(String dirName) {
        File dir = new File(dirName);	// create a File object for this directory
        Scanner scan = null;

        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        
        // make sure it exists and is actually a directory
        if (dir.exists() == false || dir.isDirectory() == false) {
            throw new IllegalArgumentException(dirName + " does not exist or is not a directory");
        }
        
        File[] files = dir.listFiles();		// get the Files in the specified directory

        for(File file : files) {
            try {
                scan = new Scanner(new FileReader(file));
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
            }
            while(scan.hasNext()) {
                String cur = scan.next().toLowerCase();
                if(!map.containsKey(cur)) {
                    Set<String> set = new HashSet<String>();
                    set.add(file.getName());
                    map.put(cur, set);
                } else {
                    map.get(cur).add(file.getName());
                }
            }
        }

        return map; // change this as necessary
        
    }
    
    public static List<String> search(String[] terms, Map<String, Set<String>> map) {
        HashMap<String, Integer> dict = new HashMap<String, Integer>();
        List<String> out = new ArrayList<String>();
        for(String term : terms) {
            for(String file : map.get(term)) {
                if(dict.get(file) == null)
                    dict.put(file, 1);
                else
                    dict.put(file, dict.get(file) + 1);
            }
        }

        System.out.println(dict.toString());

        for(String key : dict.keySet()) {
            int cur = dict.get(key);
            for(int j=0; j<=out.size(); j++) {
                if(j == out.size()) {
                    out.add(key);
                    break;
                }
                int other = dict.get(out.get(j));
                if(cur > other) {
                    out.add(j, key);
                    break;
                } else if (cur == other && key.compareTo(out.get(j)) < 0) {
                    out.add(j, key);
                    break;
                }
            }
        }

        System.out.println(out.toString());

        return out;
    }
    
    public static void main(String[] args) {
        Map<String, Set<String>> map = buildMap(args[0]);
        System.out.println(map.toString()); 					// for debugging purposes
        
        System.out.print("Enter a term to search for: ");

        try (Scanner in = new Scanner(System.in)) { // create a Scanner to read from stdin
            String input = in.nextLine();			// read the entire line that was entered
            String[] terms = input.split(" ");		// separate tokens based on a single whitespace
            List<String> list = search(terms, map);	// search for the tokens in the Map
            for (String file : list) {				// print the results
                System.out.println(file);
            }
        }
        catch (Exception e) {
            // oops! something went wrong
            e.printStackTrace();
        }
    }

}
