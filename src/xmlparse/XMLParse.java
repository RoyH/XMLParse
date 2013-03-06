/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlparse;

// Roy's XML Parser. :)
import java.awt.FlowLayout;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLParse extends JFrame {

    public static void main(String[] args)
            throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException, Exception {


        downloader();


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
                // OUTPUT urls.
            }


            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




        getMP3();


    }

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

    public static void getMP3() throws IOException {
        URLConnection conn = new URL("http://podcasts.nytimes.com/podcasts/2013/03/01/books/review/03books_pod/030113bookreview.mp3").openConnection();
        InputStream is = conn.getInputStream();

        OutputStream outstream = new FileOutputStream(new File("file.mp3"));
        byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) > 0) {
            outstream.write(buffer, 0, len);
        }
        outstream.close();

    }

    public static void downloader() throws Exception {
        String site = "http://podcasts.nytimes.com/podcasts/2013/03/01/books/review/03books_pod/030113bookreview.mp3";
        String filename = "temp.mp3";
        JFrame frm = new JFrame();
        JProgressBar current = new JProgressBar(0, 100);
        current.setSize(50, 50);
        current.setValue(43);
        current.setStringPainted(true);
        frm.add(current);
        frm.setVisible(true);
        frm.setLayout(new FlowLayout());
        frm.setSize(400, 200);
        frm.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        try {
            URL url = new URL(site);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            int filesize = connection.getContentLength();
            float totalDataRead = 0;
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(filename);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int i = 0;
            while ((i = in.read(data, 0, 1024)) >= 0) {
                totalDataRead = totalDataRead + i;
                bout.write(data, 0, i);
                float Percent = (totalDataRead * 100) / filesize;
                current.setValue((int) Percent);
            }
            bout.close();
            in.close();
            frm.dispose();
        } catch (Exception e) {
            javax.swing.JOptionPane.showConfirmDialog((java.awt.Component) null, e.getMessage(), "Error",
                    javax.swing.JOptionPane.DEFAULT_OPTION);
        }
    }
}
