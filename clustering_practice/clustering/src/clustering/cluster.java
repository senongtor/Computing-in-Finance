package clustering;

import java.util.ArrayList;

public class cluster extends ArrayList<point>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	point centroid;
	int size;
	public cluster(point p){
		centroid = p;
	}
	
/***
 * calculate the centroid point of a cluster of point
 * @param ArrayList<point>
 * @return point
 */
	private point domean(cluster acluster){
		double xcoor=0;
		double ycoor=0;
		for(int i=0;i<acluster.size();i++){
			xcoor+=(acluster.get(i).getX());
			ycoor+=(acluster.get(i).getY());
		}
		point centr = new point(xcoor/(acluster.size()),ycoor/(acluster.size()));
		
		return centr;
	}
	
	public point getmean(cluster acluster){
		return domean(acluster);
	}

}
