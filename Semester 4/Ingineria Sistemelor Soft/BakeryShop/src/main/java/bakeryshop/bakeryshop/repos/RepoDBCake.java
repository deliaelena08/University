package bakeryshop.bakeryshop.repos;

import bakeryshop.bakeryshop.models.Cake;

public class RepoDBCake extends RepoDb<Cake> implements IRepoCake {
    public RepoDBCake() {
        super(Cake.class);
    }
}
