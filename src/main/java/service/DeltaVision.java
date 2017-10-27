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

public class DeltaVision implements ShopInterface {
    public static final String BASE_URL = "http://www.deltavision.hu";
    public static final String SHOP_NAME = "Delta Vision";

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
                + "/catalogsearch/result/?x=0&y=0&limit=45&q="
                + convertedSearchQuery)
                .get();
    }

    public List<ShopSearchResultDto> getResults(Document resultPage) {
        List<ShopSearchResultDto> results = new ArrayList<ShopSearchResultDto>();

        try {
            Elements items = resultPage.select(".item");
            for (Element item : items) {
                Element href = item.select(".product-name").first().select("a").first();
                String name = href.text();
                String url = href.attr("href");
                String price = item.select(".special-price").first().text() +
                        " (" + item.select(".old-price").first().text() + ")";
                String availability = item
                        .select(".actions").first()
                        .select(".button.btn-cart").first().text();

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
