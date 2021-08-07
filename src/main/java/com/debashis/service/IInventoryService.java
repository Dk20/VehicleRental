package com.debashis.service;

import com.debashis.exception.InventoryNotAvailable;
import com.debashis.model.VehicleInventory;

import java.util.List;

public interface IInventoryService {
    List<VehicleInventory> getBlockedInventory(Long vehicleId,String slotId,long startDateEpoch,long endDateEpoch);
    void blockInventory(Long bookingId,Long vehicleId,String slotId,long startDateEpoch,long endDateEpoch) throws InventoryNotAvailable;
    void releaseInventory(Long bookingId);
}
