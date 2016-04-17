package cf.netdex.raytracer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cf.netdex.raytracer.construct.Intersection;
import cf.netdex.raytracer.construct.Ray3;
import cf.netdex.raytracer.construct.Vec3;
import cf.netdex.raytracer.primitive.Box;
import cf.netdex.raytracer.primitive.Intersectable;
import cf.netdex.raytracer.primitive.Triangle;

public class World {

	private ArrayList<Intersectable> intersectables;
	private Vec3 light = new Vec3(0, 100, 0);

	public World(Vec3 light) {
		this();
		this.light = light;
	}

	public World() {
		intersectables = new ArrayList<Intersectable>();
	}

	public Vec3 getLightSource() {
		return light;
	}

	public void addIntersect(Intersectable intersectable) {
		intersectables.add(intersectable);
	}

	public void aRectangle(Vec3 topleft, Vec3 bottomright) {
		Vec3 topright = new Vec3(bottomright.x, topleft.y, bottomright.z);
		Vec3 bottomleft = new Vec3(topleft.x, bottomright.y, topleft.z);
		Triangle t1 = new Triangle(topleft, topright, bottomleft);
		Triangle t2 = new Triangle(bottomright, bottomleft, topright);
		addIntersect(t1);
		addIntersect(t2);
	}

	public void aRectangle(Color color, Vec3 topleft, Vec3 bottomright) {
		Vec3 topright = new Vec3(bottomright.x, topleft.y, bottomright.z);
		Vec3 bottomleft = new Vec3(topleft.x, bottomright.y, topleft.z);
		Triangle t1 = new Triangle(color, topleft, topright, bottomleft);
		Triangle t2 = new Triangle(color, bottomright, bottomleft, topright);
		addIntersect(t1);
		addIntersect(t2);
	}

	public ArrayList<Intersection> getIntersections(Ray3 ray) {
		ArrayList<Intersection> intersections = new ArrayList<Intersection>();
		for (Intersectable intersectable : intersectables) {
			// Generate bounding box for the primitive
			Box bounds = intersectable.getBounds();
			// Check for preliminary intersection with bounding box for
			// efficiency
			Intersection boundIntersect = null;
			if (bounds.equals(Box.INFINITE)
					|| ((boundIntersect = bounds.getIntersection(ray)) != null)) {
				// Check intersection with actual primitive (may be
				// computationally expensive)
				Intersection intersect = intersectable.getIntersection(ray);
				if (intersect != null)
					intersections.add(intersect);
//				else if(boundIntersect != null)
//					intersections.add(boundIntersect);
			}
		}
		Collections.sort(intersections, new Comparator<Intersection>() {
			@Override
			public int compare(Intersection a, Intersection b) {
				if (a.getDistance() > b.getDistance())
					return 1;
				else if (a.getDistance() < b.getDistance())
					return -1;
				return 0;
			}
		});
		return intersections;
	}

	public ArrayList<Intersectable> getIntersectables() {
		return intersectables;
	}
}
