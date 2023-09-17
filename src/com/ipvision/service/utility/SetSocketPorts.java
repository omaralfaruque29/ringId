/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ipvision.constants.MyFnFSettings;
import com.ipvision.constants.ServerAndPortSettings;
import com.ipvision.constants.SettingsConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import com.ipvision.model.dao.InsertRingLoginSetting;
import com.ipvision.service.singinsignup.ForgetPassowordGetPorts;
import com.ipvision.service.singinsignup.RingIdRequest;
import com.ipvision.service.singinsignup.SignInRequest;
import com.ipvision.service.singinsignup.SignInRequestBackground;
import com.ipvision.service.singinsignup.SignUpRequest;
import com.ipvision.model.CommunicationPortsDto;
import com.ipvision.model.UserLogInInfo;

/**
 *
 * @author Wasif Islam
 */
public class SetSocketPorts {

    private boolean socketTester_stop = false;
    private int socket_counter = 0;
    private Gson jsonLib;
    private String communication_string;
    private RingIdRequest newRingIDobj;
    private SignInRequest signInObj;
    private ForgetPassowordGetPorts forgetPassObj;
    private SignUpRequest signUpRequestObj;
    private SignInRequestBackground signInBackgroundObj;
    private JLabel ringIdLbl;
    private String ringId;
    private String mblDc;
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SetSocketPorts.class);

    public SetSocketPorts(RingIdRequest obj) {
        this.newRingIDobj = obj;
    }

    public SetSocketPorts(RingIdRequest obj, String ringId) {
        this.newRingIDobj = obj;
        this.ringId = ringId;
    }

    public SetSocketPorts(SignInRequest obj, String ringId) {
        this.signInObj = obj;
        this.ringId = ringId;
    }

    public SetSocketPorts(SignInRequest obj, String ringId, String mblDc) {
        this.signInObj = obj;
        this.ringId = ringId;
        this.mblDc = mblDc;
    }

    public SetSocketPorts(SignInRequestBackground obj, String ringId) {
        this.signInBackgroundObj = obj;
        this.ringId = ringId;
    }

    public SetSocketPorts(SignInRequestBackground obj, String ringId, String mblDc) {
        this.signInBackgroundObj = obj;
        this.ringId = ringId;
        this.mblDc = mblDc;
    }

    public SetSocketPorts(ForgetPassowordGetPorts obj) {
        this.forgetPassObj = obj;
    }

    public SetSocketPorts(ForgetPassowordGetPorts obj, String ringId) {
        this.forgetPassObj = obj;
        this.ringId = ringId;
    }

    public SetSocketPorts(ForgetPassowordGetPorts obj, String ringId, String mblDc) {
        this.forgetPassObj = obj;
        this.ringId = ringId;
        this.mblDc = mblDc;
    }

    public SetSocketPorts(SignUpRequest obj, JLabel ringIdLbl) {
        this.signUpRequestObj = obj;
        this.ringIdLbl = ringIdLbl;
    }

    public void saveNewUserIDintoDB() {
        List<UserLogInInfo> newUserInfoList = new ArrayList<UserLogInInfo>();

        UserLogInInfo nameInfo = new UserLogInInfo();
        nameInfo.setKey(MyFnFSettings.KEY_NEW_USER_NAME);
        nameInfo.setValue(MyFnFSettings.VALUE_NEW_USER_NAME);
        newUserInfoList.add(nameInfo);

        (new InsertRingLoginSetting(newUserInfoList)).start();
    }

    public void set_socket_ports_with_new_ringID() {
        socketTester_stop = false;
        socket_counter = 0;

        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable periodicTask_for_set_socket_port = new Runnable() {
            @Override
            public void run() {
                if (socketTester_stop) {
                    if (newRingIDobj != null) {
                        newRingIDobj.startSignUpProcess();
                    } else if (forgetPassObj != null) {
                        forgetPassObj.buildForgetPasswordUI();
                    } else if (signUpRequestObj != null) {
                        ringIdLbl.setText(MyFnFSettings.VALUE_NEW_USER_NAME);
                    }
                    executor.shutdown();
                }
                if (communication_string == null || communication_string.trim().length() <= 0) {
                    communication_string = ServerAndPortSettings.getNewRingID();
                    log.info("ringID_string==>" + communication_string);
                }

                if (HelperMethods.check_string_contains_substring(communication_string, "true")) {
                    try {
                        jsonLib = new GsonBuilder().serializeNulls().create();
                        CommunicationPortsDto com = jsonLib.fromJson(communication_string, CommunicationPortsDto.class);
                        if (com != null) {
                            if (com.getRingID() != null && newRingIDobj != null && forgetPassObj == null) {
                                socketTester_stop = true;
                                MyFnFSettings.VALUE_NEW_USER_NAME = com.getRingID();
                                saveNewUserIDintoDB();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getAuthServerIP() != null) {
                                socketTester_stop = true;
                                ServerAndPortSettings.AUTH_SERVER_IP = com.getAuthServerIP();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getAuthPort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.AUTHENTICATION_PORT = com.getAuthPort();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getKeepAlivePort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.KEEP_ALIVE_PORT = com.getKeepAlivePort();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getVoicePort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.VOICE_PORT = com.getVoicePort();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getConfirmationPort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.CONFIRMATION_PORT = com.getConfirmationPort();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getRequestPort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.REQUEST_PORT = com.getRequestPort();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getUpdatePort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.UPDATE_PORT = com.getUpdatePort();
                            } else {
                                socketTester_stop = false;
                            }

                            if (com.getUpdatePort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.UPDATE_PORT = com.getUpdatePort();
                            } else {
                                socketTester_stop = false;
                            }

                            if (com.getSmsPort() != null && com.getSmsPort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.SMS_PORT = com.getSmsPort();
                            } else {
                                socketTester_stop = false;
                            }

                            if (com.getChatPort() != null && com.getChatPort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.CHAT_PORT = com.getChatPort();
                            } else {
                                socketTester_stop = false;
                            }

                            if (com.getDataSize() != null && com.getDataSize() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.DATA_SIZE = com.getDataSize();
                            } else {
                                socketTester_stop = false;
                            }

                            if (com.getHeaderSize() != null && com.getHeaderSize() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.HEADER_SIZE = com.getHeaderSize();
                            } else {
                                socketTester_stop = false;
                            }
                        }
                    } catch (Exception e) {
                    }
                }

                if (ServerAndPortSettings.AUTHENTICATION_PORT > 0) {
                    socketTester_stop = true;
                }
                if (socket_counter == 3) {
                    socketTester_stop = true;
                }
                socket_counter++;
            }
        };

        executor.scheduleAtFixedRate(periodicTask_for_set_socket_port, 0, 1, TimeUnit.SECONDS);
    }

    public void set_socket_port_with_existing_ringIDorEmailorPhone(final int type_int) {
        socketTester_stop = false;
        socket_counter = 0;

        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable periodicTask_for_set_socket_port = new Runnable() {
            @Override
            public void run() {
                if (socketTester_stop) {
                    if (signInObj != null) {
                        signInObj.startLoginProcess();
                    } else if (signInBackgroundObj != null) {
                        signInBackgroundObj.startLoginProcess();
                    } else if (newRingIDobj != null) {
                        newRingIDobj.startSignUpProcess();
                    } else if (forgetPassObj != null) {
                        forgetPassObj.buildForgetPasswordUI();
                    }
                    executor.shutdown();
                }
                if (communication_string == null || communication_string.trim().length() <= 0) {
                    if (type_int == SettingsConstants.RINGID_LOGIN) {
                        communication_string = ServerAndPortSettings.getCommunicationPortsByRingID(ringId);
                    } else if (type_int == SettingsConstants.EMAIL_LOGIN) {
                        communication_string = ServerAndPortSettings.getCommunicationPortsByEmail(ringId);
                    } else if (type_int == SettingsConstants.MOBILE_LOGIN) {
                        communication_string = ServerAndPortSettings.getCommunicationPortsByMobile(ringId, mblDc);
                    }
                    //   System.out.println("communication_string==>" + communication_string);
                    log.info("port_string==>" + communication_string);
                }

                if (HelperMethods.check_string_contains_substring(communication_string, "true")) {
                    try {
                        jsonLib = new GsonBuilder().serializeNulls().create();
                        CommunicationPortsDto com = jsonLib.fromJson(communication_string, CommunicationPortsDto.class);
                        if (com != null) {
                            if (com.getAuthServerIP() != null) {
                                socketTester_stop = true;
                                ServerAndPortSettings.AUTH_SERVER_IP = com.getAuthServerIP();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getAuthPort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.AUTHENTICATION_PORT = com.getAuthPort();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getKeepAlivePort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.KEEP_ALIVE_PORT = com.getKeepAlivePort();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getVoicePort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.VOICE_PORT = com.getVoicePort();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getConfirmationPort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.CONFIRMATION_PORT = com.getConfirmationPort();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getRequestPort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.REQUEST_PORT = com.getRequestPort();
                            } else {
                                socketTester_stop = false;
                            }
                            if (com.getUpdatePort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.UPDATE_PORT = com.getUpdatePort();
                            } else {
                                socketTester_stop = false;
                            }

                            if (com.getUpdatePort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.UPDATE_PORT = com.getUpdatePort();
                            } else {
                                socketTester_stop = false;
                            }

                            if (com.getSmsPort() != null && com.getSmsPort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.SMS_PORT = com.getSmsPort();
                            } else {
                                socketTester_stop = false;
                            }

                            if (com.getChatPort() != null && com.getChatPort() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.CHAT_PORT = com.getChatPort();
                            } else {
                                socketTester_stop = false;
                            }

                            if (com.getDataSize() != null && com.getDataSize() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.DATA_SIZE = com.getDataSize();
                            } else {
                                socketTester_stop = false;
                            }

                            if (com.getHeaderSize() != null && com.getHeaderSize() > 0) {
                                socketTester_stop = true;
                                ServerAndPortSettings.HEADER_SIZE = com.getHeaderSize();
                            } else {
                                socketTester_stop = false;
                            }
                        }
                    } catch (Exception e) {
                    } finally {
                        //  executor.shutdown();
                    }
                }

                if (ServerAndPortSettings.AUTHENTICATION_PORT > 0) {
                    socketTester_stop = true;
                }
                if (socket_counter == 3) {
                    socketTester_stop = true;
                }
                socket_counter++;
            }
        };

        executor.scheduleAtFixedRate(periodicTask_for_set_socket_port, 0, 1, TimeUnit.SECONDS);
    }
}
