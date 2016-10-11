package clustering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Solution {
	private static final int NUM_CLUSTERS = 500;
	private static final int NUM_POINTS = 10000; // if we want to change the
													// number of points, suppose
													// the number of points is
													// always a perfect square
													// so that points'
													// distribution remains the
													// same

	public static void main(String[] args) {
		Solution s=new Solution();
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

		//System.out.println(Double.toString(points.get(20).getY())); // test1

		cluster[] clusterlist = new cluster[NUM_CLUSTERS]; // picking the first
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
			if(indexes.contains(random_index))
				continue;
			indexes.add(random_index);
			clusterlist[current_cluster_index++] = new cluster(points.get(random_index)); //add initial centroids to each cluster in the clusterlist by constructor.
		}

		int processing = 0; //recording iteration times
		
		while (!s.converge(points)) {       //while for each point label is changed after iteration, keep recalculate mean and reassigning
			System.out.println("now is in:" + processing++);
			for (int i = 0; i < NUM_POINTS; i++) {

				s.assign(points.get(i), clusterlist,"before");
			}
			for (int i = 0; i < NUM_CLUSTERS; i++) {
				clusterlist[i].centroid.setX(clusterlist[i].getmean(clusterlist[i]).getX());
				clusterlist[i].centroid.setY(clusterlist[i].getmean(clusterlist[i]).getY());
				clusterlist[i].clear();
			}
			for (int i = 0; i < NUM_POINTS; i++) {

				s.assign(points.get(i), clusterlist,"after");
			}
		}

		for (int i = 0; i < NUM_CLUSTERS; i++) {
			System.out.println(
				"Cluster " + (i + 1) + " is (" + (clusterlist[i].centroid.getX())
							+ "," + (clusterlist[i].centroid.getY()) + ")\n"); // test2
		}
	}

	/***
	 * To know if every point is still being reassigned to different clusters.
	 * 
	 * @param clusterlist
	 * @return true if every point has same labelbefore and labelafter and flase otherwise.
	 */
	protected boolean converge(List<point> points) {
		for (int i = 0; i < NUM_POINTS; i++) {
			if (points.get(i).labelafter != points.get(i).labelbefore) {
				return false;
			}
		}
		return true;
	}

	/***
	 * For one point, find the nearest centroid point and add that point to the cluster where that centroid stays,
	 * then record its label, which is the index of the cluster in its clusterlist. I use two label indicating that
	 * for the while loop checking.
	 * 
	 * @param point
	 * @param clusterlist that contains a list of cluster groups.
	 */
	protected void assign(point x, cluster[] clusterlist, String s) {
		int minIndex = 0;
		double min = distance(x, clusterlist[0].centroid);
		for (int j = 0; j < clusterlist.length; j++) {
			if (distance(x, clusterlist[j].centroid) < min) {
				min = distance(x, clusterlist[j].centroid);
				minIndex = j;
			}
		}
			clusterlist[minIndex].add(x);
			if (s.equals("before")){
			x.labelbefore = minIndex;
			}
			else if (s.equals("after")){
				x.labelafter = minIndex;
			}
	}
	
	/***
	 * Calculating the Euclidean distance between two points.
	 * 
	 * @param first
	 *            point
	 * @param second
	 *            point
	 * @return double value of the distance
	 */
	protected double distance(point pt, point pt2) {
		double dist = Math.sqrt(Math.pow((pt.getX() - pt2.getX()), 2) + Math.pow((pt.getY() - pt2.getY()), 2));
		return dist;

	}
}
