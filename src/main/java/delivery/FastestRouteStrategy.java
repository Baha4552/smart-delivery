package delivery;
import java.util.*; import java.util.function.ToDoubleFunction;
public final class FastestRouteStrategy extends ScoredRouteStrategy {
 public String getStrategyName() { return "FastestRouteStrategy"; }
 protected ToDoubleFunction<Route> scorer(List<Route> valid, DeliveryRequest q) { return r -> RouteMetrics.time(r); }
 protected String formula(Route r, DeliveryRequest q, double s) { return "Base time="+r.estimatedMinutes()+", traffic penalty="+r.trafficPenaltyMinutes()+", weather penalty="+r.weatherPenaltyMinutes()+", final time="+s; }
}
