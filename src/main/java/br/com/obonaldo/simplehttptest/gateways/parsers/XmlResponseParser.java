package br.com.obonaldo.simplehttptest.gateways.parsers;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("XML")
@RequiredArgsConstructor
public class XmlResponseParser implements ResponseParser {

    @Override
    public String getValueFrom(String response, String node) {

        String value = "";

        try {

            final InputStream is = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();

            final Document doc = builder.parse(is);

            final XPath xPath = new DOMXPath(node);
            final List list = xPath.selectNodes(doc);

            value = (String) list.stream()
                    .map(n -> ((DeferredElementImpl) n).getFirstChild().getNodeValue())
                    .collect(Collectors.joining(","));

        } catch (ParserConfigurationException | SAXException | IOException | JaxenException e) {
            log.error(e.getMessage());
        }
        return value;
    }
}