public class Camera {

	int x = 0, y = 0, // position
			w = 0, h = 0; // screen width, height

	// movement
	float speed = 1; // 0 < speed <= 1 = no delay

	public void resize(int w, int h) {
		this.w = w;
		this.h = h;
	}

	// camera movement
	public void move(Bar bar, Stage stage) {
		// camera tries to follow the target
		x += (int) ((bar.x - x) * speed);
		y += (int) ((bar.y - y) * speed);

		// camera boundaries
		x = x < w / 2 ? w / 2 : x + w / 2 > stage.w ? stage.w - w / 2 : x;
		y = y < h / 2 ? h / 2 : y + h / 2 > stage.h ? stage.h - h / 2 : y;
	}
}
