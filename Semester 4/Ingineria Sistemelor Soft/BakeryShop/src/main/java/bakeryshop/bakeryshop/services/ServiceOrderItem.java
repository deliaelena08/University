package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.models.OrderItem;
import bakeryshop.bakeryshop.repos.IRepoOrderItem;

import java.util.List;

public class ServiceOrderItem extends ServiceDB<OrderItem> implements IServiceOrderItem {
    private final IRepoOrderItem orderRepo;
    public ServiceOrderItem(IRepoOrderItem userRepo) {
        super(userRepo);
        this.orderRepo = userRepo;
    }

    @Override
    public List<OrderItem> findByOrderId(int orderId) {
        return orderRepo.findByOrderId(orderId);
    }
}
