
// obs removed every japanese characters, I (thiago) was getting compile error (even from the comments) :v

public class Main {

	final static int WIDTH = 1200;
	final static int HEIGHT = 800; 
	final static int FPS = 25; // frames per second

	private static SeqID f_sequence; 
	private static Sequence sequence;

	private static Menu menu;
	private static Game game;
	private static Result result;
	private static Ranking ranking;

	// calls for OnlyMain.java
	private static Main myMain  = new Main();
	public static Main getMain(){
		return myMain;
	}

	private Main() { //
		Stage stage = new Stage(); // search for available stages
		menu = new Menu(stage.getStagesLength()); 
		game = new Game(stage);
		game.setStageID(menu.getStageID());
		result = new Result();
		ranking = new Ranking();

		f_sequence = SeqID.SEQ_MENU;
		sequence = menu;

		// test
		//f_sequence = SeqID.SEQ_RANKING;
		//sequence = ranking;
	}

	enum SeqID { //SequenceID
		SEQ_MENU {
			void after() {
				f_sequence = SeqID.SEQ_GAME;
				sequence = game;
				game.setStageID(menu.getStageID());
			}
		},
		SEQ_GAME {
			void after() {
				f_sequence = SeqID.SEQ_RESULT;
				sequence = result;
				result.setValues(game.getStageName(), game.isClear(), game.getTime());
				ranking.setValues(menu.getStageID(), game.getTime());
			}
		},
		SEQ_RESULT {
			void after() {
				f_sequence = SeqID.SEQ_RANKING;
				sequence = ranking;
			}
		},
		SEQ_RANKING {
			void after() {
				f_sequence = SeqID.SEQ_MENU;
				sequence = menu;
			}
		};

		abstract void after();
	}

	// this Main.myMain is executed only once
	void myMain() {
		while(true) {
			System.out.println("Main Loop");
			sequence.myMain();
			f_sequence.after();
		}
	}
}
