package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.models.Order;
import bakeryshop.bakeryshop.repos.IRepoOrder;

public class ServiceOrder extends ServiceDB<Order> implements IServiceOrder {
    private final IRepoOrder orderRepo;
    public ServiceOrder(IRepoOrder orderRepo) {
        super(orderRepo);
        this.orderRepo = orderRepo;
    }
}
