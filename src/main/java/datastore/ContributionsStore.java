package datastore;

import payloads.Contribution;

/**
 * A generic framework to store/compute running and aggregate contributions
 *
 * @author Pradeep Das
 * @version 27th Oct 2017
 */
public class ContributionsStore
{
    private ContributionsRunningData contributionsRunningData;
    private ContributionsAggregateData contributionsAggregateData;

    public ContributionsStore() {
        contributionsRunningData = new ContributionsRunningData();
        contributionsAggregateData = new ContributionsAggregateData();
    }

    /**
     *
     * @param contribution - a single contribution payload
     * @return returns a running contribution data at the end of processing each contribution
     */
    public ContributionsRunningData addContribution(Contribution contribution) {
        contributionsRunningData.resetCurrentData();

        contributionsRunningData.addContribution(contribution);
        contributionsAggregateData.addContribution(contribution);

        return contributionsRunningData;
    }

    public ContributionsAggregateData getContributionsAggregateData() {
        return contributionsAggregateData;
    }
}
