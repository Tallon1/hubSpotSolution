package Entities;

import java.util.ArrayList;
import java.util.List;

public class DateAndPartner {
    private String date;
    private List<Partner> partnerList;

    public DateAndPartner() {
        partnerList = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Partner> getPartnerList() {
        return partnerList;
    }

    public void setPartnerList(List<Partner> partnerList) {
        this.partnerList = partnerList;
    }
}