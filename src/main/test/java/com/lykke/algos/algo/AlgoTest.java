package com.lykke.algos.algo;

public class AlgoTest {


    public static void main(String[] args) {

        Algo algo = new Algo(1);
        try {
            algo.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}