package com.debashis.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="branch")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Branch {
    @Id
    @GeneratedValue
    private Long branchId;
    private String name;

    @OneToMany(mappedBy = "branch",fetch = FetchType.EAGER)
    private Set<Vehicle> vehicleSet;
    @OneToMany(mappedBy = "branch",fetch = FetchType.EAGER)
    private Set<VehiclePrice> priceSet;

    private String city;// can be a different entity
    private String metadata; // json
}
