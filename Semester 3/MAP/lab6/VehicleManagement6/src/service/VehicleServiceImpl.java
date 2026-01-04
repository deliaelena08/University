package service;

import java.util.List;

import domain.Vehicle;
import exceptions.NoDataAvailableException;
import repository.VehicleRepository;

import static java.util.Locale.filter;

public class VehicleServiceImpl implements VehicleService {
	private static final String PROPERTY_TO_LOAD_DATA="vehicleInitialLoadFile";
	
	private VehicleRepository vehicleRepository;

	public VehicleServiceImpl(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public Vehicle searchVehicle(String licensePlate) {
		List<Vehicle> vehicles = vehicleRepository.getVehicles();
		return vehicles.stream()
				.filter(vehicle -> vehicle.getLicensePlate()
						.equals(licensePlate)).findFirst().get();
	}

	@Override
	public List<Vehicle> getAll() throws NoDataAvailableException{
	
		vehicleRepository.initialLoadOfVehicles(PROPERTY_TO_LOAD_DATA);
		
		List<Vehicle>vehicles = vehicleRepository.getVehicles();
		
		if(vehicles.size()==0){
			throw new NoDataAvailableException("There is no vehicle in the system !");
		}
		
		return vehicles;
	}
}
