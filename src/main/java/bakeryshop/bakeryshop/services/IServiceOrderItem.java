package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.models.OrderItem;

import java.util.List;

public interface IServiceOrderItem extends IService<OrderItem> {
    List<OrderItem> findByOrderId(int orderId);
}
