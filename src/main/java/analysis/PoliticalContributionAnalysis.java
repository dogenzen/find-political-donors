package analysis;

import datastore.ContributionsAggregateData;
import datastore.ContributionsRunningData;
import datastore.ContributionsStore;
import payloads.Contribution;

import java.io.PrintWriter;
import java.util.*;

/**
 * Analyzes a stream of donations to compute the following:
 *  - running median and total contribution by zipcode
 *  - median and total contribution by date for each recipient
 *
 *  This class delegates to ContributionStore, which provides
 *  a generic framework to compute any running and aggregate
 *  contributions found on Federal Election Commission (FEC) website
 *
 *  Finally it outputs the computed results to specified files
 *
 * @author Pradeep Das
 * @version 27th Oct 2017
 */
public class PoliticalContributionAnalysis
{
    private PrintWriter runningContributionsOutputFile;
    private PrintWriter aggregateContributionsOutputFile;

    private ContributionsStore contributionsStore;

    /**
     * Constructor that accepts files to output running and aggregate computation results
     *
     * @param runningContributionsOutputFile file to output running results
     * @param aggregateContributionsOutputFile file to output aggreagte results
     */
    public PoliticalContributionAnalysis(PrintWriter runningContributionsOutputFile,
                                         PrintWriter aggregateContributionsOutputFile) {
        this.contributionsStore = new ContributionsStore();
        this.runningContributionsOutputFile = runningContributionsOutputFile;
        this.aggregateContributionsOutputFile = aggregateContributionsOutputFile;
    }

    /**
     * Processes each contribution record
     * - parses to validate the data
     * - stores and computes running data
     * - stores and computes aggregate data
     * @param contributionRecord each contribution record
     */
    public void processEachContribution(String contributionRecord) {
        Contribution contribution;
        try {
            contribution = new Contribution(contributionRecord);
            ContributionsRunningData contributionsRunningData = contributionsStore.addContribution(contribution);
            outputRunningContributionsToFile(contribution, contributionsRunningData);
        } catch (RuntimeException e) {
            // log exception
            // e.printStackTrace();
        }
    }

    /**
     * Processes aggregate data at the end, after every record is computed
     */
    public void processAllContributions() {
        ContributionsAggregateData contributionsAggregateData = contributionsStore.getContributionsAggregateData();
        Set<String> recipientList = contributionsAggregateData.getAllRecipientsSortedAlphabetically();
        for (String recipient : recipientList) {
            List<ContributionsAggregateData.ContributionsByDate> contributionsByDateList =
                    contributionsAggregateData.getAllContributionsByDateSortedChronologically(recipient);
            for (ContributionsAggregateData.ContributionsByDate contributionsByDate : contributionsByDateList) {
                outputAggregateContributionsToFile(contributionsByDate);
            }
        }
    }

    private void outputRunningContributionsToFile(Contribution contribution,
                                                  ContributionsRunningData contributionsRunningData) {
        ContributionsRunningData.ContributionsByZip contributionsByZip =
                contributionsRunningData.getCurrentContributionsByZip();
        if (contributionsByZip == null)
            return;
        String outRow = contribution.getRecipientId() + "|" + contribution.getContributorZip() + "|"
                + contributionsByZip.getRunningMedian() + "|" + contributionsByZip.getRunningTotalTxCount() + "|"
                + contributionsByZip.getRunningTotalTxAmt();
        runningContributionsOutputFile.println(outRow);
    }

    private void outputAggregateContributionsToFile(ContributionsAggregateData.ContributionsByDate contributionsByDate) {
        String outRow = contributionsByDate.getRecipientId() + "|"
                + contributionsByDate.getContributionDateStr() + "|"
                + contributionsByDate.getMedian() + "|" + contributionsByDate.getTotalTxCount() + "|"
                + contributionsByDate.getTotalTxAmt();
        aggregateContributionsOutputFile.println(outRow);
    }
}