
package net.nqlab.btmw;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TourPlan {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("published")
    @Expose
    private Boolean published;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("time_zone")
    @Expose
    private String timeZone;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("elevation")
    @Expose
    private Integer elevation;
    @SerializedName("resource_set_id")
    @Expose
    private Integer resourceSetId;
    @SerializedName("distance")
    @Expose
    private Integer distance;

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
     *     The published
     */
    public Boolean getPublished() {
        return published;
    }

    /**
     * 
     * @param published
     *     The published
     */
    public void setPublished(Boolean published) {
        this.published = published;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     *     The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     * @param updatedAt
     *     The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 
     * @return
     *     The timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * 
     * @param timeZone
     *     The time_zone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
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
     *     The elevation
     */
    public Integer getElevation() {
        return elevation;
    }

    /**
     * 
     * @param elevation
     *     The elevation
     */
    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    /**
     * 
     * @return
     *     The resourceSetId
     */
    public Integer getResourceSetId() {
        return resourceSetId;
    }

    /**
     * 
     * @param resourceSetId
     *     The resource_set_id
     */
    public void setResourceSetId(Integer resourceSetId) {
        this.resourceSetId = resourceSetId;
    }

    /**
     * 
     * @return
     *     The distance
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * 
     * @param distance
     *     The distance
     */
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(published).append(createdAt).append(updatedAt).append(timeZone).append(startTime).append(elevation).append(resourceSetId).append(distance).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TourPlan) == false) {
            return false;
        }
        TourPlan rhs = ((TourPlan) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(published, rhs.published).append(createdAt, rhs.createdAt).append(updatedAt, rhs.updatedAt).append(timeZone, rhs.timeZone).append(startTime, rhs.startTime).append(elevation, rhs.elevation).append(resourceSetId, rhs.resourceSetId).append(distance, rhs.distance).isEquals();
    }

}
