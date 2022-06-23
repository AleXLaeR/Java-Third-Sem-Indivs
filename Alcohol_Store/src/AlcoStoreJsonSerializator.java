import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.regex.*;

public class AlcoStoreJsonSerializator {
    public static void testGson(@NotNull AlcoStore store,
                                @NotNull String filename) {
        var adapter = RuntimeTypeAdapterFactory.of(Entity.class, "type")
                .registerSubtype(Customer.class)
                .registerSubtype(Supplier.class);

        var gson = new GsonBuilder()
                .registerTypeAdapterFactory(adapter)
                .setPrettyPrinting()
                .create();

        try (var fr = new FileReader(filename)) {
            Files.writeString(Path.of(filename), gson.toJson(store));

            var jsonObject = JsonParser.parseReader(fr).getAsJsonObject();

            System.out.println("\nInitial: " + store + "\n");
            System.out.println("Deserialized: " + DeserializeObject(gson, jsonObject));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static AlcoStore DeserializeObject(@NotNull Gson gson,
                                               @NotNull JsonObject jsonObject) {
        return AlcoStore.storeBuilder()
                .name(jsonObject.get("name").getAsString())
                .budget(jsonObject.get("budget").getAsDouble())
                .beverages(getDeserializedBeverages(jsonObject))
                .suppliers(getDeserializedSuppliers(jsonObject))
                .vipCustomers(getDeserializedCustomers(gson, jsonObject))
                .build();
    }
    private static Map<Beverage, Integer> getDeserializedBeverages(@NotNull JsonObject jsonObject) {
        var beveragesJson = jsonObject.get("beverages").toString();
        Pattern bevPattern = Pattern.compile("(?<name>\\w\\D+),.+?:(?<price>\\d+\\.\\d+)\":(?<count>\\d+)");
        Matcher bevMatcher = bevPattern.matcher(beveragesJson);

        TreeMap<Beverage, Integer> beverages = new TreeMap<>();
        while (bevMatcher.find()) {
            beverages.put(
                    new Beverage(
                            bevMatcher.group("name"),
                            Double.parseDouble(bevMatcher.group("price"))
                    ), Integer.parseInt(bevMatcher.group("count")));
        }
        return beverages;
    }

    private static HashSet<Customer> getDeserializedCustomers(@NotNull Gson gson,
                                                              @NotNull JsonObject jsonObject) {
        return gson.fromJson(
                jsonObject.getAsJsonArray("vipCustomers"),
                new TypeToken<Set<Entity>>() {}.getType()
        );
    }
    private static HashSet<Supplier> getDeserializedSuppliers(@NotNull JsonObject jsonObject) {
        var suppliers = new HashSet<Supplier>();
        var suppliersJson = jsonObject.getAsJsonArray("suppliers");

        Pattern supBevPattern = Pattern.compile("\"(?<name>\\w\\D+),.+?\":(?<supPrice>\\d+\\.\\d+)");

        for (int i = 0; i < suppliersJson.size(); ++i) {
            JsonObject supplierJson = suppliersJson.get(i).getAsJsonObject();

            String name = supplierJson.get("name").getAsString();
            double balance = supplierJson.get("balance").getAsDouble();
            String supBeveragesJson = supplierJson.get("availableBeverages").toString();

            Matcher supBevMatcher = supBevPattern.matcher(supBeveragesJson);

            TreeMap<Beverage, Double> beverages = new TreeMap<>();
            while (supBevMatcher.find()) {
                beverages.put(
                        new Beverage(
                                supBevMatcher.group("name"),
                                Double.parseDouble(supBevMatcher.group("supPrice"))
                        ), Double.parseDouble(supBevMatcher.group("supPrice"))
                );
            }
            suppliers.add(Supplier.of(name, balance, beverages));
        }
        return suppliers;
    }
}
