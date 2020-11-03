package com.client.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;



public class PanneauBienvenueNamaiCity extends JPanel  {
	private JMenuBar mbar;
	private JMenu men;
	private CardLayout cd;
	private PanneauConfigurationPollution pcp;
	
	
	public PanneauBienvenueNamaiCity () {
		super();
		cd = new CardLayout();
		pcp = new PanneauConfigurationPollution();
	
	
		this.setLayout(new BorderLayout());
		this.setBackground(ImageApp.getBgApp());
		JLabel labelAccueil = new JLabel("Bienvenue Dans L'univers NAMAI-CITY!");
		labelAccueil.setHorizontalAlignment(JLabel.CENTER);
		labelAccueil.setFont(new Font("Arial", Font.BOLD, 45));
		labelAccueil.setBorder(new LineBorder(ImageApp.getBgTitle(), 3));
		labelAccueil.setForeground(ImageApp.getBgApp());
		labelAccueil.setOpaque(true);
		labelAccueil.setBackground(ImageApp.getBgTitle());
		this.add(labelAccueil, BorderLayout.SOUTH);
		JLabel center = new JLabel();
		center.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		center.setIcon(new ImageIcon("ressources\\namai-city2.gif") );
		this.add(center,BorderLayout.CENTER);

		}

	

}
