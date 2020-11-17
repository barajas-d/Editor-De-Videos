
/**
 * Sample Skeleton for 'GUI.fxml' Controller Class
 */

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.util.Duration;

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

    @FXML
    private Label timeLabel;

    Media media;
    MediaPlayer mediaPlayer;
    boolean isPlaying = false;

    private Number value;
            
    @FXML
    void addPicture(MouseEvent event) {
        reproductionTime.setValue(0.5);
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
            urlVideo = "file:///C:/videoTest.mp4";
            System.out.println("subir video " + rutaVideo);

        } else {
            System.out.println(urlVideo);
        }
        media = new Media(urlVideo);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        
        reproductionTime.setMin(0);
        reproductionTime.setMax(1);
        reproductionTime.setValue(0); 
        
        reproductionTime.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (reproductionTime.isValueChanging()) {
                    mediaPlayer.seek(Duration.seconds(reproductionTime.getValue() * mediaPlayer.getTotalDuration().toSeconds()));
                }
            }
        });        

        
        mediaPlayer.currentTimeProperty().addListener((a, b, value) -> {
            reproductionTime.setValue(value.toSeconds()/mediaPlayer.getTotalDuration().toSeconds());

            double minutoActual = (int)value.toMinutes();
            double segundoActual = (int)value.toSeconds() % 60;
            timeLabel.setText(String.format("%.0f", minutoActual) + "." + String.format("%.0f", segundoActual) + "Mins");
        });
        
        
        mediaPlayer.setOnReady( () -> {
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
        double tiempoTotal = mediaPlayer.getTotalDuration().toSeconds();
        double tiempoActual = reproductionTime.getValue() * tiempoTotal;
        if(tiempoActual < tiempoTotal - 0.25){
            tiempoActual = tiempoActual + 0.25;
        }
        else{
            tiempoActual = tiempoTotal;
        }
        mediaPlayer.seek(Duration.seconds(tiempoActual));
    }

    @FXML
    void muteClick(MouseEvent event) {
        System.out.println("mute");
        navBarVolume.setValue(0);
    }

    @FXML
    void playClick(MouseEvent event) {
        Status status = this.mediaPlayer.getStatus();

        if (status == Status.UNKNOWN || status == Status.HALTED)
        {
            // don't do anything in these states
            return;
        }

        if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED)
        {
            // rewind the movie if we're sitting at the end
            if (!this.isPlaying) 
            {
                this.mediaPlayer.seek(this.mediaPlayer.getStartTime());
                this.isPlaying = true;
            }
            System.out.println("play");
            this.mediaPlayer.play();
        } else 
        {
            System.out.println("pause");
            this.mediaPlayer.pause();
        }
    }

    @FXML
    void retrocederClick(MouseEvent event) {
        System.out.println("retroceder");
        double tiempoTotal = mediaPlayer.getTotalDuration().toSeconds();
        double tiempoActual = reproductionTime.getValue() * tiempoTotal;
        if(tiempoActual > 0.25){
            tiempoActual = tiempoActual - 0.25;
        }
        else{
            tiempoActual = 0;
        }
        mediaPlayer.seek(Duration.seconds(tiempoActual));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        System.out.println("inicio");
    }
}
