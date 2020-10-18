import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Result extends Sequence implements ActionListener{

	private String f_stageName;
	private boolean f_isClear;
	private boolean leave;
	private long f_currentTime;

	//----------------------------------------------���������֐���----------------------------------------------//

	Result(){ //�󂯎����Main��Result�̃t�B�[���h�ϐ���f_main�֊i�[
		super.f_frame.getContentPane().setLayout(null);
		pDrawLabel(); //currentTime�AstageName�̃��x���\��
		pDrawButton(); //OK�̃{�^���쐬
	}

	//----------------------------------------------���������֐���----------------------------------------------//

	public void setValues(String stageName, boolean isClear, long currentTime) {
		f_stageName = stageName;
		f_isClear = isClear;
		f_currentTime = currentTime;
	}

	//----------------------------------------------�����C��������----------------------------------------------//

	public void myMain() { //Main����Ă΂�郁�\�b�h
		super.seqInit(null);
		pUpdateLabels();
		leave = false;
		while(!leave) {
			try {
				Thread.sleep(1000 / Main.FPS);
			} catch (InterruptedException e) {
				System.err.println("Exception: " + e.getMessage());
			}
		}
		super.seqEnd(null);
	}

	//Label�̍쐬
	private JLabel labelGameClearOrOver = new JLabel(),
			labelCurrentTime = new JLabel(),
			labelStage = new JLabel(),
			labelTime = new JLabel();

	private void pDrawLabel(){ //currentTime�AstageName�̃��x���\��
		Font fontLabel_32 = new Font("Arial",Font.PLAIN,32);
		Font fontLabel_100= new Font("Arial",Font.PLAIN,100);
		Font fontLabel_132= new Font("Arial",Font.PLAIN,132);


		addLabel(labelStage,		  	fontLabel_32,	0,35,Main.WIDTH,100);
		addLabel(labelGameClearOrOver,	fontLabel_100,	0,161,Main.WIDTH,100);
		addLabel(labelTime,				fontLabel_32,	0,317,Main.WIDTH,100);
		addLabel(labelCurrentTime,		fontLabel_132,	0,407,Main.WIDTH,100);
	}

	private void pUpdateLabels() {
		String sGC = getCurrentTime(f_currentTime); //getCurrentTime:current�^�C���� ��:�b �ɒ����֐�  sGC = StringGameClear
		String sGO = " -- : -- "; //sGO = StringGameOver �Q�[���I�[�o�[�̏ꍇ�\������
		String stage = "STAGE " + f_stageName;
		labelGameClearOrOver.setText(f_isClear ? "GAME CLEAR" : "GAME OVER");
		labelCurrentTime.setText(f_isClear ? sGC : sGO);
		labelStage.setText(stage);
		labelTime.setText("TIME");
	}

	private void addLabel(JLabel label, Font font, int x, int y, int width, int height) {
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(font);
		label.setBounds(x,y,width,height);
		super.f_frame.getContentPane().add(label);
	}

	private String getCurrentTime(long time){ //current�^�C���� ��:�b �ɒ����֐�
		String strTime;
		strTime = String.valueOf( String.format("%02d", time / 60000 )); //��
		strTime += " ' ";
		strTime +=  String.valueOf( String.format("%02d",(time % 60000) / 1000 )); //�b
		strTime += " \" ";
		strTime +=  String.valueOf( String.format("%02d",(time % 60000) % 1000 /10)); //�����_��Q�ʂ܂ŕ\���i������3�ʂ͐؂�̂āj
		return strTime;
	}

	private void pDrawButton(){ //OK�̃{�^���쐬
		int buttonSizeX = 80;
		int buttonSizeY = 30;
		JButton buttonOK = new JButton("OK");
		buttonOK.addActionListener(this);
		buttonOK.setBounds(Main.WIDTH/2-buttonSizeX/2,625,buttonSizeX,buttonSizeY);
		super.f_frame.getContentPane().add(buttonOK);
	}

	//----------------------------------------------�����C��������----------------------------------------------//

	//----------------------------------------------���{�^���������ꂽ���̏�����----------------------------------------------//

	public void actionPerformed(ActionEvent e){ //OK�{�^���������ꂽ���̏����A�܂�Ranking�ւ̃V�[�P���X����
		leave = true;
	}

	//----------------------------------------------���{�^���������ꂽ���̏�����----------------------------------------------//

}