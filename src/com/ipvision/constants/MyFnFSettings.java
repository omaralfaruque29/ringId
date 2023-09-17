/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.constants;

import java.io.File;
import com.ipvision.model.JsonFields;
import com.ipvision.model.UserBasicInfo;

/**
 *
 * @author FaizAhmed
 */
public class MyFnFSettings {

    public static final Integer AUTOREFRESH_TIME = 5000; // 5 SEC
    public static final Integer KEEP_ALIVE_TIME = 30000;
    public static final int DEFAULT_VERTICAL_GAP = 5;
    public static final int DEFAULT_MARGIN = 5;
    public static String EMOTICONS_INSERT_FILE = "emoticons_insert.db";
    public static String INSTANT_MESSAGES_INSERT_FILE = "instant_messages_insert.db";
    public static String DEFAULT_FONT_NAME = "solaiman-lipi.ttf";
    //  public static String DEFAULT_FONT_NAME = "HelveticaWorldRegular.ttf";
    public static final int PROFILE_IMAGE = 1;
    public static final int COVER_IMAGE = 2;
    public static String PROFILE_IMAGE_ALBUM_ID = "profileimages";
    public static String COVER_IMAGE_ALBUM_ID = "coverimage";
    public static String FEED_IMAGE_ALBUM_ID = "default";
    // public static String header_text_file = "header_json.txt";
    public static String RESOURCE_FOLDER = "resources" + File.separator;
    public static String TEMP_PROFILE_IMAGE_FOLDER = "tempprofileimages";
    public static String TEMP_COVER_IMAGE_FOLDER = "tempcoverimages";
    public static String TEMP_BOOK_IMAGE_FOLDER = "tempbookimages";
    public static String TEMP_CHAT_FILES_FOLDER = "tempchatfiles";
    public static String TEMP_BROKEN_FILES_FOLDER = "tempbrokenfiles";
    public static String EMOTICON_FOLDER = "resources" + File.separator + "emoticons";
    public static String LARGE_EMOTICON_FOLDER = "resources" + File.separator + "large_emoticons";
    public static String STICKER_FOLDER = "resources" + File.separator + "stickers";
    public static String SCRIPT_FOLDER = "resources" + File.separator + "scripts";
    public static String CHAT_BG_FOLDER = "resources" + File.separator + "chatbg";
    public static int DEFAULT_STICKER_CATEGORY_ID = 0;
    public static int DEFAULT_STICKER_COLLECTION_ID = 0;
    public static final String D_MINI = "dmini";
    public static final String D_FULL = "dfull";
    public static UserBasicInfo userProfile = null;
    public static String VERSION_PC = "1013";
    public static Integer USER_PREV_STATUS = null;
    public static String RING_OFFLINE_IM_IP = "38.127.68.54";
    public static int RING_OFFLINE_IM_PORT = 1200;
    public static int RING_CHAT_BUFFER_SIZE = 2048;
    public static int RING_CHAT_BROKEN_PACKET_SIZE = 512;
    public static int RING_CHAT_FILE_CHUNK_SIZE = 5120;//10240//5120
    public static int RING_CHAT_LOCAL_PORT = 9876;
    public static boolean FRIEND_LIST_LOADED = false;

    /*
     * saved in dat file names
     */
    public static String LOGIN_USER_ID = "";
    public static Long LOGIN_USER_TABLE_ID = null;
    public static String LOGIN_USER_PROFILE_IMAGE = "";
    public static String LOGIN_SESSIONID = null;
    public static boolean IS_FIRST_TIME_LOGIN = true;
    public static String PREVIOUS_USER_NAME = "";

    public static final String KEY_NEW_USER_NAME = "new.user.name";
    public static String VALUE_NEW_USER_NAME = "";
    public static final String KEY_LOGIN_USER_NAME = "login.user.name";
    public static String VALUE_LOGIN_USER_NAME = "";
    public static final String KEY_LOGIN_USER_TYPE = "login.user.type";
    public static int VALUE_LOGIN_USER_TYPE = 0;
    public static final String KEY_LOGIN_USER_PASSWORD = "login.user.password";
    public static String VALUE_LOGIN_USER_PASSWORD = "";
    public static final String KEY_LOGIN_AUTO_SIGNIN = "login.auto.signin";
    public static int VALUE_LOGIN_AUTO_SIGNIN = 0;
    public static final String KEY_LOGIN_SAVE_PASSWORD = "login.save.password";
    public static int VALUE_LOGIN_SAVE_PASSWORD = 1;
    public static final String KEY_LOGIN_AUTO_START = "login.auto.start";
    public static int VALUE_LOGIN_AUTO_START = 1;
    public static final String KEY_LOGIN_USER_INFO = "login.user.info";
    public static JsonFields VALUE_LOGIN_USER_INFO = null;
    public static final String KEY_MOBILE_DIALING_CODE = "mobile.dialing.code";
    public static String VALUE_MOBILE_DIALING_CODE = null;

