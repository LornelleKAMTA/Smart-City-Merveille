package socketServeur;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.connectionPool.DataSource;
import controller.DBConnectController;
import entities.AlertePolluant;
import entities.CapteurPolluant;
import entities.HistoriqueCapteurPolluant;

import java.io.*; 
import java.net.*;


public class ThreadServer extends Thread {
	private Socket clientSocket; 
	public PrintWriter outJson;
	private BufferedReader inJson;
	private Connection c; 
	private static final int NB_FAUSSE_ALERTE=2;


	public ThreadServer(Socket socket, Connection connection) {
		this.clientSocket = socket;
		this.c = connection; 

	}

	public void run()  {

		try {
			outJson = new PrintWriter(clientSocket.getOutputStream(), true);
			inJson = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			do {
			String resp = inJson.readLine();
			System.out.println("----bonjour je viens de r�cuperer le JSON");
			System.out.println(resp);
			Object obj=JSONValue.parse(resp); 
			System.out.println("----bonjour je parse le JSON");
			System.out.println(resp);
			JSONObject jsonObject = (JSONObject) obj;  
			System.out.println("----bonjour je viens de parser le JSON");
			System.out.println(resp);

			obj = crud(jsonObject); 
			// Once the Json had been processed, closing the socket and releasing the connection

			outJson.println(obj);
			
				/*
				 * DataSource.releaseConnection(c); inJson.close(); outJson.close();
				 * clientSocket.close();
				 */
		}while(!clientSocket.isClosed());
		
		}catch (Exception e) {
			System.out.println("--------Un client s'est d�connect� de mani�re pr�cipit�e !-------");
			//e.printStackTrace();
		}
		
		
		
		DBConnectController.clientsState(false);
	}


