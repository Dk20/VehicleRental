package com.debashis.repo;

import com.debashis.model.VehiclePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiclePriceRepository extends JpaRepository<VehiclePrice,Long> {
    List<VehiclePrice> findByVehicleType(String vehicleType);
}
