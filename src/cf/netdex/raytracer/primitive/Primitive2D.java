package cf.netdex.raytracer.primitive;

import java.awt.Color;

import cf.netdex.raytracer.construct.Vec3;

public abstract class Primitive2D implements Intersectable {

	private Vec3[] points;
	private Color color;
	private Box bounds;
	
	protected Primitive2D(Color color, Vec3... points) {
		this(points);
		this.color = color;
		this.points = points;
	}

	protected Primitive2D(Color color, int points) {
		this(points);
		this.color = color;
	}

	protected Primitive2D(Vec3... points) {
		this.points = points;
		this.color = Color.WHITE;
		this.bounds = generateBounds();
	}

	protected Primitive2D(int points) {
		this.points = new Vec3[points];
		for (int i = 0; i < points; i++)
			this.points[i] = Vec3.zero();
		this.color = Color.WHITE;
		this.bounds = generateBounds();
	}

	public Color getColor() {
		return color;
	}

	public Vec3[] getPoints() {
		return points;
	}

	private Box generateBounds(){
		Vec3 min = new Vec3(points[0]);
		Vec3 max = new Vec3(points[1]);
		for (int i = 1; i < points.length; i++) {
			if (points[i].x < min.x) {
				min.x = points[i].x;
			}
			if (points[i].y < min.y) {
				min.y = points[i].y;
			}
			if (points[i].z < min.z) {
				min.z = points[i].z;
			}
			
			if (points[i].x > max.x) {
				max.x = points[i].x;
			}
			if (points[i].y > max.y) {
				max.y = points[i].y;
			}
			if (points[i].z > max.z) {
				max.z = points[i].z;
			}
		}
		return new Box(min, max);
	}
	public Box getBounds() {
		return bounds;
	}
}
