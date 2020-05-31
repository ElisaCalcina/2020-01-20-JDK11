package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenze;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> artObject= new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
					artObject.add(artObj);
				
			}
			conn.close();
			return artObject;
						
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getRuoli(){
		String sql="SELECT distinct role " + 
					"FROM authorship "+
					"ORDER BY role ASC ";
		List<String> ruoli= new ArrayList<String>();
		
		try {
			Connection conn= DBConnect.getConnection();
			PreparedStatement st= conn.prepareStatement(sql);
			ResultSet res= st.executeQuery();
			
			while(res.next()) {
				ruoli.add(res.getString("role"));
			}
			conn.close();
			return ruoli;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//NON USO IDMAP PERCHE' I VERTICI SONO DEGLI INTERI
	public List<Adiacenze> getAdiacenze(String ruolo){
		String sql="SELECT a1.artist_id AS ar1, a2.artist_id AS ar2, COUNT(DISTINCT(e1.exhibition_id)) AS peso " + 
				"FROM artists a1, artists a2, authorship au1, authorship au2, exhibition_objects e1,  exhibition_objects e2 " + 
				"WHERE au1.role= ? " + 
				"		AND au2.role=? " + 
				"		AND a1.artist_id=au1.artist_id " + 
				"		AND a2.artist_id=au2.artist_id " + 
				"		AND au1.object_id=e1.object_id " + 
				"		AND au2.object_id=e2.object_id " + 
				"		AND e1.exhibition_id=e2.exhibition_id " + 
				"		AND a1.artist_id>a2.artist_id " + 
				"GROUP BY a1.artist_id, a2.artist_id ";
		
		List<Adiacenze> adiacenze = new ArrayList<Adiacenze>();
		
		try {
			
			Connection conn= DBConnect.getConnection();
			PreparedStatement st= conn.prepareStatement(sql);
			st.setString(1, ruolo);
			st.setString(2, ruolo);
			
			ResultSet res= st.executeQuery();
			
			while(res.next()) {
				adiacenze.add(new Adiacenze(res.getInt("ar1"), res.getInt("ar2"), res.getDouble("peso")));
			}
			
			conn.close();
			return adiacenze;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//se volessi inserire tutti i vertici e non solo quelli recuperati dalle adiacenze --> metodo che ritorna tutti gli artisti con un determinato ruolo
	public List<Integer> getArtisti(String ruolo){
		String sql="SELECT a.artist_id FROM artists as a, authorship as au WHERE role= ? AND a.artist_id= au.artist_id ";
		List<Integer> artisti= new ArrayList<>();
		
		try {
			
			Connection conn= DBConnect.getConnection();
			PreparedStatement st= conn.prepareStatement(sql);
			st.setString(1, ruolo);
			
			ResultSet res= st.executeQuery();
			
			while(res.next()) {
				artisti.add(res.getInt("artist_id"));
			}
			conn.close();
			return artisti;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
