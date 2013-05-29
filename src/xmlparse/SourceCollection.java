/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlparse;

import java.util.ArrayList;

/**
 *
 * @author Roy
 */
public class SourceCollection {
    
    private ArrayList<AudioSource> list = new ArrayList() ;

    public SourceCollection() {  

    }
    
    public void addStream(AudioSource source){
        list.add(source);
    }
    
    public ArrayList<AudioSource> getList(){
        return list;
    }
    
    public void removeStream (AudioSource source) {
        list.remove(source);
    }
    
    public void output (){
        for (AudioSource as:list) {
            as.output();
        }
    
    }
    
   
}
