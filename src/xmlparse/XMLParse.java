/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlparse;

// Roy's XML Parser. :)
import java.io.IOException;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.net.*;
import java.io.*;

public class XMLParse {

    public static void main(String[] args)
            throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException {


        download();


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
}