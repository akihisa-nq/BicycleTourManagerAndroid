
package net.nqlab.btmw.api;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TourPlanList {

    @SerializedName("tour_plans")
    @Expose
    private List<TourPlan> tourPlans = new ArrayList<TourPlan>();
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
     *     The tourPlans
     */
    public List<TourPlan> getTourPlans() {
        return tourPlans;
    }

    /**
     * 
     * @param tourPlans
     *     The tour_plans
     */
    public void setTourPlans(List<TourPlan> tourPlans) {
        this.tourPlans = tourPlans;
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
        return new HashCodeBuilder().append(tourPlans).append(totalCount).append(offset).append(limit).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TourPlanList) == false) {
            return false;
        }
        TourPlanList rhs = ((TourPlanList) other);
        return new EqualsBuilder().append(tourPlans, rhs.tourPlans).append(totalCount, rhs.totalCount).append(offset, rhs.offset).append(limit, rhs.limit).isEquals();
    }

}
