package com.debashis.repo;

import com.debashis.model.VehicleInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleInventoryRepository extends JpaRepository<VehicleInventory,Long> {
//    Returns all Blocked inventory
    @Query(value = "SELECT i FROM VehicleInventory AS i WHERE i.vehicle.vehicleId=:vehicleId and " +
            "i.slotId=:slotId and " +
            "((i.startDateEpoch<=:endDateEpoch and  i.startDateEpoch>=:startDateEpoch) or " +
            "(i.endDateEpoch<=:endDateEpoch and  i.endDateEpoch>=:startDateEpoch))")
    List<VehicleInventory> findBlockedInventory(
            Long vehicleId,String slotId,long startDateEpoch,long endDateEpoch
    );
    @Query(value = "SELECT i FROM VehicleInventory AS i WHERE i.vehicle.vehicleId IN :vehicleIds and " +
            "i.slotId=:slotId and " +
            "((i.startDateEpoch<=:endDateEpoch and  i.startDateEpoch>=:startDateEpoch) or " +
            "(i.endDateEpoch<=:endDateEpoch and  i.endDateEpoch>=:startDateEpoch))")
    List<VehicleInventory> findBlockedInventoryBulk(
            List<Long> vehicleIds,String slotId,long startDateEpoch,long endDateEpoch
    );
    Integer removeByBookingId(Long bookingId);
}
