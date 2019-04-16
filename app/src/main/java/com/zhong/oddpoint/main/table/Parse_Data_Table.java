package com.zhong.oddpoint.main.table;

public class Parse_Data_Table {
    public static String parse_city_code(String city_code){
        for (int i = 0; i<Data_Table.city_code.length; i++){
            if (city_code.equals(Data_Table.city_code[i])){
                return Data_Table.city[i];
            }
        }
        return "";
    }

    public static String parse_city(String city){
        for (int i = 0; i<Data_Table.city.length; i++){
            if (city.equals(Data_Table.city[i])){
                return Data_Table.city_code[i];
            }
        }
        return "";
    }
}
