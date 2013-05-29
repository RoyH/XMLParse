/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlparse;

import java.io.File;
import java.util.ArrayList;
import static xmlparse.XMLParse.downloader;
import static xmlparse.XMLParse.extractFileName;

/**
 *
 * @author Roy
 */
public class AudioSource {

    private String name;
    private String xml;
    private String AlbumTag;
    private String SavePath;
    private ArrayList<String> URL = new ArrayList();

    // Constructed the Object
    public AudioSource(String name, String xml, String AlbumTag, String SavePath) {
        this.name = name;
        this.xml = xml;
        this.AlbumTag = AlbumTag;
        this.SavePath = SavePath;

    }

    public void setXML(String xml) {
        this.xml = xml;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbumTag(String AlbumTag) {
        this.AlbumTag = AlbumTag;
    }

    public String name() {
        return name;
    }

    public String xml() {
        return xml;
    }

    public String AlbumTag() {
        return AlbumTag;
    }
    
    public String SavePath(){
        return SavePath;
    }

    public void setURL(ArrayList<String> URL) {
        this.URL = URL;
    }

    public void output() {
        System.out.println("//////////////////////////");
        System.out.println(name);
        System.out.println(xml);
        System.out.println(AlbumTag);
        System.out.println(SavePath);

    }

    public void Download() throws Exception {
        for (int i = 0; i < URL.size(); i++) {

            File f = new File(SavePath + extractFileName(URL.get(i)));
            if (f.exists()) {
                System.out.println(extractFileName(URL.get(i)) + " exists already, skipping file");
            } else {

                UI.label.setText("Downloading " + extractFileName(URL.get(i)));;
                downloader((String) URL.get(i), extractFileName(URL.get(i)), SavePath);
            }
        }
    }

   
}

