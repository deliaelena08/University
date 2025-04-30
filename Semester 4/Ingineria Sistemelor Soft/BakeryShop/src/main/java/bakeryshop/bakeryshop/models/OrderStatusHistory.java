package bakeryshop.bakeryshop.models;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_history")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_order")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private Status status;
    private LocalDateTime update_date;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getChangeDate() {
        return update_date;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.update_date = changeDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

