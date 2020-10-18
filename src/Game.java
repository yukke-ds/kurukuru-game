import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;


public class Game extends Sequence {

	// drawing related
	private Graphics gv; // Graphics for buffering
	private Image offImage; // Image for buffering
	private Image fullHeart, empHeart;

	private boolean leave, isClear;

	private Camera cam;
	private KeyList keys;
	private Stage stage;
	private Bar bar;

	private int stageID;
			int heartCheck;

	private long startTime, eplapsedTime;
	private JLabel labelTime;

	Game(Stage stage) {
		bar = new Bar();
		keys = new KeyList(); // keys for the bar's movement
		cam = new Camera();
		this.stage = stage; // now this game obj holds the same Stage reference as the menu obj
		pDrawLabel();
	}

	private void variableInit() {
		try {
			isClear = false;
			leave = false;
			heartCheck = 0;

			cam.resize(Main.WIDTH, Main.HEIGHT);
			// lifepoint images
			fullHeart = ImageIO.read(new File("resource/images/heart/full-heart.png"));
			empHeart = ImageIO.read(new File("resource/images/heart/emp-heart.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void setStageID(int stageID) {
		this.stageID = stageID;
	}

	public String getStageName() {
		return stage.getStageName(stageID);
	}

	public boolean isClear() {
		return isClear;
	}

	public long getTime() {
		if (!leave) eplapsedTime = System.currentTimeMillis() - startTime;
		return eplapsedTime;
	}

	public void myMain() {
		variableInit();
		bar.variableInit();
		super.seqInit(keys);
		stage.load(stageID, bar);
		startTime = System.currentTimeMillis();

		while (!leave) {
			play();
			try {
				Thread.sleep(1000 / Main.FPS);
			} catch (InterruptedException e) {
				System.err.println("Exception: " + e.getMessage());
			}
		}
		super.seqEnd(keys);
	}

	private void play() {
		if (bar.goalCheck(stage)) {
			isClear = true;
			getTime(); // to update the time
			leave = true;
		}

		this.updateBuffer();
		bar.move(keys, stage);
		bar.collision(stage);
		if (bar.collided == bar.collided_max) bar.move(keys, stage); // don't let the bar move into a wall when it first collides.
		cam.move(bar, stage); // camera tries to follow the bar

		this.clear();
		stage.drawBelow(gv, cam, this);
		bar.draw(gv, cam);
		stage.drawAbove(gv, cam, this);
		drawHeart();

		// update time elapsed
		labelTime.setText(getCurrentTime(getTime()));

		this.repaint();
	}

	private void drawHeart() {
		heartCheck = bar.getHeart();
		switch (heartCheck) {
		case 0:
			for (int i = 1;i <= 5; i++) gv.drawImage(fullHeart, 0, 100*i, this);
			break;
		case 1:
			gv.drawImage(empHeart, 0, 100, this);
			for (int i = 2;i <= 5; i++) gv.drawImage(fullHeart, 0, 100*i, this);
			break;
		case 2:
			for (int i = 3;i <= 5; i++) gv.drawImage(fullHeart, 0, 100*i, this);
			for (int i = 1;i <= 2; i++) gv.drawImage(empHeart, 0, 100*i, this);
			break;
		case 3:
			for (int i = 4;i <= 5; i++) gv.drawImage(fullHeart, 0, 100*i, this);
			for (int i = 1;i <= 3; i++) gv.drawImage(empHeart, 0, 100*i, this);
			break;
		case 4:
			gv.drawImage(fullHeart, 0, 500, this);
			for (int i = 1;i <= 4; i++) gv.drawImage(empHeart, 0, 100*i, this);
			break;
		case 5:
			for (int i = 1;i <= 5; i++) gv.drawImage(empHeart, 0, 100*i, this);
			super.seqEnd(keys);
			isClear = false;
			leave = true;
			System.out.println("GAME OVER");
			break;
		default:
			break;
		}
	}

	private void pDrawLabel() { //currentTime、stageNameのラベル表示
		String time = getCurrentTime(getTime()); 
		Font fontLabel_32 = new Font("Arial", Font.PLAIN,32);
		//Labelの作成
		labelTime = new JLabel(time);
		addLabel(labelTime,	fontLabel_32, 0, 0, 150, 50);
	}

	private String getCurrentTime(long time){ //currentタイムを 分:秒 に直す関数
		String strTime;
		strTime = String.valueOf( String.format("%02d", time / 60000 )); //分
		strTime += ":";
		strTime +=  String.valueOf( String.format("%02d", (time % 60000) / 1000 )); //秒
		strTime += ":";
		strTime +=  String.valueOf( String.format("%02d", (time % 60000) % 1000 /10)); //小数点第２位まで表示（少数第3位は切り捨て）
		return strTime;
	}
	private void addLabel(JLabel label,Font font,int x,int y,int width,int height){
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(font);
		label.setBounds(x,y,width,height);
		super.f_frame.getContentPane().add(label);
	}

	// Paint

	// clear drawings
	private void clear() {
		// clears the entire screen
		gv.clearRect(0, 0, cam.w, cam.h);
	}
	public void update(Graphics g) {
		paint(g);
	}
	// buffer for flickering
	private void updateBuffer() {
		offImage = createImage(cam.w, cam.h);
		gv = offImage.getGraphics();
		((Graphics2D) gv).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void paint(Graphics graphics) {
		graphics.drawImage(offImage, 0, 0, cam.w, cam.h, this);
		//graphics.drawString("GAME OVER", x, y);
	}


}