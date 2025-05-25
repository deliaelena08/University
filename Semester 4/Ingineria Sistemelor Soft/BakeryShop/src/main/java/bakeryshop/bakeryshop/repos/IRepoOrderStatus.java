package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.OrderStatus;
import bakeryshop.bakeryshop.models.OrderStatusHistory;

import java.util.List;

public interface IRepoOrderStatus extends IRepository<OrderStatusHistory> {
    List<OrderStatusHistory> findByOrderId(int orderId);
}
