/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.constants.MyFnFSettings;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import com.ipvision.view.GuiRingID;
import com.ipvision.model.NewsFeedWithMultipleImage;
import com.ipvision.constants.XMLConstants;
import com.ipvision.model.UserBasicInfo;
import com.ipvision.model.SingleBookDetails;
import com.ipvision.model.stores.FriendList;
import java.awt.Desktop;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 *
 * @author Faiz
 */
public class FeedUtils {

    public static UserBasicInfo setBasicUserInfoForFeedUser(UserBasicInfo basicinfo, NewsFeedWithMultipleImage statusDto) {
        if (statusDto.getUserIdentity() == null) {
            basicinfo = MyFnFSettings.userProfile;
        } else if (statusDto.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
            basicinfo = MyFnFSettings.userProfile;
        } else {
            if (statusDto.getUserIdentity() != null && FriendList.getInstance() != null && !FriendList.getInstance().getFriend_hash_map().isEmpty()) {

                if (FriendList.getInstance().getFriend_hash_map().get(statusDto.getUserIdentity()) != null) {
                    basicinfo = FriendList.getInstance().getFriend_hash_map().get(statusDto.getUserIdentity());
                } else {
                    basicinfo = new UserBasicInfo();
                    if (statusDto.getUserIdentity() != null) {
                        basicinfo.setUserIdentity(statusDto.getUserIdentity());
                        basicinfo.setFullName(statusDto.getUserIdentity());
                    }
                    if (statusDto.getFullName() != null) {
                        basicinfo.setFullName(statusDto.getFullName());
                    } else {
                        basicinfo.setFullName(statusDto.getUserIdentity());
                    }
                    /*if (statusDto.getLastName() != null) {
                     basicinfo.setLastName(statusDto.getLastName());
                     } else {
                     basicinfo.setLastName("");
                     }*/
                }
            } else {
                basicinfo = new UserBasicInfo();
                if (statusDto.getUserIdentity() != null) {
                    basicinfo.setUserIdentity(statusDto.getUserIdentity());
                    basicinfo.setFullName(statusDto.getUserIdentity());
                }
                if (statusDto.getFullName() != null) {
                    basicinfo.setFullName(statusDto.getFullName());
                } else {
                    basicinfo.setFullName(statusDto.getUserIdentity());
                }
                /*if (statusDto.getLastName() != null) {
                 basicinfo.setLastName(statusDto.getLastName());
                 } else {
                 basicinfo.setLastName("");
                 }*/

            }

        }
        return basicinfo;
    }

