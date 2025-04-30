package bakeryshop.bakeryshop.models;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private User client;

    private String order_date;
    private  int total_price;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getClientId() {
        return client;
    }

    public void setClientId(User client) {
        this.client = client;
    }

    public String getOrderDate() {
        return order_date;
    }

    public void setOrderDate(String orderDate) {
        this.order_date = orderDate;
    }

    public int getTotalPrice() {
        return total_price;
    }

    public void setTotalPrice(int totalPrice) {
        this.total_price = totalPrice;
    }

    public int getIdClient() {
        return client.getIdUser();
    }
    public void setIdClient(int idClient) {
        client.setIdUser(idClient);
    }
}

