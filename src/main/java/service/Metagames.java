package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dto.ShopSearchResultDto;
import service.utils.ServiceUtils;

public class Metagames implements ShopInterface {
    public static final String BASE_URL = "https://metagames.hu";
    public static final String SHOP_NAME = "Metagames";

    public String getBaseUrl() {
        return BASE_URL;
    }

    public String getShopName() {
        return SHOP_NAME;
    }

    public String convertSearchQuery(String searchQuery) {
        return searchQuery.replace(" ", "+");
    }

    public Document getResultPage(String convertedSearchQuery) throws IOException {
        return Jsoup.connect(getBaseUrl()
                + "/webshop?search="
                + convertedSearchQuery)
                .get();
    }

    public List<ShopSearchResultDto> getResults(Document resultPage) {
        List<ShopSearchResultDto> results = new ArrayList<ShopSearchResultDto>();

        try {
            Element itemList = resultPage.select(".show-grid").first();
            Elements items = itemList.select(".media-body");
            for (Element item : items) {
                Element href = item
                        .select(".webshop-list-item-name").first()
                        .select("a").first();
                String name = href.text();
                String url = getBaseUrl() + href.attr("href").substring(1);
                String price = null;
                try {
                    price = item.select(".sale").first().text();
                } catch (NullPointerException e) {
                    price = item
                            .select("h5").first()
                            .select("span").first()
                            .select("div").first().text();
                }
                try {
                    price = price + " (" + item.select(".saleDetails").first().text() + ")";
                } catch (NullPointerException e) {
                    // Has no details
                }
                String availability = item.select(".label").first().text();
                ShopSearchResultDto searchResultDto = new ShopSearchResultDto();
                searchResultDto.setShop(SHOP_NAME);
                searchResultDto.setName(name);
                searchResultDto.setUrl(url);
                searchResultDto.setPrice(price);
                searchResultDto.setPriceNum(ServiceUtils.priceExtractor(price));
                searchResultDto.setAvailability(availability);
                results.add(searchResultDto);
            }
        } catch (NullPointerException e) {
            System.out.println("No result was found for " + getShopName());
        }

        return results;
    }

    public List<ShopSearchResultDto> getAll(String searchQuery) throws IOException {
        String convertedSearchQuery = convertSearchQuery(searchQuery);
        Document resultPage = getResultPage(convertedSearchQuery);
        return getResults(resultPage);
    }
}
