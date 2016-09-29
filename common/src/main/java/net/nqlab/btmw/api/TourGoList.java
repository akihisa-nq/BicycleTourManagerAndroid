
package net.nqlab.btmw.api;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TourGoList {

    @SerializedName("tour_gos")
    @Expose
    private List<TourGo> tourGos = new ArrayList<TourGo>();
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("limit")
    @Expose
    private Integer limit;

    /**
     * 
     * @return
     *     The tourGos
     */
    public List<TourGo> getTourGos() {
        return tourGos;
    }

    /**
     * 
     * @param tourGos
     *     The tour_gos
     */
    public void setTourGos(List<TourGo> tourGos) {
        this.tourGos = tourGos;
    }

    /**
     * 
     * @return
     *     The totalCount
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 
     * @param totalCount
     *     The total_count
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 
     * @return
     *     The offset
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * 
     * @param offset
     *     The offset
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * 
     * @return
     *     The limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * 
     * @param limit
     *     The limit
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(tourGos).append(totalCount).append(offset).append(limit).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TourGoList) == false) {
            return false;
        }
        TourGoList rhs = ((TourGoList) other);
        return new EqualsBuilder().append(tourGos, rhs.tourGos).append(totalCount, rhs.totalCount).append(offset, rhs.offset).append(limit, rhs.limit).isEquals();
    }

}
