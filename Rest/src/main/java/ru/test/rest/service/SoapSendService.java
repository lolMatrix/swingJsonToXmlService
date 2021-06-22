package ru.test.rest.service;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.stereotype.Service;
import ru.test.rest.utils.XmlParser;

/**
 * It response for sending messages on remote soap service
 */
@Service
public class SoapSendService {

    /**
     * sending query into soap service
     *
     * @param message xml object, implement Client
     * @return raw xml response from soap service
     * @throws Exception
     */
    public String sendQuery(String message) throws Exception {
        message = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:test=\"test\">\n" +
                "   <soapenv:Header/>\n" +
                "   \t<soapenv:Body>\n" +
                "      <test:getPersonRequest>\n" +
                "\t\t<test:request><![CDATA[" +
                message +
                "    \t\t]]></test:request>\n" +
                "      </test:getPersonRequest>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        PostMethod postMethod = new PostMethod("http://localhost:8081/ws");

        StringRequestEntity entity = new StringRequestEntity(message, "text/xml", "UTF-8");
        postMethod.setRequestEntity(entity);

        postMethod.setRequestHeader("SOAPAction", "");

        HttpClient client = new HttpClient();
        client.executeMethod(postMethod);

        String answer = postMethod.getResponseBodyAsString();

        XmlParser parser = new XmlParser();
        String result = parser.getXmlFromSoapMessage(answer);

        return result;
    }

}
