import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.imageio.ImageIO;

public class Menu extends Sequence  implements KeyListener {

	Camera cam = new Camera();
	private int stageID, stageLength;
	private winID win;

	enum winID {
		MENU,
		HOW_TO_PLAY,
		STAGE_SELECTION;

		private int toInt() {
			switch (this) {
			case MENU:
				return MENU.ordinal();
				//break;
			case HOW_TO_PLAY:
				return HOW_TO_PLAY.ordinal();
				//break;
			case STAGE_SELECTION:
				return STAGE_SELECTION.ordinal();
				//break;
			default:
				return MENU.ordinal();
			}
		}
	}

	Image [] bgs;

	private boolean leave;

	Menu(int stageLength) {
		this.stageLength = stageLength;
	}
	
	private void variableInit() {
		try {
		leave = false;
		this.stageID = 0;
		win = winID.MENU;
		bgs = new Image[winID.values().length];
		
		// read menu background images
			bgs[winID.MENU.ordinal()] = ImageIO.read(new File("resource/images/menu/menu.png"));
			bgs[winID.HOW_TO_PLAY.ordinal()] = ImageIO.read(new File("resource/images/menu/how_to_play.png"));
			bgs[winID.STAGE_SELECTION.ordinal()] = ImageIO.read(new File("resource/images/menu/stage_selection.png"));
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
	}

	public void myMain() {
		variableInit();
		this.requestFocusInWindow();
		super.seqInit(this);
		while(!leave) {
			try {
				Thread.sleep(1000 / Main.FPS); // the player is running keyboard events while the code is blocked here.
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}
		}
		super.seqEnd(this);
	}

	public int getStageID() {
		return stageID;
	}

	public void paint(Graphics graphics) {
		try {
			Graphics2D g = (Graphics2D) graphics;
			Font f = new Font("HGS–¾’©E", Font.ITALIC, 100);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(bgs[win.toInt()], 0, 0, this);
			
			if (win.toInt() == 2) {
				g.setFont(f);
				g.setColor(Color.red);
				g.drawString(String.valueOf(getStageID()+1), 570, 460);
				Thread.sleep(150);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		repaint();
	}


public void keyTyped(KeyEvent e) {
}

public void keyPressed(KeyEvent e) {
	System.out.println("Menu:" + e.getKeyCode());
	switch (win) {
	case MENU:
		switch (e.getKeyCode()) {
		case 39: // right
			win = winID.STAGE_SELECTION;
			this.repaint();
			break;
		case 37: // left
			System.exit(0); // QUIT
			break;
		case 40: // down
			win = winID.HOW_TO_PLAY; // HOW TO PLAY
			this.repaint();
			break;
			//case 38: // up
			//break;
		default:
			return;
		}
		break;

	case HOW_TO_PLAY:
		switch (e.getKeyCode()) {
		case 39: // right
			win = winID.STAGE_SELECTION;
			this.repaint();
			break;
		case 37: // left
			System.exit(0); // QUIT
			break;
			//case 40: // down
			//break;
			//case 38: // up
			//break;
		default:
			return;
		}
		break;

	case STAGE_SELECTION:
		switch (e.getKeyCode()) {
		case 39: // right
			leave = true; // leave menu (stage selected)
			// we could bring some calls to myMain here
			return;
			//break;
		case 37: // left
			win = winID.MENU;
			this.repaint();
			break;
		case 40: // down
			stageID = stageID == 0 ? stageLength - 1 : stageID - 1;
			// TODO draw stage number or miniature
			break;
		case 38: // up
			stageID = stageID == stageLength - 1 ? 0 : stageID + 1;
			// TODO draw stage number or miniature
			break;
		default:
			return;
		}
		break;

	default:
		return;
	}
}

public void keyReleased(KeyEvent e) {
}

}