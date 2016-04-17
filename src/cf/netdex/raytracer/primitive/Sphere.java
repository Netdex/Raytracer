package cf.netdex.raytracer.primitive;

import java.awt.Color;

import cf.netdex.raytracer.construct.Intersection;
import cf.netdex.raytracer.construct.Ray3;
import cf.netdex.raytracer.construct.Vec3;

public class Sphere extends Primitive3D {

	private Vec3 center;
	private double radius;

	public Sphere(Vec3 point, double radius) {
		super(Color.MAGENTA);
		this.center = point;
		this.radius = radius;
		
		this.bounds =  new Box(new Vec3(center.x - radius, center.y - radius, center.z
				- radius), new Vec3(center.x + radius, center.y + radius,
				center.z + radius));
	}

	@Override
	public Intersection getIntersection(Ray3 ray) {
		double t0, t1;
		double radius2 = radius * radius;
		Vec3 L = ray.pos.sub(center);
		double a = ray.dir.dot(ray.dir);
		double b = 2 * ray.dir.dot(L);
		double c = L.dot(L) - radius2;
		double[] q = solveQuadratic(a, b, c);
		if (q.length == 0)
			return null;
		t0 = q[0];
		t1 = q[1];

		if (t0 > t1) {
			double t = t0;
			t0 = t1;
			t1 = t;
		}

		if (t0 < 0) {
			t0 = t1; // if t0 is negative, let's use t1 instead
			if (t0 < 0)
				return null; // both t0 and t1 are negative
		}

		double t = t0;

		return new Intersection(ray, ray.getPoint(t), this, t, this.getColor());
	}

	private double[] solveQuadratic(double a, double b, double c) {
		double discr = b * b - 4 * a * c;
		if (discr < 0)
			return new double[0];
		else if (discr == 0) {
			double x = 0.5 * b / a;
			return new double[] { x, x };
		} else {
			double q = (b > 0) ? -0.5 * (b + Math.sqrt(discr)) : -0.5
					* (b - Math.sqrt(discr));
			double x0 = q / a;
			double x1 = c / q;
			if (x0 > x1)
				return new double[] { x1, x0 };
			return new double[] { x0, x1 };
		}
	}

	public Box getBounds() {
		return bounds;
	}
}
