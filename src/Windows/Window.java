package Windows;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mainClasses.Menu;

public class Window {
    private JButton loadImageButton;
    public JPanel main;
    private JPanel imgSrc;
    private JLabel Source;
    private JLabel Grey;
    private JButton doHistogramButton;
    private JPanel BW;
    private JLabel Hist;
    private JLabel Result;
    private JButton convertToBlackAndButton;
    private JSlider sliderBW;
    private JButton convertToGreyButton;
    private JButton doFiltersButton;
    private JButton zhangSuenButton;
    private JButton minutiasButton;
    private JButton anglesButton;
    private JButton showMinutiasButton;
    private JButton doBinaryFilter1Button;
    private JButton doBinaryFilter2Button;

    public Window() {
        loadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int selection = fileChooser.showOpenDialog(main);
                if(selection == JFileChooser.APPROVE_OPTION){
                    Menu.setImgSrc(fileChooser.getSelectedFile().getAbsolutePath());
                    Menu.initialize();
                    Source.setText("");
                    if(Menu.getImgSrc().getWidth() < Menu.getImgSrc().getHeight())
                        Source.setIcon(new ImageIcon(Menu.getImgSrc().getScaledInstance(160,240, Image.SCALE_SMOOTH)));
                    else
                        Source.setIcon(new ImageIcon(Menu.getImgSrc().getScaledInstance(420,280, Image.SCALE_SMOOTH)));
                }
            }
        });
        convertToBlackAndButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.convertToGrey();
                Menu.convertToBW(sliderBW.getValue());
                Result.setText("");
                if(Menu.getImgSrc().getWidth() < Menu.getImgSrc().getHeight())
                    Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(160,240, Image.SCALE_SMOOTH)));
                else
                    Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(420,280, Image.SCALE_SMOOTH)));

            }
        });

        sliderBW.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!sliderBW.getValueIsAdjusting()) {
                    Menu.convertToGrey();
                    Menu.convertToBW(sliderBW.getValue());
                    Menu.setLastLimitValue(sliderBW.getValue());
                    Result.setText("");
                    if(Menu.getImgSrc().getWidth() < Menu.getImgSrc().getHeight())
                        Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(160,240, Image.SCALE_SMOOTH)));
                    else
                        Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(420,280, Image.SCALE_SMOOTH)));
                }
            }
        });
        convertToGreyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.convertToGrey();
                Result.setText("");
                if(Menu.getImgSrc().getWidth() < Menu.getImgSrc().getHeight())
                    Result.setIcon(new ImageIcon(Menu.getImgOutput().getScaledInstance(160,240, Image.SCALE_SMOOTH)));
                else
                    Result.setIcon(new ImageIcon(Menu.getImgOutput().getScaledInstance(420,280, Image.SCALE_SMOOTH)));
            }
        });
        doHistogramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.doHistogram();
                Result.setText("");
                if(Menu.getImgSrc().getWidth() < Menu.getImgSrc().getHeight())
                    Result.setIcon(new ImageIcon(Menu.getImgOutput().getScaledInstance(160,240, Image.SCALE_SMOOTH)));
                else
                    Result.setIcon(new ImageIcon(Menu.getImgOutput().getScaledInstance(420,280, Image.SCALE_SMOOTH)));
            }
        });
        doFiltersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.doFilters();
                Result.setText("");
                if(Menu.getImgSrc().getWidth() < Menu.getImgSrc().getHeight())
                    Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(160,240, Image.SCALE_SMOOTH)));
                else
                    Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(420,280, Image.SCALE_SMOOTH)));
            }
        });
        zhangSuenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.doZS();
                Result.setText("");
                if(Menu.getImgSrc().getWidth() < Menu.getImgSrc().getHeight())
                    Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(160,240, Image.SCALE_SMOOTH)));
                else
                    Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(420,280, Image.SCALE_SMOOTH)));
            }
        });
        minutiasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.CrossingNumbers();
                Result.setText("");
                if(Menu.getImgSrc().getWidth() < Menu.getImgSrc().getHeight())
                    Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(160,240, Image.SCALE_SMOOTH)));
                else
                    Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(420,280, Image.SCALE_SMOOTH)));
            }
        });
        anglesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.doAngles();
                Result.setText("");
                if(Menu.getImgSrc().getWidth() < Menu.getImgSrc().getHeight())
                    Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(160,240, Image.SCALE_SMOOTH)));
                else
                    Result.setIcon(new ImageIcon(Menu.getImgBW().getScaledInstance(420,280, Image.SCALE_SMOOTH)));
            }
        });

        showMinutiasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.showMinutias();
                Result.setText("");
                if(Menu.getImgSrc().getWidth() < Menu.getImgSrc().getHeight())
                    Result.setIcon(new ImageIcon(Menu.getImgOutput().getScaledInstance(160,240, Image.SCALE_SMOOTH)));
                else
                    Result.setIcon(new ImageIcon(Menu.getImgOutput().getScaledInstance(420,280, Image.SCALE_SMOOTH)));
            }
        });
    }
}
