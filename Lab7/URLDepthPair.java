
/**
 * This class represents a tuple of URL and depth. It allows the program to know
 * how far down a URL was encountered.
 */
public class URLDepthPair {
    public static final String URL_PREFIX = "http://";

    public String url;
    public int depth;

    public URLDepthPair(String url, int depth) {
        this.url = url;
        this.depth = depth;
    }

    /** Parse the URL for the host */
    public String getHost() {
        int idx = URL_PREFIX.length();
        while (idx < url.length()) {
            if (url.charAt(idx) == '/') {
                return url.substring(URL_PREFIX.length(), idx);
            }
            idx += 1;
        }
        return url.substring(URL_PREFIX.length(), url.length());
    }

    /** Parse the URL for the resource path */
    public String getPath() {
      int idx = URL_PREFIX.length();
      while (idx < url.length()) {
          if (url.charAt(idx) == '/') {
              // Once slash is found, what follows is the doc path.
              return url.substring(idx, url.length());
          }
          idx += 1;
      }
      // If no slash is found, just return a single slash
      return "/";

    }

    public int getDepth() {
        return depth;
    }

    /** Check if this URL starts with http:// */
    public boolean validURL() {
        return url.startsWith(URL_PREFIX);
    }

    @Override
    public String toString() {
        return String.format(url + " - %d", depth);
    }
}
