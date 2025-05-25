package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.OrderStatus;
import bakeryshop.bakeryshop.models.OrderStatusHistory;

import java.util.List;

public class RepoDBOrderStatus extends RepoDb<OrderStatusHistory> implements IRepoOrderStatus {
    public RepoDBOrderStatus() {
        super(OrderStatusHistory.class);
    }

    @Override
    public List<OrderStatusHistory> findByOrderId(int orderId) {
        return entityManager.createQuery("FROM OrderStatusHistory osh WHERE osh.order.id = :orderId", OrderStatusHistory.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
