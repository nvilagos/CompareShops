package service;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;

import dto.ShopSearchResultDto;

public interface ShopInterface {

    public String getBaseUrl();

    public String getShopName();

    public String convertSearchQuery(String searchQuery);

    public Document getResultPage(String convertedSearchQuery) throws IOException;

    public List<ShopSearchResultDto> getResults(Document resultPage);

    public List<ShopSearchResultDto> getAll(String searchQuery) throws IOException;
}
