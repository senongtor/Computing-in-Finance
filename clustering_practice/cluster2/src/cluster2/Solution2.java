package cluster2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
public class Solution2 {
	private static final int NUM_CLUSTERS = 500;
	private static final int NUM_POINTS = 10000; // if we want to change the
													// number of points, suppose
													// the number of points is
													// always a perfect square
													// so that points'
													// distribution remains the
													// same

	public static void main(String[] args) {
		List<point> points = new ArrayList<point>();
		for (int i = 0; i < NUM_POINTS; i++) {
			points.add(new point(Math.floor(i / (Math.sqrt(NUM_POINTS))), i % (Math.sqrt(NUM_POINTS))));// add
																										// (0,1),(0,2)...(0,sqrt(NUM_POINTS)-1),(1,1)....
																										// orderly
																										// to
																										// the
																										// ArrayList
			points.get(i).labelbefore = 0;
			points.get(i).labelafter = 1;
		}
		System.out.println("Points added");
		List<cluster> clusterlist = new ArrayList<cluster>(); // picking the
																// first
		// NUM_CUSTERS number of
		// centroid points
		// and create
		// cluster object
		// then store them
		Random rand = new Random();
		int current_cluster_index = 0;
		HashSet<Integer> indexes = new HashSet<>();
		while (current_cluster_index < NUM_CLUSTERS) {
			int random_index = Math.abs(rand.nextInt()) % 10000;
			if (indexes.contains(random_index))
				continue;
			indexes.add(random_index);
			clusterlist.add(new cluster(points.get(random_index))); // add
																	// initial
																	// centroids
			current_cluster_index++;								// to each
																	// cluster
																	// in the
																	// clusterlist
																	// by
																	// constructor.
			
		}
		System.out.println("centroids initialized");
		int processing = 0; // recording iteration times

		/***
		 * In the while loop, we will assign 20 points to desired clusters, calculate mean and replace each cluster's 
		 * centroid with the calculated mean, then we will check if all the points are stilling having the same label,
		 * i.e. if they are still in the same cluster.
		 */
		while (!converge(points)) {

			System.out.println("now is in:" + processing++);
			
			HashSet<point> notSignedPoints = new HashSet<point>();
			for(point p : points){
				notSignedPoints.add(p);
			}

			for (int i =0; i<NUM_CLUSTERS;i++){
				assignCluster(clusterlist.get(i), notSignedPoints,i,"before");
			}
			
			for (int i = 0; i < NUM_CLUSTERS; i++) {
				clusterlist.get(i).centroid.setX(clusterlist.get(i).getmean(clusterlist.get(i)).getX());
				clusterlist.get(i).centroid.setY(clusterlist.get(i).getmean(clusterlist.get(i)).getY());
				clusterlist.get(i).clear();
			}
			
			notSignedPoints.clear();
			
			for(point p : points){
				notSignedPoints.add(p);
			}
			
			for (int i =0; i<NUM_CLUSTERS;i++){
				assignCluster(clusterlist.get(i), notSignedPoints,i,"after");
			}
		}

		for (int i = 0; i < NUM_CLUSTERS; i++) {
			System.out.println("Cluster " + (i + 1) + " is (" + (clusterlist.get(i).centroid.getX()) + ","
					+ (clusterlist.get(i).centroid.getY()) + ")\n"); // test2
		}
	}

	/***
	 * To know if every point is still being reassigned to different clusters.
	 * 
	 * @param clusterlist
	 * @return true if every point has same labelbefore and labelafter and flase
	 *         otherwise.
	 */
	private static boolean converge(List<point> points) {
		for (int i = 0; i < NUM_POINTS; i++) {
			if (points.get(i).labelafter != points.get(i).labelbefore) {
				return false;
			}
		}
		return true;
	}

	
	//private static void assign(List<point> points, List<cluster> clusterlist)
	// {
	// int layer = 0;
	// int minIndex = 0;
	// while (layer < 20) {
	// for (int i = 0; i < NUM_CLUSTERS; i++) {
	// double min = 100;
	// for (int j = 0; j < NUM_POINTS-i-500*layer; j++) {
	// if (distance(points.get(j), clusterlist.get(i).centroid) < min) {
	// min = distance(points.get(j), clusterlist.get(i).centroid);
	// minIndex = j;
	// }
	// }
	// clusterlist.get(i).add(points.get(minIndex));
	// points.add(points.get(minIndex));
	// points.remove(minIndex);
	// }
	// layer++;
	// }
	// }
	
	/***
	 * For each cluster, find the nearest and unassigned 20 points and assign them to the cluster.
	 * @param cluster
	 * @param notSignedPoints
	 * @param label
	 * @param string "before" of "after"
	 */
	private static void assignCluster(cluster clu, HashSet<point> notSignedPoints, int label, String s){
		Comparator<point> pComparator = new Comparator<point>(){
			public int compare(point a, point b){
				return a.distance_from_cluster - b.distance_from_cluster; //compare the distances between the distances from the current cluster to points
			}
		};
		PriorityQueue<point> myPQ = new PriorityQueue<>(NUM_POINTS, pComparator);
		Iterator<point> p = notSignedPoints.iterator();
		while(p.hasNext()){
			point n = p.next();
			n.distance_from_cluster = (int)distance(n, clu.centroid);
			myPQ.add(n);
		}
		
		for(int i = 0; i < 20; i++){
			point closeP = myPQ.poll();
			notSignedPoints.remove(closeP);
			clu.add(closeP);
			if (s.equals("before")){
			closeP.labelbefore = label;}
			else 
				closeP.labelafter = label;
		}
		
	}

	/***
	 * Calculating the euclidean distance between two points.
	 * 
	 * @param first
	 *            point
	 * @param second
	 *            point
	 * @return double value of the distance
	 */
	public static double distance(point pt, point pt2) {
		double dist = Math.pow((pt.getX() - pt2.getX()), 2) + Math.pow((pt.getY() - pt2.getY()), 2);
		return dist;

	}
}
