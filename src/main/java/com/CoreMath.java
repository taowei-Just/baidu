package com;

import java.util.*;


/**
 * Created by Tao on 2018/6/12 0012.
 */

public class CoreMath {

    public static void main(String[] args) {
        System.err.println(mth("78901"));
        System.err.println(mth("12354"));
        System.err.println(mth("24351"));
        System.err.println(mth("76485"));
    }

    static Map<String, Float> consMap = new HashMap<>();


    // 概率常数
    static {
        consMap.put("1", 0.00001f);
        consMap.put("2", 0.045f);
        consMap.put("3", 0.009f);
        consMap.put("4", 0.000007f);
        consMap.put("5", 0.06984f);
        consMap.put("6", 0.11016f);
        consMap.put("7", 0.50399f);
        consMap.put("8", 0.30234f);
    }

    public static String[] shunziS = new String[]{"06789", "01789", "01289", "01239"};


    public static int mth(String number) {

//        System.out.println("处理数字    " + number);
        if (number.length() < 5) {
            for (int l = 0; l < 5 - number.length(); l++) {
                number = "0" + number;
            }
        }


        int[] numS = new int[number.length()];
        for (int j = 0; j < number.length(); j++) {
            String substring = number.substring(j, j + 1);
            numS[j] = Integer.valueOf(substring);
        }

        for (int i = 0; i < numS.length - 1; i++) {
            for (int j = i+1; j < numS.length; j++) {
                int num = numS[i];
                int num1 = numS[j];
                if (num1 < num) {
                    numS[i] = num1;
                    numS[j] = num;
                }

            }
        }
        number="";
        for (int i = 0; i < numS.length; i++) {
            number+=numS[i];
        }
        // 找出其中一对的数量

        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < numS.length; i++) {
            if (!map.containsKey(numS[i]))
                map.put(numS[i], 1);
            else
                continue;

            Set<Integer> keySet = map.keySet();
            Iterator<Integer> iterator = keySet.iterator();
            Integer num = 0;
            while (iterator.hasNext()) {
                Integer next = iterator.next();

                num += map.get(next);

            }
            if (num >= numS.length)
                break;


            for (int j = i + 1; j < numS.length; j++) {
                if (numS[i] == numS[j]) {
                    if (map.containsKey(numS[i]))
                        map.put(numS[i], map.get(numS[i]) + 1);
                }
            }
        }


//        System.out.println("map :" + map.toString());


        boolean isShunzi = true;
        for (int i = 1; i < numS.length - 1; i++) {
            if (numS[i - 1] + 1 == numS[i] && numS[i + 1] - 1 == numS[i] || checkNumber(number)) {
                //一个顺子
            } else {
                // 不是
                isShunzi = false;
            }
        }


        Iterator<Integer> iterator = map.keySet().iterator();

        if (isShunzi) {
//			System.err.println("shun zi ");
            return 4;
        } else {

            if (map.size() == 1) {
//				System.err.println("5 tiao ");
                return 1;
            } else if (map.size() == 2) {

                boolean ishulu = false;
                while (iterator.hasNext()) {
                    Integer next = iterator.next();
                    Integer integer = map.get(next);
                    if (integer == 2) {
                        ishulu = true;
                        break;
                    }
                }
                if (ishulu) {
//							System.err.println(" hu lu ");
                    return 3;
                } else {
//							System.err.println("za dan ");
                    return 2;
                }

            } else if (map.size() == 3) {


                boolean isSantiao = false;

                while (iterator.hasNext()) {
                    Integer next = iterator.next();
                    Integer integer = map.get(next);
                    if (integer == 3) {

                        isSantiao = true;
                        break;
                    }


                }
                if (isSantiao) {
//						System.err.println(" san tiao ");

                    return 5;
                } else {

//						System.err.println(" liang dui");
                    return 6;
                }

            } else if (map.size() == 4) {
//					System.err.println(" dan dui");
                return 7;
            } else if (map.size() == 5) {
//					System.err.println("san hao");
                return 8;

            } else {
                return 0;
            }
        }
    }

    public static int mth02(String number) {
        if (number == null || number.length() < 5)
            return -1;
        char[] chars = number.toCharArray();
        int[] numS = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            Integer integer = Integer.valueOf(chars[i]);
            numS[i] = integer;
        }

        numS = sortInt(numS);

        Map<Integer, Integer> sameMap = new HashMap<>();
        int sameNum = 0;

        for (int i = 0; i < numS.length; i++) {
            for (int j = i + 1; j < numS.length - 1; j++) {
                if (numS[i] == numS[j]) {
                    sameMap.containsKey(numS[i]);

                }
            }
        }
        return -1;
    }

    private static int[] sortInt(int[] numS) {
        for (int i = 0; i < numS.length; i++) {
            for (int j = i; j < numS.length; j++) {
                int num = numS[j];
                int num1 = numS[i];
                if (num > num1) {
                    numS[i] = num;
                    numS[j] = num1;
                }
            }
        }
        return numS;


    }


    private static boolean checkNumber(String number) {
        for (int i = 0; i < shunziS.length; i++) {
            if (number.equals(shunziS[i]))
                return true;
        }
        return false;
    }


    public static float probability(int size, int total) {

        return size / (float) total;
    }

    public static float probability(Map<String, Integer> map, int winState, int total) {

        if (map == null || map.size() <= 0)
            return -1f;

        if (map.containsKey(String.valueOf(winState))) {
            return probability(map.get(String.valueOf(winState)), total);
        }
        return -1f;
    }

    public static Map<String, Float> probability(Map<String, Integer> map, int total) {

        if (map == null || map.size() <= 0)
            return null;

        Map<String, Float> proMap = new HashMap<>();

        for (int i = 0; i <= 7; i++) {
            proMap.put(String.valueOf(i), probability(map, i, total));
        }
        return proMap;
    }


    private static Map<String, Float> getDiskMax(Map<String, Float> map) {
        List<Float> list = new ArrayList<>();
        for (String temp : map.keySet()) {
            Float value = map.get(temp);
            list.add(value);
        }
        Float max = 0f;
        for (int i = 0; i < list.size(); i++) {
            Float size = list.get(i);
            max = (max > size) ? max : size;
        }
        for (String key : map.keySet()) {
            if (max == map.get(key)) {
                map.clear();
                map.put(key, max);
                return map;
            }
        }
        return null;
    }

    public static String detail(int location) {
        String desc = "";
        switch (location) {
            case 0:
                desc = "5条";

                break;
            case 1:
                desc = "炸弹";

                break;

            case 2:
                desc = "葫芦";

                break;

            case 3:
                desc = "顺子";
                break;

            case 4:

                desc = "三条";
                break;

            case 5:

                desc = "两对";
                break;

            case 6:
                desc = "单对";
                break;
            case 7:
                desc = "散号";
                break;

            case -1:
                desc = "error_";
                break;
        }
        return desc;
    }
}
