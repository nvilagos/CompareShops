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

public class Gemklub implements ShopInterface{
    public static final String BASE_URL = "http://www.gemklub.hu";
    public static final String SHOP_NAME = "Gemklub";

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
                + "/catalogsearch/result/?cat=0&limit=48&q="
                + convertedSearchQuery)
                .get();
    }

    public List<ShopSearchResultDto> getResults(Document resultPage) {
        List<ShopSearchResultDto> results = new ArrayList<ShopSearchResultDto>();

        try {
            Element itemList = resultPage.select(".row.gridview").first();
            Elements items = itemList.select(".product-info");
            for (Element item : items) {
                String name = item.select(".prod-name").first().text();
                String price = item.select(".normal-price").first().text() +
                        " (" + item.select(".unique-price").first().text() + ")";
                String url = item.select("a").first().attr("href");
                String availability = item
                        .select(".icon-list").first()
                        .select("li").first()
                        .select("span").first()
                        .attr("title");

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
