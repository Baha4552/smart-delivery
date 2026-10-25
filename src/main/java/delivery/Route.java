package delivery;
import java.util.Objects;
public record Route(String id, Location origin, Location destination, double distanceKm, int estimatedMinutes, double baseCost, double costPerKm, double additionalFees, double carbonEmission, int trafficPenaltyMinutes, int weatherPenaltyMinutes, boolean available) {
 public Route { Checks.text(id); Objects.requireNonNull(origin); Objects.requireNonNull(destination); Checks.nonnegative(distanceKm,"distance"); Checks.nonnegative(baseCost,"base cost"); Checks.nonnegative(costPerKm,"rate"); Checks.nonnegative(additionalFees,"fees"); Checks.nonnegative(carbonEmission,"emission"); if(estimatedMinutes<0 || trafficPenaltyMinutes<0 || weatherPenaltyMinutes<0) throw new IllegalArgumentException("time"); }
}
