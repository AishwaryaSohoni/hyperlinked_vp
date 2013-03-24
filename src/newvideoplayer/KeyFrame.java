/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newvideoplayer;

/**
 *
 * @author Aishwarya
 */
public class KeyFrame {
    public int frameNum,topLeftX,topLeftY,bottomRightX,bottomRightY;
    
    
    public KeyFrame(){
        frameNum=0;
        topLeftX=0;
        topLeftY=0;
        bottomRightX=0;
        bottomRightY=0;
    }
    
    public void display(){
        System.out.println("Frame Num : "+frameNum);
        System.out.println("topLeftX : "+topLeftX);
        System.out.println("topLeftY : "+topLeftY);
        System.out.println("bottom X : "+bottomRightX);
        System.out.println("bottom Y : "+bottomRightY);
        
    }
}
