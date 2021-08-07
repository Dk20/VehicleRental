package com.debashis.service;

import com.debashis.model.Booking;
import com.debashis.model.Vehicle;
import com.debashis.model.response.VehicleAvailabilityResponse;
import com.debashis.model.response.VehiclePriceResponse;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface IRentalService {
    List<VehicleAvailabilityResponse> getAvailableVehicle(Long branchId,String slotId,long startDateEpoch,long endDateEpoch);
//    Default strategy: minimum price
    Booking rentVehicle(String vehicleType, String slotId, long startDateEpoch, long endDateEpoch,
                        Function<Map<String,Vehicle>, VehiclePriceResponse> selectionStrategy);
}
