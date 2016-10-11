package clustering;

import junit.framework.TestCase;

public class Test_Cluster extends TestCase {
	public void test(){
	point p = new point(1,1);
	point p1 = new point(2,2);
	point p2 = new point(3,1);
	
cluster clu=new cluster(p);
System.out.println((clu.centroid.getX()));
clu.add(p1);
clu.add(p2);
assertTrue(clu.size()==2);

assertTrue(clu.getmean(clu).getX()==(p1.getX()+p2.getX())/2);

clu.clear();
clu.add(p);
assertTrue(clu.getmean(clu).getX()==p.getX());
assertTrue(clu.size()==1);
	}
}
