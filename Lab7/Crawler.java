import java.net.*;
import java.io.*;
import java.util.*;

/**
 * This class implements a web crawler that keeps a list of pending URLs and
 * URLs it has already visited.
 */
public class Crawler {
    public static String usage() {
        return "usage: java Crawler <URL> <depth> <numThreads>";
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println(usage());
            System.exit(1);
        }
        String startURL = args[0];
        int maxDepth = 0;
        int numThreads = 0;

        // Try to parse maxDepth and crawlers and handle errors gracefully
        try {
            maxDepth = Integer.parseInt(args[1]);
            numThreads = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException e) {
            System.out.println(usage());
            System.exit(1);
        }

        // Initialize pool and add root URL
        URLPool pool = new URLPool(maxDepth);
        URLDepthPair root = new URLDepthPair(startURL, 0);
        pool.put(root);

        // Create and start the CrawlerTasks in separate threads
        for (int i = 0; i < numThreads; i++) {
            CrawlerTask c = new CrawlerTask(pool);
            Thread t = new Thread(c);
            t.start();
        }

        // Check for stopping condition (all threads are waiting)
        while (pool.getWaitCount() != numThreads) {
            try {
                Thread.sleep(500);  // 0.1 second
            } catch (InterruptedException ie) {
                System.out.println("Caught unexpected " +
                 "InterruptedException, ignoring...");
            }
        }

        // Print out found URLs and exit
        List<URLDepthPair> sites = pool.getSites();
        for (URLDepthPair pair : sites) {
            try {
                System.out.println(pair);
            }
            // Handle unknown error
            catch (UnknownFormatConversionException e) {
                System.out.println("Error");
            }
        }
        System.exit(0);

    }
}
