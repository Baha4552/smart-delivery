package delivery;
import java.util.*;
public record DeliveryRequest(DeliveryPriority priority, double maxBudget, int maxDeliveryMinutes, boolean ecoPreferred, double maxEmission, Set<String> prohibitedRouteIds) {
 public DeliveryRequest { Objects.requireNonNull(priority,"priority"); Checks.nonnegative(maxBudget,"budget"); Checks.nonnegative(maxEmission,"emission limit"); if(maxDeliveryMinutes<0) throw new IllegalArgumentException("deadline"); prohibitedRouteIds=Set.copyOf(prohibitedRouteIds); }
}
