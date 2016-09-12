
package net.nqlab.btmw;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TourPlanScheduleRoute {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tour_plan_schedule_points")
    @Expose
    private List<TourPlanSchedulePoint> tourPlanSchedulePoints = new ArrayList<TourPlanSchedulePoint>();

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The tourPlanSchedulePoints
     */
    public List<TourPlanSchedulePoint> getTourPlanSchedulePoints() {
        return tourPlanSchedulePoints;
    }

    /**
     * 
     * @param tourPlanSchedulePoints
     *     The tour_plan_schedule_points
     */
    public void setTourPlanSchedulePoints(List<TourPlanSchedulePoint> tourPlanSchedulePoints) {
        this.tourPlanSchedulePoints = tourPlanSchedulePoints;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(tourPlanSchedulePoints).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TourPlanScheduleRoute) == false) {
            return false;
        }
        TourPlanScheduleRoute rhs = ((TourPlanScheduleRoute) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(tourPlanSchedulePoints, rhs.tourPlanSchedulePoints).isEquals();
    }

}
