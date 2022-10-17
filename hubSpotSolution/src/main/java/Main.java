import Entities.DateAndPartner;
import Entities.Partners;
import Entities.Countries;
import Entities.Country;
import Entities.Partner;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

public class Main {
    // Personal API Key = 44a7de63014748983e656f1fbb86
    public static String GET_URL = "https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=44a7de63014748983e656f1fbb86";
    public static String POST_URL = "https://candidate.hubteam.com/candidateTest/v3/problem/result?userKey=44a7de63014748983e656f1fbb86";
    public static Countries COUNTRIES = new Countries();

    public static void main(String[] args) throws Exception {
        String content = apiHelper.sendGet(GET_URL);
        ObjectMapper objectMapper = new ObjectMapper();
        Partners partners = objectMapper.readValue(content, Partners.class);
        
        // (1) Creating a map for <Country, Partner from this Country>
        Map<String, List<Partner>> partnerMap = createPartnerMap(partners);

        for (Map.Entry<String, List<Partner>> country : partnerMap.entrySet()) {
            // Creating a calendar for the country. The calendar will contain
            // the dates which partners are available.
            List<Partner> countryPartners = country.getValue();
            List<DateAndPartner> calendar = createCalendar(countryPartners);

            // Finds the optimal date & the max number of people attending.
            int maxPeople = 0;
            String bestDate = "";
            for (int i = 0; i < calendar.size() - 1; i++) {
                DateAndPartner firstDay = calendar.get(i);
                DateAndPartner secondDay = calendar.get(i + 1);
                int size = 0;
                for (Partner p1 : firstDay.getPartnerList()) {
                    for (Partner p2 : secondDay.getPartnerList()) {
                        if (p1.equals(p2)) {
                            size++;
                        }
                    }
                }
                if (size > maxPeople) {
                    maxPeople = size;
                    bestDate = firstDay.getDate();
                }
            }
            //Adds the country to the Countries object.
            createCountryToReturn(maxPeople, country.getKey(), bestDate, calendar);
        }

        String response = objectMapper.writeValueAsString(COUNTRIES);
        apiHelper.sendPost(POST_URL, response);
    }

    /**
     * Adds the country to the COUNTRIES object
     * @param maxPeople - Number of partners able to attend.
     * @param countryName - Name of country.
     * @param bestDate - Optimal date in the calendar for attendees.
     * @param calendar - Calendar for the country.
     */
    private static void createCountryToReturn(int maxPeople, String countryName,
            String bestDate, List<DateAndPartner> calendar) {
        Country returnCountry = new Country();
        returnCountry.setName(countryName);

        if (maxPeople == 0) {
            returnCountry.setAttendeeCount(0);
            returnCountry.setStartDate(bestDate);
        } else {
            // Creates a return object.
            for (DateAndPartner date : calendar) {
                if (date.getDate().equals(bestDate)) {
                    returnCountry.setAttendeeCount(date.getPartnerList().size());
                    List<String> partnerList = new ArrayList<>();
                    
                    for (Partner partner : date.getPartnerList()) {
                        partnerList.add(partner.getEmail());
                    }
                    
                    returnCountry.setAttendees(partnerList);
                    returnCountry.setStartDate(bestDate);
                    COUNTRIES.getCountries().add(returnCountry);
                    break;
                }
            }
        }
    }

    /**
     * Creates a map where keys are COUNTRIES & values are partners from their COUNTRIES.
     * @param partners - Parsed list of partners from the JSON input file.
     * @return Map - <Country, Partners>
     */
    private static Map<String, List<Partner>> createPartnerMap(Partners partners) {
        Map<String, List<Partner>> partnerMap = new HashMap<>();
        for (Partner partner : partners.getPartners()) {
            if (partnerMap.get(partner.getCountry()) != null) {
                partnerMap.get(partner.getCountry()).add(partner);
            } else {
                List<Partner> partnerList = new ArrayList<>();
                partnerList.add(partner);
                partnerMap.put(partner.getCountry(), partnerList);
            }
        }
        return partnerMap;
    }

    /**
     * Creates the calendar for each given country.
     * This calendar contains dates where each date has its own attendee(s).
     * @param countryPartners - Partners from the given country.
     * @return calendar
     */
    private static List<DateAndPartner> createCalendar(List<Partner> countryPartners) {
        List<DateAndPartner> calendar = new ArrayList<>();
        //Gets all the available dates from each partner in the given country.
        Set<String> dates = new HashSet<>();
        
        for (Partner partner : countryPartners) {
            dates.addAll(partner.getAvailableDates());
        }
        
        for (String date : dates) {
            DateAndPartner dateAndPartner = new DateAndPartner();
            dateAndPartner.setDate(date);
            calendar.add(dateAndPartner);
        }

        // Sorts through the calendar.
        Collections.sort(calendar, (o1, o2) -> {
            Integer day1 = Integer.valueOf(o1.getDate().replace("-", ""));
            Integer day2 = Integer.valueOf(o2.getDate().replace("-", ""));
            if (day1 > day2) return 1;
            else return -1;
        });

        // Adds all of the partners to partnerList for each date.
        for (Partner partner : countryPartners) {
            List<String> availableDates = partner.getAvailableDates();
            for (String date : availableDates) {
                for (DateAndPartner dateAndPartner : calendar) {
                    if (dateAndPartner.getDate().equals(date)) {
                        dateAndPartner.getPartnerList().add(partner);
                        break;
                    }
                }
            }
        }
        return calendar;
    }
}