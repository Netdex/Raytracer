import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

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

	class RaytracerPanel extends JPanel {

		public static final int WIDTH = 1600;
		private static final int HEIGHT = 900;

		private static final float MOUSE_SENSITIVITY = 0.0025f;
		private static final float MOVE_SPEED = 1.5f;

		private boolean[] KEY_STATE = new boolean[256];

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
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg,
					new Point(0, 0), "blank cursor");
			this.setCursor(blankCursor);

			world = new World();
			
			world.addIntersect(new Plane(Color.GREEN, new Ray3(new Vector3(0, -10, 0), new Vector3(0, -1, 0))));

			camera = new Camera(world, WIDTH, HEIGHT, WIDTH / 8, HEIGHT / 8, Vector3.zero(),
					new Vector3(1, 0, 0));

			new Thread() {
				public void run() {
					while (true) {
						Vector3 dir = camera.getDirection();
						Vector3 pos = camera.getPosition();
						Vector3 rot = dir.mult(new Vector3(MOVE_SPEED, MOVE_SPEED, MOVE_SPEED));
						System.out.println(pos + " " + dir);
						try {
							if (KEY_STATE[KeyEvent.VK_W]) {
								pos.set(pos.add(rot));
							}
							if (KEY_STATE[KeyEvent.VK_S]) {
								pos.set(pos.sub(rot));
							}
							if (KEY_STATE[KeyEvent.VK_A]) {
								pos.set(pos.sub(rot.getRotateY((float) (Math.PI / 2))));
							}
							if (KEY_STATE[KeyEvent.VK_D]) {
								pos.set(pos.sub(rot.getRotateY(-(float) (Math.PI / 2))));
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
				}

				public void keyReleased(KeyEvent event) {
					int keycode = event.getKeyCode();
					KEY_STATE[keycode] = false;
				}
			});

			this.addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseMoved(MouseEvent event) {
					Point p = event.getPoint();

					Vector3 dir = camera.getDirection();
					Point center = getCenter();

					int dx = p.x - center.x;
					int dy = p.y - center.y;

					dir.rotateY(MOUSE_SENSITIVITY * dx);

					robot.mouseMove(center.x + 3, center.y);
					
				}
			});
		}

		public Point getCenter() {
			Point loc = this.getLocation();
			Dimension size = this.getSize();
			return new Point((int) (loc.x + size.getWidth() / 2),
					(int) (loc.y + size.getHeight() / 2));
		}

		public void render(Graphics2D g) {
			camera.render(g);
		}

		@Override
		public void paintComponent(Graphics gr) {
			super.paintComponent(gr);
			Graphics2D g = (Graphics2D) gr;
			//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			render(g);
		}
	}

}
