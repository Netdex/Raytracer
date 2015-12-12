import java.awt.Color;

public interface Intersectable {

	public static final float K_EPSILON = 0.0001f;
	
	public Intersection getIntersection(Ray3 ray);
}
