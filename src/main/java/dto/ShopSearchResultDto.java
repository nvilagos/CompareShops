package dto;

public class ShopSearchResultDto {
    private String shop;
    private String name;
    private String url;
    private String price;
    private Integer priceNum;
    private String availability;

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Integer getPriceNum() {
        return priceNum;
    }

    public void setPriceNum(Integer priceNum) {
        this.priceNum = priceNum;
    }

    @Override
    public String toString() {
        return getShop() + "\n" +
                "\tname = " + getName() + "\n" +
                "\turl = " + getUrl() + "\n" +
                "\tprice = " + getPrice() + "\n" +
                "\tpriceNum = " + getPriceNum() + "\n" +
                "\tavailability = " + getAvailability() + "\n";
    }
}
