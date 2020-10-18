import java.awt.*;
import java.awt.geom.*;

public class Bar {

	int vx = 22, vy = 22, // velocity
			len = 80, // length
			thick = 20, // thickness
			spin = 12, // rotation angular velocity
			collision_points = 5, // number of tests in each side of the bar
			collided_max = 10; // how long the bar is pushed back
	int x = 0, y = 0, // position
			vx_old = 0, vy_old = 0, // the velocity before we collided
			rot = 0, // rotation angle
			collided = 0, // counter
			heartCheck = 0; // lifepoint

	void variableInit() {
		x = 0; y = 0; // position
		vx_old = 0; vy_old = 0; // the velocity before we collided
		rot = 0; // rotation angle
		collided = 0; // counter
		heartCheck = 0; // lifepoint
	}

	// bar movement
	void move(KeyList keys, Stage stage) {
		if (collided == 0) { // movement when not collided
			vx_old = keys.right ? vx : keys.left ? -vx : 0;
			vy_old = keys.up ? vy : keys.down ? -vy : 0;

			// change position
			x += vx_old;
			y -= vy_old;

			// change rotation
			rot += spin; 
		} else { // if the bar is pushed back by the collision
			x -= vx_old * collided / collided_max;
			y += vy_old * collided / collided_max;
			rot -= spin; // bar backwards rotation
		}

		// bar boundaries (the should not disappear from the screen)
		x = x < len + thick ? len + thick : x > stage.w - thick - len ? stage.w - thick - len : x;
		y = y < len + thick ? len + thick : y > stage.h - thick - len ? stage.h - thick - len : y;

	}

	void collision(Stage stage) {
		// if we are being pushed back from a collision, we don't need to check for more collisions
		if (collided > 0) {
			collided--;
			return;
		}

		double rad = Math.toRadians(rot); // rotation in radians
		float len2 = len / collision_points; // each point has it's distance from the center of the bar
		boolean new_collision = stage.test_collision(x, y); // tests the middle point
		// check for one side of the bar
		for (int i = 0; i < collision_points && !new_collision; i++) {
			new_collision = stage.test_collision(x + (int)(len2 * (i + 1) * Math.cos(rad)), y + (int)(len2 * (i + 1) * Math.sin(rad)));
		}

		// and check for the other side
		for (int i = 0; i < collision_points && !new_collision; i++) {
			new_collision = stage.test_collision(x - (int)(len2 * (i + 1) * Math.cos(rad)), y - (int)(len2 * (i + 1) * Math.sin(rad)));
		}

		if (new_collision) {			
			heartCheck ++;
			collided = collided_max;
			//System.out.println("collided");

		}
	}

	public int getHeart() {
		return this.heartCheck;
	}

	boolean goalCheck(Stage stage) {
		return stage.test_collision(x, y, 0xFF0000FF); // searches for blue
	}

	void draw(Graphics gv, Camera cam) {
		// we rotate the coordinates points, then we draw the rectangle
		double rad = Math.toRadians(rot);
		int pax = x + (int)(len * Math.cos(rad)),
				pay = y + (int)(len * Math.sin(rad)),
				pbx = x - (int)(len * Math.cos(rad)),
				pby = y - (int)(len * Math.sin(rad));

		Graphics2D gv2 = (Graphics2D) gv;
		gv2.setPaint(Color.gray);
		gv2.setStroke(new BasicStroke(thick));
		gv2.draw(new Line2D.Double(pax - cam.x + cam.w / 2, pay - cam.y + cam.h / 2, pbx - cam.x + cam.w / 2, pby - cam.y + cam.h / 2));
	}
}