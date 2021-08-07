package com.debashis.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="vehicle",indexes = @Index(name = "type_idx", columnList = "type"))
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Vehicle {
    @Id
    @GeneratedValue
    private Long vehicleId;

    @ToString.Exclude
    @OneToMany(mappedBy = "vehicle",fetch = FetchType.EAGER)
    private Set<VehicleInventory> vehicleInventorySet;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "branchId")
    private Branch branch;

    private String type;// not keeping an enum because we do not want to make a new deployment for every new type added
    private String metadata; // any other details in a json
}
