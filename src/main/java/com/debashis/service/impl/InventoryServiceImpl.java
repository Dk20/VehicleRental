package com.debashis.service.impl;

import com.debashis.exception.InventoryNotAvailable;
import com.debashis.model.Vehicle;
import com.debashis.model.VehicleInventory;
import com.debashis.repo.VehicleInventoryRepository;
import com.debashis.repo.VehicleRepository;
import com.debashis.service.IInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class InventoryServiceImpl implements IInventoryService {
    private VehicleInventoryRepository vehicleInventoryRepository;
    private VehicleRepository vehicleRepository;
    @Autowired
    public InventoryServiceImpl(VehicleInventoryRepository vehicleInventoryRepository, VehicleRepository vehicleRepository) {
        this.vehicleInventoryRepository = vehicleInventoryRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<VehicleInventory> getBlockedInventory(Long vehicleId, String slotId, long startDateEpoch, long endDateEpoch) {
        return vehicleInventoryRepository.findBlockedInventory(
                vehicleId,
                slotId,
                startDateEpoch,
                endDateEpoch
        );
    }
    @Transactional(isolation = Isolation.SERIALIZABLE,rollbackFor = {InventoryNotAvailable.class,Exception.class})
    @Override
    public void blockInventory(Long bookingId,Long vehicleId, String slotId, long startDateEpoch, long endDateEpoch) throws InventoryNotAvailable {
        List<VehicleInventory> overLappingBooking = getBlockedInventory(vehicleId,slotId,startDateEpoch,endDateEpoch);
        if(!overLappingBooking.isEmpty())
            throw new InventoryNotAvailable("Inventory Blocked by: "+overLappingBooking);
        Vehicle vehicle = vehicleRepository.getById(vehicleId);
        VehicleInventory vehicleInventory = vehicleInventoryRepository.save(
                VehicleInventory.builder()
                        .bookingId(bookingId).vehicle(vehicle)
                        .slotId(slotId).startDateEpoch(startDateEpoch).endDateEpoch(endDateEpoch)
                        .build()
        );
        Set<VehicleInventory> inventorySet = vehicle.getVehicleInventorySet();
        inventorySet.add(vehicleInventory);
        vehicleRepository.save(vehicle.toBuilder().vehicleInventorySet(inventorySet).build());
    }
    @Transactional
    @Override
    public void releaseInventory(Long bookingId) {
        try {
            vehicleInventoryRepository.removeByBookingId(bookingId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Booking does not exist or Inventory already released for bookingId {}",bookingId);
        }
    }
}
