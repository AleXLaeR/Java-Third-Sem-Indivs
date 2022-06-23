import org.jetbrains.annotations.*;
import java.util.*;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@Builder(builderMethodName="storeBuilder")
public class AlcoStore {
    private double budget;
    private final String name;
    private Map<Beverage, Integer> beverages = new TreeMap<>();
    private Set<Customer> vipCustomers = new HashSet<>();
    private Set<Supplier> suppliers = new HashSet<>();

    @Getter @Setter
    private static double DISCOUNT_COEFFICIENT = .85;

    public AlcoStore(String name,
                     Beverage @NotNull... beverages) {
        Arrays.stream(beverages).forEach(this::stockBeverage);
        this.name = name;
    }

    public int buyBeverages(@NotNull Customer customer,
                            Beverage @NotNull... beverages) {
        int numberOfBoughtBeverages = 0;
        final double discountCoefficient =
                this.isCustomerVip(customer) ?
                        AlcoStore.getDISCOUNT_COEFFICIENT() : 1;

        if (!customer.canPurchase(discountCoefficient, beverages)) return 0;

        double totalPrice = 0;
        for (var beverage : beverages) {
            totalPrice += beverage.getPrice() * discountCoefficient;

            if (!this.isBeveragePresent(beverage)) continue;

            this.onCustomerPurchase(beverage, totalPrice);
            numberOfBoughtBeverages++;
        }
        customer.makePayment(totalPrice);

        return numberOfBoughtBeverages;
    }

    public boolean orderBeverage(@NotNull Beverage beverage,
                                 @Range(from=1, to=Integer.MAX_VALUE) int quantity,
                                 double priceLimit) {

        var bestSupplier = getBestSupplier(beverage);
        final double totalPrice = bestSupplier.getItemPrice(beverage) * quantity;

        if (totalPrice > priceLimit || this.budget < totalPrice) return false;
        if (!this.makePayment(bestSupplier, totalPrice)) return false;

        this.stockBeverage(beverage, quantity);
        return true;
    }

    public void onCustomerPurchase(@NotNull Beverage beverage,
                                   double paymentPrice) {
        this.beverages.put(beverage, beverages.get(beverage) - 1);
        this.budget += paymentPrice;
    }

    public boolean isBeveragePresent(@NotNull Beverage beverage) {
        return this.beverages.containsKey(beverage)
                && this.beverages.get(beverage) > 0;
    }

    public boolean isCustomerVip(@NotNull Customer customer) {
        return this.vipCustomers.contains(customer);
    }

    public boolean addSupplier(@NotNull Supplier supplier) {
        return this.suppliers.add(supplier);
    }

    public boolean markAsVIP(@NotNull Customer customer) {
        return this.vipCustomers.add(customer);
    }

    private boolean makePayment(@NotNull Supplier supplier,
                                double amount) {
        double leftovers = this.budget - amount;
        if (leftovers < 0) return false;

        supplier.receivePayment(amount);
        this.budget = leftovers;
        return true;
    }

    private void stockBeverage(@NotNull Beverage beverage) {
        if (this.beverages.computeIfPresent(
                beverage, (k, v) -> v + 1) == null) {
            this.beverages.put(beverage, 1);
        }
    }

    private void stockBeverage(@NotNull Beverage beverage,
                               int quantity) {
        if (this.beverages.computeIfPresent(
                beverage, (k, v) -> v + quantity) == null) {
            this.beverages.put(beverage, quantity);
        }
    }

    private Supplier getBestSupplier(@NotNull Beverage beverage) {
        return this.suppliers.stream()
                .filter(s -> s.hasInStock(beverage))
                .min(Comparator.comparing(s -> s.getItemPrice(beverage)))
                .orElseThrow();
    }
}
