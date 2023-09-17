/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipvision.view.utility;

import com.ipvision.view.utility.DesignClasses;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author Sirat Samyoun
 */
public class DatePanel extends JPanel {

    String[] ARRAY_MONTH = {" January ", " February ", " March ", " April ", " May ", " June ", " July ", " August ", " September ", " October ", " November ", " December "};
    JComboBox monthcomboBox, yearcomboBox, daycomboBox;
    int y, m, d;
    int startingyr, startingmn, startingday, selectedyr, selectedmn, selectedday;// endingyr, endingmn, endingday;

    public DatePanel(int startingyr, int startingmn, int startingday) {
        this.startingyr = startingyr;
        this.startingmn = startingmn;
        this.startingday = startingday;
        this.selectedyr = startingyr;
        this.selectedmn = startingmn;
        this.selectedday = startingday;
        setLayout(new BorderLayout());

        setOpaque(false);
        setPreferredSize(new Dimension(195, 20));
        init();
    }

    public DatePanel(int startingyr, int startingmn, int startingday, int selectedyr, int selectedmn, int selectedday) {
        this.startingyr = startingyr;
        this.startingmn = startingmn;
        this.startingday = startingday;
        this.selectedyr = selectedyr;
        this.selectedmn = selectedmn;
        this.selectedday = selectedday;
        setLayout(new BorderLayout());
        // setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
        setOpaque(false);
        setPreferredSize(new Dimension(195, 20));
        init();
    }

    public void init() {
        JPanel holder = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        holder.setOpaque(false);
        //add(Box.createRigidArea(new Dimension(10, 2)), BorderLayout.NORTH);
        add(holder, BorderLayout.CENTER);

        monthcomboBox = DesignClasses.createJCombobox(ARRAY_MONTH);
        monthcomboBox.setSelectedIndex(selectedmn - 1);

        yearcomboBox = DesignClasses.createJCombobox();
        for (int i = startingyr; i < 2016; i++) {
            yearcomboBox.addItem("  " + i + "  ");
        }
        yearcomboBox.setSelectedItem("  " + selectedyr + "  ");

        daycomboBox = DesignClasses.createJCombobox();
        Calendar cal = new GregorianCalendar(selectedyr, selectedmn - 1, 11);
        int initialDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= initialDaysInMonth; i++) {
            daycomboBox.addItem("  " + i + "  ");
        }
        daycomboBox.setSelectedItem("  " + selectedday + "  ");

//        yearcomboBox.setSelectedItem("  " + y + "  ");
//        monthcomboBox.setSelectedIndex(m);
//        daycomboBox.setSelectedItem("  " + d + "  ");
        monthcomboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    y = Integer.parseInt(yearcomboBox.getSelectedItem().toString().trim());
                    m = monthcomboBox.getSelectedIndex();
                    Calendar mycal = new GregorianCalendar(y, m, 11);
                    int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    daycomboBox.removeAllItems();
                    for (int i = 1; i <= daysInMonth; i++) {
                        daycomboBox.addItem("  " + i + "  ");
                    }
                }
            }
        });
        yearcomboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    y = Integer.parseInt(yearcomboBox.getSelectedItem().toString().trim());
                    m = monthcomboBox.getSelectedIndex();
                    Calendar mycal = new GregorianCalendar(y, m, 11);
                    int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    daycomboBox.removeAllItems();
                    for (int i = 1; i <= daysInMonth; i++) {
                        daycomboBox.addItem("  " + i + "  ");
                    }
                }
            }
        });
        holder.add(yearcomboBox);
        holder.add(monthcomboBox);
        holder.add(daycomboBox);
    }

    public void disableBoxes() {
        yearcomboBox.setEnabled(false);
        monthcomboBox.setEnabled(false);
        daycomboBox.setEnabled(false);
    }

    public void enableBoxes() {
        yearcomboBox.setEnabled(true);
        monthcomboBox.setEnabled(true);
        daycomboBox.setEnabled(true);
    }

    public long getDateinMillisecs() {
        int day = Integer.parseInt(daycomboBox.getSelectedItem().toString().trim());
        int month = (int) monthcomboBox.getSelectedIndex() + 1;
        int year = Integer.parseInt(yearcomboBox.getSelectedItem().toString().trim());
        Date d = new Date();
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse("" + year + "-" + month + "-" + day + "");
        } catch (Exception e) {
        }
        return d.getTime();
    }

    public int getAge() {
        int day = Integer.parseInt(daycomboBox.getSelectedItem().toString().trim());
        int month = (int) monthcomboBox.getSelectedIndex() + 1;
        int year = Integer.parseInt(yearcomboBox.getSelectedItem().toString().trim());
        Date now = new Date();
        int nowMonth = now.getMonth() + 1;
        int nowYear = now.getYear() + 1900;
        int result = nowYear - year;

        if (month > nowMonth) {
            result--;
        } else if (month == nowMonth) {
            int nowDay = now.getDate();

            if (day > nowDay) {
                result--;
            }
        }
        return result;
    }

    public String getStringDate() {
        int day = Integer.parseInt(daycomboBox.getSelectedItem().toString().trim());
        int month = (int) monthcomboBox.getSelectedIndex() + 1;
        int year = Integer.parseInt(yearcomboBox.getSelectedItem().toString().trim());
        return year + "-" + month + "-" + day;
    }

    public String DateasString() {
        return "" + monthcomboBox.getSelectedItem() + " " + daycomboBox.getSelectedItem() + ", " + yearcomboBox.getSelectedItem() + "";
    }
}
