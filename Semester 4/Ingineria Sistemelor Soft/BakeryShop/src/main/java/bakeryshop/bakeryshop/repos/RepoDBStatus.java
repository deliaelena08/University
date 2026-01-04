package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.OrderStatus;
import bakeryshop.bakeryshop.models.Status;

import java.util.stream.StreamSupport;

public class RepoDBStatus extends RepoDb<Status> implements IRepoStatus {
    public RepoDBStatus() {
        super(Status.class);
    }

    @Override
    public Status getByOrderStatus(OrderStatus status) {
        return StreamSupport.stream(getAll().spliterator(), false)
                .filter(s -> s.getOrderStatus().equals(status))
                .findFirst()
                .orElse(null);
    }

}
