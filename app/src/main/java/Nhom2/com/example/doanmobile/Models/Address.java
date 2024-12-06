package Nhom2.com.example.doanmobile.Models;

public class Address {
    private String city;       // Thành phố
    private String district;   // Quận/Huyện
    private String commune;    // Xã
    private String details;    // Chi tiết địa chỉ (số nhà, tên đường)

    public Address() {
    }

    public Address(String city, String district, String commune, String details) {
        this.city = city;
        this.district = district;
        this.commune = commune;
        this.details = details;
    }

    // Getters and setters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return details + ", " + commune + ", " + district + ", " + city;
    }
}
