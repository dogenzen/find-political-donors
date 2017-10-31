package payloads;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Represents a single Contribution payload
 *
 * @author Pradeep Das
 * @version 27th Oct 2017
 */
public class Contribution
{
    private String recipientId;
    private String donorZip;
    private String txDateStr;
    private double txAmount;
    private Date txDate;

    /**
     * Populates zipcode, date and contribution amount only if other-id is empty
     * @param contributionRecord - parses each contribution row separated by pipe
     *                             to reads relevant data
     */
    public Contribution(String contributionRecord) {
        List<String> contributionData = Arrays.asList(contributionRecord.split("\\|"));
        String otherId = contributionData.size() > 15 ? contributionData.get(15) : null;
        if (otherId == null || otherId.length() == 0) {
            recipientId = contributionData.get(0);
            if (recipientId != null && recipientId.length() >= 0) {
                donorZip = contributionData.get(10);
                txDateStr = contributionData.get(13);
                txAmount = Double.valueOf(contributionData.get(14));
            }
            validateData();
        }
    }

    /**
     * Validate input stream data
     * - Sets zipcode to null if the corresponding input data is malformed
     * - Sets date to null if the corresponding input data is malformed
     */
    private void validateData() {
        if (donorZip != null) {
            if (donorZip.length() < 5) {
                donorZip = null;
            } else {
                if (donorZip.length() > 5) {
                    donorZip = donorZip.substring(0, 5);
                }
                try {
                    donorZip = Integer.parseInt(donorZip) > 0 ? donorZip : null;
                } catch (NumberFormatException nfe) {
                    // log bad contribution row
                    donorZip = null;
                }
            }
        }
        if (txDateStr != null) {
            boolean validDateStr = false;
            if (txDateStr.length() == 8) {
                String mStr = txDateStr.substring(0, 2);
                String dStr = txDateStr.substring(2, 4);
                String yStr = txDateStr.substring(4, 8);
                try {
                    int m = Integer.parseInt(mStr);
                    int d = Integer.parseInt(dStr);
                    int y = Integer.parseInt(yStr);
                    if (y > 0 && m >= 1 && m <= 12 && d >= 1 && d <= 31) {
                        validDateStr = true;
                    }
                } catch (NumberFormatException nfe) {
                    // log bad contribution row
                }
            }
            if (validDateStr) {
                try {
                    txDate = new SimpleDateFormat("MMddyyyy").parse(txDateStr);
                } catch (ParseException e) {
                    // log bad contribution row
                }
            }
        }
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getContributorZip() {
        return donorZip;
    }

    public String getTxDateStr() {
        return txDateStr;
    }

    public double getTxAmount() {
        return txAmount;
    }

    public Date getTxDate() {
        return txDate;
    }

    @Override
    public String toString() {
        return "Contribution{" +
                "recipientId='" + recipientId + '\'' +
                ", donorZip='" + donorZip + '\'' +
                ", txDateStr='" + txDateStr + '\'' +
                ", txAmount=" + txAmount +
                ", txDate=" + txDate +
                '}';
    }
}
