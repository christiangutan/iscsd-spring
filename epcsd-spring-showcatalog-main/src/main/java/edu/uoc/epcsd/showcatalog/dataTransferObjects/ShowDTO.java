package edu.uoc.epcsd.showcatalog.dataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowDTO {
    private String name;
    private String image;
    private float price;
    private int duration;
    private int capacity;
    private Date onSaleDate;
    private List<Long> categories;
}

