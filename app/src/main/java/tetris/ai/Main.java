package tetris.ai;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import tetris.controller.dispatch.MainThreadDispatcher;
import tetris.engine.GameEngine;
import tetris.engine.Mino;
import tetris.engine.Tetromino;

public class Main {
    private final GameEngine engine;
    private final MainThreadDispatcher dispatcher;

    public Main(GameEngine engine, MainThreadDispatcher dispatcher) {
        this.engine = engine;
        this.dispatcher = dispatcher;
    }

    public void run() throws InterruptedException, ExecutionException {
        GameEngine explorerEngine = new GameEngine(this.engine);
        Set<DropPoint> dropPoints = new DropPointExporer(explorerEngine).get();
        if (dropPoints.isEmpty()) {
            return;
        }

        DropPoint lowestStack = chooselowestStack(dropPoints);
        System.out.println("Got Lowest stack drop point: " + lowestStack);
        execute(lowestStack, engine);
    }

    public void execute(DropPoint dropPoint, GameEngine engine) throws InterruptedException, ExecutionException {
        FutureTask<Void> actionTask = new FutureTask<>(() -> {
            for (AIAction action : dropPoint.executionOrder) {
                action.execute(engine);
            }
            return null;
        });
        dispatcher.dispatch(actionTask);
        actionTask.get();
    }

    private DropPoint chooselowestStack(Set<DropPoint> dropPoints) throws InterruptedException, ExecutionException{
        DropPoint lowestStack = null;
        int minHeight = Integer.MAX_VALUE;
        for (DropPoint dp : dropPoints) {
            GameEngine engine = new GameEngine(this.engine);
            execute(dp, engine);
            int stackHeight = stackHeight(engine);
            System.out.println("Stack height for drop point " + dp + ": " + stackHeight);
            if (stackHeight < minHeight) {
                minHeight = stackHeight;
                lowestStack = dp;
            }
        }
        return lowestStack;
    }

    private int stackHeight(GameEngine engine) {
        int maxHeight = 0;
        for (Tetromino tetromino : engine.getAllTetrominos()) {
            if (tetromino == engine.getCurrentTetromino())
                continue;
            for (Mino m : tetromino.children) {
                int height = (int)engine.getHeight() - (int)m.getPosition().getY();
                if (height > maxHeight)
                    maxHeight = height;
            }
        }
        System.out.println("Stack height on " + engine + ": " + maxHeight);
        return maxHeight;
    }

    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10, 20, null);
        DropPointExporer explorer = new DropPointExporer(engine);
        engine.start();

        for (DropPoint dp : explorer.get()) {
            System.out.println(dp);
        }

        System.out.println("Total drop points: " + explorer.get().size());
    }
}
