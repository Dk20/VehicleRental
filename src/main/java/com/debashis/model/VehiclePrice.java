package com.debashis.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="pricing",
        indexes = @Index(name = "branch_vehicleType_idx", columnList = "branchId, vehicleType",unique = true)
)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehiclePrice {
    @Id
    @GeneratedValue
    private Long priceId;
    private String vehicleType;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "branchId",nullable = false)
    private Branch branch;
    /*
    * hourly price
    * */
    private String price; // storing price as string. Will convert to BigDecimal when operating on it.
}
