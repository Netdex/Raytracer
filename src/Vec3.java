import static java.lang.Math.*;

public class Vec3 {

	public double x;
	public double y;
	public double z;

	public Vec3(Vec3 o){
		this.set(o);
	}
	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static Vec3 zero() {
		return new Vec3(0, 0, 0);
	}

	public void normalize() {
		double len = length();
		x /= len;
		y /= len;
		z /= len;
	}

	public void set(Vec3 o){
		this.x = o.x;
		this.y = o.y;
		this.z = o.z;
	}
	
	public Vec3 getNormalize() {
		double len = length();
		return new Vec3(x / len, y / len, z / len);
	}

	public double length() {
		return (double) (Math.sqrt(lengthSq()));
	}

	public double lengthSq() {
		return x * x + y * y + z * z;
	}

	public Vec3 add(Vec3 o) {
		return new Vec3(this.x + o.x, this.y + o.y, this.z + o.z);
	}

	public Vec3 sub(Vec3 o) {
		return new Vec3(this.x - o.x, this.y - o.y, this.z - o.z);
	}

	public Vec3 div(double b) {
		return new Vec3(this.x / b, this.y / b, this.z / b);
	}

	public Vec3 mult(double b) {
		return new Vec3(this.x * b, this.y * b, this.z * b);
	}
	public Vec3 mult(Vec3 o){
		return new Vec3(x * o.x, y * o.y, z * o.z);
	}

	public double dot(Vec3 o) {
		return x * o.x + y * o.y + z * o.z;
	}

	public Vec3 cross(Vec3 o) {
		return new Vec3(y * o.z - o.y * z, z * o.x - o.z * x, x * o.y - o.x * y);
	}
	
	public void rotateZ(double theta){
		x = (double) (x * cos(theta) - y * sin(theta));
		y = (double) (x * sin(theta) + y * cos(theta));
	}
	
	public void rotateY(double theta){
		x = (double) (x * cos(theta) + z * sin(theta));
		z = (double) (-x * sin(theta) + z * cos(theta));
	}
	
	public void rotateX(double theta){
		y = (double) (y * cos(theta) - z * sin(theta));
		z = (double) (y * sin(theta) + z * cos(theta));
	}

	public Vec3 getRotateZ(double theta){
		return new Vec3((double) (x * cos(theta) - y * sin(theta)), (double) (x * sin(theta) + y * cos(theta)), z);
	}
	
	public Vec3 getRotateY(double theta){
		return new Vec3((double) (x * cos(theta) + z * sin(theta)), y, (double) (-x * sin(theta) + z * cos(theta)));
	}
	
	public Vec3 getRotateX(double theta){
		return new Vec3(x, (double) (y * cos(theta) - z * sin(theta)), (double) (y * sin(theta) + z * cos(theta)));
	}
	
	public double getAngleZ(){
		double rotx = getAngleX();
		double roty = getAngleY();
		return atan2( cos(rotx), sin(rotx) * sin(roty) );
	}
	
	public double getAngleY(){
		return atan2( x * cos(getAngleX()), z );
	}
	
	public double getAngleX(){
		return atan2(y, z);
	}
	
	public Vec3 getRotationVector(){
		double rotx = getAngleX();
		double roty = atan2( x * cos(rotx), z );
		double rotz =  atan2( cos(rotx), sin(rotx) * sin(roty) );
		return new Vec3(rotx, roty, rotz);
	}
	
	public void rotateAboutAxis(Vec3 axis, double angle){
		this.set(getRotatedAboutAxis(axis, angle));
	}
	
	public Vec3 getRotatedAboutAxis(Vec3 axis, double angle){
		double norm = axis.length();

	    double halfAngle = -0.5 * angle;
	    double coeff = Math.sin(halfAngle) / norm;

	    double q0 = Math.cos (halfAngle);
	    double q1 = coeff * axis.x;
	    double q2 = coeff * axis.y;
	    double q3 = coeff * axis.z;
	    
	    double s = q1 * x + q2 * y + q3 * z;
	    return new Vec3(2 * (q0 * (x * q0 - (q2 * z - q3 * y)) + s * q1) - x,
                2 * (q0 * (y * q0 - (q3 * x - q1 * z)) + s * q2) - y,
                2 * (q0 * (z * q0 - (q1 * y - q2 * x)) + s * q3) - z);
	}
	
	public Vec3 toDegrees(){
		return new Vec3(Math.toDegrees(x), Math.toDegrees(y), Math.toDegrees(z));
	}
	public String toString() {
		return String.format("[%f, %f, %f]", x, y, z);
	}

}
