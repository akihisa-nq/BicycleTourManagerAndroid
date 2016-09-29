
package net.nqlab.btmw.api;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TourGo {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tour_plan_id")
    @Expose
    private Integer tourPlanId;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("tour_go_events")
    @Expose
    private List<TourGoEvent> tourGoEvents = new ArrayList<TourGoEvent>();

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
     *     The tourPlanId
     */
    public Integer getTourPlanId() {
        return tourPlanId;
    }

    /**
     * 
     * @param tourPlanId
     *     The tour_plan_id
     */
    public void setTourPlanId(Integer tourPlanId) {
        this.tourPlanId = tourPlanId;
    }

    /**
     * 
     * @return
     *     The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The start_time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     * @return
     *     The tourGoEvents
     */
    public List<TourGoEvent> getTourGoEvents() {
        return tourGoEvents;
    }

    /**
     * 
     * @param tourGoEvents
     *     The tour_go_events
     */
    public void setTourGoEvents(List<TourGoEvent> tourGoEvents) {
        this.tourGoEvents = tourGoEvents;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(tourPlanId).append(startTime).append(tourGoEvents).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TourGo) == false) {
            return false;
        }
        TourGo rhs = ((TourGo) other);
        return new EqualsBuilder().append(id, rhs.id).append(tourPlanId, rhs.tourPlanId).append(startTime, rhs.startTime).append(tourGoEvents, rhs.tourGoEvents).isEquals();
    }

}
