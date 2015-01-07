/*
 * Created by JFormDesigner on Fri Dec 26 11:05:52 CST 2014
 */

package com.monitor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

/**
 * @author shenxin xu
 */
public class vixform2 extends JFrame {

    ShortStraddleHedge hedge;
    public vixform2() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.stopBtn.setEnabled(false);

    }

    private void startBtnActionPerformed(ActionEvent e) {
        // TODO add your code here
        System.out.println("start button clicked");

        // get the value from textfield
        double upThreshold = Double.parseDouble(upThresholdTextField.getText());
        double downThreshold = Double.parseDouble(downThresholdTextField.getText());
        int interval = Integer.parseInt(intervalTextField.getText());
        int futureLongShort = Integer.parseInt(futurePositionTextField.getText());




        hedge = new ShortStraddleHedge(upThreshold, downThreshold, interval, futureLongShort, null);
        hedge.assignLogger(new TextAreaLogger(logTextArea));
        hedge.startHedge();
        this.startBtn.setEnabled(false);
        this.stopBtn.setEnabled(true);
    }

    private void stopBtnActionPerformed(ActionEvent e) {
        // TODO add your code here
        this.hedge.stopHedge();
        this.startBtn.setEnabled(true);
        this.stopBtn.setEnabled(false);
    }

    private void orderBtnActionPerformed(ActionEvent e) {
        // TODO add your code here
        System.out.println("order clicked");

        this.hedge.sell(1, 20);
        this.hedge.handler.m_socket.reqOpenOrders();
    }

    private void reqOpenOrdersBtnActionPerformed(ActionEvent e) {
        // TODO add your code here
        this.hedge.handler.m_socket.reqOpenOrders();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - shenxin xu
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        upThresholdTextField = new JTextField();
        label2 = new JLabel();
        downThresholdTextField = new JTextField();
        label3 = new JLabel();
        futurePositionTextField = new JTextField();
        scrollPane1 = new JScrollPane();
        logTextArea = new JTextArea();
        label4 = new JLabel();
        intervalTextField = new JTextField();
        orderBtn = new JButton();
        reqOpenOrdersBtn = new JButton();
        buttonBar = new JPanel();
        updateBtn = new JButton();
        startBtn = new JButton();
        stopBtn = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

            // JFormDesigner evaluation mark
            dialogPane.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), dialogPane.getBorder())); dialogPane.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- label1 ----
                label1.setText("Up Threshold");

                //---- upThresholdTextField ----
                upThresholdTextField.setText("19");

                //---- label2 ----
                label2.setText("Down Threshold");

                //---- downThresholdTextField ----
                downThresholdTextField.setText("17");

                //---- label3 ----
                label3.setText("Future Position");

                //---- futurePositionTextField ----
                futurePositionTextField.setText("0");

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(logTextArea);
                }

                //---- label4 ----
                label4.setText("Interval");

                //---- intervalTextField ----
                intervalTextField.setText("30");

                //---- orderBtn ----
                orderBtn.setText("Order");
                orderBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        orderBtnActionPerformed(e);
                    }
                });

                //---- reqOpenOrdersBtn ----
                reqOpenOrdersBtn.setText("reqOpenOrder");
                reqOpenOrdersBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        reqOpenOrdersBtnActionPerformed(e);
                    }
                });

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(upThresholdTextField, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                    .addComponent(label1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                                .addComponent(downThresholdTextField, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label2, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label4)
                                .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(intervalTextField, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                    .addComponent(futurePositionTextField, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                    .addComponent(label3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                                .addComponent(orderBtn)
                                .addComponent(reqOpenOrdersBtn))
                            .addGap(18, 18, 18)
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addComponent(label1)
                            .addGap(4, 4, 4)
                            .addComponent(upThresholdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(5, 5, 5)
                            .addComponent(label2)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(downThresholdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(label3)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(futurePositionTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(label4)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(intervalTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(orderBtn)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(reqOpenOrdersBtn)
                            .addGap(0, 66, Short.MAX_VALUE))
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0.0};

                //---- updateBtn ----
                updateBtn.setText("Update");
                buttonBar.add(updateBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- startBtn ----
                startBtn.setText("Start");
                startBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        startBtnActionPerformed(e);
                    }
                });
                buttonBar.add(startBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- stopBtn ----
                stopBtn.setText("Stop");
                stopBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        stopBtnActionPerformed(e);
                    }
                });
                buttonBar.add(stopBtn, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - shenxin xu
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField upThresholdTextField;
    private JLabel label2;
    private JTextField downThresholdTextField;
    private JLabel label3;
    private JTextField futurePositionTextField;
    private JScrollPane scrollPane1;
    private JTextArea logTextArea;
    private JLabel label4;
    private JTextField intervalTextField;
    private JButton orderBtn;
    private JButton reqOpenOrdersBtn;
    private JPanel buttonBar;
    private JButton updateBtn;
    private JButton startBtn;
    private JButton stopBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
