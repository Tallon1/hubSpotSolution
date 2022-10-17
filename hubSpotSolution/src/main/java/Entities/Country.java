package Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"attendeeCount",
                    "attendees",
                    "name",
                    "startDate"})
public class Country {
    public Country() {
        attendees = new ArrayList<>();
    }

    @JsonProperty("attendeeCount")
    private Integer attendeeCount;
    
    @JsonProperty("attendees")
    private List<String> attendees = null;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("startDate")
    private String startDate;
    
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("attendeeCount")
    public Integer getAttendeeCount() {
        return attendeeCount;
    }

    @JsonProperty("attendeeCount")
    public void setAttendeeCount(Integer attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    @JsonProperty("attendees")
    public List<String> getAttendees() {
        return attendees;
    }

    @JsonProperty("attendees")
    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}