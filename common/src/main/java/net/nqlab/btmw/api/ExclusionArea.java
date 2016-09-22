
package net.nqlab.btmw.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ExclusionArea {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("point")
    @Expose
    private String point;

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
     *     The distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * 
     * @param distance
     *     The distance
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * 
     * @return
     *     The point
     */
    public String getPoint() {
        return point;
    }

    /**
     * 
     * @param point
     *     The point
     */
    public void setPoint(String point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(distance).append(point).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ExclusionArea) == false) {
            return false;
        }
        ExclusionArea rhs = ((ExclusionArea) other);
        return new EqualsBuilder().append(id, rhs.id).append(distance, rhs.distance).append(point, rhs.point).isEquals();
    }

}
