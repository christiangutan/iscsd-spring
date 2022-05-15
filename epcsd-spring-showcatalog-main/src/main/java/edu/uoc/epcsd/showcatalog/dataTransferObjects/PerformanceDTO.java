package edu.uoc.epcsd.showcatalog.dataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceDTO {
    private Date date;
    private String URL;
    private int remainingSeats;
    private String status;
    private Long show;
}
