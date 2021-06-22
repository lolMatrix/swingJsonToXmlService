package ru.test.rest.entity;

import io.swagger.annotations.ApiModelProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import ru.test.rest.entity.enums.DocumentType;
import ru.test.rest.entity.enums.Gender;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Client db entity
 */
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(notes = "First name", name = "name", required = true, value = "Тест")
    private String name;
    @ApiModelProperty(notes = "Second name", name = "surname", required = true, value = "Тестов")
    private String surname;
    @ApiModelProperty(notes = "Third name", name = "patronymic", required = true, value = "Тестович")
    private String patronymic;
    @ApiModelProperty(notes = "Birthday date", name = "birthDate", required = true, value = "1990-01-01")
    private Date birthday;
    @ApiModelProperty(notes = "Gender", name = "gender", required = true, value = "MAN")
    private Gender gender;

    @ApiModelProperty(notes = "Document", name = "document", required = true)
    @OneToMany(targetEntity = Document.class)
    @JoinColumn(name = "client_id")
    private List<Document> document;

    public Client() {
    }

    /**
     * Convert json to client entity
     *
     * @param json input json
     * @return new Client instance assembled from json
     */
    public static Client fromJson(JSONObject json) throws JSONException {
        Client client = new Client();

        client.setBirthDate(Date.valueOf(json.getString("birthDate")));
        client.setName(json.getString("name"));
        client.setGender(json.getEnum(Gender.class, "gender"));
        client.setSurname(json.getString("surname"));
        client.setPatronymic(json.getString("patronymic"));

        JSONObject jsonDocument = json.optJSONObject("document");
        List<Document> documents = new ArrayList<>();

        if (jsonDocument == null) {
            JSONArray array = json.optJSONArray("document");

            if (array == null)
                throw new JSONException("parsing error");

            for (int i = 0; i < array.length(); i++) {
                Document document = getDocumentFromJson(array.getJSONObject(i));
                documents.add(document);
            }
        } else {
            documents.add(getDocumentFromJson(jsonDocument));
        }

        client.setDocument(documents);

        return client;
    }

    private static Document getDocumentFromJson(JSONObject jsonDocument) {
        Document document = new Document();
        document.setDocument(jsonDocument.getEnum(DocumentType.class, "type"));
        document.setIssuseDate(Date.valueOf(jsonDocument.getString("issueDate")));
        document.setSeries(jsonDocument.getString("series"));
        document.setNumber(jsonDocument.getString("number"));

        return document;
    }

    /**
     * Convert from xml Client instance
     *
     * @param document xml document
     * @return new client instance assembled from xml
     */
    public static Client fromXml(org.w3c.dom.Document document) {
        Client client = new Client();
        NamedNodeMap attributePerson = document.getElementsByTagName("person").item(0).getAttributes();

        client.surname = attributePerson.getNamedItem("surname").getNodeValue();
        client.name = attributePerson.getNamedItem("name").getNodeValue();
        client.patronymic = attributePerson.getNamedItem("patronymic").getNodeValue();
        client.birthday = Date.valueOf(attributePerson.getNamedItem("birthDate").getNodeValue());
        client.gender = Gender.valueOf(attributePerson.getNamedItem("gender").getNodeValue());

        NodeList docs = document.getElementsByTagName("document");
        List<Document> documents = new ArrayList<>();

        for (int i = 0; i < docs.getLength(); i++) {
            Document doc = new Document();
            NamedNodeMap attributeDocument = docs.item(i).getAttributes();
            doc.setSeries(attributeDocument.getNamedItem("series").getNodeValue());
            doc.setNumber(attributeDocument.getNamedItem("number").getNodeValue());
            doc.setDocument(DocumentType.valueOf(attributeDocument.getNamedItem("type").getNodeValue()));
            doc.setIssuseDate(Date.valueOf(attributeDocument.getNamedItem("issueDate").getNodeValue()));
            documents.add(doc);
        }

        client.document = documents;

        return client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthDate(Date birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<Document> getDocument() {
        return document;
    }

    public void setDocument(List<Document> document) {
        this.document = document;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
