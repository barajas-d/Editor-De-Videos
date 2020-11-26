
/**
 * Sample Skeleton for 'GUI.fxml' Controller Class
 */

import Model.ImagePosition;
import Model.Video;
import com.xuggle.mediatool.IMediaReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import static java.lang.Math.random;

//AUDIO
import jaco.mp3.player.MP3Player;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//XUGGLER
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.Utils;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

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
    
    @FXML
    private StackPane mediaViewPane;
    
    Media media;
    MediaPlayer mediaPlayer;
    boolean isPlaying = false;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    Rectangle cuadrado;

    boolean imageIn = false;
    boolean videoIn = false;
    boolean soundIn = false;

    double muteVolumenOld;
    boolean muted = false;
    
    String urlVideo="";
    String urlAudio="";
    String urlImagen="";
    String urlFinal="";
    
    
    List<ImagePosition> imagePosition = new ArrayList<>();
    
    public void addImagePosition(ImagePosition newPosition){
        //System.out.println("new: " + newItem + " old: " + imagePosition);
        int index = 0;
        Boolean insert = false;
        for (ImagePosition actualItem : imagePosition) {
            if(newPosition.getTimePercent() == actualItem.getTimePercent()){
                insert = true;
                break;
            }
            
            if(newPosition.getTimePercent() < actualItem.getTimePercent() && !insert){
                System.out.println("agrego" + newPosition.getTimePercent());
                imagePosition.add(index, newPosition);
                insert = true;
                break;
            }
            index++;
        }
        
        if(!insert){
            imagePosition.add(newPosition);
        }
        
        for (ImagePosition imagePosition1 : imagePosition) {
            System.out.println("pos: " + imagePosition1.getTimePercent() + " x: " + imagePosition1.getPosx() + " Y: " + imagePosition1.getPosy());
        }
    }

    public ImagePosition getImagePosicion(double time){
        for (ImagePosition imagePosition1 : imagePosition) {
            if(imagePosition1.getTimePercent() == time){
                return imagePosition1;
            }
        }
        return null;
    }
 
    @FXML
    void addPicture(MouseEvent event) {
        if(!imageIn && mediaPlayer!=null){
            //reproductionTime.setValue(0.5);
            System.out.println("subir imagen");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Imagen");
            fileChooser.getExtensionFilters()
                    .addAll(new FileChooser.ExtensionFilter("Imagen (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif"));

            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                System.out.println(file.getPath());
                String filePath = file.getPath().replace("\\", "/");
                urlImagen = new String("file:///" + filePath);

            } else {
                System.out.println("Error addPicture " + urlImagen);
                return;
            }

            cuadrado = crearCuadrado(urlImagen);
            mediaViewPane.getChildren().add(cuadrado);
            imageIn=true;
        }
    }

    private Rectangle crearCuadrado(String url) {
        cuadrado = new Rectangle(150, 150);
        System.out.println("url " + url);
        cuadrado.setFill(new ImagePattern(new Image(url)));
        cuadrado.setOnMouseDragged(squareOnMouseDraggedEventHandler);
        cuadrado.setOnMouseReleased(squareOnMouseReleasedEventHandler);
        cuadrado.setStroke(Color.SEAGREEN);
        cuadrado.setCursor(Cursor.WAIT);
        cuadrado.setTranslateX(0);
        cuadrado.setTranslateY(0);
        return cuadrado;
    }

    @FXML
    void addSound(MouseEvent event) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        if(!soundIn && mediaPlayer!=null){
            System.out.println("subir audido");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Audio");
            fileChooser.getExtensionFilters()
                    .addAll(new FileChooser.ExtensionFilter("Sonido (*.mp3, *.aac, *.wav)", "*.mp3", "*.wav", "*.aac"));
            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                System.out.println(file.getPath());
                urlAudio = file.getPath();
            } else
                return;

            String substring = urlAudio.substring(urlAudio.length() - 3, urlAudio.length());

            if (substring.equals("wav")) {
                Clip sonido = AudioSystem.getClip();
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                sonido.open(audioInputStream);
                // mediaPlayer.setVolume(0);
                mediaPlayer.setMute(true);
                sonido.start();
            } else if (substring.equals("mp3")) {
                MP3Player mp3 = new MP3Player(file);
                // mediaPlayer.setVolume(0);
                mediaPlayer.setMute(true);
                mp3.play();
            }
            // AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(file);

        // for( :audioInputStream )

        // Comienza la reproducción
            soundIn=true;
        }
    }

    @FXML
    void addVideo(MouseEvent event) {
        if(!videoIn){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Vídeo");
            fileChooser.getExtensionFilters()
                    .addAll(new FileChooser.ExtensionFilter("Vídeo (*.mp4, *.mkv)", "*.mp4", "*.mkv"));

            File file = fileChooser.showOpenDialog(null);
            String url="";
            if (file != null) {
                urlVideo=file.getAbsolutePath();
                String filePath = urlVideo.replace("\\", "/");
                url = "file:///" + filePath;

                System.out.println("subir video " + url);
            } else {
                System.out.println("Error URL Video");
            }

            media = new Media(url);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            reproductionTime.setMin(0);
            reproductionTime.setMax(1);
            reproductionTime.setValue(0);

            reproductionTime.valueProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable ov) {
                    if (reproductionTime.isValueChanging()) {
                        mediaPlayer.seek(
                                Duration.seconds(reproductionTime.getValue() * mediaPlayer.getTotalDuration().toSeconds()));
                    }
                }

            });

            mediaPlayer.currentTimeProperty().addListener((a, b, value) -> {
                reproductionTime.setValue(value.toSeconds() / mediaPlayer.getTotalDuration().toSeconds());

                double minutoActual = (int) value.toMinutes();
                double segundoActual = (int) value.toSeconds() % 60;
                timeLabel.setText(String.format("%.0f", minutoActual) + "." + String.format("%.0f", segundoActual) + "Mins");
                //System.out.println("ImageX: " + cuadrado.getTranslateX() + " ImageY: " + cuadrado.getTranslateY());

                double actualTime = Math.round(reproductionTime.getValue()*mediaPlayer.getTotalDuration().toSeconds() * 10d) / 10d;
                ImagePosition actualPosition = getImagePosicion(actualTime); 
                if(actualPosition != null){
                    System.out.println("coincidencia");
                    cuadrado.setTranslateX(actualPosition.getPosx());
                    cuadrado.setTranslateY(actualPosition.getPosy());
                }

            });

            mediaPlayer.setOnReady(() -> {
                mediaPlayer.play();
            });
            this.isPlaying = true;

            navBarVolume.setMin(0);
            navBarVolume.setMax(1);
            navBarVolume.setValue(1);
            mediaPlayer.volumeProperty().bind(navBarVolume.valueProperty());
            
            videoIn=true;
        }
    }

    @FXML
    void saveVideo(MouseEvent event) throws Exception {
        
        System.out.println("Guarda Nuevo video");
        if(urlVideo.isEmpty())
            System.out.println("NO HA CARGADO VIDEO");
        else{
            System.out.println("YA CARGO VIDEO");
            urlFinal="VideoFinal.mp4";
            System.out.println(urlFinal);
            Video v;
            if(urlAudio.isEmpty())
                 v=new Video(urlVideo,urlFinal);
            else{
                IMediaReader reader = ToolFactory.makeReader(urlVideo);
                IContainer container = reader.getContainer();
                IMediaWriter writer = ToolFactory.makeWriter(urlFinal);
                System.out.println("Termino");
                IContainer containerVideo = IContainer.make();
                IContainer containerAudio = IContainer.make();

                if (containerVideo.open(urlVideo, IContainer.Type.READ, null) < 0)
                    throw new IllegalArgumentException("Cant find " + urlVideo);

                if (containerAudio.open(urlAudio, IContainer.Type.READ, null) < 0)
                    throw new IllegalArgumentException("Cant find " + urlAudio);

                int numStreamVideo = containerVideo.getNumStreams();
                int numStreamAudio = containerAudio.getNumStreams();

                System.out.println("Number of video streams: "+numStreamVideo + "\n" + "Number of audio streams: "+numStreamAudio );

                int videostreamt = -1; //this is the video stream id
                int audiostreamt = -1;

                IStreamCoder  videocoder = null;

                for(int i=0; i<numStreamVideo; i++){
                    IStream stream = containerVideo.getStream(i);
                    IStreamCoder code = stream.getStreamCoder();

                    if(code.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
                    {
                        videostreamt = i;
                        videocoder = code;
                        break;
                    }

                }

                for(int i=0; i<numStreamAudio; i++){
                    IStream stream = containerAudio.getStream(i);
                    IStreamCoder code = stream.getStreamCoder();

                    if(code.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
                    {
                        audiostreamt = i;
                        break;
                    }
                }

                if (videostreamt == -1) throw new RuntimeException("No video steam found");
                if (audiostreamt == -1) throw new RuntimeException("No audio steam found");

                if(videocoder.open()<0 ) throw new RuntimeException("Cant open video coder");

                System.out.println("Paso 1");
                IPacket packetvideo = IPacket.make();

                IStreamCoder audioCoder = containerAudio.getStream(audiostreamt).getStreamCoder();

                if(audioCoder.open()<0 ) throw new RuntimeException("Cant open audio coder");
                System.out.println("Audio 1");
                writer.addAudioStream(0, 0, audioCoder.getChannels(), audioCoder.getSampleRate());
                System.out.println("Audio 2");
                writer.addVideoStream(1, 1, videocoder.getWidth(), videocoder.getHeight());
                System.out.println("Video");
                IPacket packetaudio = IPacket.make();

                System.out.println("Paso 2");
                System.out.println("Guardando nuevo video..");
                while(containerVideo.readNextPacket(packetvideo) >= 0 || containerAudio.readNextPacket(packetaudio) >= 0){
                    if(packetvideo.getStreamIndex() == videostreamt){

                        //video packet
                        IVideoPicture picture = IVideoPicture.make(videocoder.getPixelType(),
                                videocoder.getWidth(),
                                videocoder.getHeight());
                        int offset = 0;
                        while (offset < packetvideo.getSize()){
                            int bytesDecoded = videocoder.decodeVideo(picture,packetvideo,offset);
                            if(bytesDecoded < 0) throw new RuntimeException("bytesDecoded not working");
                            offset += bytesDecoded;

                            if(picture.isComplete()){
                                writer.encodeVideo(1, picture);                        
                            }
                        }
                    }
                    if(packetaudio.getStreamIndex() == audiostreamt){   
                    //audio packet
                        IAudioSamples samples = IAudioSamples.make(512, 
                                audioCoder.getChannels(),
                                IAudioSamples.Format.FMT_S32);  
                        int offset = 0;
                        while(offset<packetaudio.getSize())
                        {
                            int bytesDecodedaudio = audioCoder.decodeAudio(samples,packetaudio,offset);
                            if (bytesDecodedaudio < 0)
                                throw new RuntimeException("could not detect audio");
                            offset += bytesDecodedaudio;

                            if (samples.isComplete())
                                writer.encodeAudio(0, samples);
                        }
                    }

                    /*
                    int sampleRate = 44100;
                    int channels = 2;
                    writer.setMaskLateStreamExceptions(true);
                    writer.addAudioStream(1, 0, ICodec.ID.CODEC_ID_MP3, channels, sampleRate);
                    reader.addListener(writer);
                    while(reader.readPacket() == null);
                    writer.close();*/
                }
                writer.close();
            }
            System.out.println("Video Guardado Correctamente");
        } 
        
    }

    @FXML
    void avanzarClick(MouseEvent event) {
        System.out.println("avanzar");
        double tiempoTotal = mediaPlayer.getTotalDuration().toSeconds();
        double tiempoActual = reproductionTime.getValue() * tiempoTotal;
        if (tiempoActual < tiempoTotal - 0.25) {
            tiempoActual = tiempoActual + 0.25;
        } else {
            tiempoActual = tiempoTotal;
        }
        mediaPlayer.seek(Duration.seconds(tiempoActual));
    }

    @FXML
    void muteClick(MouseEvent event) throws Exception {
        System.out.println("mute");        
        if(muted){
            navBarVolume.setValue(muteVolumenOld);
            muted = false;
        }
        else{
            muteVolumenOld = navBarVolume.getValue();
            navBarVolume.setValue(0);
            muted = true;
        } 
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
        double tiempoTotal = mediaPlayer.getTotalDuration().toSeconds();
        double tiempoActual = reproductionTime.getValue() * tiempoTotal;
        if (tiempoActual > 0.25) {
            tiempoActual = tiempoActual - 0.25;
        } else {
            tiempoActual = 0;
        }
        mediaPlayer.seek(Duration.seconds(tiempoActual));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        System.out.println("inicio");
    }

    EventHandler<MouseEvent> squareOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            //System.out.println("sotiene: " + t.getSceneX());
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            //System.out.println("orgSceneX :" + orgSceneX + " orgSceneY: " + orgSceneY);
            orgTranslateX = ((Rectangle) (t.getSource())).getTranslateX();
            orgTranslateY = ((Rectangle) (t.getSource())).getTranslateY();
            double posX=orgSceneX-400-40;
            double posY=orgSceneY-300-120;
            if(posX<-300)
                posX=-300;
            if(posX>300)
                posX=300;
            if(posY >226)
                posY=226;
            if(posY <-226)
                posY=-226;
            ((Rectangle) (t.getSource())).setTranslateX(posX);
            ((Rectangle) (t.getSource())).setTranslateY(posY);
        }
    };

    EventHandler<MouseEvent> squareOnMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) 
        {
            //System.out.println("SOLTE EL MOUSE");
            if(mediaPlayer == null){
                System.out.println("No hay video");
            }
            else{
                //System.out.println("Tiempo: " + reproductionTime.getValue() + "X: " + cuadrado.getTranslateX() + "Y: " + cuadrado.getTranslateY());
                double actualTime = Math.round(reproductionTime.getValue()*mediaPlayer.getTotalDuration().toSeconds() * 10d) / 10d;
                ImagePosition newPosition = new ImagePosition(actualTime, cuadrado.getTranslateX(), cuadrado.getTranslateY());
                addImagePosition(newPosition);
            }
            
            /*System.out.println("solto mause");
            
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            System.out.println("orgSceneX :" + orgSceneX + " orgSceneY: " + orgSceneY);
            double offsetX = orgSceneX - orgSceneX ;
            double offsetY = orgSceneY - orgSceneY ;
            System.out.println("offsetX :" + offsetX + " offsetY: " + offsetY);
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
            System.out.println("newTranslateX :" + newTranslateX + " offsetY: " + newTranslateY);

            ((Rectangle) (t.getSource())).setTranslateX(newTranslateX);
            ((Rectangle) (t.getSource())).setTranslateY(newTranslateY);*/
        }
    };
    
    public void setVideoTime(double percent){
        if(mediaPlayer != null){
            mediaPlayer.seek(Duration.seconds(mediaPlayer.getTotalDuration().toSeconds() * percent));
            reproductionTime.setValue(percent);
        }
    }
}
