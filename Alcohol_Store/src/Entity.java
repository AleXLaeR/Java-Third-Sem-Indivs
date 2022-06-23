import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.*;

@ToString
public class Entity {
    @Getter
    protected String name;
    @Getter @Setter
    protected double balance;
    @Setter
    protected String type = "Entity";

    public Entity(@NotNull @NonNls String name, double balance) {
        this.name = name;
        this.balance = balance;
    }
}
