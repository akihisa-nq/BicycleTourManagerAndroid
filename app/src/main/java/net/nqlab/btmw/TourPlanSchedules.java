
package net.nqlab.btmw;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TourPlanSchedules {

    @SerializedName("tour_plan_schedule_routes")
    @Expose
    private List<TourPlanScheduleRoute> tourPlanScheduleRoutes = new ArrayList<TourPlanScheduleRoute>();

    /**
     * 
     * @return
     *     The tourPlanScheduleRoutes
     */
    public List<TourPlanScheduleRoute> getTourPlanScheduleRoutes() {
        return tourPlanScheduleRoutes;
    }

    /**
     * 
     * @param tourPlanScheduleRoutes
     *     The tour_plan_schedule_routes
     */
    public void setTourPlanScheduleRoutes(List<TourPlanScheduleRoute> tourPlanScheduleRoutes) {
        this.tourPlanScheduleRoutes = tourPlanScheduleRoutes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(tourPlanScheduleRoutes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TourPlanSchedules) == false) {
            return false;
        }
        TourPlanSchedules rhs = ((TourPlanSchedules) other);
        return new EqualsBuilder().append(tourPlanScheduleRoutes, rhs.tourPlanScheduleRoutes).isEquals();
    }

}
