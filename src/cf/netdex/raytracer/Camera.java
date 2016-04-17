package cf.netdex.raytracer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import cf.netdex.raytracer.construct.Intersection;
import cf.netdex.raytracer.construct.Ray3;
import cf.netdex.raytracer.construct.Vec3;

public class Camera {

	public static final Vec3 DOWN = new Vec3(0, -1, 0);
	private static final boolean RENDER_SHADOWS = true;
	private static final double SHADOW_RENDER_EPSILON = 0.1;
	private static final boolean DITHER = false;
	private static final double FOV = 1.5;
	
	private World world;
	private Vec3 pos;
	private Vec3 forward;

	private int windowWidth;
	private int windowHeight;

	private int width;
	private int height;

	private BufferedImage bufferImage;
	private int[] renderBuffer;

	public Camera(World world, int windowWidth, int windowHeight, int width, int height, Vec3 pos,
			Vec3 dir) {
		this.world = world;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.width = width;
		this.height = height;

		this.pos = pos;
		forward = dir.getNormalize();

		GraphicsConfiguration gfx_config = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
		bufferImage = gfx_config.createCompatibleImage(windowWidth, windowHeight);
		renderBuffer = ((DataBufferInt) bufferImage.getRaster().getDataBuffer()).getData();
	}

	public Vec3 getDirection() {
		return forward;
	}

	public void render(Graphics2D g) {
		int buf = windowWidth / width;
		
		// Calculate the orientation of the camera
		Vec3 right = forward.cross(DOWN).getNormalize().mult(FOV);
		Vec3 up = forward.cross(right).getNormalize().mult(FOV);
		
		for (int y = 0; y < height; y += DITHER ? 2 : 1) {
			for (int x = 0; x < width; x += DITHER ? 2 : 1) {
				Color color = getTraceColor(new Ray3(pos, getPoint(x, y, right, up)));
				for (int k = 0; k < buf; k++) {
					for (int l = 0; l < buf; l++) {
						renderBuffer[(y * buf + k) * width * buf + (x * buf + l)] = color.getRGB();
					}
				}
			}
		}

		// Do some interpolation hacks to increase efficiency at the cost of aesthetic
		if (DITHER) {
			for (int y = 0; y < height; y++) {
				for (int x = 1; x < width - 1; x += 2) {
					int rgb1 = renderBuffer[(y * buf) * width * buf + ((x - 1) * buf)];
					int rgb2 = renderBuffer[(y * buf) * width * buf + ((x + 1) * buf)];
					int avg = getAverageColor(rgb1, rgb2);
					for (int k = 0; k < buf; k++) {
						for (int l = 0; l < buf; l++) {
							renderBuffer[(y * buf + k) * width * buf + (x * buf + l)] = avg;
						}
					}
				}
			}
			for (int y = 1; y < height - 1; y += 2) {
				for (int x = 0; x < width; x++) {
					int rgb1 = renderBuffer[((y - 1) * buf) * width * buf + (x * buf)];
					int rgb2 = renderBuffer[((y + 1) * buf) * width * buf + (x * buf)];
					int avg = getAverageColor(rgb1, rgb2);
					for (int k = 0; k < buf; k++) {
						for (int l = 0; l < buf; l++) {
							renderBuffer[(y * buf + k) * width * buf + (x * buf + l)] = avg;
						}
					}
				}
			}
		}
		g.drawImage(bufferImage, 0, 0, null);
	}

	public int getAverageColor(int a, int b) {
		int r1 = (a >> 16) & 0xFF;
		int g1 = (a >> 8) & 0xFF;
		int b1 = a & 0xFF;
		int r2 = (b >> 16) & 0xFF;
		int g2 = (b >> 8) & 0xFF;
		int b2 = b & 0xFF;
		int avg = (r1 + r2) / 2;
		avg = (avg << 8) + (g1 + g2) / 2;
		avg = (avg << 8) + (b1 + b2) / 2;
		return avg;
	}

	public Color getTraceColor(Ray3 ray) {
		ArrayList<Intersection> intersections = world.getIntersections(ray);
		if (intersections.size() == 0)
			return Color.BLUE;

		Intersection minIntersect = intersections.get(0);
		Color color = minIntersect.getIntersectionColor();
		if (RENDER_SHADOWS) {
			Vec3 shadowRayAim = world.getLightSource().sub(minIntersect.getPoint());
			Ray3 shadowRay = new Ray3(minIntersect.getPoint(), shadowRayAim);
			intersections = world.getIntersections(shadowRay);
			if (intersections.size() > 0) {
				minIntersect = intersections.get(0);
				if (minIntersect.getPoint().sub(shadowRay.pos).lengthSq() > SHADOW_RENDER_EPSILON
						* SHADOW_RENDER_EPSILON)
					color = color.darker();
			}
		}
		return color;
	}

	public Vec3 getPosition() {
		return pos;
	}

	private double recenterX(double x) {
		return (x - (width / 2.0f)) / (2.0f * width);
	}

	private double recenterY(double y) {
		return -(y - (height / 2.0f)) / (2.0f * height);
	}

	private Vec3 getPoint(double x, double y, Vec3 right, Vec3 up) {
		Vec3 reright = right.mult(recenterX(x));
		Vec3 reup = up.mult(recenterY(y));
		return forward.add(reright).add(reup).getNormalize();
	}
}
