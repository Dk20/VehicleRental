package com.debashis.repo;

import com.debashis.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    /* todo: debug
    @Query("SELECT v FROM Vehicle AS v LEFT OUTER JOIN VehicleInventory as i ON v.vehicleId=i.vehicle.vehicleId WHERE v.type=:vehicleType" +
            " and NOT ( " +
                "i.slotId=:slotId and " +
                "( (i.startDateEpoch<=:endDateEpoch and  i.startDateEpoch>=:startDateEpoch) " +
                "or (i.endDateEpoch<=:endDateEpoch and i.endDateEpoch>=:startDateEpoch) )" +
            ")"
    )
    List<Vehicle> findByTypeAndAvailability(String vehicleType, String slotId, long startDateEpoch, long endDateEpoch);
    * */

    List<Vehicle> findByType(String vehicleType);
}
