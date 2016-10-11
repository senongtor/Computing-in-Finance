package clustering;

public class point {
	private double x,y;
	public int labelbefore;  //label after assignment to centroids which are means;
	public int labelafter;   //label 
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
