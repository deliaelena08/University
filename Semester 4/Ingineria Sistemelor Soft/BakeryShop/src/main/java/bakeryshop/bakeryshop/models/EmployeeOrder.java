package bakeryshop.bakeryshop.models;
import javax.persistence.*;

@Entity
@Table(name = "employees_orders")
public class EmployeeOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_employee")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "id_cake")
    private Cake cake;

    @ManyToOne
    @JoinColumn(name = "id_order")
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Cake getCake() {
        return cake;
    }

    public void setCake(Cake cake) {
        this.cake = cake;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}

