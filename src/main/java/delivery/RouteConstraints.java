package delivery;
import java.util.*;
public final class RouteConstraints {
 public String rejection(Route r, DeliveryRequest q) {
  List<String> reasons=new ArrayList<>();
  if(!r.available()) reasons.add("Unavailable");
  if(RouteMetrics.cost(r,q)>q.maxBudget()) reasons.add("Budget exceeded");
  if(RouteMetrics.time(r)>q.maxDeliveryMinutes()) reasons.add("Deadline exceeded");
  if(r.carbonEmission()>q.maxEmission()) reasons.add("Emission limit exceeded");
  if(q.prohibitedRouteIds().contains(r.id())) reasons.add("Prohibited route");
  return String.join("; ",reasons);
 }
}
