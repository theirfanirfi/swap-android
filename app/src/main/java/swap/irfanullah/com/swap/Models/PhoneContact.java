package swap.irfanullah.com.swap.Models;

public class PhoneContact {
    private String CONTACT_NAME;
    private String CONTACT_NUMBER;

    public PhoneContact(String CONTACT_NAME, String CONTACT_NUMBER) {
        this.CONTACT_NAME = CONTACT_NAME;
        this.CONTACT_NUMBER = CONTACT_NUMBER;
    }

    public String getCONTACT_NAME() {
        return CONTACT_NAME;
    }

    public String getCONTACT_NUMBER() {
        return CONTACT_NUMBER;
    }
}
