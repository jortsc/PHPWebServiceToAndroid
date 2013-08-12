package services;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import android.util.Log;

import com.innovacom.webservicestatus.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by jortsc on 10/08/13.
 *
 */
public class SoapService {
    /**
     * Note for work in local environment:
     * 127.0.0.1 refers to localhost in the Emulator, not your machine.
     * Use 10.0.2.2 to connect to your host machine.
     * */
    //private static final String SOAP_ACTION = "http://10.0.2.2/cert_topics/TestCases/wsdl";
    //private static final String NAMESPACE = "http://10.0.2.2/cert_topics/";
    //private static final String URL = "http://10.0.2.2/cert_topics/TestCases/TWSSOAP_server.php";

    private static final String SOAP_ACTION = "http://dev.haystack.es/TestCases/wsdl";
    private static final String NAMESPACE = "http://dev.haystack.es";
    private static final String URL = "http://dev.haystack.es/TestCases/TWSSOAP_server.php";

    /**
     * Retrieve the status of given customer from a PHP Web Service, which returns an XML String
     * */
     public CustomerStatus getStatus(String id, String secret){
        String METHOD_NAME = "getCustomerStatus";
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        DocumentBuilderFactory dom;
        NodeList cell;
        Element element;


        System.out.println("**********************************************************************");
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("id", id);
        request.addProperty("secret",  secret);

        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.dotNet = true;
        soapEnvelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);
        CustomerStatus cs = new CustomerStatus();
        try {
            ht.call(SOAP_ACTION, soapEnvelope);
            //Well, seems that this can retrieve from web service String or Array plain, it doesn't need to retrieve xml although it's able to parse it how in this case:
            // SoapPrimitive response = (SoapPrimitive) soapEnvelope.getResponse();
            // Object response= (Object) soapEnvelope.getResponse(); //for handle a String response ;)

            //passing the string to xml document to acces its properties
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            StringReader sr = new StringReader(soapEnvelope.getResponse().toString());
            InputSource is = new InputSource(sr);
            Document d = builder.parse(is);
            //now I've got a xml document :P
            //now it's time to get and return a proper object

            //Extracting data from xml document
            //cell = d.getElementsByTagName("id").item(0).getTextContent();
            //element = (Element) cell.item(0);
           // cs.setId(Integer.parseInt(element.getTextContent()));
            //That's better : d.getElementsByTagName("nombre").item(0).getTextContent()
            cs.setId(Integer.parseInt(d.getElementsByTagName("id").item(0).getTextContent()));
            cs.setNombre(d.getElementsByTagName("nombre").item(0).getTextContent());
            cs.setComentario(d.getElementsByTagName("comentario_estado").item(0).getTextContent());
            cs.setDocumento(d.getElementsByTagName("numero_documento").item(0).getTextContent());
            cs.setTelefono(Integer.parseInt(d.getElementsByTagName("telefono_contacto").item(0).getTextContent()));
            cs.setEstado(d.getElementsByTagName("estado").item(0).getTextContent());
            System.out.println(cs.getId());
            System.out.println(cs.getNombre());
            System.out.println(cs.getComentario());
            System.out.println("**********************************************************************");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ht.debug == true) {
            System.out.println(ht.responseDump);
        }
         return cs;
    }
}
