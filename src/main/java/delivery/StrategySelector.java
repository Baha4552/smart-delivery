package delivery;
import java.util.*;
public final class StrategySelector {
 private final Map<DeliveryPriority,RouteStrategy> strategies;
 public StrategySelector(Map<DeliveryPriority,RouteStrategy> strategies) { this.strategies=Map.copyOf(strategies); }
 public RouteStrategy select(DeliveryRequest request) {
  Objects.requireNonNull(request); DeliveryPriority key=request.priority()==DeliveryPriority.STANDARD && request.ecoPreferred()?DeliveryPriority.ECO:request.priority();
  RouteStrategy selected=strategies.get(key); if(selected==null) throw new IllegalArgumentException("No strategy registered for "+key); return selected;
 }
}
