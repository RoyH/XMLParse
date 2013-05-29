// Version 3.0
package xmlparse;

// Roy's XML Parser. :)
import org.jaudiotagger.audio.*;
import org.jaudiotagger.logging.*;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.utils.*;
import org.jaudiotagger.test.*;
import java.io.IOException;
import java.awt.FlowLayout;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLParse extends JFrame {

    public static void main(String[] args)
            throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException, Exception {

        SourceCollection Collection = new SourceCollection();
        ArrayList<AudioSource> Sources = new ArrayList();
        Collection = ReadStreams();

        //<editor-fold defaultstate="collapsed" desc="UI INITIALIZATION">
        // Initiallizing UI Ignore..
        

        //--------

        UI.label.setText("Downloading XML FILE");
        UI.frm.add(UI.label);
        UI.current.setSize(50, 50);
        UI.current.setValue(43);
        UI.current.setStringPainted(true);
        UI.frm.add(UI.current);
        UI.frm.setVisible(true);
        UI.frm.setLayout(new FlowLayout());
        UI.frm.setSize(400, 70);
        UI.frm.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //</editor-fold>


        //last working code
        // DOWNLOAD the LATEST XML FILE 
        //downloader(Global.XMLLink, extractFileName(Global.XMLLink), "");
        //Parses file 
        //parseXML();

        //convert AudioSource object to arraylist.
        Sources = Collection.getList();

        // Loops through..
        for (AudioSource s : Sources) {
            // Get XML FILE
            downloader(s.xml(), extractFileName(s.xml()), "");
            //Now extract the URLS from it using XMLPARSE
            s.setURL(parseXML(extractFileName(s.xml())));
            //now remove the xml file to stop conflicts..
            Delete(extractFileName(s.xml()));
            // now we can iterate through the download Process.
            s.Download();
            //Now tag Files
            ListFiles(s.SavePath(),s.AlbumTag());
            
            
            


        }



        
        UI.label.setText("TAGGING FILES...");
        
        UI.frm.dispose();

    }
/* old code.
    public static void readLines() throws IOException {
        FileReader fileReader;
        fileReader = new FileReader("config.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            Global.config.add(line);
        }
        bufferedReader.close();
        Global.XMLLink = Global.config.get(0);
        Global.save = Global.config.get(1);



    }
    */

    public static String extractFileName(String path) {

        if (path == null) {
            return null;
        }
        String newpath = path.replace('\\', '/');
        int start = newpath.lastIndexOf("/");
        if (start == -1) {
            start = 0;
        } else {
            start = start + 1;
        }
        String pageName = newpath.substring(start, newpath.length());

        return pageName;
    }

    //<editor-fold defaultstate="collapsed" desc="PARSEXML">
    public static ArrayList<String> parseXML(String path)
            throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException, Exception {




        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // important line
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(path);

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        // experimental XPATH query that will extract URL's
        // seems to work...


        XPathExpression expr = xpath.compile("//item/enclosure/@url"); // XPATH QUERY.



        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;

        System.out.println("Parsed XML file sucessfully.. Displaying Results");

        //for (int i = 0; i < nodes.getLength(); i++) {
        //  System.out.println(nodes.item(i).getNodeValue());
        // OUTPUT urls.
        // }

        //initiallize output arraylist
        ArrayList<String> output = new ArrayList();

        try {
            // FileWriter outFile = new FileWriter(args[0]);
            PrintWriter out = new PrintWriter("output.txt");

            // Also could be written as follows on one line
            // Printwriter out = new PrintWriter(new FileWriter(args[0]));
            // Write text to file

            //out.println("This is line 1");


            for (int i = 0; i < nodes.getLength(); i++) {
                //saves it to output.txt for debugging
                out.println(nodes.item(i).getNodeValue());
                //saves to arraylist
                output.add(nodes.item(i).getNodeValue());
                // OUTPUT urls.
            }





            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }
    //</editor-fold>

    public static void downloader(String site, String filename, String directory) throws Exception {
        //site = "http://podcasts.nytimes.com/podcasts/2013/03/01/books/review/03books_pod/030113bookreview.mp3";
        //filename = "temp.mp3";

        try {
            URL url = new URL(site);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            int filesize = connection.getContentLength();
            float totalDataRead = 0;
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(directory + filename);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int i = 0;
            while ((i = in.read(data, 0, 1024)) >= 0) {
                totalDataRead = totalDataRead + i;
                bout.write(data, 0, i);
                float Percent = (totalDataRead * 100) / filesize;
                UI.current.setValue((int) Percent);
            }
            bout.close();
            in.close();

        } catch (Exception e) {
            javax.swing.JOptionPane.showConfirmDialog((java.awt.Component) null, e.getMessage(), "Error",
                    javax.swing.JOptionPane.DEFAULT_OPTION);
        }
    }

    public static void ListFiles(String Path, String Tag) throws Exception {

        // Directory path here
        String path = Path;

        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                if (files.endsWith(".mp3") || files.endsWith(".MP3")) {
                    //System.out.println(files);
                    ID3WRITE(listOfFiles[i], Tag);
                }
            }
        }
    }

    public static void ID3WRITE(File file, String Tag) throws Exception {
        AudioFile f = AudioFileIO.read(file);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.ALBUM, Tag);
        AudioFileIO.write(f);
        System.out.println("Sucessfully Tagged " + file.getName());

    }

    public static SourceCollection ReadStreams() throws FileNotFoundException, IOException {
        SourceCollection Collection = new SourceCollection();

        ArrayList<String> Streams = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader("streams.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            // now breakdown the line and parse into objects. 
            // format. name,xml,albumtag,savepath
            String[] sort = new String[4];
            sort = line.split(",");

            AudioSource temp = new AudioSource(sort[0], sort[1], sort[2], sort[3]);
            Collection.addStream(temp);

        }
        br.close();
        Collection.output();
        return Collection;


    }

    public static void Delete(String path) {
        try {

            File file = new File(path);

            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
