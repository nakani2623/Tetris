package tetris.ai;

import tetris.engine.GameEngine;
import tetris.engine.type.Direction;
import tetris.engine.type.Rotation;

enum AIAction {
    MOVE_LEFT {
        @Override
        void execute(GameEngine engine) {
            engine.move(Direction.LEFT);
        }
    },
    MOVE_RIGHT {
        @Override
        void execute(GameEngine engine) {
            engine.move(Direction.RIGHT);
        }
    },
    ROTATE_CLOCKWISE {
        @Override
        void execute(GameEngine engine) {
            engine.rotate(Rotation.CLOCKWISE);
        }
    },
    ROTATE_COUNTER_CLOCKWISE {
        @Override
        void execute(GameEngine engine) {
            engine.rotate(Rotation.COUNTER_CLOCKWISE);
        }
    },
    HARD_DROP {
        @Override
        void execute(GameEngine engine) {
            engine.hardDrop();
        }
    },
    SOFT_DROP {
        @Override
        void execute(GameEngine engine) {
            engine.moveDAS(Direction.DOWN);
        }
    }
    ;


    abstract void execute(GameEngine engine);
}
