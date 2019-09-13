package br.com.obonaldo.simplehttptest;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestXMLParser {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, JaxenException {

        File xmlFile = new File("invoice.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        XPath xPath = new DOMXPath("nfeProc/NFe/infNFe/ide/natOp");
        List list = xPath.selectNodes(doc);

        for (Object node : list) {
            String value = ((DeferredElementImpl) node).getFirstChild().getNodeValue();
            System.out.println(value);
        }

        //TODO step 0 recebe a chamada no enpoint POST /tests/{configId} com o payload a ser enviado no body
        //TODO step 1 batida no endpoint configurado com o payload passado
        //TODO step 2 espera pelo delay configurado/padrão
        //TODO step 3 batida no endpoint de xml
        //TODO step 4 validação dos campos solicitados
        //TODO step 5 gravação dos resultados
        //TODO step 6 cancelamento da nota emitida

        //TODO entrada desejada POST /tests/{configId}/payload/{payloadId} e no body os asserts
    }
}