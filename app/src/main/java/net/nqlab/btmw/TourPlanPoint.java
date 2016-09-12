
package net.nqlab.btmw;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TourPlanPoint {

    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("rest_time")
    @Expose
    private Double restTime;
    @SerializedName("target_speed")
    @Expose
    private Double targetSpeed;
    @SerializedName("limit_speed")
    @Expose
    private Double limitSpeed;
    @SerializedName("elevation")
    @Expose
    private Double elevation;
    @SerializedName("distance_addition")
    @Expose
    private Double distanceAddition;
    @SerializedName("target_time_addition")
    @Expose
    private Integer targetTimeAddition;
    @SerializedName("limit_time_addition")
    @Expose
    private Integer limitTimeAddition;
    @SerializedName("road_nw")
    @Expose
    private String roadNw;
    @SerializedName("road_n")
    @Expose
    private String roadN;
    @SerializedName("road_ne")
    @Expose
    private String roadNe;
    @SerializedName("road_w")
    @Expose
    private String roadW;
    @SerializedName("road_e")
    @Expose
    private String roadE;
    @SerializedName("road_sw")
    @Expose
    private String roadSw;
    @SerializedName("road_s")
    @Expose
    private String roadS;
    @SerializedName("road_se")
    @Expose
    private String roadSe;
    @SerializedName("direction_image")
    @Expose
    private String directionImage;
    @SerializedName("pc_total_distance")
    @Expose
    private Double pcTotalDistance;
    @SerializedName("total_distance")
    @Expose
    private Double totalDistance;
    @SerializedName("pc_total_target_time")
    @Expose
    private String pcTotalTargetTime;
    @SerializedName("pc_total_limit_time")
    @Expose
    private String pcTotalLimitTime;
    @SerializedName("pass")
    @Expose
    private Boolean pass;

    /**
     * 
     * @return
     *     The name
     */
    public Object getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(Object name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * 
     * @param comment
     *     The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 
     * @return
     *     The restTime
     */
    public Double getRestTime() {
        return restTime;
    }

    /**
     * 
     * @param restTime
     *     The rest_time
     */
    public void setRestTime(Double restTime) {
        this.restTime = restTime;
    }

    /**
     * 
     * @return
     *     The targetSpeed
     */
    public Double getTargetSpeed() {
        return targetSpeed;
    }

    /**
     * 
     * @param targetSpeed
     *     The target_speed
     */
    public void setTargetSpeed(Double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    /**
     * 
     * @return
     *     The limitSpeed
     */
    public Double getLimitSpeed() {
        return limitSpeed;
    }

    /**
     * 
     * @param limitSpeed
     *     The limit_speed
     */
    public void setLimitSpeed(Double limitSpeed) {
        this.limitSpeed = limitSpeed;
    }

    /**
     * 
     * @return
     *     The elevation
     */
    public Double getElevation() {
        return elevation;
    }

    /**
     * 
     * @param elevation
     *     The elevation
     */
    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    /**
     * 
     * @return
     *     The distanceAddition
     */
    public Double getDistanceAddition() {
        return distanceAddition;
    }

    /**
     * 
     * @param distanceAddition
     *     The distance_addition
     */
    public void setDistanceAddition(Double distanceAddition) {
        this.distanceAddition = distanceAddition;
    }

    /**
     * 
     * @return
     *     The targetTimeAddition
     */
    public Integer getTargetTimeAddition() {
        return targetTimeAddition;
    }

    /**
     * 
     * @param targetTimeAddition
     *     The target_time_addition
     */
    public void setTargetTimeAddition(Integer targetTimeAddition) {
        this.targetTimeAddition = targetTimeAddition;
    }

    /**
     * 
     * @return
     *     The limitTimeAddition
     */
    public Integer getLimitTimeAddition() {
        return limitTimeAddition;
    }

    /**
     * 
     * @param limitTimeAddition
     *     The limit_time_addition
     */
    public void setLimitTimeAddition(Integer limitTimeAddition) {
        this.limitTimeAddition = limitTimeAddition;
    }

    /**
     * 
     * @return
     *     The roadNw
     */
    public String getRoadNw() {
        return roadNw;
    }

    /**
     * 
     * @param roadNw
     *     The road_nw
     */
    public void setRoadNw(String roadNw) {
        this.roadNw = roadNw;
    }

    /**
     * 
     * @return
     *     The roadN
     */
    public String getRoadN() {
        return roadN;
    }

    /**
     * 
     * @param roadN
     *     The road_n
     */
    public void setRoadN(String roadN) {
        this.roadN = roadN;
    }

    /**
     * 
     * @return
     *     The roadNe
     */
    public String getRoadNe() {
        return roadNe;
    }

    /**
     * 
     * @param roadNe
     *     The road_ne
     */
    public void setRoadNe(String roadNe) {
        this.roadNe = roadNe;
    }

    /**
     * 
     * @return
     *     The roadW
     */
    public String getRoadW() {
        return roadW;
    }

    /**
     * 
     * @param roadW
     *     The road_w
     */
    public void setRoadW(String roadW) {
        this.roadW = roadW;
    }

    /**
     * 
     * @return
     *     The roadE
     */
    public String getRoadE() {
        return roadE;
    }

    /**
     * 
     * @param roadE
     *     The road_e
     */
    public void setRoadE(String roadE) {
        this.roadE = roadE;
    }

    /**
     * 
     * @return
     *     The roadSw
     */
    public String getRoadSw() {
        return roadSw;
    }

    /**
     * 
     * @param roadSw
     *     The road_sw
     */
    public void setRoadSw(String roadSw) {
        this.roadSw = roadSw;
    }

    /**
     * 
     * @return
     *     The roadS
     */
    public String getRoadS() {
        return roadS;
    }

    /**
     * 
     * @param roadS
     *     The road_s
     */
    public void setRoadS(String roadS) {
        this.roadS = roadS;
    }

    /**
     * 
     * @return
     *     The roadSe
     */
    public String getRoadSe() {
        return roadSe;
    }

    /**
     * 
     * @param roadSe
     *     The road_se
     */
    public void setRoadSe(String roadSe) {
        this.roadSe = roadSe;
    }

    /**
     * 
     * @return
     *     The directionImage
     */
    public String getDirectionImage() {
        return directionImage;
    }

    /**
     * 
     * @param directionImage
     *     The direction_image
     */
    public void setDirectionImage(String directionImage) {
        this.directionImage = directionImage;
    }

    /**
     * 
     * @return
     *     The pcTotalDistance
     */
    public Double getPcTotalDistance() {
        return pcTotalDistance;
    }

    /**
     * 
     * @param pcTotalDistance
     *     The pc_total_distance
     */
    public void setPcTotalDistance(Double pcTotalDistance) {
        this.pcTotalDistance = pcTotalDistance;
    }

    /**
     * 
     * @return
     *     The totalDistance
     */
    public Double getTotalDistance() {
        return totalDistance;
    }

    /**
     * 
     * @param totalDistance
     *     The total_distance
     */
    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    /**
     * 
     * @return
     *     The pcTotalTargetTime
     */
    public String getPcTotalTargetTime() {
        return pcTotalTargetTime;
    }

    /**
     * 
     * @param pcTotalTargetTime
     *     The pc_total_target_time
     */
    public void setPcTotalTargetTime(String pcTotalTargetTime) {
        this.pcTotalTargetTime = pcTotalTargetTime;
    }

    /**
     * 
     * @return
     *     The pcTotalLimitTime
     */
    public String getPcTotalLimitTime() {
        return pcTotalLimitTime;
    }

    /**
     * 
     * @param pcTotalLimitTime
     *     The pc_total_limit_time
     */
    public void setPcTotalLimitTime(String pcTotalLimitTime) {
        this.pcTotalLimitTime = pcTotalLimitTime;
    }

    /**
     * 
     * @return
     *     The pass
     */
    public Boolean getPass() {
        return pass;
    }

    /**
     * 
     * @param pass
     *     The pass
     */
    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(comment).append(restTime).append(targetSpeed).append(limitSpeed).append(elevation).append(distanceAddition).append(targetTimeAddition).append(limitTimeAddition).append(roadNw).append(roadN).append(roadNe).append(roadW).append(roadE).append(roadSw).append(roadS).append(roadSe).append(directionImage).append(pcTotalDistance).append(totalDistance).append(pcTotalTargetTime).append(pcTotalLimitTime).append(pass).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TourPlanPoint) == false) {
            return false;
        }
        TourPlanPoint rhs = ((TourPlanPoint) other);
        return new EqualsBuilder().append(name, rhs.name).append(comment, rhs.comment).append(restTime, rhs.restTime).append(targetSpeed, rhs.targetSpeed).append(limitSpeed, rhs.limitSpeed).append(elevation, rhs.elevation).append(distanceAddition, rhs.distanceAddition).append(targetTimeAddition, rhs.targetTimeAddition).append(limitTimeAddition, rhs.limitTimeAddition).append(roadNw, rhs.roadNw).append(roadN, rhs.roadN).append(roadNe, rhs.roadNe).append(roadW, rhs.roadW).append(roadE, rhs.roadE).append(roadSw, rhs.roadSw).append(roadS, rhs.roadS).append(roadSe, rhs.roadSe).append(directionImage, rhs.directionImage).append(pcTotalDistance, rhs.pcTotalDistance).append(totalDistance, rhs.totalDistance).append(pcTotalTargetTime, rhs.pcTotalTargetTime).append(pcTotalLimitTime, rhs.pcTotalLimitTime).append(pass, rhs.pass).isEquals();
    }

}
