package ru.test.rest.entity;

import ru.test.rest.entity.enums.DocumentType;
import javax.persistence.*;
import java.sql.Date;

/**
 * document db entity
 */
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String series;
    private String number;
    private DocumentType document;
    private Date issuseDate;

    @ManyToOne(targetEntity = Client.class)
    private Client client;

    public Document() {
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public DocumentType getDocument() {
        return document;
    }

    public void setDocument(DocumentType document) {
        this.document = document;
    }

    public Date getIssuseDate() {
        return issuseDate;
    }

    public void setIssuseDate(Date issuseDate) {
        this.issuseDate = issuseDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
