package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.OrderItem;

import java.util.List;

public interface IRepoOrderItem extends IRepository<OrderItem> {
    List<OrderItem> findByOrderId(int orderId);
}
