package rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import dto.ShopSearchResultDto;
import service.DeltaVision;
import service.Gemklub;
import service.Metagames;
import service.Reflexshop;
import service.Szellemlovas;

@Path("prices")
public class Prices {

    @GET
    @Path("/{product_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll(@PathParam("product_name") String productName) throws IOException {
        //String text = doc.body().text(); // "An example link"

        productName = java.net.URLDecoder.decode(productName, "UTF-8");

        Szellemlovas szellemlovas = new Szellemlovas();
        Gemklub gemklub = new Gemklub();
        Reflexshop reflexshop = new Reflexshop();
        DeltaVision deltaVision = new DeltaVision();
        Metagames metagames = new Metagames();

        List<ShopSearchResultDto> listOfItems = new ArrayList<ShopSearchResultDto>();
        listOfItems.addAll(szellemlovas.getAll(productName));
        listOfItems.addAll(gemklub.getAll(productName));
        listOfItems.addAll(reflexshop.getAll(productName));
        listOfItems.addAll(deltaVision.getAll(productName));
        listOfItems.addAll(metagames.getAll(productName));

        System.out.println("Request got for: " + productName);

        Gson gson = new Gson();
        return gson.toJson(listOfItems);

        /*
        List<ShopSearchResultDto> szellemlovasResults =
                szellemlovas.getAll("7 wonders");
        for (ShopSearchResultDto item : szellemlovasResults) {
            System.out.println(item);
        }
        List<ShopSearchResultDto> gemklubResults = gemklub.getAll("7 wonders duel");
        for (ShopSearchResultDto item : gemklubResults) {
            System.out.println(item);
        }
        List<ShopSearchResultDto> reflexshopResults = reflexshop.getAll("7 wonders");
        for (ShopSearchResultDto item: reflexshopResults) {
            System.out.println(item);
        }
        List<ShopSearchResultDto> deltaVisionResults = deltaVision.getAll("12mm d6");
        for (ShopSearchResultDto item: deltaVisionResults) {
            System.out.println(item);
        }
        List<ShopSearchResultDto> metagamesResults = metagames.getAll("x-wing");
        for (ShopSearchResultDto item: metagamesResults ) {
            System.out.println(item);
        }
        */
    }
}
