package cf.netdex.raytracer.construct;

public class Ray3 {

	public Vec3 pos;
	public Vec3 dir;

	

	public Ray3(Vec3 pos, Vec3 dir) {
		this.pos = pos;
		this.dir = dir;

		
	}

	public Vec3 getPoint(double t) {
		return pos.add(dir.mult(t));
	}
}
