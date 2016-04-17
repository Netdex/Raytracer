package cf.netdex.raytracer.primitive;

import cf.netdex.raytracer.construct.Intersection;
import cf.netdex.raytracer.construct.Ray3;

public interface Intersectable {

	public static final double K_EPSILON = 0.01f;
	
	public Intersection getIntersection(Ray3 ray);
	public Box getBounds();
}
