package datastore;

import payloads.Contribution;
import util.MedianHeapStore;

import java.util.HashMap;
import java.util.Map;

/**
 * A generic framework to store and compute running contributions
 *
 * Currently computes running count, median and total contributions for each recipient
 *
 * @author Pradeep Das
 * @version 27th Oct 2017
 */
public class ContributionsRunningData
{
    private ContributionsByZip currentContributionsByZip;
    private Map<String, ContributionsByZip> contributionsByZipMap;

    ContributionsRunningData() {
        contributionsByZipMap = new HashMap<>();
    }

    void resetCurrentData() {
        currentContributionsByZip = null;
    }

    /**
     * Adds one contribution to the data store for a given recipient and zipcode
     *
     * @param contribution - contribution payload
     */
    void addContribution(Contribution contribution) {
        if (contribution.getContributorZip() != null) {
            currentContributionsByZip = contributionsByZipMap
                    .computeIfAbsent(contribution.getRecipientId() + "_" + contribution.getContributorZip(),
                                      key -> new ContributionsByZip()).add(contribution.getTxAmount());
        }
    }

    public ContributionsByZip getCurrentContributionsByZip() {
        return currentContributionsByZip;
    }

    /**
     * A class that represents running contributions by zipcode for a recipient
     */
    public static class ContributionsByZip {
        private int runningTotalTxCount;
        private double runningTotalTxAmt;
        private MedianHeapStore runningMedian;

        ContributionsByZip() {
            runningMedian = new MedianHeapStore();
        }

        ContributionsByZip add(double amount) {
            double amountInCents = amount*100;
            runningTotalTxAmt += amountInCents;
            runningTotalTxCount += 1;
            runningMedian.add(amountInCents);
            return this;
        }

        public int getRunningTotalTxCount() {
            return runningTotalTxCount;
        }

        public long getRunningTotalTxAmt() {
            return (long) Math.rint(runningTotalTxAmt / 100);
        }

        public long getRunningMedian() {
            return (long) Math.rint(runningMedian.getMedian() / 100);
        }
    }
}
