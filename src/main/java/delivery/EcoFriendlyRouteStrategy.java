package delivery;
import java.util.*; import java.util.function.ToDoubleFunction;
public final class EcoFriendlyRouteStrategy extends ScoredRouteStrategy {
 private final double emissionWeight, distanceWeight;
 public EcoFriendlyRouteStrategy() { this(0.9,0.1); }
 public EcoFriendlyRouteStrategy(double e,double d) { Checks.nonnegative(e,"eco emission weight"); Checks.nonnegative(d,"eco distance weight"); if(!Double.isFinite(e+d)||e+d==0) throw new IllegalArgumentException("Eco weights"); emissionWeight=e; distanceWeight=d; }
 public String getStrategyName() { return "EcoFriendlyRouteStrategy"; }
 protected ToDoubleFunction<Route> scorer(List<Route> valid, DeliveryRequest q) { return r -> r.carbonEmission()*emissionWeight+r.distanceKm()*distanceWeight; }
 protected String formula(Route r,DeliveryRequest q,double s) { return "Emission * "+emissionWeight+" + distance * "+distanceWeight+" = "+s; }
}
