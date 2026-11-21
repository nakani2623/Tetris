package tetris.graphics;

import java.net.URL;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;

final class VideoBackground extends StackPane {
    private final MediaView view = new MediaView();
    private MediaPlayer player;
    private String resource;

    VideoBackground() {
        setMouseTransparent(true);
        view.setPreserveRatio(false);
        view.fitWidthProperty().bind(widthProperty());
        view.fitHeightProperty().bind(heightProperty());
        getChildren().add(view);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);
    }

    void showVideo(String path) {
        if (path != null && path.equals(resource)) return;
        dispose();
        resource = path;
        if (path == null) return;
        URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Background video not found: " + path);
            return;
        }
        try {
            Media media = new Media(url.toExternalForm());
            player = new MediaPlayer(media);
            player.setMute(true);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setOnError(() -> {
                System.err.println("Background video failed: " + path + ": " + player.getError());
                dispose();
            });
            view.setMediaPlayer(player);
            player.play();
        } catch (RuntimeException error) {
            System.err.println("Background video failed: " + path + ": " + error.getMessage());
            dispose();
        }
    }

    void dispose() {
        view.setMediaPlayer(null);
        if (player != null) {
            player.dispose();
            player = null;
        }
        resource = null;
    }
}
