package org.example.ofertedevacanta.models;

import java.util.Date;

public class SpecialOffer extends Entity<Double>{
    private double hotelId;
    private Date startDate;
    private Date endDate;
    private Integer percents;

    public SpecialOffer(double hotelId, Date startDate, Date endDate, Integer percents){
        this.hotelId=hotelId;
        this.startDate=startDate;
        this.endDate=endDate;
        this.percents=percents;
    }

    public double getHotelId(){
        return hotelId;
    }

    public void setHotelId(double hotelId){
        this.hotelId=hotelId;
    }

    public Date getStartDate(){
        return startDate;
    }

    public void setStartDate(Date startDate){
        this.startDate=startDate;
    }

    public Date getEndDate(){
        return endDate;
    }

    public void setEndDate(Date endDate){
        this.endDate=endDate;
    }

    public Integer getPercents(){
        return percents;
    }

    public void setPercents(Integer percents){
        this.percents=percents;
    }

    @Override
    public String toString(){
        return "SpecialOffer{" +"id="+getId()+","+
                "hotelId=" + hotelId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", percents=" + percents +
                '}';
    }
}
