# Vehicle Rental System
Rent different kinds of vehicles such as cars and bikes.

### Supported Features:
1. Rental service has multiple branches throughout the city. 
2. Each branch has a limited number of different kinds of vehicles. 
3. Each vehicle can be booked with a predefined fixed price. 
4. Each vehicle can be booked in multiples of 1-hour slots each. (For simplicity, assuming slots of a single day) 

### Requirements Covered:
1. Onboard a new branch with available vehicles 
2. Onboard new vehicle(s) of an existing type to a particular branch 
3. Rent a vehicle for a time slot and a vehicle type(the lowest price as the default choice extendable to any other strategy). 
4. Display available vehicles for a given branch sorted on price 
5. The vehicle will have to be dropped at the same branch where it was picked up. 

### Implementation Details:
- Implementation framework: spring-boot (v2.5.3)

- The implementation uses an **in-memory** store called H2.
 H2 is an open-source lightweight Java database. It can be embedded in Java applications.
 
- H2 is chosen because we can easily configure the database to run on a separate server instance.

- All apis are implemented in `com.debashis.service`

- All entities are defined in `com.debashis.model`

- To Run, please use below test driver class:
  `com.debashis.service.impl.RentalServiceImplTest`
  
### Sample Output:
```
Data Added:
[Branch(branchId=1, name=branch_A, vehicleSet=[Vehicle(vehicleId=4, type=SEDAN, metadata={}), Vehicle(vehicleId=2, type=SUV, metadata={}), Vehicle(vehicleId=3, type=SEDAN, metadata={})], priceSet=[VehiclePrice(priceId=5, vehicleType=SUV, price=200.00), VehiclePrice(priceId=6, vehicleType=SEDAN, price=150.00)], city=Bangalore, metadata={}), 
Branch(branchId=7, name=branch_B, vehicleSet=[Vehicle(vehicleId=9, type=SUV, metadata={}), Vehicle(vehicleId=8, type=SUV, metadata={}), Vehicle(vehicleId=10, type=SEDAN, metadata={})], priceSet=[VehiclePrice(priceId=11, vehicleType=SUV, price=210.00), VehiclePrice(priceId=12, vehicleType=SEDAN, price=180.00)], city=Bangalore, metadata={})]

Available Vehicles:
[VehicleAvailabilityResponse(branchName=branch_A, branchId=1, vehicleId=3, type=SEDAN, price=150.00), VehicleAvailabilityResponse(branchName=branch_A, branchId=1, vehicleId=4, type=SEDAN, price=150.00), VehicleAvailabilityResponse(branchName=branch_A, branchId=1, vehicleId=2, type=SUV, price=200.00)]

Booking(bookingId=13, status=null, vehicleId=3, amount=150.00, slotId=0, startDateEpoch=0, endDateEpoch=0, metadata=null, createdBy=null, createdAt=0, updatedBy=null, updatedAt=0)
Booking(bookingId=15, status=null, vehicleId=4, amount=150.00, slotId=0, startDateEpoch=0, endDateEpoch=0, metadata=null, createdBy=null, createdAt=0, updatedBy=null, updatedAt=0)

Available Vehicles:
[VehicleAvailabilityResponse(branchName=branch_A, branchId=1, vehicleId=2, type=SUV, price=200.00)]

releasing inventory for last booking

Available Vehicles:
[VehicleAvailabilityResponse(branchName=branch_A, branchId=1, vehicleId=4, type=SEDAN, price=150.00), VehicleAvailabilityResponse(branchName=branch_A, branchId=1, vehicleId=2, type=SUV, price=200.00)]

```
### Future Work:
- Implement: Dynamic pricing – demand vs supply. If 80% of cars in a particular branch are booked, increase the price by 10%.
- Combine multiple calls to db to a single optimised join query.
- Benchmark apis for higher workload 





