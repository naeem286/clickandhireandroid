package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 2/8/2016.
 */
public class AppConfig {
        // Server user login url

        public static String SITE_URL = "http://45.55.134.100/clickandhire";
       // public static String SITE_URL = "http://10.0.3.2" + "/clickandhire";
        public static String SITE_URL1 = "http://45.55.134.100/hamza";

        public static String URL_LOGIN = SITE_URL +"/login.php";
        public static String URL_REGISTER = SITE_URL +"/register.php";
        public static String URL_UPDATE = SITE_URL +"/update.php";
        public static String URL_GET_ALL_USERS = SITE_URL +"/allUsers.php";
        //public static String URL_UPLOAD_PICTURE = SITE_URL +"/UploadPicture.php";
        public static String URL_UPLOAD_PICTURE = SITE_URL +"/uploadPicture.php";
        public static String URL_All_City = SITE_URL +"/getAllcities.php";
        public static String URL_All_Category =SITE_URL + "/getAllCategories.php";
        public static String URL_For_Image = SITE_URL +"/images/";
        //public static String URL_For_Image = SITE_URL +"/getImage.php?id=";
        public static String URL_USER_BY_NAME = SITE_URL +"/getUsersByName.php";
        public static String URL_USER_BY_ID = SITE_URL +"/getUserById.php";
        public static String URL_USER_BY_SPECIALITY = SITE_URL +"/getUserBySpeciality.php";
        public static String URL_POST_TASK = SITE_URL +"/postTask.php";
        public static String URL_TASKS_BY_TASK_ID = SITE_URL +"/getTaskByTaskId.php";
        public static String URL_TASKS_BY_TITLE_FILTER = SITE_URL +"/getTasksByTitle.php";
        public static String URL_TASKS_BY_TITLE_All_CATEGORIES = SITE_URL +"/getTasksByTitleAllSubCategories.php";
        public static String URL_TASKS_BY_CATEGORIES = SITE_URL +"/getAllTasksByCatId.php";
        public static String URL_TASKS_BY_USER_ID = SITE_URL +"/getTasksByUserId.php";
        public static String URL_TASKS_UPDATE = SITE_URL +"/updateTask.php";
        public static String URL_AWARD_BID = SITE_URL +"/updateAwaredBidOfTask.php";
        public static String URL_PLACE_TASK_BID = SITE_URL +"/PlaceBid.php";
        public static String URL_GET_BIDS_OF_TASK = SITE_URL +"/getBidsByTaskId.php";
        public static String URL_GET_BIDS_BY_USER_ID = SITE_URL +"/getBidsByUserId.php";
        public static String URL_POST_NEWS = SITE_URL +"/postNews.php";
        public static String URL_GET_CITY_NEWS = SITE_URL +"/getCityNews.php";
        public static String URL_POST_RATING = SITE_URL +"/postRating.php";
        public static String URL_GET_RATING_BY_USER_ID = SITE_URL +"/getRatingByUserId.php";
        public static String URL_UPDATE_USER_PASSWORD = SITE_URL +"/changePassword.php";
    }
