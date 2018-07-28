package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "machines")
public class Bumblebee implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "imei" , unique = true)
    private String imei;

    @Column(name = "coordinate", unique = true)
    private String coordinate;


    private Status status;

    public Bumblebee() {
    }

    public Bumblebee(long id) {
        this.id = id;
    }

    public Bumblebee(String imei, String coordinate, String password) {
        this.imei = imei;
        this.coordinate = coordinate;
    }

    public Bumblebee(long id, String imei, String coordinate, String password) {
        this.id = id;
        this.imei = imei;
        this.coordinate = coordinate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    enum Status {OK, ERROR, SUBMITTED, COMPLETED}
}
