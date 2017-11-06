package com.lykke.algos.algo;

import com.lykke.hft.ApiClient;
import com.lykke.hft.client.AssetPairsApi;
import com.lykke.hft.client.OrderBooksApi;
import com.lykke.hft.model.AssetPairModel;
import com.lykke.hft.model.OrderBook;
import feign.RequestInterceptor;

import java.util.List;
import java.util.Map;

public class Algo {

	int counterMax;

	OrderBooksApi orderBooksApi;
	AssetPairsApi assetPairsApi;

	public Algo(ApiClient api, int counterMax) {
		init(api);
		this.counterMax = counterMax;
	}
	public void init(ApiClient api){
		this.orderBooksApi = api.buildClient(OrderBooksApi.class);
		this.assetPairsApi = api.buildClient(AssetPairsApi.class);
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
		Map<String, RequestInterceptor> authMap = apiClient.getApiAuthorizations();

		apiClient.setBasePath("https://hft-service-dev.lykkex.net");


		Algo algo  = new Algo(apiClient, 1000);
		algo.run();
	}

}