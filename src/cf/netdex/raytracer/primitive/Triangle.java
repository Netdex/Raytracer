package cf.netdex.raytracer.primitive;

import java.awt.Color;

import cf.netdex.raytracer.construct.Intersection;
import cf.netdex.raytracer.construct.Ray3;
import cf.netdex.raytracer.construct.Vec3;

public class Triangle extends Primitive2D {

	public Triangle(Vec3 a, Vec3 b, Vec3 c) {
		super(a, b, c);
	}

	public Triangle(Color color, Vec3 a, Vec3 b, Vec3 c) {
		super(color, a, b, c);
	}
	
	public Intersection getIntersection(Ray3 ray) {
		Vec3[] p = this.getPoints();
		Vec3 v0v1 = p[1].sub(p[0]);
		Vec3 v0v2 = p[2].sub(p[0]);
		Vec3 pvec = ray.dir.cross(v0v2);
		
		double det = v0v1.dot(pvec);
		if (Math.abs(det) < K_EPSILON)
			return null;
		double invDet = 1 / det;
		
		Vec3 tvec = ray.pos.sub(p[0]);
		double u = tvec.dot(pvec) * invDet;
		if (u < 0 || u > 1)
			return null;
		
		Vec3 qvec = tvec.cross(v0v1);
		double v = ray.dir.dot(qvec) * invDet;
		if (v < 0 || u + v > 1)
			return null;
		
		double t = v0v2.dot(qvec) * invDet;
		if(t < 0)
			return null;
		return new Intersection(ray, ray.getPoint(t), this, t, this.getColor());

	}

	public boolean edgeCheck(Vec3 normal, Vec3 P, Vec3 a, Vec3 b) {
		Vec3 edge = a.sub(b);
		Vec3 vp = P.sub(b);
		Vec3 C = edge.cross(vp);
		if (normal.dot(C) < 0)
			return false;
		return true;
	}
}
