public interface Intersectable {

	public static final double K_EPSILON = 0.0001f;
	
	public Intersection getIntersection(Ray3 ray);
}
