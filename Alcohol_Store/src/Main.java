import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        var beer = new Beverage("beer", 45.80);
        var wine = new Beverage("wine", 213.42);

        var customer_1 = new Customer("Alex Boyko", 456.78);
        var customer_2 = new Customer("Gram Potter", 23.1);

        var supplier_1 = new Supplier("Harry Jenkins", beer);
        var supplier_2 = new Supplier("Dennis Ritchie", wine, beer);
        var supplier_3 = Supplier.of("Alex Bale", Map.entry(beer, 34.67));

        var store = AlcoStore.storeBuilder()
                .name("U Borisa")
                .budget(13124.59)
                .vipCustomers(new HashSet<>(
                        Set.of(customer_1, customer_2))
                )
                .beverages(new HashMap<>(
                                Map.ofEntries(
                                        Map.entry(beer, 5),
                                        Map.entry(wine, 2))
                        )
                )
                .suppliers(new HashSet<>(
                        Set.of(supplier_1, supplier_2, supplier_3))
                )
                .build();

        Main.testScenario(store, customer_1, beer, wine);

        AlcoStoreJsonSerializator.testGson(store, "AlcoStore_2.json");
    }

    public static void testScenario(@NotNull AlcoStore store,
                                    Customer customer,
                                    Beverage beer, Beverage wine) {
        System.out.println(store.buyBeverages(customer, wine, wine, beer));
        System.out.println(store.getBeverages());
        System.out.println(store.getBudget());
        System.out.println(customer.getBalance());

        System.out.println();
        store.orderBeverage(beer, 4, 500);
        System.out.println(store.getBeverages());
        System.out.println(store.getBudget());
    }
}