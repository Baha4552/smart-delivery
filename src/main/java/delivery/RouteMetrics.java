package delivery;
public final class RouteMetrics {
 private RouteMetrics() {}
 public static int time(Route r) { return Math.addExact(Math.addExact(r.estimatedMinutes(),r.trafficPenaltyMinutes()),r.weatherPenaltyMinutes()); }
 public static double cost(Route r, DeliveryRequest q) {
  double surcharge=q.priority()==DeliveryPriority.URGENT ? 500 : 0;
  double value=r.baseCost()+r.distanceKm()*r.costPerKm()+surcharge+r.additionalFees();
  if(!Double.isFinite(value)) throw new IllegalArgumentException("Cost overflow"); return value;
 }
}
