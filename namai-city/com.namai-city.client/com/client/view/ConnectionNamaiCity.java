package com.client.view;

import java.awt.CardLayout;

import javax.swing.JFrame;



public class ConnectionNamaiCity extends JFrame {
	CardLayout superpos;
	PanneauLoginNamaiCity pan;
	PanneauBienvenueNamaiCity menu;
	PanneaApplication pa;
	
	
	
	public ConnectionNamaiCity() {
		super("NAMAI-CITY");
		pan = new PanneauLoginNamaiCity();
		pa = new PanneaApplication();
		
		menu = new PanneauBienvenueNamaiCity();
		superpos= new CardLayout();
	
		this.setLayout(superpos);
		this.setLocation(550, 150);
		this.setSize(600,600);
		this.getContentPane().add("fr",pan);
		this.getContentPane().add("de",pa);
		superpos.show(this.getContentPane(), "fr");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public CardLayout getSuperpos() {
		return superpos;
	}
	public PanneauLoginNamaiCity getPan() {
		return pan;
	}
	public PanneauBienvenueNamaiCity getMenu() {
		return menu;
	}
	public PanneaApplication getPa() {
		return pa;
	}

}
