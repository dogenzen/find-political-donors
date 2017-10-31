package datastore;

import payloads.Contribution;
import util.MedianHeapStore;

import java.util.*;

/**
 * A generic framework to store and compute aggregate contributions
 *
 * Currently computes count, median and total contributions
 * sorted by date chronologically for each recipient sorted alphabetically
 *
 *  - Uses a map of map of Contributions
 *  - Uses a TreeMap to store the recipients sorted alphabetically
 *  - Uses a HashMap to group contributions by date
 *  - Uses a custom comparator to chronologically sort the contribution objects by its date field
 *
 * @author Pradeep Das
 * @version 27th Oct 2017
 */
public class ContributionsAggregateData
{
    private Map<String, Map<String, ContributionsByDate>> contributionsByRecipientMap;

    ContributionsAggregateData() {
        contributionsByRecipientMap = new TreeMap<>();
    }

    /**
     * Adds one contribution to the data store for a given recipient and date
     *
     * @param contribution - contribution payload
     */
    void addContribution(Contribution contribution) {
        if (contribution.getTxDate() != null) {
            contributionsByRecipientMap
                    .computeIfAbsent(contribution.getRecipientId(), key -> new HashMap<>())
                    .computeIfAbsent(contribution.getTxDateStr(), key -> new ContributionsByDate(
                            contribution.getRecipientId(), contribution.getTxDateStr(), contribution.getTxDate()))
                    .add(contribution.getTxAmount());
        }
    }

    /**
     * @return all recipients, sorted alphabetically, with at least one valid contribution date
     */
    public Set<String> getAllRecipientsSortedAlphabetically() {
        return contributionsByRecipientMap.keySet();
    }

    /**
     * @param recipientId - specified recipient whose contributions are obtained
     * @return returns contributions of a recipient aggregated by date, sorted chronologically
     */
    public List<ContributionsByDate> getAllContributionsByDateSortedChronologically(String recipientId) {
        List<ContributionsByDate> contributionsByDateList =
                new ArrayList<>(contributionsByRecipientMap.get(recipientId).values());
        Collections.sort(contributionsByDateList);
        return contributionsByDateList;
    }

    /**
     * A class that represents aggregate contributions by date for a recipient
     */
    public static class ContributionsByDate implements Comparable<ContributionsByDate>
    {
        private String recipientId;
        private String contributionDateStr;
        private Date contributionDate;
        private int totalTxCount;
        private double totalTxAmt;
        private MedianHeapStore median;

        ContributionsByDate(String recipientId, String contributionDateStr, Date contributionDate) {
            this.recipientId = recipientId;
            this.contributionDateStr = contributionDateStr;
            this.contributionDate = contributionDate;
            median = new MedianHeapStore();
        }

        void add(double amount) {
            amount *= 100;
            totalTxAmt += amount;
            totalTxCount += 1;
            median.add(amount);
        }

        public String getRecipientId() {
            return recipientId;
        }

        public String getContributionDateStr() {
            return contributionDateStr;
        }

        public int getTotalTxCount() {
            return totalTxCount;
        }

        public long getTotalTxAmt() {
            return (long) Math.rint(totalTxAmt / 100);
        }

        public long getMedian() {
            return (long) Math.rint(median.getMedian() / 100);
        }

        public int compareTo(ContributionsByDate o) {
            return this.contributionDate.compareTo(o.contributionDate);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ContributionsByDate that = (ContributionsByDate) o;

            return (recipientId != null ? recipientId.equals(that.recipientId) : that.recipientId == null) &&
                    (contributionDateStr != null ?
                            contributionDateStr.equals(that.contributionDateStr) : that.contributionDateStr == null);
        }

        @Override
        public int hashCode() {
            int result = recipientId != null ? recipientId.hashCode() : 0;
            result = 31 * result + (contributionDateStr != null ? contributionDateStr.hashCode() : 0);
            return result;
        }
    }

}
