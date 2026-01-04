package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.Order;

public class RepoDBOrder extends RepoDb<Order> implements IRepoOrder {
    public RepoDBOrder() {
        super(Order.class);
    }
}
