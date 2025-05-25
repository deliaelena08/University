package bakeryshop.bakeryshop.services;
import bakeryshop.bakeryshop.models.OrderStatus;
import bakeryshop.bakeryshop.models.OrderStatusHistory;

import java.util.List;

public interface IServiceOrderStatus extends IService<OrderStatusHistory> {
    List<OrderStatusHistory> getHistoryByOrderId(int orderId);
}
