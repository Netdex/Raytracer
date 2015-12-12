import java.awt.Color;

public class Plane implements Intersectable {

	private Ray3 planeRay;
	private Color color = Color.GREEN;
	
	public Plane(Ray3 planeRay) {
		this.planeRay = planeRay;
	}

	public Plane(Color c, Ray3 planeRay) {
		this.planeRay = planeRay;
		this.color = c;
	}

	public Color getIntersectionColor(){
		return color;
	}
	
	public Intersection getIntersection(Ray3 ray) {
		float denom = planeRay.dir.dot(ray.dir);
		if(denom < K_EPSILON)
			return null;
		Vector3 p0l0 = planeRay.pos.sub(ray.pos);
		float t = p0l0.dot(planeRay.dir) / denom;

		return new Intersection(ray, this, t, color);
	}
}
