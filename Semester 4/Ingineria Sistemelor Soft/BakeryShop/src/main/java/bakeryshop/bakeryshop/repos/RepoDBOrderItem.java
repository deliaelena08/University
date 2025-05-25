package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.OrderItem;
import bakeryshop.bakeryshop.models.OrderStatusHistory;

import java.util.List;

public class RepoDBOrderItem extends RepoDb<OrderItem> implements IRepoOrderItem {
    public RepoDBOrderItem() {
        super(OrderItem.class);
    }


    @Override
    public List<OrderItem> findByOrderId(int orderId) {
        return entityManager.createQuery("FROM OrderItem oi WHERE oi.order.id = :orderId", OrderItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

}
