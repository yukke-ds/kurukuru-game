import javax.swing.JApplet;
import javax.swing.JFrame;
import java.awt.event.KeyListener;

public abstract class Sequence extends JApplet {

	public abstract void myMain();

	public JFrame f_frame = new JFrame("KuruKuru"); //?
	Main f_main = Main.getMain();
	/*public void seqMain(){
		seqInit();
		myMain();
	}*/

	protected void seqInit(KeyListener keyListener) {
		f_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f_frame.setBounds((f_frame.getToolkit().getScreenSize().width/2)-Main.WIDTH/2,
				(f_frame.getToolkit().getScreenSize().height/2)- Main.HEIGHT/2, Main.WIDTH, Main.HEIGHT);
		f_frame.add(this);
		f_frame.setVisible(true);
		if (keyListener != null) {
			this.addKeyListener(keyListener);
			this.setFocusable(true); // keyboard focus
			//this.requestFocusInWindow();
		}
		f_frame.setLayout(null);
		System.out.println("seqInit");
	}

	protected void seqEnd(KeyListener keyListener) {
		if (keyListener != null) {
			this.removeKeyListener(keyListener);
			this.setFocusable(false); // keyboard focus
		}
		f_frame.setVisible(false);
		f_frame.remove(this);
		System.out.println("Sequence End");
	}

	/*protected void sequence(Main.SeqID seqId){
		f_main.setNext(seqId);
		seqEnd();
		f_main.myMain();
	}*/

	/*protected void sequence(Main.SeqID seqId,int stageId){
		f_main.setStageID(stageId);
		sequence(seqId);
	}

	protected void sequence(Main.SeqID seqId,long currentTime,boolean isClear){
		f_main.setCurrentTime(currentTime);
		f_main.setIsClear(isClear);
		sequence(seqId);
	}*/
}
