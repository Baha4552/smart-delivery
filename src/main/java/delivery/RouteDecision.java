package delivery;
import java.util.*;
public record RouteDecision(String strategyName, Route route, double score, int finalMinutes, double totalCost, String explanation, Map<String,String> rejectedRoutes) {
 public RouteDecision { rejectedRoutes=Collections.unmodifiableMap(new LinkedHashMap<>(rejectedRoutes)); }
}
