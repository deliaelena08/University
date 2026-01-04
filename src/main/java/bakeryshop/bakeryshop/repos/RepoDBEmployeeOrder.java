package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.EmployeeOrder;

public class RepoDBEmployeeOrder extends RepoDb<EmployeeOrder> implements IRepoEmployeeOrder {
    public RepoDBEmployeeOrder() {
        super(EmployeeOrder.class);
    }
}
