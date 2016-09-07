
package net.nqlab.btmw;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Schedules {

    @SerializedName("routes")
    @Expose
    private List<List<Route>> routes = new ArrayList<List<Route>>();

    /**
     * 
     * @return
     *     The routes
     */
    public List<List<Route>> getRoutes() {
        return routes;
    }

    /**
     * 
     * @param routes
     *     The routes
     */
    public void setRoutes(List<List<Route>> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(routes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Schedules) == false) {
            return false;
        }
        Schedules rhs = ((Schedules) other);
        return new EqualsBuilder().append(routes, rhs.routes).isEquals();
    }

}
