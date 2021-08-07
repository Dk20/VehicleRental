package com.debashis.model.response;

import com.debashis.model.Vehicle;
import lombok.*;

@Builder
@Getter
@ToString
public class VehiclePriceResponse {
    private Vehicle vehicle;
    private String price;
}
