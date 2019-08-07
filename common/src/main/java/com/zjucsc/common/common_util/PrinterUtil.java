package com.zjucsc.common.common_util;

public class PrinterUtil {

    public static void printStarter(){
        System.out.println("/**********************************");
    }

    public static void printStarter(int enterNumber){
        System.out.println("/**********************************");
        for (int i = 0; i < enterNumber; i++) {
            System.out.println();
        }
    }

    public static void printMsg(String msg){
        System.out.println("* " + msg);
    }

    public static void printEnder(){
        System.out.println("**********************************/");
    }

    public static void printEnder(int enterNumber){
        for (int i = 0; i < enterNumber; i++) {
            System.out.println();
        }
        System.out.println("**********************************/");
    }

    public static void printMsg(int aroundEnterNumber,String msg){
        printStarter(aroundEnterNumber);
        printMsg(msg);
        printEnder(aroundEnterNumber);
    }

    public static void printError(String msg){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>\n");
        System.out.println("ERROR MSG : " + msg);
        System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
