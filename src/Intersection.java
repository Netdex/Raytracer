import java.awt.Color;

public class Intersection {
	
	private Ray3 ray;
	private Intersectable intersectable;
	private float distance;
	private Color color;
	
	public Intersection(Ray3 ray, Intersectable intersectable, float distance, Color color){
		this.ray = ray;
		this.intersectable = intersectable;
		this.distance = distance;
		this.color = color;
	}
	
	public Ray3 getRay() {
		return ray;
	}
	
	public Intersectable getIntersectable() {
		return intersectable;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public Color getIntersectionColor(){
		return color;
	}

}
