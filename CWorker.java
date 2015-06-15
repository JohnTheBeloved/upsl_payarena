/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import java.util.*;
import java.io.*;

/**
 *
 * @author Cochonma
 *@author JohnTheBeloved
 */
public class CWorker {
    public  static HashMap<String,String> validateXML(String xmlmsg) throws SAXParseException, Exception
    {
        String xmlResponse = xmlmsg ;
       
        HashMap<String,String> orderHashMap = new HashMap<String,String>();
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inStream = new InputSource();
            inStream.setCharacterStream(new StringReader(xmlResponse));
            Document doc = builder.parse(inStream);
            NodeList statusNodeList = doc.getElementsByTagName("Status");
            Node node = statusNodeList.item(0);
            String statusCode = node.getTextContent();
            orderHashMap.put(node.getNodeName(), node.getTextContent());
           if(statusCode.equals("00"))
          {
                    NodeList orderNodeList = doc.getElementsByTagName("Order").item(0).getChildNodes(); 
                    System.out.println("Length ==="+orderNodeList.getLength());
                    for (int i = 0; i < orderNodeList.getLength(); i++)
                    {
                          node = orderNodeList.item(i);
                            orderHashMap.put(node.getNodeName(), node.getTextContent());

           }
            }else if(statusCode.equals("10"))
            {


            }else{
                  System.out.println("Invalid Order Status ==="+statusCode);

            }
            
           

        }
        catch(Exception ex)
        {

            ex.printStackTrace();
            System.out.println(ex);
            throw ex;
        }
       return orderHashMap;
    } 


    

    public static void main(String [] args){
        String xmlToParse = "<TKKPG><Response><Operation>CreateOrder</Operation><Status>00</Status><Order><OrderID>28161</OrderID>";
            xmlToParse += "<SessionID>61DCC90F866CAF52F3C8EFCA1404E513</SessionID>";
            xmlToParse += "<URL>https://196.46.20.36/index.jsp</URL>";
            xmlToParse += "</Order></Response></TKKPG>";

            try{
             System.out.println("Status"+validateXML(xmlToParse).get("Status"));
            }catch(Exception ex)
            {
                System.out.println(ex);
            }
            
    
    }
           
 
}