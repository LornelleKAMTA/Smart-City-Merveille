package com.client.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class MenuApplication extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar mbar;
	private JMenu men;
	
	private JButton empreinte;
	private JButton pollution; 
	private JButton borne; 
	private JButton indicateur;
	private JButton detecteur;
	private JButton accueil;
	
	
	
	public MenuApplication() {
		super();
		empreinte = new JButton("empreinteCarbone");
		pollution = new JButton("configPollution");
		borne = new JButton("configBorne");
		indicateur = new JButton("analyse");
		detecteur = new JButton("configdetecteur");
		accueil = new JButton("Accueil");
		pollution.setPreferredSize(new Dimension(75,75));
		this.setLayout(new GridLayout(6,1));
		this.add(borne);
		this.add(detecteur);
		this.add(empreinte);
		this.add(indicateur);
		this.add(pollution);
		this.add(accueil);
		pollution.setBackground(Couleur.getBgThem());
		pollution.setForeground(Couleur.getBgApp());
		pollution.setFont(new Font("Arial", Font.BOLD, 14) );
		pollution.setBorder(new LineBorder(Couleur.getBgTitle()));
		accueil.setBackground(Couleur.getBgThem());
		accueil.setForeground(Couleur.getBgApp());
		accueil.setFont(new Font("Arial", Font.BOLD, 14) );
		accueil.setBorder(new LineBorder(Couleur.getBgTitle()));
		detecteur.setBackground(Couleur.getBgThem());
		detecteur.setForeground(Couleur.getBgApp());
		detecteur.setFont(new Font("Arial", Font.BOLD, 14) );
		detecteur.setBorder(new LineBorder(Couleur.getBgTitle()));
		indicateur.setBackground(Couleur.getBgThem());
		indicateur.setForeground(Couleur.getBgApp());
		indicateur.setFont(new Font("Arial", Font.BOLD, 14) );
		indicateur.setBorder(new LineBorder(Couleur.getBgTitle()));
		borne.setBackground(Couleur.getBgThem());
		borne.setForeground(Couleur.getBgApp());
		borne.setFont(new Font("Arial", Font.BOLD, 14) );
		borne.setBorder(new LineBorder(Couleur.getBgTitle()));
		empreinte.setBackground(Couleur.getBgThem());
		empreinte.setForeground(Couleur.getBgApp());
		empreinte.setFont(new Font("Arial", Font.BOLD, 14) );
		empreinte.setBorder(new LineBorder(Couleur.getBgTitle()));
		
		this.setPreferredSize(new Dimension(150,150));
	}



	public JMenuBar getMbar() {
		return mbar;
	}



	public JButton getEmpreinte() {
		return empreinte;
	}



	public JButton getPollution() {
		return pollution;
	}



	public JButton getBorne() {
		return borne;
	}



	public JButton getIndicateur() {
		return indicateur;
	}



	public JButton getDetecteur() {
		return detecteur;
	}



	public JButton getAccueil() {
		return accueil;
	}
	
	
}
