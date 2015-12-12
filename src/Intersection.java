import java.awt.Color;

public class Intersection {
	
	private Ray3 ray;
	private Vec3 point;
	private Intersectable intersectable;
	private double distance;
	private Color color;
	
	public Intersection(Ray3 ray, Vec3 point, Intersectable intersectable, double distance, Color color){
		this.ray = ray;
		this.point = point;
		this.intersectable = intersectable;
		this.distance = distance;
		this.color = color;
	}
	
	public Ray3 getRay() {
		return ray;
	}
	
	public Vec3 getPoint(){
		return point;
	}
	
	public Intersectable getIntersectable() {
		return intersectable;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public Color getIntersectionColor(){
		return color;
	}

}
