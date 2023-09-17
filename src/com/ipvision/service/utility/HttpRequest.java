package com.ipvision.service.utility;

import com.ipvision.constants.SettingsConstants;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpRequest {

    private static int READ_TIME_OUT = 2500;

    /*  public static String getJsonByRingID(String url, String code, String number) {
     StringBuilder response = new StringBuilder();
     HttpURLConnection conn = null;
     InputStream is = null;
     try {
     String urlParameters = urlPair("dialingCode", code) + "&" + urlPair("phoneNumber", number);

     URL obj = new URL(url);
     conn = (HttpURLConnection) obj.openConnection();
     conn.setRequestMethod("POST");
     conn.setReadTimeout(READ_TIME_OUT);
     conn.setDoOutput(true);

     DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
     wr.writeBytes(urlParameters);
     wr.flush();
     wr.close();

     is = conn.getInputStream();
     BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
     String inputLine;

     while ((inputLine = in.readLine()) != null) {
     response.append(inputLine);
     }

     in.close();
     } catch (Exception ex) {

     } finally {
     if (is != null) {
     try {
     is.close();
     } catch (IOException e) {
     }
     }
     if (conn != null) {
     conn.disconnect();
     }
     }

     return response.toString();
     }*/
    public static String getJsonByRingID(String url, String ringId) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String urlParameters = urlPair("lt", "" + SettingsConstants.RINGID_LOGIN) + "&" + urlPair("ringID", ringId);

            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(READ_TIME_OUT);
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (Exception ex) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return response.toString();
    }

    public static String getJsonByEmail(String url, String email) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String urlParameters = urlPair("lt", "" + SettingsConstants.EMAIL_LOGIN) + "&" + urlPair("el", email);

            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(READ_TIME_OUT);
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (Exception ex) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return response.toString();
    }

    public static String getJsonByMobile(String url, String mobile, String mblDc) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String urlParameters = urlPair("lt", "" + SettingsConstants.MOBILE_LOGIN) + "&" + urlPair("mbl", mobile) + "&" + urlPair("mblDc", mblDc.trim());

            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(READ_TIME_OUT);
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (Exception ex) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return response.toString();
    }

    public static String getRingID(String url) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String urlParameters = urlPair("did", HelperMethods.getPCMacAddress());

            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(READ_TIME_OUT);
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (Exception ex) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return response.toString();
    }

    private static String urlPair(String name, String val) {
        try {
            return name + "=" + URLEncoder.encode(val.toString(), "UTF-8");
        } catch (Exception ex) {
            return "";
        }
    }
}
