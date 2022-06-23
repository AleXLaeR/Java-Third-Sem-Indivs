import org.jetbrains.annotations.*;
import lombok.ToString;
import java.util.*;

@ToString(callSuper=true)
public class Supplier extends Entity {
    private final Map<Beverage, Double> availableBeverages = new TreeMap<>(
            Comparator.comparing(Beverage::getName));
    public Supplier(@NotNull String name, double balance) {
        super(name, balance);
        this.setType("Supplier");
    }

    public Supplier(@NotNull String name,
                    Beverage @NotNull... beverages) {
        super(name, 0);
        Arrays.stream(beverages)
                .forEach(bev -> this.availableBeverages.put(bev, bev.getPrice()));
        this.setType("Supplier");
    }

    @SafeVarargs
    public static @NotNull Supplier
    of(@NotNull String name,
       @Nullable Map.Entry<Beverage, Double>... listedBeverages) {

        var newSupplier = new Supplier(name, 0);

        if (listedBeverages != null) {
            Arrays.stream(listedBeverages)
                    .forEach(bevEntry -> newSupplier.availableBeverages.put(
                                    Objects.requireNonNull(bevEntry).getKey(),
                                    bevEntry.getValue()
                            )
                    );
        }
        return newSupplier;
    }

    public static @NotNull Supplier
    of(@NotNull String name,
       double balance,
       @Nullable Map<Beverage, Double> listedBeverages) {

        var newSupplier = new Supplier(name, balance);

        if (listedBeverages != null) {
            listedBeverages.entrySet()
                    .forEach(bevEntry -> newSupplier.availableBeverages.put(
                                    Objects.requireNonNull(bevEntry).getKey(),
                                    bevEntry.getValue()
                            )
                    );
        }
        return newSupplier;
    }

    public boolean hasInStock(@NotNull Beverage beverage) {
        return availableBeverages.containsKey(beverage);
    }

    public double getItemPrice(@NotNull Beverage beverage) {
        return availableBeverages.get(beverage);
    }

    public void setItemPrice(@NotNull Beverage beverage, double price) {
        this.availableBeverages.computeIfPresent(beverage, (k, v) -> v = price);
    }

    public void receivePayment(double amount) {
        this.balance += amount;
    }

    public Map<Beverage, Double> listAvailableBeverages() {
        return Map.copyOf(availableBeverages);
    }
}
