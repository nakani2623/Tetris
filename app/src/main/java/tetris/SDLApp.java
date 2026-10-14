package tetris;

import java.lang.foreign.*;

import tetris.controller.GameController;
import tetris.engine.GameEngine;
import tetris.engine.observers.BoardDisplayManager;
import tetris.engine.observers.HoldDisplayManager;
import tetris.engine.observers.NextQueueDisplayManager;
import tetris.engine.observers.ScoreDisplayManager;
import tetris.graphics.GameWindow;
import tetris.utils.SDL;

public class SDLApp {
    private final GameEngine engine = new GameEngine();
    private final GameWindow window = new GameWindow();
    private final GameController controller = new GameController(engine, window);
    public static void start() {
        MemorySegment renderer = SDL.init_SDL(500, 800);
        SDL.simp_win(renderer);


        // create observers and register
        // todo: implement new displayManagers(observer) for SDL
        // new BoardDisplayManager(engine, window.getBoardPane());
        // new ScoreDisplayManager(engine.getScore(), window.getScorePane());
        // new HoldDisplayManager(engine, window.getHoldPane());   
        // new NextQueueDisplayManager(engine, window.getNextQueuePane());

        
        // engine.setAutoSpawnListener(controller);
        // // controller.setSoftDropFactor(15);
        // controller.setSoftDropFactor(Double.MAX_VALUE);
        // engine.setGameOverListener(controller);

        // controller.showMenu();
        // window.setOnStart(controller::start);
        // window.setOnExit(Platform::exit);
        // window.setOnReturn(controller::showMenu);
        // window.setOnRestart(controller::restart);

        
        // gameEngine.loop();
    }
}
