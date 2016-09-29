
package net.nqlab.btmw.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TourGoDeleteResult {

    @SerializedName("result")
    @Expose
    private Boolean result;

    /**
     * 
     * @return
     *     The result
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * 
     * @param result
     *     The result
     */
    public void setResult(Boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(result).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TourGoDeleteResult) == false) {
            return false;
        }
        TourGoDeleteResult rhs = ((TourGoDeleteResult) other);
        return new EqualsBuilder().append(result, rhs.result).isEquals();
    }

}
