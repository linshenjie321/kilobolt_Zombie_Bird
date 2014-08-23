package com.kilobolt.gameworld;

import com.kilobolt.gameobjects.Bird;
import com.kilobolt.gameobjects.ScrollHandler;
import com.kilobolt.zbHelpers.AssetLoader;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class GameWorld {
	
	private Bird bird;
	private ScrollHandler scroller;
	
	private Rectangle ground;
	
	private int score = 0;
	
	private GameState currentState;
	
	public int midPointY;
	
	
	public GameWorld(int midPointY) {
		currentState = GameState.READY;
		bird = new Bird(33, midPointY - 5, 17,12);
		// The grass should start 66 pixels below the midPointY
		scroller = new ScrollHandler(this, midPointY + 66);
        ground = new Rectangle(0, midPointY + 66, 136, 11);
        this.midPointY = midPointY;
	}
	
	public void updateRunning(float delta){
		// Add a delta cap so that if our game takes too long
        // to update, we will not break our collision detection.

        if (delta > .15f) {
            delta = .15f;
        }
		
		bird.update(delta);
		scroller.update(delta);
		
		if (scroller.collides(bird) && bird.isAlive()) {
            scroller.stop();
            bird.die();
            AssetLoader.dead.play();
        }

        if (Intersector.overlaps(bird.getBoundingCircle(), ground)) {
            scroller.stop();
            bird.die();
            bird.decelerate();
            currentState = GameState.GAMEOVER;
            
            if (score > AssetLoader.getHighScore()) {
                AssetLoader.setHighScore(score);
                currentState = GameState.HIGHSCORE;
            }
        }
	}
	
	public void restart() {
        currentState = GameState.READY;
        score = 0;
        bird.onRestart(midPointY - 5);
        scroller.onRestart();
        currentState = GameState.READY;
    }
	
	public void update(float delta) {

        switch (currentState) {
        case READY:
            updateReady(delta);
            break;

        case RUNNING:
            updateRunning(delta);
            break;
        default:
            break;
        }

    }

    private void updateReady(float delta) {
        // Do nothing for now
    }
	
	public Bird getBird(){
		return bird;
	}
	
	public ScrollHandler getScroller() {
        return scroller;
    }
	
	public int getScore() {
	    return score;
	}
	
	public void addScore(int increment) {
	    score += increment;
	}
	
	public boolean isHighScore() {
	    return currentState == GameState.HIGHSCORE;
	}
	
	public boolean isReady() {
	        return currentState == GameState.READY;
	}

	public void start() {
	        currentState = GameState.RUNNING;
	}
	
	public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }
	
	public enum GameState {

	    READY, RUNNING, GAMEOVER, HIGHSCORE

	}
	
	
	
	

}
