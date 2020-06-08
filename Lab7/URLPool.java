import java.net.*;
import java.io.*;
import java.util.*;

/**
 * This class represents the pool of both processed and pending URLs. It is
 * thread safe and also keeps track of the number of waiting threads.
 */
public class URLPool {
  /** A list of pending URLs, yet to be visited. */
  private LinkedList<URLDepthPair> pendingURLs;
  /** A list of processed URLs, which will be printed at the end. */
  private LinkedList<URLDepthPair> processedURLs;
  /** A count of the threads currently waiting */
  private int waiting;
  /** The maximum depth */
  private int maxDepth;

  public URLPool(int maxDepth) {
      pendingURLs = new LinkedList<URLDepthPair>();
      processedURLs = new LinkedList<URLDepthPair>();
      waiting = 0;
      this.maxDepth = maxDepth;
  }

  /** Get a URLDepthPair from pendingURLs (thread-safe) */
  public synchronized URLDepthPair get() {
      URLDepthPair item = null;
      while (pendingURLs.size() == 0) {
          waiting += 1;
          try {
             wait();
          } catch (InterruptedException ie) {
             System.out.println("Caught unexpected " +
                 "InterruptedException, ignoring...");
          }
          waiting -= 1;
      }
      return pendingURLs.removeFirst();
  }

  /** Add a URLDepthPair to the pool (thread-safe) */
  public synchronized void put(URLDepthPair newPair) {
      if (newPair.getDepth() < maxDepth) {
          pendingURLs.add(newPair);
      }
      processedURLs.add(newPair);
      notify();
  }

  /** Get the number of waiting threads (thread-safe) */
  public synchronized int getWaitCount() {
      return waiting;
  }

  /** Get the visited sites */
  public List<URLDepthPair> getSites() {
      return processedURLs;
  }
}
