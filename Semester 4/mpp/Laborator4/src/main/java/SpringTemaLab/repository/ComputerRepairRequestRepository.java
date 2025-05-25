package SpringTemaLab.repository;

import SpringTemaLab.model.ComputerRepairRequest;
import SpringTemaLab.model.RequestStatus;

import java.util.List;

public interface ComputerRepairRequestRepository extends Repository<Integer,ComputerRepairRequest>{
    List<ComputerRepairRequest> findByOwnerName(String name);
    List<ComputerRepairRequest> findByModel(String model);
    List<ComputerRepairRequest> filterByStatus(RequestStatus status);
    List<ComputerRepairRequest> filterByModelAndStatus(String model, RequestStatus status);



}
