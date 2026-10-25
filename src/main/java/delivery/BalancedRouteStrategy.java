package delivery;
import java.util.*; import java.util.function.ToDoubleFunction;
public final class BalancedRouteStrategy extends ScoredRouteStrategy {
 private final double time,cost,distance,emission;
 public BalancedRouteStrategy(double t,double c,double d,double e) { for(double w:new double[]{t,c,d,e}) Checks.nonnegative(w,"weight"); if(Math.abs(t+c+d+e-1)>1e-9) throw new IllegalArgumentException("Weights must sum to one"); time=t; cost=c; distance=d; emission=e; }
 public String getStrategyName() { return "BalancedRouteStrategy"; }
 private ToDoubleFunction<Route> normalized(List<Route> routes,ToDoubleFunction<Route> metric) {
  double min=routes.stream().mapToDouble(metric).min().orElseThrow(), max=routes.stream().mapToDouble(metric).max().orElseThrow();
  return r -> max==min ? 0 : (metric.applyAsDouble(r)-min)/(max-min);
 }
 protected ToDoubleFunction<Route> scorer(List<Route> valid,DeliveryRequest q) {
  var t=normalized(valid,r->RouteMetrics.time(r)); var c=normalized(valid,r->RouteMetrics.cost(r,q)); var d=normalized(valid,Route::distanceKm); var e=normalized(valid,Route::carbonEmission);
  return r->time*t.applyAsDouble(r)+cost*c.applyAsDouble(r)+distance*d.applyAsDouble(r)+emission*e.applyAsDouble(r);
 }
 protected String formula(Route r,DeliveryRequest q,double s) { return "Min-max normalized time/cost/distance/emission weights="+time+"/"+cost+"/"+distance+"/"+emission+", score="+s; }
}
