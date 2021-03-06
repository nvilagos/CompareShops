package rest;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import dto.ErrorMessageDto;
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

        System.out.println("Request got for: " + productName);

        List<ShopSearchResultDto> listOfItems = new ArrayList<ShopSearchResultDto>();
        try {
            listOfItems.addAll(szellemlovas.getAll(productName));
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }

        try {
            listOfItems.addAll(gemklub.getAll(productName));
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }

        try {
            listOfItems.addAll(reflexshop.getAll(productName));
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }

        try {
            listOfItems.addAll(deltaVision.getAll(productName));
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }

        try {
            listOfItems.addAll(metagames.getAll(productName));
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        if (listOfItems.size() <= 0) {
            ErrorMessageDto errorMessageDto = new ErrorMessageDto();
            errorMessageDto.setErrorMessage("No result was found for: '" + productName +"'.");
            return gson.toJson(errorMessageDto);
        }
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
