import java.net.*;
import java.io.*;
import java.util.*;

/**
 * This class performs the task for a single thread, grabbing
 * a new URLDepthPair and visiting it, adding new URLs it finds to the pool.
 */
public class CrawlerTask implements Runnable {
    public static final String HTML_PREFIX = "a href=\"";

    /** Reference to the shared URL pool */
    private URLPool pool;

    public CrawlerTask(URLPool pool) {
        this.pool = pool;
    }

    /**
     * This function visits a single URL, and adds any new links it finds
     * to the pool.
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
                                pool.put(newPair);
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

        sock.close();
        if (malformed) {
            throw new MalformedURLException();
        }
    }

    public void run() {
        while(true) {
            URLDepthPair nextURLpair = pool.get();
            try {
                this.visit(nextURLpair);
            }
            catch (Exception e) {}
        }

    }
}
