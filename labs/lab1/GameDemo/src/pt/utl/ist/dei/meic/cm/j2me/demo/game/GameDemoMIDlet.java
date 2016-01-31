package pt.utl.ist.dei.meic.cm.j2me.demo.game;
import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class GameDemoMIDlet extends MIDlet implements CommandListener {

	private GameEngine 		engine;
	private Display    		display;
	private Background 		background;
	private CharacterSprite	characterSprite;
	private Command	   		exit;
	private Command	   		pause;
	private Command	   		resume;
	
	public GameDemoMIDlet() {
		this.display = Display.getDisplay(this);
		
		MyModel model = new MyModel(112, 112, 32, 32, 5);
		this.background = new Background();
		this.characterSprite = new CharacterSprite(model);
		this.engine = new GameEngine(model, 10);
		
		Displayable drawingArea = this.engine.getDrawingArea(); 
			
		drawingArea.setCommandListener(this);
		this.engine.attach(background);
		this.engine.attach(characterSprite);
		
		this.resume = new Command("Resume", Command.SCREEN, 2);
		drawingArea.addCommand(this.resume);

		this.pause = new Command("Pause", Command.SCREEN, 1);
		drawingArea.addCommand(this.pause);
		
		this.exit = new Command("Exit", Command.EXIT, 1);
		drawingArea.addCommand(this.exit);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		this.display.setCurrent(this.engine.getDrawingArea());
		this.engine.start();
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == this.exit) {
			this.notifyDestroyed();
		} else
		if (c == this.resume) {
			this.engine.resume();
		} else
		if (c == this.pause) {
			this.engine.pause();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {}
	
	private class Background implements GameView {
		
		private TiledLayer background;
		
		public Background() {
			try {
				background = new TiledLayer(12, 12, Image.createImage("/System/TileA2.png"), 32, 32);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			background.fillCells(0, 0, 8, 8, 50);
		}
		
		public void render(Graphics g) {
			background.paint(g);
		}
	}
	
	private class CharacterSprite implements GameView {

		private MyModel model;	
		private Sprite sprite;
		private int[] frameSequence;
		
		public CharacterSprite(MyModel model) {
			this.model = model;
			try {
				this.sprite = new Sprite(Image.createImage("/Characters/People1.png"), 32, 32);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void render(Graphics g) {
			if (frameSequence != model.frameSequence) {
				frameSequence = model.frameSequence;
				sprite.setFrameSequence(model.frameSequence);
			}
			sprite.setPosition(model.x, model.y);
			sprite.nextFrame();
			sprite.paint(g);			
		}
	}
	
	private class MyModel implements GameModel {
		
		private int xd, yd;
		private int speed;
		
		int x, y;
		int w, h;
		
		int lastKeyPressed = 0;
		
		int[] walkingUpFrameSequence = { 36, 37, 38 };
		int[] walkingDownFrameSequence = { 0, 1, 2 };
		int[] walkingLeftFrameSequence = { 12, 13, 14 };
		int[] walkingRightFrameSequence = { 24, 25, 26 };
		int[] lookingUpFrameSequence = { 37 };
		int[] lookingDownFrameSequence = { 1 };
		int[] lookingLeftFrameSequence = { 13 };
		int[] lookingRightFrameSequence = { 25 };
		int[] frameSequence = lookingDownFrameSequence;
		
		public MyModel(int x, int y, int w, int h, int speed) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			
			this.xd = this.yd = 1;
			this.speed = speed;
		}
		
		public void Ai(int keys, int width, int height) {
			if ((keys & GameCanvas.UP_PRESSED) != 0 && this.y - speed > 0) {
				this.xd = 0;
				this.yd = -1;
				lastKeyPressed = GameCanvas.UP_PRESSED;
				frameSequence = walkingUpFrameSequence;
			} else if ((keys & GameCanvas.DOWN_PRESSED) != 0 && (this.y + h + speed) < height) {
				this.xd = 0;
				this.yd = 1;
				lastKeyPressed = GameCanvas.DOWN_PRESSED;
				frameSequence = walkingDownFrameSequence;
			} else if ((keys & GameCanvas.LEFT_PRESSED) != 0 && this.x - speed > 0) {
				this.xd = -1;
				this.yd = 0;
				lastKeyPressed = GameCanvas.LEFT_PRESSED;
				frameSequence = walkingLeftFrameSequence;
			} else if ((keys & GameCanvas.RIGHT_PRESSED) != 0 && (this.x + w + speed) < width) {
				this.xd = 1;
				this.yd = 0;
				lastKeyPressed = GameCanvas.RIGHT_PRESSED;
				frameSequence = walkingRightFrameSequence;
			} else {
				this.xd = this.yd = 0;
				
				if ((lastKeyPressed & GameCanvas.UP_PRESSED) != 0) {
					frameSequence = lookingUpFrameSequence;
				} else if ((lastKeyPressed & GameCanvas.DOWN_PRESSED) != 0) {
					frameSequence = lookingDownFrameSequence;
				} else if ((lastKeyPressed & GameCanvas.LEFT_PRESSED) != 0) {
					frameSequence = lookingLeftFrameSequence;
				} else if ((lastKeyPressed & GameCanvas.RIGHT_PRESSED) != 0) {
					frameSequence = lookingRightFrameSequence;
				}
			}			
			
			this.x += speed * this.xd;
			this.y += speed * this.yd;
		}
	}
}
