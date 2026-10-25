package delivery;
import java.util.*; import java.util.function.ToDoubleFunction;
public final class CheapestRouteStrategy extends ScoredRouteStrategy {
 public String getStrategyName() { return "CheapestRouteStrategy"; }
 protected ToDoubleFunction<Route> scorer(List<Route> valid, DeliveryRequest q) { return r -> RouteMetrics.cost(r,q); }
 protected String formula(Route r, DeliveryRequest q, double s) { return "base + distance * rate + priority surcharge + fees = "+r.baseCost()+" + "+r.distanceKm()+" * "+r.costPerKm()+" + "+(q.priority()==DeliveryPriority.URGENT?500:0)+" + "+r.additionalFees()+" = "+s; }
}
