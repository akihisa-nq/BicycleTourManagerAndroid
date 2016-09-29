
package net.nqlab.btmw.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TourGoEvent {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tour_go_id")
    @Expose
    private Integer tourGoId;
    @SerializedName("occured_on")
    @Expose
    private String occuredOn;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("tour_plan_point_id")
    @Expose
    private Integer tourPlanPointId;

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
     *     The tourGoId
     */
    public Integer getTourGoId() {
        return tourGoId;
    }

    /**
     * 
     * @param tourGoId
     *     The tour_go_id
     */
    public void setTourGoId(Integer tourGoId) {
        this.tourGoId = tourGoId;
    }

    /**
     * 
     * @return
     *     The occuredOn
     */
    public String getOccuredOn() {
        return occuredOn;
    }

    /**
     * 
     * @param occuredOn
     *     The occured_on
     */
    public void setOccuredOn(String occuredOn) {
        this.occuredOn = occuredOn;
    }

    /**
     * 
     * @return
     *     The eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * 
     * @param eventType
     *     The event_type
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * 
     * @return
     *     The tourPlanPointId
     */
    public Integer getTourPlanPointId() {
        return tourPlanPointId;
    }

    /**
     * 
     * @param tourPlanPointId
     *     The tour_plan_point_id
     */
    public void setTourPlanPointId(Integer tourPlanPointId) {
        this.tourPlanPointId = tourPlanPointId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(tourGoId).append(occuredOn).append(eventType).append(tourPlanPointId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TourGoEvent) == false) {
            return false;
        }
        TourGoEvent rhs = ((TourGoEvent) other);
        return new EqualsBuilder().append(id, rhs.id).append(tourGoId, rhs.tourGoId).append(occuredOn, rhs.occuredOn).append(eventType, rhs.eventType).append(tourPlanPointId, rhs.tourPlanPointId).isEquals();
    }

}
