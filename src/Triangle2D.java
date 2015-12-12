import java.awt.Color;

public class Triangle2D extends Primitive2D {

	public Triangle2D(Vector3 a, Vector3 b, Vector3 c) {
		super(a, b, c);
	}

	public Triangle2D(Color color, Vector3 a, Vector3 b, Vector3 c) {
		super(color, a, b, c);
	}
	public Intersection getIntersection(Ray3 ray) {
		Vector3[] p = this.getPoints();
		Vector3 v0v1 = p[1].sub(p[0]);
		Vector3 v0v2 = p[2].sub(p[0]);
		Vector3 pvec = ray.dir.cross(v0v2);
		
		float det = v0v1.dot(pvec);
		if (Math.abs(det) < K_EPSILON)
			return null;
		float invDet = 1 / det;
		
		Vector3 tvec = ray.pos.sub(p[0]);
		float u = tvec.dot(pvec) * invDet;
		if (u < 0 || u > 1)
			return null;
		
		Vector3 qvec = tvec.cross(v0v1);
		float v = ray.dir.dot(qvec) * invDet;
		if (v < 0 || u + v > 1)
			return null;
		
		float t = v0v2.dot(qvec) * invDet;
		if(t < 0)
			return null;
		return new Intersection(ray, this, t, this.getColor());

	}

	public boolean edgeCheck(Vector3 normal, Vector3 P, Vector3 a, Vector3 b) {
		Vector3 edge = a.sub(b);
		Vector3 vp = P.sub(b);
		Vector3 C = edge.cross(vp);
		if (normal.dot(C) < 0)
			return false;
		return true;
	}
}
