
/**
 * Sample Skeleton for 'GUI.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class GUIController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

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
        System.out.println("subir video");
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
    }

    @FXML
    void playClick(MouseEvent event) {
        System.out.println("play");
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