    public static boolean COMMUNICATION_SERVER_STATUS = true;

    public static final String[] GENDER_ARRAY = {"  Male", "  Female"};
    public static final String[] HISTORY_ARRAY = {"  All ", "  Today ", "  Yesterday ", "  3 days ", "  7 days ", "  15 days ", "  1 Month ", "  3 Months "};
    public static final int[] HISTORY_ARRAY_DAY = {- 1, 0, 1, 2, 6, 14, 29, 89};
    public static final String[][] COUNTRY_MOBILE_CODE = {
        {"ringID", "+878"},
        {"E-mail", "E-mail"},
        {"Afghanistan", "+93"},
        {"Albania", "+355"},
        {"Algeria", "+213"},
        {"American Samoa", "+1684"},
        {"Andorra", "+376"},
        {"Angola", "+244"},
        {"Anguilla", "+1264"},
        {"Antarctica", "+672"},
        {"Antigua and Barbuda", "+1268"},
        {"Argentina", "+54"},
        {"Armenia", "+374"},
        {"Aruba", "+297"},
        {"Australia", "+61"},
        {"Austria", "+43"},
        {"Azerbaijan", "+994"},
        {"Bahamas", "+1242"},
        {"Bahrain", "+973"},
        {"Bangladesh", "+880"},
        {"Barbados", "+1246"},
        {"Belarus", "+375"},
        {"Belgium", "+32"},
        {"Belize", "+501"},
        {"Benin", "+229"},
        {"Bermuda", "+1441"},
        {"Bhutan", "+975"},
        {"Bolivia", "+591"},
        {"Bonaire, Sint Eustatius", "+599"},
        {"Bosnia and Herzegovina", "+387"},
        {"Botswana", "+267"},
        {"Brazil", "+55"},
        {"British Virgin Islands", "+1284"},
        {"Brunei", "+673"},
        {"Bulgaria", "+359"},
        {"Burkina Faso", "+226"},
        {"Burundi", "+257"},
        {"Cambodia", "+855"},
        {"Cameroon", "+237"},
        {"Canada", "+1"},
        {"Cape Verde", "+238"},
        {"Cayman Islands", "+1345"},
        {"Central African Republic", "+236"},
        {"Chad", "+235"},
        {"Chile", "+56"},
        {"China", "+86"},
        {"Colombia", "+57"},
        {"Comoros", "+269"},
        {"Congo", "+242"},
        {"Congo (DRC)", "+243"},
        {"Cook Islands", "+682"},
        {"Costa Rica", "+506"},
        {"C'te d'Ivoire", "+225"},
        {"Croatia", "+385"},
        {"Cuba", "+53"},
        {"Cyprus", "+357"},
        {"Czech Republic", "+420"},
        {"Denmark", "+45"},
        {"Djibouti", "+253"},
        {"Dominica", "+1767"},
        {"Dominican Republic", "+18"},
        {"Ecuador", "+593"},
        {"Egypt", "+20"},
        {"El Salvador", "+503"},
        {"Equatorial Guinea", "+240"},
        {"Eritrea", "+291"},
        {"Estonia", "+372"},
        {"Ethiopia", "+251"},
        {"Falkland Islands", "+500"},
        {"Faroe Islands", "+298"},
        {"Fiji", "+679"},
        {"Finland", "+358"},
        {"France", "+33"},
        {"French Guiana", "+594"},
        {"French Polynesia", "+689"},
        {"Gabon", "+241"},
        {"Gambia", "+220"},
        {"Georgia", "+995"},
        {"Germany", "+49"},
        {"Ghana", "+233"},
        {"Gibraltar", "+350"},
        {"Greece", "+30"},
        {"Greenland", "+299"},
        {"Grenada", "+1473"},
        {"Guadeloupe", "+590"},
        {"Guam", "+1671"},
        {"Guatemala", "+502"},
        {"Guinea", "+224"},
        {"Guinea Bissau", "+245"},
        {"Guyana", "+592"},
        {"Haiti", "+509"},
        {"Holy See (Vatican City)", "+3"},
        {"Honduras", "+504"},
        //     {"Hong Kong SAR", "+852"},
        {"Hungary", "+36"},
        {"Iceland", "+354"},
        {"India", "+91"},
        {"Indonesia", "+62"},
        {"Iran", "+98"},
        {"Iraq", "+964"},
        {"Ireland", "+353"},
        {"Israel", "+972"},
        {"Italy", "+39"},
        {"Jamaica", "+1876"},
        {"Japan", "+81"},
        {"Jordan", "+962"},
        {"Kazakhstan", "+7"},
        {"Kenya", "+254"},
        {"Kiribati", "+686"},
        {"Korea", "+82"},
        {"Kuwait", "+965"},
        {"Kyrgyzstan", "+996"},
        {"Laos", "+856"},
        {"Latvia", "+371"},
        {"Lebanon", "+961"},
        {"Lesotho", "+266"},
        {"Liberia", "+231"},
        {"Libya", "+218"},
        {"Liechtenstein", "+423"},
        {"Lithuania", "+370"},
        {"Luxembourg", "+352"},
        {"Macao SAR", "+853"},
        {"Macedonia, FYRO", "+389"},
        {"Madagascar", "+261"},
        {"Malawi", "+265"},
        {"Malaysia", "+60"},
        {"Maldives", "+960"},
        {"Mali", "+223"},
        {"Malta", "+356"},
        {"Marshall Islands", "+692"},
        {"Martinique", "+596"},
        {"Mauritania", "+222"},
        {"Mauritius", "+230"},
        {"Mayotte", "+269"},
        {"Mexico", "+52"},
        {"Micronesia", "+691"},
        {"Moldova", "+373"},
        {"Monaco", "+377"},
        {"Mongolia", "+976"},
        {"Montenegro", "+382"},
        {"Montserrat", "+1664"},
        {"Morocco", "+212"},
        {"Mozambique", "+258"},
        {"Myanmar", "+95"},
        {"Namibia", "+264"},
        {"Nauru", "+674"},
        {"Nepal", "+977"},
        {"Netherlands", "+31"},
        {"New Caledonia", "+687"},
        {"New Zealand", "+64"},
        {"Nicaragua", "+505"},
        {"Niger", "+227"},
        {"Nigeria", "+234"},
        {"Niue", "+683"},
        {"Northern Mariana Islands", "+1670"},
        {"North Korea", "+850"},
        {"Norway", "+47"},
        {"Oman", "+968"},
        {"Pakistan", "+92"},
        {"Palau", "+680"},
        {"Panama", "+507"},
        {"Papua New Guinea", "+675"},
        {"Paraguay", "+595"},
        {"Peru", "+51"},
        {"Philippines", "+63"},
        {"Poland", "+48"},
        {"Portugal", "+351"},
        {"Puerto Rico", "+1787"},
        {"Qatar", "+974"},
        {"Romania", "+40"},
        {"Russia", "+7"},
        {"Rwanda", "+250"},
        {"Saint Helena, Ascension", "+290"},
        {"Saint Kitts and Nevis", "+1869"},
        {"Saint Lucia", "+1758"},
        {"Saint Pierre and Miquelon", "+508"},
        {"Saint Vincent and the Grenadines", "+1784"},
        {"Samoa", "+685"},
        {"San Marino", "+378"},
        {"Sao Tome and Principe", "+239"},
        {"Saudi Arabia", "+966"},
        {"Senegal", "+221"},
        {"Serbia", "+381"},
        {"Seychelles", "+248"},
        {"Sierra Leone", "+232"},
        {"Singapore", "+65"},
        {"Slovakia", "+421"},
        {"Slovenia", "+386"},
        {"Solomon Islands", "+677"},
        {"Somalia", "+252"},
        {"South Africa", "+27"},
        {"Spain", "+34"},
        {"Sri Lanka", "+94"},
        {"Sudan", "+249"},
        {"Suriname", "+597"},
        {"Swaziland", "+268"},
        {"Sweden", "+46"},
        {"Switzerland", "+41"},
        {"Syria", "+963"},
        {"Taiwan", "+886"},
        {"Tajikistan", "+992"},
        {"Tanzania", "+255"},
        {"Thailand", "+66"},
        {"Timor Leste", "+670"},
        {"Togo", "+228"},
        {"Tokelau", "+690"},
        {"Tonga", "+676"},
        {"Trinidad and Tobago", "+1868"},
        {"Tunisia", "+216"},
        {"Turkey", "+90"},
        {"Turkmenistan", "+993"},
        {"Turks and Caicos Islands", "+1649"},
        {"Tuvalu", "+688"},
        {"Uganda", "+256"},
        {"Ukraine", "+380"},
        {"United Arab Emirates", "+971"},
        {"United Kingdom", "+44"},
        {"United States", "+1"},
        {"Uruguay", "+598"},
        {"US Virgin Islands", "+1340"},
        {"Uzbekistan", "+998"},
        {"Vanuatu", "+678"},
        {"Venezuela", "+58"},
        {"Vietnam", "+84"},
        {"Wallis and Futuna", "+681"},
        {"Yemen", "+967"},
        {"Zambia", "+260"},
        {"Zimbabwe", "+263"}};
    /*
     * Create or Update user Fields
     */
}
