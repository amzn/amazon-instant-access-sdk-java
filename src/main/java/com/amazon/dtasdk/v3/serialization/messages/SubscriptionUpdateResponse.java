package com.amazon.dtasdk.v3.serialization.messages;

import com.amazon.dtasdk.base.SubscriptionResponse;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SubscriptionUpdateResponse extends SubscriptionResponse {
    private Integer numberOfUnusedLicenses;
    private Integer numberOfUsedLicenses;
    private String managementURL;

    public Integer getNumberOfUnusedLicenses() {
        return numberOfUnusedLicenses;
    }

    public void setNumberOfUnusedLicenses(Integer numberOfUnusedLicenses) {
        this.numberOfUnusedLicenses = numberOfUnusedLicenses;
    }

    public Integer getNumberOfUsedLicenses() {
        return numberOfUsedLicenses;
    }

    public void setNumberOfUsedLicenses(Integer numberOfUsedLicenses) {
        this.numberOfUsedLicenses = numberOfUsedLicenses;
    }

    public String getManagementURL() {
        return managementURL;
    }

    public void setManagementURL(String managementUrl) {
        this.managementURL = managementUrl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((numberOfUnusedLicenses == null) ? 0 : numberOfUnusedLicenses.hashCode());
        result = prime * result + ((numberOfUsedLicenses == null) ? 0 : numberOfUsedLicenses.hashCode());
        result = prime * result + ((managementURL == null) ? 0 : managementURL.hashCode());
        result = prime * result + ((response == null) ? 0 : response.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubscriptionUpdateResponse other = (SubscriptionUpdateResponse) obj;
        if (numberOfUnusedLicenses == null) {
            if (other.numberOfUnusedLicenses != null) {
                return false;
            }
        } else if (!numberOfUnusedLicenses.equals(other.numberOfUnusedLicenses)) {
            return false;
        }

        if (numberOfUsedLicenses == null) {
            if (other.numberOfUsedLicenses != null) {
                return false;
            }
        } else if (!numberOfUsedLicenses.equals(other.numberOfUsedLicenses)) {
            return false;
        }

        if (managementURL == null) {
            if (other.managementURL != null) {
                return false;
            }
        } else if (!managementURL.equals(other.managementURL)) {
            return false;
        }

        if (response == null) {
            if (other.response != null) {
                return false;
            }
        } else if (!response.equals(other.response)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("numberOfUnusedLicenses", numberOfUnusedLicenses)
                .append("numberOfUsedLicenses", numberOfUsedLicenses)
                .append("managementURL", managementURL)
                .append("response", response).toString();
    }
}
