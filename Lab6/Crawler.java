import java.net.*;
import java.io.*;
import java.util.*;


/**
 * This class implements a web crawler that keeps a list of pending URLs and
 * URLs it has already visited.
 */
public class Crawler {
    public static final String HTML_PREFIX = "a href=\"";

    /** A list of pending URLs, yet to be visited. */
    private LinkedList<URLDepthPair> pendingURLs;
    /** A list of processed URLs, which will be printed at the end. */
    private LinkedList<URLDepthPair> processedURLs;

    public Crawler() {
        pendingURLs = new LinkedList<URLDepthPair>();
        processedURLs = new LinkedList<URLDepthPair>();
    }

    public List<URLDepthPair> getSites() {
        return processedURLs;
    }

    public static String usage() {
        return "usage: java Crawler <URL> <depth>";
    }

    /**
     * This function starts at the start URL and visits all URLs it encounters
     * until it reaches the max depth.
     */
    public void crawl(String startURL, int maxDepth) {
        URLDepthPair root = new URLDepthPair(startURL, 0);
        pendingURLs.add(root);

        // While there are still pending URLs, visit them.
        while (!pendingURLs.isEmpty()) {
            URLDepthPair nextURLpair = pendingURLs.removeFirst();

            if (nextURLpair.getDepth() <= maxDepth) {
                try {
                    this.visit(nextURLpair);
                }
                catch (Exception e) {}
            }
        }
    }

    /**
     * This function visits a single URL, and adds any new links it finds
     * to pending URLs.
     */
    public void visit(URLDepthPair nextURLpair) throws Exception {
        String webHost = nextURLpair.getHost();
        String docPath = nextURLpair.getPath();
        Socket sock = new Socket(webHost, 80);
        sock.setSoTimeout(3000);

        // Convert Output Stream to more usable format
        PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);

        writer.println("GET " + docPath + " HTTP/1.1");
        writer.println("Host: " + webHost);
        writer.println("Connection: close");
        writer.println();

        // Convert Input Stream to more usable format
        InputStreamReader in = new InputStreamReader(sock.getInputStream());
        BufferedReader reader = new BufferedReader(in);

        boolean malformed = false;

        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;  // Done reading document!
            // Do something with this line of text.
            int idx = 0;
            int start = 0;
            while (idx < line.length() - HTML_PREFIX.length()) {
                if (line.substring(idx,
                    idx + HTML_PREFIX.length()).equals(HTML_PREFIX)) {
                    idx += HTML_PREFIX.length();
                    start = idx;
                    // Look for closing quotation marks
                    while (idx < line.length()) {
                        idx += 1;
                        if (line.charAt(idx) == '\"') {
                            String url = line.substring(start, idx);
                            URLDepthPair newPair = new URLDepthPair(url,
                                nextURLpair.getDepth() + 1);
                            // Test if found URL is valid. Throw exception if not.
                            if (newPair.validURL()) {
                                pendingURLs.add(newPair);
                            }
                            else {
                                malformed = true;
                            }
                            break;
                        }
                    }
                }
                else {
                    idx += 1;
                }
            }
        }

        processedURLs.add(nextURLpair);

        sock.close();
        if (malformed) {
            throw new MalformedURLException();
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(usage());
            System.exit(1);
        }
        String startURL = args[0];
        int maxDepth = 0;

        // Try to parse maxDepth and handle errors gracefully
        try {
            maxDepth = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            System.out.println(usage());
            System.exit(1);
        }

        Crawler webCrawler = new Crawler();

        webCrawler.crawl(startURL, maxDepth);

        List<URLDepthPair> sites = webCrawler.getSites();
        for (URLDepthPair pair : sites) {
            System.out.println(pair);
        }



    }
}
