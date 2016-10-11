package clustering;

import junit.framework.TestCase;

public class Test_Point extends TestCase {
public void testpoint(){
	point p =new point(2,2);
	point p1 = new point(4,4);
	assertTrue((p.getX())==2 && p.getY()==2);
	p.setX(p1.getX());
	p.setY(p1.getY());
	assertTrue((p.getX())==4 && p.getY()==4);
	
}
}
