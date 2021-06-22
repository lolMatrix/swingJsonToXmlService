package ru.test.soap.endpoint;


import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.xml.transform.StringSource;
import test.GetPersonRequest;
import test.GetPersonResponse;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;

/**
 * endpoint for handle query
 */
@Endpoint
public class PersonEndpoint {

    /**
     * xml namespace
     */
    private static final String NAMESPACE_URI = "test";

    /**
     * transform xml document
     *
     * @param request body query
     * @return transformed xml
     */
    @PayloadRoot(localPart = "getPersonRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    public GetPersonResponse getPersonRequest(@RequestPayload GetPersonRequest request) {
        GetPersonResponse response = new GetPersonResponse();

        try {
            Source domSource = new StringSource(request.getRequest());
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Source xslt = new StreamSource(getClass().getClassLoader().getResource("transform.xsl").getFile());
            Transformer transformer = tf.newTransformer(xslt);
            transformer.transform(domSource, result);

            response.setResponse(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}