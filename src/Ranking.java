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
 * 秋山へ
 *
 * 54,55,62,74,173以降,を変更しました。
 * それに加え、public → private の変更と getTopRecord() メソッドの追加をしました。
 *
 * getTopRecord()は引数にstageIDを入れるとそのステージのtopの記録をreturnするメソッドです。 
 * long ??? = Ranking.getTopRecord(stageID); ってやれば使えますが、使う側がRankingをnewしなきゃいけなくなっちゃう。。。
 * rankingDataにstaticを付けるのはあまりやってほしくないです。（外部からのrankingの書き換えを防ぐため）
 * 
 * それと、実行毎に表示が変わってrankingが表示されたりされなかったりするのはなんでだろう。
 * makeLabel()が原因だと思うんだけど書き方で、これがおかしいってところがあったら教えてほしい。
 * 実行は別クラスでRankingのコンストラクタを作ってmyMain()を実行しています。
 *　確認をお願いします。
 *
 ***************************************************************/

public class Ranking extends Sequence implements ActionListener { //■extends Sequenceに変更(JFarmeは使えるよ)

	public Ranking() { //■コンストラクタと値の代入方法を変更
		myInit();
		printWindow();   //  ☆printWindowメソッドの追加
	}

	// ArrayListの使い方：http://www.tohoho-web.com/java/collection.htm
	ArrayList<Long> rankingData = new ArrayList<Long>();
	private long currentTime;
	private int next;
	public JButton button;
	public int stageID;
	final int NUMBER_OF_STAGE = 9;
	private Container container;  //   ☆containerの追加
	private boolean leave;

	public void myMain() { //■Main関数から呼ばれるmyMain関数の追加（旧:test.java）
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

	// ArrayListの初期化
	// currentTimeにどのような値が入るかによって「10000L」を変更
	private void myInit() {
		for (int i = 0; i < NUMBER_OF_STAGE; i++) {
			rankingData.add(i, -1L);
		}
	}

	public void setValues(int stageID, long currentTime) {
		this.stageID = stageID;
		this.currentTime = currentTime;
	}

	// 引数に指定されたステージの1位の記録を返すメソッド
	public long getTopRecord(int stageID) {
		return rankingData.get(3 * (stageID - 1));
	}

	//rankingDataと引数を比較し、上位3位に入れば書き換え更新するメソッド
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

	//rankingの値を受け取るメソッド
	private ArrayList<Long> getRanking() {
		return this.rankingData;
	}

	//failにrankingを格納するメソッド
	private void writeRanking() {
		try {
			// ファイルのオープン
			PrintWriter pw = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(
							new File("./RankingDB.csv"), false), "Shift_JIS")));
			// データの追加
			for (int i = 0; i < NUMBER_OF_STAGE - 1; i++) {
				pw.print(rankingData.get(i) + ",");
			}
			pw.println(rankingData.get(NUMBER_OF_STAGE - 1) + "");
			pw.close();

		} catch (FileNotFoundException e) {
			// Fileオブジェクト生成時の例外補足
			e.printStackTrace();
		} catch (IOException e) {
			// BufferedWriterオブジェクトのクローズ時の例外補足
			e.printStackTrace();
		}

	}

	//failからrankingを取り出し、rankingDataに格納するメソッド
	private void readRanking() {
		try {
			// ファイルを読み込む
			FileReader fr = new FileReader("./RankingDB.csv");
			BufferedReader br = new BufferedReader(fr);

			// 読み込んだファイルを１行ずつ処理する
			String line;
			StringTokenizer token;
			while ((line = br.readLine()) != null) {
				// 区切り文字","で分割する
				token = new StringTokenizer(line, ",");

				// 分割した文字をrankingDataに格納する
				int j = 0;
				while (token.hasMoreTokens()) {
					rankingData.set(j, Long.parseLong(token.nextToken()));
					j += 1;
				}
			}
			br.close();
		} catch (IOException ex) {
			// 例外発生時処理
			ex.printStackTrace();
		}
	}


	//nextに値を格納するメソッド
	public void setNext(int next) {
		this.next = next;
	}

	//nextの値を受け取るメソッド
	public int getNext() {
		return this.next;
	}

	// ----------------------------以下追加項目-----------------------------
	// Resultを参考にして作ったので基本的な処理はResultと一緒（使ってるメソッドもだいたい一緒）

	// ウィンドウを作成するメソッド
	private void printWindow() {
		initFrame();
		makeLabel();
		makeButtonOK();
	}

	// フレームの初期化
	private void initFrame() {
		container = super.f_frame.getContentPane();
		container.setLayout(null);
	}
	//　OKのボタン作成
	private void makeButtonOK() {
		int buttonSizeX = 80;
		int buttonSizeY = 30;
		JButton buttonOK = new JButton("OK");
		buttonOK.addActionListener(this);
		buttonOK.setBounds(Main.WIDTH/2-buttonSizeX/2, 625, buttonSizeX, buttonSizeY);
		super.f_frame.getContentPane().add(buttonOK);
	}

	// rankingDataに値が入っているかのチェック
	private boolean getIsExist(long rankingData) {
		if (rankingData == -1L) {
			return false;
		} else {
			return true;
		}
	}

	//Labelの作成
	private JLabel labelTitle   = new JLabel();
	JLabel labelStage1          = new JLabel();
	JLabel labelStage2          = new JLabel();
	JLabel labelStage3          = new JLabel();
	JLabel []labelTime; 

	// Labelの作成
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

	// Labelをフレームにセットするメソッド 
	private void addLabel(JLabel label, Font font, int x, int y, int width, int height) {
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(font);
		label.setBounds(x, y, width, height);
		container.add(label);
	}

	// currentタイムを 分:秒 に直す関数
	private String getCurrentTime(long time) {
		String strTime;
		strTime = String.valueOf( String.format("%02d", time / 60000 )); //分
		strTime += " ' ";
		strTime +=  String.valueOf( String.format("%02d",(time % 60000) / 1000 )); //秒
		strTime += " \" ";
		strTime +=  String.valueOf( String.format("%02d",(time % 60000) % 1000 /10)); //小数点第２位まで表示（少数第3位は切り捨て）
		return strTime;
	}

	// OKボタンが押された時の処理、つまりRankingへのシーケンス処理
	public void actionPerformed(ActionEvent e) {
		leave = true;
	}



	// -----------------------------以上追加項目--------------------------------------


}