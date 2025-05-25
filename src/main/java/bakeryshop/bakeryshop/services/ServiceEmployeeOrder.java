package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.models.EmployeeOrder;
import bakeryshop.bakeryshop.repos.IRepoEmployeeOrder;
import bakeryshop.bakeryshop.repos.IRepoUser;

public class ServiceEmployeeOrder extends ServiceDB<EmployeeOrder> implements IServiceEmployeeOrder {
    private final IRepoEmployeeOrder repoEmployeeOrder;
    public ServiceEmployeeOrder(IRepoEmployeeOrder repoEmployeeOrder) {
        super(repoEmployeeOrder);
        this.repoEmployeeOrder = repoEmployeeOrder;
    }
}
