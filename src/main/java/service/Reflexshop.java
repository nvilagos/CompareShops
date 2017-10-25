package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dto.ShopSearchResultDto;

public class Reflexshop implements ShopInterface{
    public static final String BASE_URL = "https://reflexshop.hu";
    public static final String SHOP_NAME = "Reflexshop";

    public String getBaseUrl() {
        return BASE_URL;
    }

    public String getShopName() {
        return SHOP_NAME;
    }

    public String convertSearchQuery(String searchQuery) {
        return searchQuery.replace(" ", "%20");
    }

    public Document getResultPage(String convertedSearchQuery) throws IOException {
        return Jsoup.connect(getBaseUrl()
                + "/index.php?route=product/list&description=0&keyword="
                + convertedSearchQuery)
                .get();
    }

    public List<ShopSearchResultDto> getResults(Document resultPage) {
        List<ShopSearchResultDto> results = new ArrayList<ShopSearchResultDto>();

        Elements items = resultPage.select(".snapshot_vertical_product");
        for (Element item : items) {
            Element href = item.select(".list-productname-link").first();
            String name = href.text();
            String url = href.attr("href");
            String price = item.select(".list_price").first().text();
            String availability = item.select(".list_stock").first().text();

            ShopSearchResultDto searchResultDto = new ShopSearchResultDto();
            searchResultDto.setShop(SHOP_NAME);
            searchResultDto.setName(name);
            searchResultDto.setUrl(url);
            searchResultDto.setPrice(price);
            searchResultDto.setAvailability(availability);
            results.add(searchResultDto);
        }

        return results;
    }

    public List<ShopSearchResultDto> getAll(String searchQuery) throws IOException {
        String convertedSearchQuery = convertSearchQuery(searchQuery);
        Document resultPage = getResultPage(convertedSearchQuery);
        return getResults(resultPage);
    }
}
