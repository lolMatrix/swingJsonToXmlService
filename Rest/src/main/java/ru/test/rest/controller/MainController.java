package ru.test.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import ru.test.rest.entity.Client;
import ru.test.rest.repository.ClientRepository;
import ru.test.rest.service.SoapSendService;
import ru.test.rest.utils.XmlParser;

@Tag(name = "client", description = "The User API")
@RestController
@RequestMapping("/")
public class MainController {

    /**
     * Repository for all clients
     */
    private ClientRepository repository;
    /**
     * Sending messages on remote service
     */
    private SoapSendService sendService;

    public MainController(ClientRepository repository, SoapSendService sendService) {
        this.repository = repository;
        this.sendService = sendService;
    }


    @Operation(summary = "Save person and get xml", tags = "client", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            schema = @Schema(
                    type = "object"
            ),
            examples = {
                    @ExampleObject(
                            description = "multi document object",
                            value = "{\"name\": \"Тест\", \"surname\": \"Тестов\", \"patronymic\": \"Тестович\", \"birthDate\": \"1990-01-01\", \"gender\": \"MAN\", \"document\": [{\"series\": \"1333\", \"number\": \"112233\", \"type\": \"PASSPORT\", \"issueDate\": \"2020-01-01\"},{\"series\": \"1233\",\"number\": \"112133\",\"type\": \"DRIVER\", \"issueDate\": \"2020-02-01\"}]}"
                    )
            }
    )))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "It is returning xml file, describe one person",
                    content = {
                            @Content(
                                    mediaType = "application/xml",
                                    examples = {
                                            @ExampleObject(
                                                    value = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><person patronymic=\"Тестович\" gender=\"MAN\" surname=\"Тестов\" name=\"Тест\" birthDate=\"1990-01-01\"><document series=\"1333\" number=\"112233\" type=\"PASSPORT\" issueDate=\"2020-01-01\"/></person>"
                                            )
                                    }
                            )
                    }
            ),
            @ApiResponse(responseCode = "400", description = "You have been sent wrong json",
                    content = {
                            @Content(
                                    examples = @ExampleObject(value = "{\"time\":\"2021-06-21T15:21:43.329+03:00\",\"message\":\"Wrong json format. Please, check your request.\",\"statusCode\":\"400 bad request\"}")
                            )
                    }
            )
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> index(@RequestBody String responseJson) {

        var jsonObject = new JSONObject(responseJson);

        Client client;

        try {
            client = Client.fromJson(jsonObject);
        } catch (Exception e) {
            return getError("Wrong json format. Please, check your request.", HttpStatus.BAD_REQUEST);
        }

        repository.save(client);

        var toXML = new JSONObject();
        toXML.put("person", jsonObject);

        String message;
        Document document;

        try {
            message = sendService.sendQuery(XML.toString(toXML));
            document = new XmlParser().getXmlFromString(message);
        } catch (Exception e) {
            return getError("Internal service error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        client = Client.fromXml(document);
        repository.save(client);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/xml");

        return new ResponseEntity<>(message, headers, HttpStatus.OK);

    }

    /**
     * get error response entity
     *
     * @param message error message
     * @param status  http status code
     * @return json error response with installed headers
     */
    private ResponseEntity<String> getError(String message, HttpStatus status) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(getErrorJson(status, message).toString(), headers, status);
    }

    /**
     * get error in json format
     *
     * @param statusCode error code
     * @param message    error message
     * @return formatted json object with datetime, status and message error
     */
    private JSONObject getErrorJson(HttpStatus statusCode, String message) {
        JSONObject error = new JSONObject();
        error.put("statusCode", statusCode.toString().toLowerCase().replace('_', ' '));
        error.put("message", message);
        error.put("time", DateTime.now().toString());

        return error;
    }

}
