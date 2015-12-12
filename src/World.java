import java.awt.Color;
import java.util.ArrayList;

public class World {

	private ArrayList<Intersectable> intersectables;

	public World() {
		intersectables = new ArrayList<Intersectable>();
	}

	public void addIntersect(Intersectable intersectable) {
		intersectables.add(intersectable);
	}

	public void summonRectangle(Vector3 topleft, Vector3 bottomright) {
		Vector3 topright = new Vector3(bottomright.x, topleft.y, bottomright.z);
		Vector3 bottomleft = new Vector3(topleft.x, bottomright.y, topleft.z);
		Triangle2D t1 = new Triangle2D(topleft, topright, bottomleft);
		Triangle2D t2 = new Triangle2D(bottomright, bottomleft, topright);
		addIntersect(t1);
		addIntersect(t2);
	}

	public void summonRectangle(Color color, Vector3 topleft, Vector3 bottomright) {
		Vector3 topright = new Vector3(bottomright.x, topleft.y, bottomright.z);
		Vector3 bottomleft = new Vector3(topleft.x, bottomright.y, topleft.z);
		Triangle2D t1 = new Triangle2D(color, topleft, topright, bottomleft);
		Triangle2D t2 = new Triangle2D(color, bottomright, bottomleft, topright);
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
		return intersections;
	}

	public ArrayList<Intersectable> getIntersectables() {
		return intersectables;
	}
}
