package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.models.OrderStatus;
import bakeryshop.bakeryshop.models.Status;

public interface IServiceStatus extends IService<Status> {
     Status getByOrderStatus(OrderStatus newStatus);
}
