import org.jetbrains.annotations.*;
import java.util.Arrays;
import lombok.ToString;

@ToString(callSuper=true)
public class Customer extends Entity {
    public Customer(@NotNull String name, double balance) {
        super(name, balance);
        this.setType("Customer");
    }

    public boolean canPurchase(double discountCoefficient,
                               Beverage @NotNull... beverages) {
        return this.balance >= Arrays.stream(beverages)
                .mapToDouble(
                        bev -> bev.getPrice() * discountCoefficient
                ).sum();
    }
    public boolean makePayment(double amount) {
        return (this.balance -= amount) >= 0;
    }
}