    public static void onClickFriendName(final JLabel label, final UserBasicInfo userBasicInfo) {
        label.addMouseListener(new MouseAdapter() {
            Font original;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (userBasicInfo.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                    e.getComponent().setFont(original);
                    GuiRingID.getInstance().action_of_myProfile_button();
                } else if (FriendList.getInstance().getFriend_hash_map().get(userBasicInfo.getUserIdentity()) != null) {
                    e.getComponent().setFont(original);
                    GuiRingID.getInstance().showFriendProfile(userBasicInfo.getUserIdentity());
                } else {
                    e.getComponent().setFont(original);
                    GuiRingID.getInstance().showUnknownProfile(userBasicInfo);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                Map attributes = original.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                e.getComponent().setFont(original.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
            }
        });
    }

    public static void onClickCircleName(final JLabel label, final long circleId) {
        label.addMouseListener(new MouseAdapter() {
            Font original;

            @Override
            public void mouseClicked(MouseEvent e) {
                e.getComponent().setFont(original);
                GuiRingID.getInstance().getMainRight().showCircleProfile(circleId);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                original = e.getComponent().getFont();
                Map attributes = original.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                e.getComponent().setFont(original.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.getComponent().setFont(original);
            }
        });
    }
    /* (specially for feed.) proper showable time will be as below formats:
     * Newsfeed Time should be all in am  and pm  
     * or 1 min ago /30min ago/  1 hr ago / 23hr ago/ 
     * Yesterday at 9.36 PM, Tuesday at 10.30PM/ 
     * February 3 at 6.06AM/
     * 
     * @param timestamp
     * @return properShowableString
     */

    public static String getShowableTimeNotification(long milliSeconds) {
        long currentime = System.currentTimeMillis();
        long duration = currentime - milliSeconds;
        if (duration < 0) {
            //return convetToDate(timestamp);
            return getFormatedTime(milliSeconds, "yyyy MMMM dd");
        }
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        if (days > 0) {

            Date currentDate = new Date(currentime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long diff = cal.getTimeInMillis() - milliSeconds;
            long d = TimeUnit.MILLISECONDS.toDays(diff);

            if (d == 0) {
                return getFormatedTime(milliSeconds, "hh.mm a");
            } else if (d < 6) {
                return getFormatedTime(milliSeconds, "hh.mm a");
            } else if (d < 365) {
                return getFormatedTime(milliSeconds, "hh.mm a");
            } else {
                return getFormatedTime(milliSeconds, "yyyy MMMM dd");
            }

        }
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        if (hours > 0) {
            return hours + (hours == 1 ? " hr" : " hrs") + " ago";
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        if (minutes > 0) {
            return minutes + (minutes == 1 ? " min" : " mins") + " ago";
        }

        return "few secs ago";
    }

    public static String getActualDate(long milliSeconds) {
        return getFormatedTime(milliSeconds, "EEEE',' MMMM dd',' yyyy 'at' hh.mm a");
    }

    public static String getDateForPending(long milliSeconds) {
        return getFormatedTime(milliSeconds, "MMMM dd',' yyyy 'at' hh.mm a");
    }

    public static String getShowableDate(long milliSeconds) {
        /*Date date = new Date(milliSeconds);
         String dateformate = "hh:mm aaa dd MMM yyyy";
         DateFormat format = new SimpleDateFormat(dateformate);
         String formatted = format.format(date);
         return formatted;*/

        long currentime = System.currentTimeMillis();
        long duration = currentime - milliSeconds;
        if (duration < 0) {
            //return convetToDate(timestamp);
            return getFormatedTime(milliSeconds, "yyyy MMMM dd");
        }
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        if (days > 0) {

            Date currentDate = new Date(currentime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long diff = cal.getTimeInMillis() - milliSeconds;
            long d = TimeUnit.MILLISECONDS.toDays(diff);

            if (d == 0) {
                return getFormatedTime(milliSeconds, "'Yesterday at' hh.mm a");
            } else if (d < 6) {
                return getFormatedTime(milliSeconds, "EEEE 'at' hh.mm a");
            } else if (d < 365) {
                return getFormatedTime(milliSeconds, "MMMM dd 'at' hh.mm a");
            } else {
                return getFormatedTime(milliSeconds, "yyyy MMMM dd");
            }

        }
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        if (hours > 0) {
            return hours + (hours == 1 ? " hr" : " hrs") + " ago";
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        if (minutes > 0) {
            return minutes + (minutes == 1 ? " min" : " mins") + " ago";
        }
        return "few secs ago";

    }

    public static String getShowableDateForLeftPane(long milliSeconds) {

        long currentime = System.currentTimeMillis();
        long duration = currentime - milliSeconds;
        if (duration < 0) {
            return getFormatedTime(milliSeconds, "yyyy MMMM dd");
        }
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        if (days > 0) {

            Date currentDate = new Date(currentime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long diff = cal.getTimeInMillis() - milliSeconds;
            long d = TimeUnit.MILLISECONDS.toDays(diff);

            if (d == 0) {
                return getFormatedTime(milliSeconds, "'Yesterday at' hh.mm a");
            } else if (d < 6) {
                return getFormatedTime(milliSeconds, "EEEE 'at' hh.mm a");
            } else if (d < 365) {
                return getFormatedTime(milliSeconds, "MMM dd 'at' hh.mm a");
            } else {
                return getFormatedTime(milliSeconds, "MMM dd, yyyy 'at' hh.mm a");
            }

        }
        return getFormatedTime(milliSeconds, "hh:mm a");

    }

    /**
     * for format see
     * http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
     *
     * @param timestamp
     * @param format
     * @return formated String
     */
    public static String getFormatedTime(long timestamp, String format) {
        return new SimpleDateFormat(format).format(new Date(timestamp));
    }

    public static void changeComment(String text, int width, JPanel textPanel) {
        Pattern reqular_expresion = null;
        HTMLDocument chat_doc = null;
        HTMLEditorKit editorKit;
        HtmlHelpers htmlHelp = new HtmlHelpers();
        if (text.length() > 0) {
            try {
                if (XMLConstants.EMOTION_REGULAR_EXPRESSION != null) {
                    reqular_expresion = Pattern.compile(XMLConstants.EMOTION_REGULAR_EXPRESSION);
                }
                textPanel.removeAll();
                String replace = htmlHelp.replaceStringForEmoticons(text, reqular_expresion);
                if (!replace.equals(text)) {
                    JTextWrapPane area = new JTextWrapPane();
                    area.setOpaque(false);
                    area.setEditable(false);
                    area.addMouseListener(new ContextMenuMouseListener());
                    area.addHyperlinkListener(new HyperlinkListener() {

                        @Override
                        public void hyperlinkUpdate(HyperlinkEvent e) {
                            HyperlinkEvent.EventType type = e.getEventType();
                            if (type == HyperlinkEvent.EventType.ACTIVATED) {
                                if (!(e instanceof HTMLFrameHyperlinkEvent)) {
                                    try {
                                        Desktop.getDesktop().browse(e.getURL().toURI());
                                    } catch (Exception ex) {
                                    }
                                }
                            }
                        }
                    });
                    area.setContentType("text/html");
                    chat_doc = (HTMLDocument) area.getDocument();
                    editorKit = (HTMLEditorKit) area.getEditorKit();
                    editorKit.insertHTML(chat_doc, chat_doc.getLength(), htmlHelp.setStyle_inChat(width), 0, 0, null);
                    String style = "singleChat";
                    String url = "<div  id=\"" + style + "\">" + replace + "</div>";
                    try {
                        editorKit.insertHTML(chat_doc, chat_doc.getLength(), url, 0, 0, null);
                    } catch (BadLocationException ex) {
                    } catch (IOException ex) {
                    }

                    textPanel.add(area);
                    area.setCaretPosition(chat_doc.getLength());
                } else {
                    JTextArea textOnly = DesignClasses.createJTextAreaNoBorderNogap(text);
                    textOnly.setOpaque(false);
                    textOnly.addMouseListener(new ContextMenuMouseListener());
                    textPanel.add(textOnly);
                }
                textPanel.revalidate();
                textPanel.repaint();

            } catch (Exception e) {
            }

        }
    }

//    public boolean whoShareDetails(ArrayList<SingleBookDetails> whoShare, JLabel l, int idx) {
//        // for (int i = 0; i <= whoShare.size(); i++){
//        //boolean isUserLogin = true;
//
//        if (whoShare.get(idx).getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
//            
//            l.setText(StaticFields.STRING_YOU);            
//            //whoShare.remove(0);
//            return true;
//            
//
//        } else {
//            if (whoShare.get(idx).getLastName() != null && whoShare.get(idx).getLastName().length() > 0) {
//                l.setText(whoShare.get(idx).getFullName() + " " + whoShare.get(idx).getLastName());
//            } else {
//                l.setText(whoShare.get(idx).getFullName());
//            }
//            
//            l.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            UserBasicInfo firstPerson = new UserBasicInfo();
//            firstPerson.setUserIdentity(whoShare.get(idx).getUserIdentity());
//            onClickFriendName(l, firstPerson);
//            return false;
//            
//        }
//        
//        
//    }
    public static String makeResponsePacket(String packid, int type) {
        return packid + "_" + type;
    }

    public static boolean whoShareDetails(ArrayList<SingleBookDetails> whoShareList) {

        for (SingleBookDetails whoShareList1 : whoShareList) {
            if (whoShareList1.getUserIdentity().equals(MyFnFSettings.LOGIN_USER_ID)) {
                return true;
            }
        }
        return false;

    }

    public static boolean isMe(String userID) {
        return userID.equals(MyFnFSettings.LOGIN_USER_ID);
    }

}
