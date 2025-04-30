package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.models.OrderStatus;
import bakeryshop.bakeryshop.models.OrderStatusHistory;
import bakeryshop.bakeryshop.repos.IRepoOrderStatus;

import java.util.List;

public class ServiceOrderStatus extends ServiceDB<OrderStatusHistory> implements IServiceOrderStatus {
    private final IRepoOrderStatus orderStatusRepo;
    public ServiceOrderStatus(IRepoOrderStatus orderStatusRepo) {
        super(orderStatusRepo);
        this.orderStatusRepo = orderStatusRepo;
    }

    @Override
    public List<OrderStatusHistory> getHistoryByOrderId(int orderId) {
        return orderStatusRepo.findByOrderId(orderId);
    }
}
