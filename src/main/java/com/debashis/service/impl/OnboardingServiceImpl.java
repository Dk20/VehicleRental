package com.debashis.service.impl;

import com.debashis.model.Branch;
import com.debashis.model.Vehicle;
import com.debashis.model.VehiclePrice;
import com.debashis.repo.BranchRepository;
import com.debashis.repo.VehiclePriceRepository;
import com.debashis.repo.VehicleRepository;
import com.debashis.service.IOnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class OnboardingServiceImpl implements IOnboardingService {
    private BranchRepository branchRepository;
    private VehicleRepository vehicleRepository;
    private VehiclePriceRepository vehiclePriceRepository;

    @Autowired
    public OnboardingServiceImpl(BranchRepository branchRepository, VehicleRepository vehicleRepository, VehiclePriceRepository vehiclePriceRepository) {
        this.branchRepository = branchRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehiclePriceRepository = vehiclePriceRepository;
    }

    @Override
    public Branch createBranch(String name, String city, String metadata) {
        Branch.BranchBuilder builder = Branch.builder().city(city).metadata(metadata).name(name);
        return branchRepository.save(builder.build());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Vehicle createVehicle(String type, String metadata, Long branchId) {
        Branch branch = branchRepository.getById(branchId);
        Vehicle vehicle = vehicleRepository.save(
                Vehicle.builder().type(type).metadata(metadata).branch(branch).build()
        );
        Set<Vehicle> vehicleSet = branch.getVehicleSet();
        vehicleSet.add(vehicle);
        branchRepository.save(branch.toBuilder().vehicleSet(vehicleSet).build());
        return vehicle;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VehiclePrice createPrice(String vehicleType, String price, Long branchId) {
        Branch branch = branchRepository.getById(branchId);
        VehiclePrice vehiclePrice = vehiclePriceRepository.save(
                VehiclePrice.builder().vehicleType(vehicleType).price(price).branch(branch).build()
        );
        Set<VehiclePrice> vehiclePriceSet = branch.getPriceSet();
        vehiclePriceSet.add(vehiclePrice);
        branchRepository.save(branch.toBuilder().priceSet(vehiclePriceSet).build());
        return vehiclePrice;
    }
}
