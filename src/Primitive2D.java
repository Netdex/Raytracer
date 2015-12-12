import java.awt.Color;

public abstract class Primitive2D implements Intersectable {
	
	private Vec3[] points;
	private Color color;

	protected Primitive2D(Color color, Vec3... points){
		this(points);
		this.color = color;
		this.points = points;
	}

	protected Primitive2D(Color color, int points) {
		this(points);
		this.color = color;
	}

	protected Primitive2D(Vec3... points){
		this.points = points;
		this.color = Color.WHITE;
	}
	
	protected Primitive2D(int points) {
		this.points = new Vec3[points];
		for (int i = 0; i < points; i++)
			this.points[i] = Vec3.zero();
		this.color = Color.WHITE;
	}
	
	public Color getColor(){
		return color;
	}
	
	public Vec3[] getPoints(){
		return points;
	}
}
