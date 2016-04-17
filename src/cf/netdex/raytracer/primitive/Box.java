package cf.netdex.raytracer.primitive;

import java.awt.Color;

import cf.netdex.raytracer.construct.Intersection;
import cf.netdex.raytracer.construct.Ray3;
import cf.netdex.raytracer.construct.Vec3;

public class Box extends Primitive3D {

	public Vec3[] bounds;
	public static Box INFINITE = new Box(new Vec3(Double.NEGATIVE_INFINITY,
			Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), new Vec3(
			Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
			Double.POSITIVE_INFINITY));

	public Box(Vec3 minBound, Vec3 maxBound) {
		super(Color.RED);
		bounds = new Vec3[] { minBound, maxBound };
	}

	public boolean equals(Object o) {
		if (!(o instanceof Box))
			return false;
		Box b = (Box) o;
		if (bounds[0].equals(b.bounds[0]) && bounds[1].equals(b.bounds[1]))
			return true;
		return false;
	}

	@Override
	public Intersection getIntersection(Ray3 r) {
		double tmin, tmax, tymin, tymax, tzmin, tzmax;

		Vec3 invdir = new Vec3(1 / r.dir.x, 1 / r.dir.y, 1 / r.dir.z);
		byte[] sign = new byte[] { (byte) (invdir.x < 0 ? 1 : 0),
				(byte) (invdir.y < 0 ? 1 : 0), (byte) (invdir.z < 0 ? 1 : 0) };

		tmin = (bounds[sign[0]].x - r.pos.x) * invdir.x;
		tmax = (bounds[1 - sign[0]].x - r.pos.x) * invdir.x;
		tymin = (bounds[sign[1]].y - r.pos.y) * invdir.y;
		tymax = (bounds[1 - sign[1]].y - r.pos.y) * invdir.y;

		if ((tmin > tymax) || (tymin > tmax))
			return null;
		if (tymin > tmin)
			tmin = tymin;
		if (tymax < tmax)
			tmax = tymax;

		tzmin = (bounds[sign[2]].z - r.pos.z) * invdir.z;
		tzmax = (bounds[1 - sign[2]].z - r.pos.z) * invdir.z;

		if ((tmin > tzmax) || (tzmin > tmax))
			return null;
		if (tzmin > tmin)
			tmin = tzmin;
		if (tzmax < tmax)
			tmax = tzmax;

		if (tmin < 0)
			return null;
		return new Intersection(r, r.getPoint(tmin), this, tmin,
				this.getColor());
	}

	public Box getBounds() {
		return null;
	}

}
