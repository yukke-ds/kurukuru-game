import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**************************************************************
 *
 * @author TANABE
 *
 * �H�R��
 *
 * 54,55,62,74,173�ȍ~,��ύX���܂����B
 * ����ɉ����Apublic �� private �̕ύX�� getTopRecord() ���\�b�h�̒ǉ������܂����B
 *
 * getTopRecord()�͈�����stageID������Ƃ��̃X�e�[�W��top�̋L�^��return���郁�\�b�h�ł��B 
 * long ??? = Ranking.getTopRecord(stageID); ���Ă��Ύg���܂����A�g������Ranking��new���Ȃ��Ⴂ���Ȃ��Ȃ����Ⴄ�B�B�B
 * rankingData��static��t����̂͂��܂����Ăق����Ȃ��ł��B�i�O�������ranking�̏���������h�����߁j
 * 
 * ����ƁA���s���ɕ\�����ς����ranking���\�����ꂽ�肳��Ȃ������肷��̂͂Ȃ�ł��낤�B
 * makeLabel()���������Ǝv���񂾂��Ǐ������ŁA���ꂪ�����������ĂƂ��낪�������狳���Ăق����B
 * ���s�͕ʃN���X��Ranking�̃R���X�g���N�^�������myMain()�����s���Ă��܂��B
 *�@�m�F�����肢���܂��B
 *
 ***************************************************************/

public class Ranking extends Sequence implements ActionListener { //��extends Sequence�ɕύX(JFarme�͎g�����)

	public Ranking() { //���R���X�g���N�^�ƒl�̑�����@��ύX
		myInit();
		printWindow();   //  ��printWindow���\�b�h�̒ǉ�
	}

	// ArrayList�̎g�����Fhttp://www.tohoho-web.com/java/collection.htm
	ArrayList<Long> rankingData = new ArrayList<Long>();
	private long currentTime;
	private int next;
	public JButton button;
	public int stageID;
	final int NUMBER_OF_STAGE = 9;
	private Container container;  //   ��container�̒ǉ�
	private boolean leave;

	public void myMain() { //��Main�֐�����Ă΂��myMain�֐��̒ǉ��i��:test.java�j
		readRanking();
		setRanking(stageID, currentTime);
		writeRanking();

		super.seqInit(null);
		updateLabel();
		leave = false;
		while(!leave) {
			try {
				Thread.sleep(1000 / Main.FPS);
			} catch (InterruptedException e) {
				System.err.println("Exception: " + e.getMessage());
			}
		}
		super.seqEnd(null);
		System.out.println("Ranking End");
	}

	// ArrayList�̏�����
	// currentTime�ɂǂ̂悤�Ȓl�����邩�ɂ���āu10000L�v��ύX
	private void myInit() {
		for (int i = 0; i < NUMBER_OF_STAGE; i++) {
			rankingData.add(i, -1L);
		}
	}

	public void setValues(int stageID, long currentTime) {
		this.stageID = stageID;
		this.currentTime = currentTime;
	}

	// �����Ɏw�肳�ꂽ�X�e�[�W��1�ʂ̋L�^��Ԃ����\�b�h
	public long getTopRecord(int stageID) {
		return rankingData.get(3 * (stageID - 1));
	}

	//rankingData�ƈ������r���A���3�ʂɓ���Ώ��������X�V���郁�\�b�h
	private void setRanking(int stageID, long currentTime) {

		for (int i = 0; i < 3; i++) {
			if(rankingData.get((stageID * 3) + i) >= currentTime || rankingData.get((stageID * 3) + i) == -1){
				switch(i) {
				case 0:
					rankingData.set((stageID * 3) + 2, rankingData.get((stageID * 3) + 1));
					rankingData.set((stageID * 3) + 1, rankingData.get(stageID * 3));
					rankingData.set((stageID * 3) + i, currentTime);
					break;
				case 1:
					rankingData.set((stageID * 3) + 2, rankingData.get((stageID * 3) + 1));
					rankingData.set((stageID * 3) + i, currentTime);
					break;
				case 2:
					rankingData.set((stageID * 3) + i, currentTime);
					break;
				}
				break;
			} else {
			}
		}
	}

	//ranking�̒l���󂯎�郁�\�b�h
	private ArrayList<Long> getRanking() {
		return this.rankingData;
	}

	//fail��ranking���i�[���郁�\�b�h
	private void writeRanking() {
		try {
			// �t�@�C���̃I�[�v��
			PrintWriter pw = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(
							new File("./RankingDB.csv"), false), "Shift_JIS")));
			// �f�[�^�̒ǉ�
			for (int i = 0; i < NUMBER_OF_STAGE - 1; i++) {
				pw.print(rankingData.get(i) + ",");
			}
			pw.println(rankingData.get(NUMBER_OF_STAGE - 1) + "");
			pw.close();

		} catch (FileNotFoundException e) {
			// File�I�u�W�F�N�g�������̗�O�⑫
			e.printStackTrace();
		} catch (IOException e) {
			// BufferedWriter�I�u�W�F�N�g�̃N���[�Y���̗�O�⑫
			e.printStackTrace();
		}

	}

	//fail����ranking�����o���ArankingData�Ɋi�[���郁�\�b�h
	private void readRanking() {
		try {
			// �t�@�C����ǂݍ���
			FileReader fr = new FileReader("./RankingDB.csv");
			BufferedReader br = new BufferedReader(fr);

			// �ǂݍ��񂾃t�@�C�����P�s����������
			String line;
			StringTokenizer token;
			while ((line = br.readLine()) != null) {
				// ��؂蕶��","�ŕ�������
				token = new StringTokenizer(line, ",");

				// ��������������rankingData�Ɋi�[����
				int j = 0;
				while (token.hasMoreTokens()) {
					rankingData.set(j, Long.parseLong(token.nextToken()));
					j += 1;
				}
			}
			br.close();
		} catch (IOException ex) {
			// ��O����������
			ex.printStackTrace();
		}
	}


	//next�ɒl���i�[���郁�\�b�h
	public void setNext(int next) {
		this.next = next;
	}

	//next�̒l���󂯎�郁�\�b�h
	public int getNext() {
		return this.next;
	}

	// ----------------------------�ȉ��ǉ�����-----------------------------
	// Result���Q�l�ɂ��č�����̂Ŋ�{�I�ȏ�����Result�ƈꏏ�i�g���Ă郁�\�b�h�����������ꏏ�j

	// �E�B���h�E���쐬���郁�\�b�h
	private void printWindow() {
		initFrame();
		makeLabel();
		makeButtonOK();
	}

	// �t���[���̏�����
	private void initFrame() {
		container = super.f_frame.getContentPane();
		container.setLayout(null);
	}
	//�@OK�̃{�^���쐬
	private void makeButtonOK() {
		int buttonSizeX = 80;
		int buttonSizeY = 30;
		JButton buttonOK = new JButton("OK");
		buttonOK.addActionListener(this);
		buttonOK.setBounds(Main.WIDTH/2-buttonSizeX/2, 625, buttonSizeX, buttonSizeY);
		super.f_frame.getContentPane().add(buttonOK);
	}

	// rankingData�ɒl�������Ă��邩�̃`�F�b�N
	private boolean getIsExist(long rankingData) {
		if (rankingData == -1L) {
			return false;
		} else {
			return true;
		}
	}

	//Label�̍쐬
	private JLabel labelTitle   = new JLabel();
	JLabel labelStage1          = new JLabel();
	JLabel labelStage2          = new JLabel();
	JLabel labelStage3          = new JLabel();
	JLabel []labelTime; 

	// Label�̍쐬
	private void makeLabel(){

		Font fontLabel_32 = new Font("Arial", Font.PLAIN,32);
		Font fontLabel_50= new Font("Arial", Font.PLAIN,50);
		Font fontLabel_100= new Font("Arial", Font.PLAIN,100);


		addLabel(labelTitle, fontLabel_100, 0, 35, Main.WIDTH, 100);
		labelTime = new JLabel[NUMBER_OF_STAGE];

		// TODO fix label showing/hiding
		for (int i = 0; i < NUMBER_OF_STAGE; i++) {
			int j;
			if (0 <= i && i <= 2) {
				j = -345;
				addLabel(labelStage1, fontLabel_32, j, 35, Main.WIDTH, 300);
			} else if (3 <= i && i <= 5) {
				j = 0;
				addLabel(labelStage2, fontLabel_32, j, 35, Main.WIDTH, 300);
			} else {
				j = 345;
				addLabel(labelStage3, fontLabel_32, j, 35, Main.WIDTH, 300);
			}
			labelTime[i] = new JLabel();
			addLabel(labelTime[i], fontLabel_50, j, 120 + ((i % 3) * 120), Main.WIDTH, 300);
		}
	}

	private void updateLabel() {
		String noTime = " -- : -- ";
		labelTitle.setText("RANKING");
		labelStage1.setText("EASY");
		labelStage2.setText("NORMAL");
		labelStage3.setText("HARD");
		for (int i = 0; i < NUMBER_OF_STAGE; i++){
			labelTime[i].setText(getIsExist(rankingData.get(i)) ? getCurrentTime(rankingData.get(i)) : noTime);
		}

	}

	// Label���t���[���ɃZ�b�g���郁�\�b�h 
	private void addLabel(JLabel label, Font font, int x, int y, int width, int height) {
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(font);
		label.setBounds(x, y, width, height);
		container.add(label);
	}

	// current�^�C���� ��:�b �ɒ����֐�
	private String getCurrentTime(long time) {
		String strTime;
		strTime = String.valueOf( String.format("%02d", time / 60000 )); //��
		strTime += " ' ";
		strTime +=  String.valueOf( String.format("%02d",(time % 60000) / 1000 )); //�b
		strTime += " \" ";
		strTime +=  String.valueOf( String.format("%02d",(time % 60000) % 1000 /10)); //�����_��Q�ʂ܂ŕ\���i������3�ʂ͐؂�̂āj
		return strTime;
	}

	// OK�{�^���������ꂽ���̏����A�܂�Ranking�ւ̃V�[�P���X����
	public void actionPerformed(ActionEvent e) {
		leave = true;
	}



	// -----------------------------�ȏ�ǉ�����--------------------------------------


}