package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.OrderStatus;
import bakeryshop.bakeryshop.models.Status;

public interface IRepoStatus extends IRepository<Status> {
    Status getByOrderStatus(OrderStatus status);
}
