package andkantor.f1betting.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static andkantor.f1betting.model.DateTime.DATE_TIME_FORMAT;

@Entity
@Table(name = "race")
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "start_date_time")
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime startDateTime;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    public Race() {
        dateCreated = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
}
