package com.debashis.model.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleAvailabilityResponse {
    private String branchName;
    private Long branchId;
    private Long vehicleId;
    private String type;
    private String price;
}
