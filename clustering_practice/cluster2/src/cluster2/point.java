package cluster2;

public class point {
	private double x,y;
	int labelbefore;
	int labelafter;
	int distance_from_cluster;
	public point(double _x, double _y) {
		this.x=_x;
		this.y=_y;
		
	}
	public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}
	
}
