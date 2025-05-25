package bakeryshop.bakeryshop.services;

import bakeryshop.bakeryshop.models.Cake;
import bakeryshop.bakeryshop.repos.IRepoCake;
import bakeryshop.bakeryshop.repos.IRepoUser;

public class ServiceCake extends ServiceDB<Cake> implements IServiceCake {
    private final IRepoCake cakeRepo;
    public ServiceCake(IRepoCake cakeRepo) {
        super(cakeRepo);
        this.cakeRepo = cakeRepo;
    }
}
