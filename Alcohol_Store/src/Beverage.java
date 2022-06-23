import org.jetbrains.annotations.NotNull;
import lombok.AllArgsConstructor;
import java.util.Comparator;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Beverage implements Comparable<Beverage> {
    private final String name;
    @Setter
    private double price;

    @Override
    public String toString() {
        return String.format("%s, price:%s", this.name, this.price);
    }

    @Override
    public int compareTo(@NotNull Beverage o) {
        return Comparator
                .comparing(Beverage::getName)
                .thenComparing(Beverage::getPrice)
                .reversed()
                .compare(this, o);
    }
}
