package jp.java.voyage.simulateAssetFormationWithNISA;

public class lifeEventValidation {
    String lifeEventAge1Error;
    String requiredFunds1Error;
    String lifeEventAge2Error;
    String requiredFunds2Error;

    public lifeEventValidation(String lifeEventAge1Error, String requiredFunds1Error, String lifeEventAge2Error, String requiredFunds2Error) {
        this.lifeEventAge1Error = lifeEventAge1Error;
        this.requiredFunds1Error = requiredFunds1Error;
        this.lifeEventAge2Error = lifeEventAge2Error;
        this.requiredFunds2Error = requiredFunds2Error;
    }

    public lifeEventValidation() {
    }

    public void setLifeEventAge1Error(String lifeEventAge1Error) {
        this.lifeEventAge1Error = lifeEventAge1Error;
    }

    public void setRequiredFunds1Error(String requiredFunds1Error) {
        this.requiredFunds1Error = requiredFunds1Error;
    }

    public void setLifeEventAge2Error(String lifeEventAge2Error) {
        this.lifeEventAge2Error = lifeEventAge2Error;
    }

    public void setRequiredFunds2Error(String requiredFunds2Error) {
        this.requiredFunds2Error = requiredFunds2Error;
    }
}
