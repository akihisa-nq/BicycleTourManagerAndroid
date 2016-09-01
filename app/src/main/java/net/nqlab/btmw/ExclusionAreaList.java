
package net.nqlab.btmw;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
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

}
