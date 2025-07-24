package com.example.projectmanager;

public class KeyCon {
    public static class project{
        public  static String name="name";
        public  static String problem="Problem";
        public  static String img="img";
        public static  String strtDate="strtDate";
        public static  String endDate="endDate";
        public  static String status="status";
        public  static String text="text";
        public  static String futureGoal="futureGoal";

        public static class value{
            public static String UpdationAvailable="0";
            public static String Working="1";
            public static String Completed="2";

        }

        public static  class version{
            public static String name="name";
            public static String status="status";
            public static String strtDate="StrtDate";
            public static String lastDate="lastDate";

            public static class value{
                static String UpdationAvailable="0";
                static String Working="1";
                static String Completed="2";

            }

            public static class topic{
                public static  String name="name";
                public  static String status="status";
                public  static String date="date";

                public static class value{
                    static String Created="0";
                    static String Working="1";
                    static String Cancelled="2";
                    static String Verified="3";
                    static String Completed="4";

                }
                public static class data{
                    public static String data="data";
                    public static String date="date";

                }
            }


        }
    }
}
