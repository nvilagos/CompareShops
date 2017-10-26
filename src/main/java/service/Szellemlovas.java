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

public class Szellemlovas implements ShopInterface {

    public static final String BASE_URL = "http://szellemlovas.hu";
    public static final String SHOP_NAME = "Szellemlovas";

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
                + "/tarsasjatekok/index.php?r=webboltTermekValtozat/index&termek_nev="
                + convertedSearchQuery)
                .get();
    }

    public List<ShopSearchResultDto> getResults(Document resultPage) {
        List<ShopSearchResultDto> results = new ArrayList<ShopSearchResultDto>();

        try {
            Element itemList = resultPage.select(".items").first();
            Elements items = itemList.select(".view");
            for (Element item : items) {
                Element href = item.select(".listcim").first().select("a").first();
                String name = href.text();
                try {
                    item.select(".bontott").first().text();
                    name = name + " - BONTOTT";
                } catch (NullPointerException e) {
                    // Not opened
                }
                String url = getBaseUrl() + href.attr("href");
                String price = null;
                try {
                    price = item.select(".normalprice").first().text();
                } catch (NullPointerException e) {
                    try {
                        price = item.select(".discountprice").first().text();
                        price = price +
                                " (eredeti ar: " + item.select(".originalprice").first().text() + ")";
                    } catch (NullPointerException e1) {
                        price = "?";
                    }
                }
                String availability = item.select(".szallitasi_ido").first().text();

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

        //System.out.println(resultPage.body());
        return results;
    }

    public List<ShopSearchResultDto> getAll(String searchQuery) throws IOException {
        String convertedSearchQuery = convertSearchQuery(searchQuery);
        Document resultPage = getResultPage(convertedSearchQuery);
        return getResults(resultPage);
    }
}
