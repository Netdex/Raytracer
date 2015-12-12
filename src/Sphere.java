import java.awt.Color;

public class Sphere implements Intersectable {

	private Vec3 center;
	private double radius;
	private Color color = Color.MAGENTA;

	public Sphere(Vec3 point, double radius) {
		this.center = point;
		this.radius = radius;
	}

	public Sphere(Color color, Vec3 point, double radius) {
		this.center = point;
		this.radius = radius;
		this.color = color;
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

		return new Intersection(ray, ray.getPoint(t), this, t, color);
	}

	private double[] solveQuadratic(double a, double b, double c) {
		double discr = b * b - 4 * a * c;
		if (discr < 0)
			return new double[0];
		else if (discr == 0) {
			double x = 0.5 * b / a;
			return new double[] { x, x };
		} else {
			double q = (b > 0) ? -0.5 * (b + Math.sqrt(discr)) : -0.5 * (b - Math.sqrt(discr));
			double x0 = q / a;
			double x1 = c / q;
			if (x0 > x1)
				return new double[] { x1, x0 };
			return new double[] { x0, x1 };
		}
	}

}
