package edu.uoc.epcsd.showcatalog.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

@Entity
@ToString
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "streamingURL")
    private String streamingURL;

    @Column(name = "remainingSeats")
    private int remainingSeats;

    @Column(name = "status")
    private String status;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Show show;

}
