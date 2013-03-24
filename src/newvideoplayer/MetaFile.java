/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newvideoplayer;
import java.util.*;
/**
 *
 * @author Aishwarya
 */
public class MetaFile {

    public int rectID,numOfKeyFrames,linkedVideoStartFrame,nextKf,prevKf;
    public String rectName,linkedVideoName;
    public ArrayList<KeyFrame> kf;
    //added this comment.
    
    public MetaFile(){
        kf=new ArrayList<KeyFrame>();
        rectID=0;
        numOfKeyFrames=0;
        linkedVideoStartFrame=0;
        rectName="";
        nextKf=1;
        prevKf=0;
        linkedVideoName="";
    }
    
    public void setEmpty(){
        rectID=0;
        numOfKeyFrames=0;
        linkedVideoStartFrame=0;
        rectName="";
        nextKf=0;
        prevKf=0;
        linkedVideoName="";
        
    }
    public void display(){
        System.out.println("rectID : "+rectID);
        System.out.println("rectName : "+rectName);
        System.out.println("linkedvideoname : "+linkedVideoName);
        System.out.println("linkedVideoStartFrame : "+linkedVideoStartFrame);
        System.out.println("numofkeyframes : "+numOfKeyFrames);
        System.out.println("prevKf ="+prevKf);
        System.out.println("nextKf ="+nextKf);
        int i=0;
        while(i<kf.size())
        {
            kf.get(i++).display();
        }
    }
}
