package cf.netdex.raytracer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cf.netdex.raytracer.construct.Ray3;
import cf.netdex.raytracer.construct.Vec3;
import cf.netdex.raytracer.primitive.Box;
import cf.netdex.raytracer.primitive.Plane;
import cf.netdex.raytracer.primitive.Sphere;
import cf.netdex.raytracer.primitive.Triangle;

public class Raytracer extends JFrame {

	public Raytracer() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);

		this.add(new RaytracerPanel());
	}

	public static void main(String[] args) {
		Raytracer r = new Raytracer();
		r.pack();
		r.setVisible(true);
	}

	static class RaytracerPanel extends JPanel {

		public static final int WIDTH = 1024;
		private static final int HEIGHT = 768;

		private static final double MOUSE_SENSITIVITY = 0.0025f;
		private static final Vec3 MOVE_SPEED = new Vec3(1.5, 0, 1.5);
		private static final int SCALE_FACTOR = 4;
		
		private boolean[] KEY_STATE = new boolean[256];

		private boolean pause = false;

		private World world;
		private Camera camera;
		private Robot robot;

		public RaytracerPanel() {
			this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
			this.setFocusable(true);
			try {
				robot = new Robot();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Get rid of the ugly ass cursor in the middle of the screen
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg,
					new Point(0, 0), "blank cursor");
			this.setCursor(blankCursor);

			world = new World();
			world.addIntersect(
					new Plane(Color.GREEN, new Ray3(new Vec3(0, 0, 0), new Vec3(0, -1, 0))));
			world.addIntersect(new Triangle(Color.CYAN, new Vec3(10, 0, 50), new Vec3(-15, 0, 60),
					new Vec3(0, 20, 50)));
			world.aRectangle(new Vec3(100, 100, 0), new Vec3(100, 0, 100));
			
			world.addIntersect(new Sphere(new Vec3(30, 30, 30), 15));
			for(int i = 0; i < 15; i++)
			world.addIntersect(new Box(new Vec3(0 + 15 * i,0 + 15 * i,0), new Vec3(10 + 15 * i,10 + 15 * i,10)));
			camera = new Camera(world, WIDTH, HEIGHT, WIDTH / SCALE_FACTOR, HEIGHT / SCALE_FACTOR, new Vec3(0, 5, 0),
					new Vec3(1, 0, 0));

			new Thread() {
				public void run() {
					while (true) {
						Vec3 dir = camera.getDirection();
						Vec3 pos = camera.getPosition();
						Vec3 rot = dir.mult(MOVE_SPEED).getNormalize();
						
						try {
							if (KEY_STATE[KeyEvent.VK_W]) {
								pos.set(pos.add(rot));
							}
							if (KEY_STATE[KeyEvent.VK_S]) {
								pos.set(pos.sub(rot));
							}
							if (KEY_STATE[KeyEvent.VK_A]) {
								pos.set(pos.sub(rot.getRotateY((double) (Math.PI / 2))));
							}
							if (KEY_STATE[KeyEvent.VK_D]) {
								pos.set(pos.sub(rot.getRotateY(-(double) (Math.PI / 2))));
							}
							if(KEY_STATE[KeyEvent.VK_SPACE]){
								pos.set(pos.add(new Vec3(0,1,0)));
							}
							if(KEY_STATE[KeyEvent.VK_SHIFT]){
								pos.set(pos.add(new Vec3(0,-1,0)));
							}
							
							repaint();
							Thread.sleep(16);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

			this.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent event) {
					int keycode = event.getKeyCode();
					KEY_STATE[keycode] = true;
					if (keycode == KeyEvent.VK_ESCAPE) {
						pause = !pause;
					}
				}

				public void keyReleased(KeyEvent event) {
					int keycode = event.getKeyCode();
					KEY_STATE[keycode] = false;
				}
			});

			this.addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseMoved(MouseEvent event) {
					Point p = event.getPoint();
					Point loc = RaytracerPanel.this.getLocationOnScreen();
					Vec3 dir = camera.getDirection();
					Point center = getCenter();
					int dx = p.x - center.x;
					int dy = p.y - center.y;
					
					if (!pause) {
						dir.rotateY(MOUSE_SENSITIVITY * dx);
						dir.rotateAboutAxis(dir.cross(Camera.DOWN), MOUSE_SENSITIVITY * dy);
						dir.normalize();
						robot.mouseMove(loc.x + center.x,
								loc.y + center.y);
					}
				}
			});
		}

		public Point getCenter() {
			Dimension size = this.getSize();
			return new Point((int) (size.getWidth() / 2), (int) (size.getHeight() / 2));
		}

		private final Font DEBUG_FONT = new Font(Font.MONOSPACED, Font.BOLD, 15);

		public void render(Graphics2D g) {
			camera.render(g);

			g.setColor(Color.WHITE);
			g.setFont(DEBUG_FONT);
			String[] debug = { "CAMERA_POS: " + camera.getPosition(),
					"CAMERA_DIR: " + camera.getDirection(),
					"ROT_VEC: " + camera.getDirection().getRotationVector().toDegrees(),
					"POLY_COUNT: " + world.getIntersectables().size(),
					"PAUSED: " + pause};
			for (int i = 0; i < debug.length; i++) {
				g.drawString(debug[i], 10, 20 + 20 * i);
			}
		}

		@Override
		public void paintComponent(Graphics gr) {
			super.paintComponent(gr);
			Graphics2D g = (Graphics2D) gr;
			// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			// RenderingHints.VALUE_ANTIALIAS_ON);
			render(g);
		}
	}

}
