
/**
 * Sample Skeleton for 'GUI.fxml' Controller Class
 */

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
//import java.time.Duration;

public class GUIController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // urlVideo location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private Slider reproductionTime;

    @FXML
    private Slider navBarVolume;

    @FXML
    private MediaView mediaView;

    Media media;
    MediaPlayer mediaPlayer;
    boolean isPlaying = false;

    @FXML
    void addPicture(MouseEvent event) {
        System.out.println("subir imagen");
    }

    @FXML
    void addSound(MouseEvent event) {
        System.out.println("subir audido");
    }

    @FXML
    void addVideo(MouseEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Vídeo");
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Vídeo (*.mp4, *.mkv)", "*.mp4", "*.mkv"));
        String urlVideo = "";
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println(file.getPath());
            String rutaVideo = file.getPath();
            urlVideo = "file:///" + file.getPath();
            urlVideo = "file:///D:/videoTest.mp4";
            System.out.println("subir video " + rutaVideo);

        } else {
            System.out.println(urlVideo);
        }
        media = new Media(urlVideo);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        reproductionTime.setMin(0);
        reproductionTime.setMax(mediaPlayer.getTotalDuration().toSeconds());
        reproductionTime.setValue(0);
        /*
         * reproductionTime.valueProperty().addListener( (paramet, observer, value) ->
         * {​​​​ //GUIController.this.value = value; reproductionTime.setValue((double)
         * GUIController.this.value); }​​​ );
         */
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
        });
        this.isPlaying = true;

        navBarVolume.setMin(0);
        navBarVolume.setMax(1);
        navBarVolume.setValue(1);
        mediaPlayer.volumeProperty().bind(navBarVolume.valueProperty());

    }

    @FXML
    void saveVideo(MouseEvent event) {
        System.out.println("Guarda Nuevo video");
    }

    @FXML
    void avanzarClick(MouseEvent event) {
        System.out.println("avanzar");
    }

    @FXML
    void muteClick(MouseEvent event) {
        System.out.println("mute");
        // this.navBarVolume.setValue(0);
    }

    @FXML
    void playClick(MouseEvent event) {
        Status status = this.mediaPlayer.getStatus();

        if (status == Status.UNKNOWN || status == Status.HALTED) {
            // don't do anything in these states
            return;
        }

        if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
            // rewind the movie if we're sitting at the end
            if (!this.isPlaying) {
                this.mediaPlayer.seek(this.mediaPlayer.getStartTime());
                this.isPlaying = true;
            }
            System.out.println("play");
            this.mediaPlayer.play();
        } else {
            System.out.println("pause");
            this.mediaPlayer.pause();
        }
    }

    @FXML
    void retrocederClick(MouseEvent event) {
        System.out.println("retroceder");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        System.out.println("inicio");
    }
}
