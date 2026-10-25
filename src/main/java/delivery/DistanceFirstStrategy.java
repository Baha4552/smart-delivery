package delivery;
import java.util.*; import java.util.function.ToDoubleFunction;
public final class DistanceFirstStrategy extends ScoredRouteStrategy {
 public String getStrategyName() { return "DistanceFirstStrategy"; }
 protected ToDoubleFunction<Route> scorer(List<Route> valid,DeliveryRequest q) { return Route::distanceKm; }
 protected String formula(Route r,DeliveryRequest q,double s) { return "Minimum feasible distance = "+s+" km"; }
}
