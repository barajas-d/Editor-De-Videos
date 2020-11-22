/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Barajas-d
 */
public class ImagePosition {
    
    private double timePercent;
    private double posx;
    private double posy;

    public ImagePosition(double timePercent, double posx, double posy) {
        this.timePercent = timePercent;
        this.posx = posx;
        this.posy = posy;
    }

    public double getTimePercent() {
        return timePercent;
    }

    public double getPosx() {
        return posx;
    }

    public double getPosy() {
        return posy;
    }

    public void setTimePercent(double timePercent) {
        this.timePercent = timePercent;
    }

    public void setPosx(double posx) {
        this.posx = posx;
    }

    public void setPosy(double posy) {
        this.posy = posy;
    }
    
    
}
