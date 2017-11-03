package com.lykke.algos.algo;

public class Algo {

	int counterMax;

	public Algo(int counterMax) {
		this.counterMax = counterMax;
	}

	public void run() throws InterruptedException {
		int counter = 0;
		while(true) {
			System.out.println("Hello, World "+ counter);
			Thread.sleep(10000);
			counter++;
			if (counter==counterMax){
				break;
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		Algo algo = new Algo(1000);
		algo.run();
	}

}