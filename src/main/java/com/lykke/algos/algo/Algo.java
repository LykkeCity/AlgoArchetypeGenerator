package com.lykke.algos.algo;

import com.lykke.hft.ApiClient;
import com.lykke.hft.client.OrdersApi;
import com.lykke.hft.client.WalletsApi;
import com.lykke.hft.model.LimitOrderRequest;
import com.lykke.hft.model.MarketOrderRequest;
import com.lykke.hft.model.ResponseModelDouble;
import com.lykke.hft.model.Wallet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Algo {
	private Logger logger = LogManager.getLogger(getClass());

	int counterMax;
	String apiKey;

	OrdersApi ordersApi;
	WalletsApi walletsApi;

	public Algo(OrdersApi ordersApi, WalletsApi walletsApi,String apiKey,int counterMax) {

		this.ordersApi = ordersApi;
		this.walletsApi = walletsApi;
		this.counterMax = counterMax;
		this.apiKey = apiKey;

	}


	public void run() throws InterruptedException, feign.FeignException{
		int counter = 0;
		while(true) {

			MarketOrderRequest marketOrderRequest = new MarketOrderRequest();
			marketOrderRequest.assetPairId("BTCUSD");
			marketOrderRequest.setOrderAction(MarketOrderRequest.OrderActionEnum.BUY);
			marketOrderRequest.setVolume(0.0001);
			marketOrderRequest.setAsset("BTC");
			logger.info("Buying some coins on the market price!!!");
			ResponseModelDouble responseModelDouble = ordersApi.placeMarketOrder(apiKey, marketOrderRequest);

			Double boughtPrice = responseModelDouble.getResult();

			Random rand = new Random();
			long seconds = rand.nextInt(50);

			logger.info("Sleeping for: " + seconds+" seconds!!!");

			Thread.sleep(seconds * 1000);
			logger.info("Selling some coins on the market price!!!");

			LimitOrderRequest limitOrderRequest = new LimitOrderRequest();
			limitOrderRequest.assetPairId("BTCUSD");
			limitOrderRequest.setVolume(0.0001);
			limitOrderRequest.setPrice(boughtPrice + 1);
			limitOrderRequest.setOrderAction(LimitOrderRequest.OrderActionEnum.SELL);

			UUID uuid = ordersApi.placeLimitOrder(apiKey, limitOrderRequest);

			logger.info("What has happend with my order!!!");
			logger.info(ordersApi.getOrderInfo(uuid, apiKey));

			logger.info("Do I still have some money!!!");
			List<Wallet> walletList = walletsApi.wallets(apiKey);
			logger.info(walletsApi.wallets(apiKey));

			for (Wallet wallet : walletList) {
				logger.info("Asset: " + wallet.getAssetId());
				logger.info("Ballance: " + wallet.getBalance());
				logger.info("GetReserved: "+wallet.getReserved());
				if (wallet.getBalance()==0){
					logger.info("Game over!!!");
					break;
				}
			}


		}
	}




	public static void main(String[] args) throws InterruptedException {
		Environment.load();
		String basePath = Environment.getVariable("HFT_API_BASE_PATH");
		String apiKey =   Environment.getVariable("HFT_KEY");

		OrdersApi ordersApi = new ApiClient().setBasePath(basePath).buildClient(OrdersApi.class);

		WalletsApi walletsApiApi = new ApiClient().setBasePath(basePath).buildClient(WalletsApi.class);

		Algo algo  = new Algo(ordersApi,walletsApiApi,apiKey,1000);
		try {
			algo.run();
		}catch (RuntimeException ex){
			System.out.println("Bad stuff happened!!! Bye!!!");

		}
	}

}