	// crud method allowing to according to customer's choice (select / insert/ update / delete) to do the request
	private Object crud(JSONObject JsonRecu) throws SQLException, InterruptedException {

		if(JsonRecu.get("demandType").equals("SELECT")) {
			long idcaste = Long.valueOf(JsonRecu.get("Id").toString());
			int idJson = (int) idcaste;
			System.out.println("bonjour voici le ID recu apres traitement");
			System.out.println(idJson);
			if(idJson == 0) {

				PreparedStatement stmt1 = c.prepareStatement("select * from utilisateur;");
				ResultSet rs2 = stmt1.executeQuery();

				JSONObject obj=new JSONObject();
				// creation of users list 
				ArrayList<JSONObject> listUsers = new ArrayList<JSONObject>();

				while (rs2.next()) {
					JSONObject user=new JSONObject();
					// recovery of each user's data (id/ name/ first name) 
					user.put("Id", rs2.getInt("id_user"));
					user.put("nom", rs2.getString("nom"));
					user.put("prenom", rs2.getString("prenom"));

					// adding each user to the list already created
					listUsers.add(user);


				}
				//System.out.println("voici l'arrayList : ");
				// displaying the list 
				//System.out.println(listUsers);

				obj.put("users", listUsers);
				System.out.println("voici le json envoy� avec le select All: ");
				// displaying the Json
				System.out.println(obj);
				Thread.sleep(3000); 
				return obj; 
			}
			else {
				PreparedStatement stmtJson = c.prepareStatement("select * from utilisateur where id_user = ?");
				stmtJson.setInt(1, idJson);
				ResultSet jsonResponse = stmtJson.executeQuery();
				JSONObject obj=new JSONObject(); 
				int cpt = 0;

				while (jsonResponse.next()) {
					//recovery of the data of the user in question 
					cpt++;
					obj.put("Id",jsonResponse.getInt("id_user"));
					obj.put("nom",jsonResponse.getString("nom"));
					obj.put("prenom",jsonResponse.getString("prenom"));
				}
				if(cpt == 0) {
					obj.put("reponse", "verifier l'id insere");
				}
				// displaying the json 
				System.out.println(obj);
				Thread.sleep(3000); 
				return obj; 
			}
		}

		if(JsonRecu.get("demandType").equals("INSERT")) {
			System.out.println("Je suis rentr� dans la requ�te INSERT"); 
			// recovery of data that the client had completed (name / first name
			String nomInsert =(String) JsonRecu.get("nom");
			String prenomInsert =(String) JsonRecu.get("prenom");
			System.out.println("bonjour voici les donnees recu apres traitement");
			System.out.println(nomInsert +  " " + prenomInsert);

			PreparedStatement stmt3 = c.prepareStatement("insert into utilisateur(nom,prenom) values (?,?);");
			// the request takes name and first name already retrieved 
			stmt3.setString(1, nomInsert);
			stmt3.setString(2,prenomInsert);
			// query execution 
			

			JSONObject obj=new JSONObject(); 

			// if (insertion bien pass�) => executer les lignes suivantes sinon dire erreur
			if(stmt3.executeUpdate()>=1) {
				obj.put("reponse",String.valueOf("insertion reussi"));
				obj.put("nom",String.valueOf(nomInsert));
				obj.put("prenom",String.valueOf(prenomInsert));
			}
			else {
				obj.put("reponse",String.valueOf("erreur lors de l'insertion"));
			}
			System.out.println(obj);
			return obj; 
		}
		
		
		
		if(JsonRecu.get("demandType").equals("INSERT_CAPTEUR_POLLUANT")) {
			System.out.println("Je suis rentr� dans la requ�te INSERT"); 
			// recovery of data that the client had completed (name / first name
			String insertAdresse_Ip =(String) JsonRecu.get("Adresse_Ip");
			String insertlocalisation =(String) JsonRecu.get("localisation");
			String iseuil_co2 =(String) JsonRecu.get("Seuil_CO2");
			String iseuil_no2 =(String) JsonRecu.get("Seuil_NO2");
			String iseuil_pm =(String) JsonRecu.get("Seuil_PM");
			String iseuil_min_tmp =(String) JsonRecu.get("Seuil_Min_Tmp");
			String iseuil_max_tmp =(String) JsonRecu.get("Seuil_Max_Tmp");
			System.out.println("bonjour voici les donnees recu apres traitement");
		System.out.println(insertAdresse_Ip +  " " + insertlocalisation + " " + iseuil_co2 + " " 
								+ iseuil_no2 + " " + iseuil_pm + " " + iseuil_min_tmp + " " + iseuil_max_tmp);
			String adresse_ip = String.valueOf(insertAdresse_Ip);
			String localisation = String.valueOf(insertlocalisation);
			String seuil_co2 = String.valueOf(iseuil_co2);
			String seuil_no2 = String.valueOf(iseuil_no2);
			String seuil_pf = String.valueOf(iseuil_pm);
			String seuil_min_tmp = String.valueOf(iseuil_min_tmp);
			String seuil_max_tmp = String.valueOf(iseuil_max_tmp);
			PreparedStatement stmt = c.prepareStatement("insert into capteur_polluant (adresse_ip,localisation,seuil_co2,seuil_no2,seuil_pf,seuil_min_tmp, seuil_max_tmp) values (?,?,?,?,?,?,?);");
			// the request takes name and first name already retrieved 
			stmt.setString(1,adresse_ip);
			stmt.setString(2,localisation);
			stmt.setString(3,seuil_co2);
			stmt.setString(4,seuil_no2);
			stmt.setString(5,seuil_pf);
			stmt.setString(6,seuil_min_tmp);
			stmt.setString(7,seuil_max_tmp);
			// query execution 
			stmt.execute();

			JSONObject obj=new JSONObject(); 

			// if (insertion bien pass�) => executer les lignes suivantes sinon dire erreur
			obj.put("reponse",String.valueOf("insertion r�ussi"));
			obj.put("Adresse_ip",adresse_ip);
			obj.put("localisation",localisation);
			obj.put("Seuil_CO2",seuil_co2);
			obj.put("Seuil_NO2",seuil_no2);
			obj.put("Seuil_PM",seuil_pf);
			obj.put("Seuil_Min_Tmp",seuil_min_tmp);
			obj.put("Seuil_Max_Tmp",seuil_max_tmp);
			System.out.println(obj);
			return obj; 
			
			
		}
		if(JsonRecu.get("demandType").equals("SELECT_ALL_CAPTEUR_POLLUANT")) {

			PreparedStatement stmt1 = c.prepareStatement("select * from capteur_polluant;");
			ResultSet rs2 = stmt1.executeQuery();

			JSONObject obj=new JSONObject();
			// creation of capteurpolluant list 
			ArrayList<JSONObject> listCapteurs = new ArrayList<JSONObject>();

			while (rs2.next()) {
				JSONObject capteurPolluant =new JSONObject();

				capteurPolluant.put("id", rs2.getInt("id"));
				capteurPolluant.put("adresse_ip", rs2.getString("adresse_ip"));
				capteurPolluant.put("localisation", rs2.getString("localisation"));
				capteurPolluant.put("seuil_co2", rs2.getString("seuil_co2"));
				capteurPolluant.put("seuil_no2", rs2.getString("seuil_no2"));
				capteurPolluant.put("seuil_pf", rs2.getString("seuil_pf"));
				capteurPolluant.put("seuil_min_tmp", rs2.getString("seuil_min_tmp"));
				capteurPolluant.put("seuil_max_tmp", rs2.getString("seuil_max_tmp"));
				// adding each capteur to the list already created
				listCapteurs.add(capteurPolluant);


			}

			obj.put("listCapteurs", listCapteurs);
			System.out.println("voici le json envoy� avec le select All CapteurPolluant : ");
			// displaying the Json
			System.out.println(obj);

			return obj; 
		}
		if(JsonRecu.get("demandType").equals("SEND_MESSAGE_CAPTEUR_POLLUANT")) {

			//Timestamp start_date =(String) JsonRecu.get("start_date");
			String val_co2 =(String) JsonRecu.get("val_co2");
			String val_no2 =(String) JsonRecu.get("val_no2");
			String val_pf =(String) JsonRecu.get("val_pf");
			String val_tmp =(String) JsonRecu.get("val_tmp");
			String fk_id_capteur =(String) JsonRecu.get("fk_id_capteur");

			System.out.println("bonjour voici les donnees recu apres traitement");
			System.out.println(JsonRecu);


			PreparedStatement stmt = c.prepareStatement("insert into historique_capteurpol (start_date,val_co2 ,val_no2,val_pf,val_tmp,fk_id_capteur) values (?,?,?,?,?,?);");
			// the request takes name and first name already retrieved 
			stmt.setTimestamp(1,new Timestamp(new Date().getTime()));
			stmt.setString(2,val_co2);
			stmt.setString(3,val_no2);
			stmt.setString(4,val_pf);
			stmt.setString(5,val_tmp);
			stmt.setInt(6,Integer.parseInt(fk_id_capteur));
			// query execution 
			stmt.execute();

			JSONObject obj=new JSONObject(); 

			// if (insertion bien pass�) => executer les lignes suivantes sinon dire erreur
			obj.put("reponse",String.valueOf("Insertion Historique Capteur Polluant OK"));

			//creation historique polluant
			HistoriqueCapteurPolluant h = new HistoriqueCapteurPolluant();
			h.setFk_id_capteur(Long.parseLong(fk_id_capteur));
			h.setVal_co2(val_co2);
			h.setVal_no2(val_no2);
			h.setVal_pf(val_pf);
			h.setVal_tmp(val_tmp);
			
			//Detection de l'alerte
			this.detection_alerte_pollution(h);

			System.out.println(obj);
			return obj; 
		}
		
		
		
		 
		
		if(JsonRecu.get("demandType").equals("UPDATE")) {
			System.out.println("Je suis rentr� dans la requete UPDATE");
			String nomUpdate =(String) JsonRecu.get("nom");
			String prenomUpdate =(String) JsonRecu.get("prenom");
			System.out.println("J'accede a la donn�e id ");
			long idcaste = Long.valueOf(JsonRecu.get("Id").toString());
			int idJson = (int) idcaste;
			System.out.println("acces r�ussi");
			System.out.println("bonjour voici les donnees recu apres traitement");
			System.out.println(nomUpdate + prenomUpdate + idJson);
			PreparedStatement stmt4 = c.prepareStatement("update utilisateur set nom= ?, prenom = ? where id_user = ?;  ");
			stmt4.setString(1, nomUpdate);
			stmt4.setString(2,prenomUpdate);
			System.out.println("je met le idJson dans la requete "); 
			stmt4.setInt(3, idJson);
			System.out.println("l'id est bien mis"); 
			JSONObject obj=new JSONObject(); 


			//if (update  bien pass�) => executer les lignes suivantes sinon dire erreur

			if(stmt4.executeUpdate()>=1) {
				obj.put("reponse",String.valueOf("mise � jour reussie"));
				obj.put("nom",String.valueOf(nomUpdate));
				obj.put("prenom",String.valueOf(prenomUpdate));
				obj.put("Id", Integer.valueOf(idJson)); 
			}
			else {
				obj.put("reponse",String.valueOf("erreur lors de la mise a jour verifier l'id"));
			}
			System.out.println(obj);
			return obj; 
		}

		if(JsonRecu.get("demandType").equals("DELETE")) {

			long idcaste = Long.valueOf(JsonRecu.get("Id").toString());
			int idJson = (int) idcaste;
			System.out.println("bonjour voici l'ID � supprimer");
			System.out.println(idJson);
			PreparedStatement stmt5 = c.prepareStatement("delete from utilisateur where id_user = ?");
			stmt5.setInt(1, idJson);
			

			JSONObject obj=new JSONObject(); 
			if(stmt5.executeUpdate()>=1) {
				obj.put("reponse",String.valueOf("suppression r�ussie"));
				obj.put("Id", Integer.valueOf(idJson));
				System.out.println(obj);
			}
			else {
				obj.put("reponse",String.valueOf("echec lors de la suppression verifier l'Id insere"));
				System.out.println(obj);		
			}
			return obj;
		}

		// Case where no if is checked 
		return new JSONObject(); 
	}
	private void detection_alerte_pollution(HistoriqueCapteurPolluant h) throws SQLException {
		// r�cupration du capteur
		CapteurPolluant cp =  this.selectCapteurPolluant(h);

		//Detection d'alertes
		AlertePolluant a = new AlertePolluant();
		a.setDate_alerte(h.getStart_date());
		a.setFk_id_capteur(Integer.parseInt(""+h.getFk_id_capteur()));

		//R�cup�ration des  dernieres historiques
		ArrayList<HistoriqueCapteurPolluant> hists = this.getLastHistoriques(cp, this.NB_FAUSSE_ALERTE);
		
		//V�rifier si les  derniers historiques d�passent le seuil	
		int cptCo2 = 0, cptNo2 = 0, cptPf = 0, cptTmpMax = 0, cptTmpMin=0;
		for (HistoriqueCapteurPolluant h2 : hists) {
			if(Integer.parseInt(h2.getVal_co2()) >= Integer.parseInt(cp.getSeuil_co2())) {
				cptCo2++;
			}
			if(Integer.parseInt(h2.getVal_no2()) >= Integer.parseInt(cp.getSeuil_no2())) {
				cptNo2++;
			}
			if(Integer.parseInt(h2.getVal_pf()) >= Integer.parseInt(cp.getSeuil_pf())) {
				cptPf++;
			}
			if(Integer.parseInt(h2.getVal_tmp()) >= Integer.parseInt(cp.getSeuil_max_tmp())) {
				cptTmpMax++;
			}
			if(Integer.parseInt(h2.getVal_tmp()) >= Integer.parseInt(cp.getSeuil_min_tmp())) {
				cptTmpMin++;
			}
		}
		
		//Est ce que la valeur re�u dans h d�passe le seuil
		if(Integer.parseInt(h.getVal_co2()) >= Integer.parseInt(cp.getSeuil_co2())) {
			//V�rifier si les derniers historiques d�passent le seuil	
		
			if (cptCo2== this.NB_FAUSSE_ALERTE) {
				a.setDescription("Alerte CO2: seuil d�pass� : "+(h.getVal_co2()));
				//insertion alerte
				this.insertAlert(a);
			}
			
		}
		if(Integer.parseInt(h.getVal_no2()) >= Integer.parseInt(cp.getSeuil_no2())) {
			//V�rifier si les derniers historiques d�passe le seuil 
			
			if (cptNo2== this.NB_FAUSSE_ALERTE) {
				//cr�ation d'une alerte
				a.setDescription("Alerte NO2: seuil d�pass� : "+h.getVal_no2());
				//insertion alerte
				this.insertAlert(a);
			}
		}
		if(Integer.parseInt(h.getVal_pf()) >= Integer.parseInt(cp.getSeuil_pf())) {
			//V�rifier si les derniers historiques d�passe le seuil de co2	
			
			if (cptPf== this.NB_FAUSSE_ALERTE) {
				//cr�ation d'une alerte
				a.setDescription("Alerte PF: seuil d�pass� : "+h.getVal_pf());
				//insertion alerte
				this.insertAlert(a);
			}
		}
		if(Integer.parseInt(h.getVal_tmp()) >= Integer.parseInt(cp.getSeuil_max_tmp())) {

			//V�rifier si les derniers historiques d�passe le seuil 	
			
			if (cptTmpMax== this.NB_FAUSSE_ALERTE) {
				//cr�ation d'une alerte
				a.setDescription("Alerte Temperature MAX : seuil d�pass�: "+h.getVal_tmp());
				//insertion alerte
				this.insertAlert(a);
			}
		}
		if(Integer.parseInt(h.getVal_tmp()) <= Integer.parseInt(cp.getSeuil_min_tmp())) {
			
			//V�rifier si les derniers historiques d�passe le seuil 	
			
			if (cptTmpMin== this.NB_FAUSSE_ALERTE) {
				//cr�ation d'une alerte
				a.setDescription("Alerte Temperature MIN: seuil d�pass� : "+(h.getVal_no2()));
				//insertion alerte
				this.insertAlert(a);
			}
		}



	}
	private CapteurPolluant selectCapteurPolluant(HistoriqueCapteurPolluant h) throws SQLException {
		PreparedStatement stmt= c.prepareStatement("select * from capteur_polluant where id= ?");
		stmt.setLong(1, h.getFk_id_capteur());
		ResultSet rs2 = stmt.executeQuery();
		CapteurPolluant cp = new CapteurPolluant();

		while (rs2.next()) {
			cp.setId(rs2.getLong("id"));
			cp.setAdresse_ip(rs2.getString("adresse_ip"));
			cp.setLocalisation( rs2.getString("localisation"));
			cp.setSeuil_co2(rs2.getString("seuil_co2"));
			cp.setSeuil_no2(rs2.getString("seuil_no2"));
			cp.setSeuil_pf(rs2.getString("seuil_pf"));
			cp.setSeuil_min_tmp(rs2.getString("seuil_min_tmp"));
			cp.setSeuil_max_tmp(rs2.getString("seuil_max_tmp"));

		}
		return cp;
	}

