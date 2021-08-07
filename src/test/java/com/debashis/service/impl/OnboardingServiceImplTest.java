package com.debashis.service.impl;

import com.debashis.model.Branch;
import com.debashis.model.Vehicle;
import com.debashis.model.VehiclePrice;
import com.debashis.repo.BranchRepository;
import com.debashis.repo.VehicleRepository;
import com.debashis.service.IOnboardingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnboardingServiceImplTest {
    @Autowired
    private IOnboardingService iOnboardingService;
    @Autowired
    private BranchRepository branchRepository;

    /*
    * Requirements Tested:
    *
    * Onboard a new branch with available vehicles
    * Onboard new vehicle(s) of an existing type to a particular branch
    * */
    @Test
    public void onboardTest(){
        Branch branch = iOnboardingService.createBranch("branch_A","Bangalore","{}");
        System.out.println(branch);
        Vehicle vehicle = iOnboardingService.createVehicle("SUV","{}",branch.getBranchId());
        System.out.println(vehicle);
        vehicle = iOnboardingService.createVehicle("SEDAN","{}",branch.getBranchId());
        System.out.println(vehicle);
        vehicle = iOnboardingService.createVehicle("SEDAN","{}",branch.getBranchId());
        System.out.println(vehicle);
        VehiclePrice vehiclePrice = iOnboardingService.createPrice("SUV","200.00",branch.getBranchId());
        System.out.println(vehiclePrice);
        vehiclePrice = iOnboardingService.createPrice("SEDAN","150.00",branch.getBranchId());
        System.out.println(vehiclePrice);
        System.out.println(branchRepository.findById(branch.getBranchId()));
    }
}