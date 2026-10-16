package tetris;

import javafx.application.Application;

public class Start {
    public static final RendererType rendererType = RendererType.JavaFX;

    public static void main(String[] args) {
        if (rendererType == RendererType.JavaFX) {
            Application.launch(App.class, args);
        }
        else if (rendererType == RendererType.SDL3) {
            SDLApp.start();
        }
    }
}

enum RendererType {
    JavaFX, SDL3
}