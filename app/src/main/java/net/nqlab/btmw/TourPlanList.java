
package net.nqlab.btmw;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(tourPlans).toHashCode();
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
        return new EqualsBuilder().append(tourPlans, rhs.tourPlans).isEquals();
    }

}
