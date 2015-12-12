import java.awt.Color;

public abstract class Primitive2D implements Intersectable {
	
	private Vector3[] points;
	private Color color;

	protected Primitive2D(Color color, Vector3... points){
		this(points);
		this.color = color;
		this.points = points;
	}

	protected Primitive2D(Color color, int points) {
		this(points);
		this.color = color;
	}

	protected Primitive2D(Vector3... points){
		this.points = points;
		this.color = Color.WHITE;
	}
	
	protected Primitive2D(int points) {
		this.points = new Vector3[points];
		for (int i = 0; i < points; i++)
			this.points[i] = Vector3.zero();
		this.color = Color.WHITE;
	}
	
	public Color getColor(){
		return color;
	}
	
	public Vector3[] getPoints(){
		return points;
	}
}
