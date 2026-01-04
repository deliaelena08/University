package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.models.OrderStatus;
import bakeryshop.bakeryshop.models.Status;
import bakeryshop.bakeryshop.repos.IRepoStatus;

public class ServiceStatus extends ServiceDB<Status> implements IServiceStatus {
    private final IRepoStatus statusRepo;
    public ServiceStatus(IRepoStatus repoStatus) {
        super(repoStatus);
        this.statusRepo = repoStatus;
    }

    @Override
    public Status getByOrderStatus(OrderStatus newStatus) {
        return statusRepo.getByOrderStatus(newStatus);
    }
}
