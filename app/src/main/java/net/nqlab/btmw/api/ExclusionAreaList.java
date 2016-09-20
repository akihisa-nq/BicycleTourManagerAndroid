package net.nqlab.btmw.api;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ExclusionAreaList {

    @SerializedName("exclusion_areas")
    @Expose
    private List<ExclusionArea> exclusionAreas = new ArrayList<ExclusionArea>();

    /**
     * 
     * @return
     *     The exclusionAreas
     */
    public List<ExclusionArea> getExclusionAreas() {
        return exclusionAreas;
    }

    /**
     * 
     * @param exclusionAreas
     *     The exclusion_areas
     */
    public void setExclusionAreas(List<ExclusionArea> exclusionAreas) {
        this.exclusionAreas = exclusionAreas;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(exclusionAreas).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ExclusionAreaList) == false) {
            return false;
        }
        ExclusionAreaList rhs = ((ExclusionAreaList) other);
        return new EqualsBuilder().append(exclusionAreas, rhs.exclusionAreas).isEquals();
    }

}
