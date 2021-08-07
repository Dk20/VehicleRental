package com.debashis.service;

import com.debashis.model.Branch;
import com.debashis.model.Vehicle;
import com.debashis.model.VehiclePrice;

import java.util.Set;

public interface IOnboardingService {
    Branch createBranch(String name, String city, String metadata);
    Vehicle createVehicle(String type,String metadata,Long branchId);
    VehiclePrice createPrice(String vehicleType,String price,Long branchId);
}
