import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

	public Vec3 getLightSource(){
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
			Intersection intersect = intersectable.getIntersection(ray);
			if (intersect != null)
				intersections.add(intersect);
		}
		Collections.sort(intersections, new Comparator<Intersection>(){
			@Override
			public int compare(Intersection a, Intersection b) {
				if(a.getDistance() > b.getDistance())
					return 1;
				else if(a.getDistance() < b.getDistance())
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
