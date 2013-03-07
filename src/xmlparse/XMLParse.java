/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlparse;

// Roy's XML Parser. :)
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.FlowLayout;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.util.ArrayList;

public class XMLParse extends JFrame {

    public static void main(String[] args)
            throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException, Exception {
        try {
           readLines(); 
        } catch (Exception e) {
            javax.swing.JOptionPane.showConfirmDialog((java.awt.Component) null, e.getMessage(), "Error",
                    javax.swing.JOptionPane.DEFAULT_OPTION);
        }
        

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


        // DOWNLOAD the LATEST XML FILE 
        downloader(Global.XMLLink, extractFileName(Global.XMLLink), "");
        //Parses file 
        parseXML();
        
        for (int i = 0; i < Global.file_list.size(); i++) {

            File f = new File(Global.save + extractFileName(((String) Global.file_list.get(i))));
            if (f.exists()) {
                System.out.println(extractFileName((String) Global.file_list.get(i)) + " exists already, skipping file");
            } else {

                UI.label.setText("Downloading " + extractFileName((String) Global.file_list.get(i)));;
                downloader((String) Global.file_list.get(i), extractFileName((String) Global.file_list.get(i)), Global.save);
            }
        }
        UI.frm.dispose();

    }

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
    public static void parseXML()
            throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException, Exception {




        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // important line
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse("bookupdate.xml");

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

        try {
            // FileWriter outFile = new FileWriter(args[0]);
            PrintWriter out = new PrintWriter("output.txt");

            // Also could be written as follows on one line
            // Printwriter out = new PrintWriter(new FileWriter(args[0]));
            // Write text to file

            //out.println("This is line 1");


            for (int i = 0; i < nodes.getLength(); i++) {
                out.println(nodes.item(i).getNodeValue());
                Global.file_list.add(nodes.item(i).getNodeValue());
                // OUTPUT urls.
            }





            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    public static void download() {
        try {
            /*
             * Get a connection to the URL and start up
             * a buffered reader.
             */
            long startTime = System.currentTimeMillis();

            System.out.println("Connecting to NYT servers \n");

            URL url = new URL("http://www.nytimes.com/services/xml/rss/nyt/podcasts/bookupdate.xml");
            url.openConnection();
            InputStream reader = url.openStream();

            /*
             * Setup a buffered file writer to write
             * out what we read from the website.
             */
            FileOutputStream writer = new FileOutputStream("bookupdate.xml");
            byte[] buffer = new byte[153600];
            int totalBytesRead = 0;
            int bytesRead = 0;

            System.out.println("Reading XML file 150KB blocks at a time.\n");

            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[153600];
                totalBytesRead += bytesRead;
            }

            long endTime = System.currentTimeMillis();

            System.out.println("Done. " + (new Integer(totalBytesRead).toString()) + " bytes read (" + (new Long(endTime - startTime).toString()) + " millseconds).\n");
            writer.close();
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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
}
