package nl.ipwrcServer.model;

import com.fasterxml.jackson.annotation.JsonView;
import nl.ipwrcServer.service.JsonViewService;
import javax.validation.constraints.NotEmpty;

public class User{

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private long userID;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String firstName;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String lastName;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String emailAddress;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String profilePicture;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String gender;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String dateOfBirth;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String country;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String zipCode;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String streetName;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private int houseNumber;

    @JsonView(JsonViewService.Public.class)
    private String addition;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String town;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String phoneNumber;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
