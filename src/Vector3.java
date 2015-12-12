import static java.lang.Math.*;

public class Vector3 {

	public float x;
	public float y;
	public float z;

	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static Vector3 zero() {
		return new Vector3(0, 0, 0);
	}

	public void normalize() {
		float len = length();
		x /= len;
		y /= len;
		z /= len;
	}

	public void set(Vector3 o){
		this.x = o.x;
		this.y = o.y;
		this.z = o.z;
	}
	
	public Vector3 getNormalize() {
		float len = length();
		return new Vector3(x / len, y / len, z / len);
	}

	public float length() {
		return (float) (Math.sqrt(lengthSq()));
	}

	public float lengthSq() {
		return x * x + y * y + z * z;
	}

	public Vector3 add(Vector3 o) {
		return new Vector3(this.x + o.x, this.y + o.y, this.z + o.z);
	}

	public Vector3 sub(Vector3 o) {
		return new Vector3(this.x - o.x, this.y - o.y, this.z - o.z);
	}

	public Vector3 div(float b) {
		return new Vector3(this.x / b, this.y / b, this.z / b);
	}

	public Vector3 mult(float b) {
		return new Vector3(this.x * b, this.y * b, this.z * b);
	}
	public Vector3 mult(Vector3 o){
		return new Vector3(x * o.x, y * o.y, z * o.z);
	}

	public float dot(Vector3 o) {
		return x * o.x + y * o.y + z * o.z;
	}

	public Vector3 cross(Vector3 o) {
		return new Vector3(y * o.z - o.y * z, z * o.x - o.z * x, x * o.y - o.x * y);
	}
	
	public void rotateZ(float theta){
		x = (float) (x * cos(theta) - y * sin(theta));
		y = (float) (x * sin(theta) + y * cos(theta));
	}
	
	public void rotateY(float theta){
		x = (float) (x * cos(theta) + z * sin(theta));
		z = (float) (-x * sin(theta) + z * cos(theta));
	}
	
	public void rotateX(float theta){
		y = (float) (y * cos(theta) - z * sin(theta));
		z = (float) (y * sin(theta) + z * cos(theta));
	}

	public Vector3 getRotateZ(float theta){
		return new Vector3((float) (x * cos(theta) - y * sin(theta)), (float) (x * sin(theta) + y * cos(theta)), z);
	}
	
	public Vector3 getRotateY(float theta){
		return new Vector3((float) (x * cos(theta) + z * sin(theta)), y, (float) (-x * sin(theta) + z * cos(theta)));
	}
	
	public Vector3 getRotateX(float theta){
		return new Vector3(x, (float) (y * cos(theta) - z * sin(theta)), (float) (y * sin(theta) + z * cos(theta)));
	}
	public String toString() {
		return String.format("[%f, %f, %f]", x, y, z);
	}

}