	private ArrayList<HistoriqueCapteurPolluant> getLastHistoriques(CapteurPolluant cp, int limit) throws SQLException {
		ArrayList<HistoriqueCapteurPolluant> hists = new ArrayList<>();
		// r�cupration du capteur
		PreparedStatement stmt= c.prepareStatement("select * from historique_capteurpol where fk_id_capteur=? order by 2 desc limit ?;");
		stmt.setLong(1, cp.getId());
		stmt.setLong(2, limit);
		ResultSet rs2 = stmt.executeQuery();
		while (rs2.next()) {
			HistoriqueCapteurPolluant h = new HistoriqueCapteurPolluant();
			h.setId(rs2.getLong("id"));
			h.setStart_date(rs2.getTimestamp("start_date"));
			h.setVal_co2(rs2.getString("val_co2"));
			h.setVal_no2(rs2.getString("val_no2"));
			h.setVal_pf(rs2.getString("val_pf"));
			h.setVal_tmp(rs2.getString("val_tmp"));
			h.setFk_id_capteur(rs2.getLong("fk_id_capteur"));
			hists.add(h);
		}
		return hists;
	}

	private void insertAlert(AlertePolluant a) throws SQLException {
		System.err.println(a.toString());
		PreparedStatement stmtAlerte = c.prepareStatement("insert into alerte_polluant (date_alerte,description, fk_id_capteur) values (?,?,?);");
		// the request takes name and first name already retrieved 
		stmtAlerte.setTimestamp(1,new Timestamp(new Date().getTime()));
		stmtAlerte.setString(2,a.getDescription());
		stmtAlerte.setInt(3,a.getFk_id_capteur());
		// query execution 
		stmtAlerte.execute();
	}
	

	
}

