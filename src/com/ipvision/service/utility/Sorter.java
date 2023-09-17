/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.service.utility;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Sorter {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Sorter.class);
    //  static Logger logger = Logger.getLogger(Sorter.class.getName());
    private static String methodName;

    public Sorter() {
    }

    public static String getMethodName() {
        return methodName;
    }

    public static void setMethodName(String methodName) {
        Sorter.methodName = methodName;
    }

    public static ArrayList sortArrayListASC(ArrayList list, String methodName) {
        setMethodName(methodName);
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    int val = 0;
                    try {
                        java.lang.Class[] params = {};
                        java.lang.Object[] objs = {};
                        Class cls = Object.class.forName(o1.getClass().getName());
                        Method meth1 = cls.getMethod(getMethodName(), params);
                        cls = Object.class.forName(o2.getClass().getName());
                        Method meth2 = cls.getMethod(getMethodName(), params);

                        if (Long.parseLong(String.valueOf(meth1.invoke(o1, objs))) > Long.parseLong(String.valueOf(meth2.invoke(o2, objs)))) {
                            val = 1;
                        }
                    } catch (Exception ex) {
                        //  CustomLogger.log("exception during sorting arraylist-->" + ex);
                    }
                    return val;
                }
            });
        }
        return list;
    }

    public static ArrayList sortArrayListDESC(ArrayList list, String methodName) {
        setMethodName(methodName);
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    int val = 0;
                    try {
                        java.lang.Class[] params = {};
                        java.lang.Object[] objs = {};
                        Class cls = Object.class.forName(o1.getClass().getName());
                        Method meth1 = cls.getMethod(getMethodName(), params);
                        cls = Object.class.forName(o2.getClass().getName());
                        Method meth2 = cls.getMethod(getMethodName(), params);

                        if (Long.parseLong(String.valueOf(meth1.invoke(o1, objs))) < Long.parseLong(String.valueOf(meth2.invoke(o2, objs)))) {
                            val = 1;
                        }
                    } catch (Exception ex) {
                        log.error("Error in here ==>" + ex.getMessage());
                        // ex.printStackTrace();
                        //CustomLogger.log("exception during sorting arraylist-->" + ex);
                    }
                    return val;
                }
            });
        }
        return list;
    }

    public static ArrayList sortFloatingArrayListASC(ArrayList list, String methodName) {
        setMethodName(methodName);
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    int val = 0;
                    try {
                        java.lang.Class[] params = {};
                        java.lang.Object[] objs = {};
                        Class cls = Object.class.forName(o1.getClass().getName());
                        Method meth1 = cls.getMethod(getMethodName(), params);
                        cls = Object.class.forName(o2.getClass().getName());
                        Method meth2 = cls.getMethod(getMethodName(), params);

                        if (Double.parseDouble(String.valueOf(meth1.invoke(o1, objs))) > Double.parseDouble(String.valueOf(meth2.invoke(o2, objs)))) {
                            val = 1;
                        }
                    } catch (Exception ex) {
                        //  CustomLogger.log("exception during sorting arraylist-->" + ex);
                    }
                    return val;
                }
            });
        }
        return list;
    }

    public static ArrayList sortFloatingArrayListDESC(ArrayList list, String methodName) {
        setMethodName(methodName);
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    int val = 0;
                    try {
                        java.lang.Class[] params = {};
                        java.lang.Object[] objs = {};
                        Class cls = Object.class.forName(o1.getClass().getName());
                        Method meth1 = cls.getMethod(getMethodName(), params);
                        cls = Object.class.forName(o2.getClass().getName());
                        Method meth2 = cls.getMethod(getMethodName(), params);

                        if (Double.parseDouble(String.valueOf(meth1.invoke(o1, objs))) < Double.parseDouble(String.valueOf(meth2.invoke(o2, objs)))) {
                            val = 1;
                        }
                    } catch (Exception ex) {
                        //  CustomLogger.log("exception during sorting arraylist-->" + ex);
                    }
                    return val;
                }
            });
        }
        return list;
    }

    public static List sortStrArrayListASC(List list, String methodName) {
        setMethodName(methodName);
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    try {
                        java.lang.Class[] params = {};
                        java.lang.Object[] objs = {};
                        Class cls = Object.class.forName(o1.getClass().getName());
                        Method meth1 = cls.getMethod(getMethodName(), params);
                        cls = Object.class.forName(o2.getClass().getName());
                        Method meth2 = cls.getMethod(getMethodName(), params);
                        if (String.valueOf(meth1.invoke(o1, objs)).compareToIgnoreCase(String.valueOf(meth2.invoke(o2, objs))) > 0) {
                            return 1;
                        }
                    } catch (Exception ex) {
                        // CustomLogger.log("exception during sorting arraylist-->" + ex);
                    }
                    return 0;
                }
            });
        }
        return list;
    }

    public static ArrayList sortStrArrayListDESC(ArrayList list, String methodName) {
        setMethodName(methodName);
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    try {
                        java.lang.Class[] params = {};
                        java.lang.Object[] objs = {};
                        Class cls = Object.class.forName(o1.getClass().getName());
                        Method meth1 = cls.getMethod(getMethodName(), params);
                        cls = Object.class.forName(o2.getClass().getName());
                        Method meth2 = cls.getMethod(getMethodName(), params);
                        if (String.valueOf(meth1.invoke(o1, objs)).compareToIgnoreCase(String.valueOf(meth2.invoke(o2, objs))) < 0) {
                            return 1;
                        }
                    } catch (Exception ex) {
                        // CustomLogger.log("exception during sorting arraylist-->" + ex);
                    }
                    return 0;
                }
            });
        }
        return list;
    }
}
