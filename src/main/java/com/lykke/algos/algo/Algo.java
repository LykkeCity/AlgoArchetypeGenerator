package com.lykke.algos.algo;

import com.lykke.hft.ApiClient;
import com.lykke.hft.client.AssetPairsApi;
import com.lykke.hft.client.OrderBooksApi;
import com.lykke.hft.client.OrdersApi;
import com.lykke.hft.model.AssetPairModel;
import com.lykke.hft.model.LimitOrderRequest;
import com.lykke.hft.model.OrderBook;

import java.util.List;

public class Algo {

	int counterMax;

	OrderBooksApi orderBooksApi;
	AssetPairsApi assetPairsApi;
	OrdersApi ordersApi;

	public Algo(ApiClient api, int counterMax) {
		init(api);
		this.counterMax = counterMax;
	}
	public void init(ApiClient api){
		this.orderBooksApi = api.buildClient(OrderBooksApi.class);
		this.assetPairsApi = api.buildClient(AssetPairsApi.class);
		this.ordersApi = api.buildClient(OrdersApi.class);
	}

	public void run() throws InterruptedException {
		int counter = 0;
		while(true) {
			System.out.println("------------ORDERS------------");
			for (OrderBook orderBook : 		getOrderBooks()) {
				System.out.println(orderBook.toString());
			}
			System.out.println("!!!!!!!!ASSETS!!!!!!!!");
			for(AssetPairModel assetPairModel : getAssetPairs()){
				System.out.println(assetPairModel.toString());

			}
			Thread.sleep(10000);
			counter++;
			if (counter==counterMax){
				break;
			}
			LimitOrderRequest limitOrderRequest = new LimitOrderRequest();
			limitOrderRequest.assetPairId("AUDCHF");
			limitOrderRequest.setOrderAction(LimitOrderRequest.OrderActionEnum.BUY);
			limitOrderRequest.setPrice(2.34);
			limitOrderRequest.setVolume(1.1);
			ordersApi.placeLimitOrder("dc305d-8add-4fc2-9efa-387d3bfe4b43",limitOrderRequest);
		}
	}



	private List<OrderBook> getOrderBooks(){
		return orderBooksApi.orderBooks();
	}

	private List<AssetPairModel> getAssetPairs(){
		return assetPairsApi.assetPairs();
	}

	public static void main(String[] args) throws InterruptedException {
		ApiClient apiClient = new ApiClient();


		apiClient.setBasePath("https://hft-service-dev.lykkex.net");

//		apiClient.setApiKey("4edc305d-8add-4fc2-9efa-387d3bfe4b43");
		Algo algo  = new Algo(apiClient, 1000);
		algo.run();
	}

}