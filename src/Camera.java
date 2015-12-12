import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

public class Camera {

	private static final Vector3 DOWN = new Vector3(0, -1, 0);

	private World world;
	private Vector3 pos;
	private Vector3 forward;

	private int windowWidth;
	private int windowHeight;

	private int width;
	private int height;

	private BufferedImage bufferImage;
	private int[] renderBuffer;
	
	public Camera(World world, int windowWidth, int windowHeight, int width, int height, Vector3 pos, Vector3 dir) {
		this.world = world;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.width = width;
		this.height = height;

		this.pos = pos;
		forward = dir.getNormalize();
		
		bufferImage = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
		renderBuffer = ((DataBufferInt) bufferImage.getRaster().getDataBuffer()).getData();
	}
	
	public Vector3 getDirection(){
		return forward;
	}
	
	public void render(Graphics2D g) {
		int buf = windowWidth / width;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = getTraceColor(new Ray3(pos, getPoint(x, y)));
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				for (int k = 0; k < buf; k++) {
					for (int l = 0; l < buf; l++) {
						renderBuffer[(y * buf + k) * width* buf + (x * buf + l)] = red << 16 | green << 8 | blue;
					}
				}
			}
		}
		g.drawImage(bufferImage, 0, 0, null);
	}

	public Color getTraceColor(Ray3 ray) {
		ArrayList<Intersection> intersections = world.getIntersections(ray);
		Intersection min = null;
		float minDist = Float.MAX_VALUE;
		for (Intersection intersect : intersections) {
			float d = intersect.getDistance();
			if (d < minDist) {
				minDist = d;
				min = intersect;
			}
		}
		if(min == null)
			return Color.BLUE;
		return min.getIntersectionColor();
	}

	public Vector3 getPosition() {
		return pos;
	}

	private float recenterX(float x) {
		return (x - (width / 2.0f)) / (2.0f * width);
	}

	private float recenterY(float y) {
		return -(y - (height / 2.0f)) / (2.0f * height);
	}

	private Vector3 getPoint(float x, float y) {
		Vector3 right = forward.cross(DOWN).getNormalize().mult(1.5f);
		Vector3 up = forward.cross(right).getNormalize().mult(1.5f);
		Vector3 reright = right.mult(recenterX(x));
		Vector3 reup = up.mult(recenterY(y));
		return (forward.add(reright).add(reup)).getNormalize();
	}
}
