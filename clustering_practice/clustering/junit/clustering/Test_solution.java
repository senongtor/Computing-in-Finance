package clustering;

import java.util.ArrayList;
import java.util.List;
import clustering.point;
import junit.framework.TestCase;

public class Test_solution extends TestCase {
	public void testdistance() {
		List<point> points = new ArrayList<point>();
		points.add(new point(0, 0));
		points.add(new point(5, 0));
		points.add(new point(10, 0));
		cluster[] clusterlist = new cluster[3];
		clusterlist[0] = new cluster(points.get(0));
		clusterlist[1] = new cluster(points.get(1));
		clusterlist[2] = new cluster(points.get(2));
		for (int i = 0; i < 3; i++) {
			points.get(i).labelbefore = 0;
			points.get(i).labelafter = 1;
		}

		class solution1 extends Solution {
			protected boolean converge(List<point> points) {
				for (int i = 0; i < 3; i++) {
					if (points.get(i).labelafter != points.get(i).labelbefore) {
						return false;
					}
				}
				return true;
			}

			protected double distance(point pt, point pt2) {
				double dist = Math.sqrt(Math.pow((pt.getX() - pt2.getX()), 2) + Math.pow((pt.getY() - pt2.getY()), 2));
				return dist;
			}

			protected void assign(point x, cluster[] clusterlist, String s) {
				int minIndex = 0;
				double min = 100;
				for (int j = 0; j < clusterlist.length; j++) {
					if (distance(x, clusterlist[j].centroid) < min) {
						min = distance(x, clusterlist[j].centroid);
						minIndex = j;
					}
				}
				clusterlist[minIndex].add(x);
				if (s.equals("before")) {
					x.labelbefore = minIndex;
				} else if (s.equals("after")) {
					x.labelafter = minIndex;
				}

			}
		}
		solution1 s1 = new solution1();
		System.out.println((points.get(0).getX()) + "," + (points.get(0).getY()));
		System.out.println((points.get(2).getX()) + "," + (points.get(2).getY()));
		assertTrue(s1.distance(points.get(0), points.get(1)) == 5);
		while (!s1.converge(points)) {
			for (int i = 0; i < 3; i++) {

				s1.assign(points.get(i), clusterlist, "before");
				
			}
			for (int i = 0; i < 3; i++){
				System.out.println(points.get(i).labelbefore);
				System.out.println(clusterlist[i].get(0).getX());
			}
			for (int i = 0; i < 3; i++) {
				clusterlist[i].centroid.setX(clusterlist[i].getmean(clusterlist[i]).getX());
				clusterlist[i].centroid.setY(clusterlist[i].getmean(clusterlist[i]).getY());
				clusterlist[i].clear();
			}
			for (int i = 0; i < 3; i++) {
				System.out.println(clusterlist[i].centroid.getX());
				s1.assign(points.get(i), clusterlist, "after");
			}
			for (int i = 0; i < 3; i++){
				System.out.println(points.get(i).labelafter);
			}
		}

		for (int i = 0; i < 3; i++) {
			System.out.println("Cluster " + (i + 1) + " is (" + (clusterlist[i].centroid.getX()) + ","
					+ (clusterlist[i].centroid.getY()) + ")\n"); // test2
		}

	}

}
