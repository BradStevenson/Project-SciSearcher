import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class MatrixGraph {
    private static Connection conn;
    public boolean[][] graph;
    
    public MatrixGraph() {
	
	startConnection();
	for (int paper : getAllPaperIDs()) {
	    for(int citedPaper : getPapersCitedByPaperID(paper)) {
		addEdge(paper, citedPaper);
	    }
	}
	
	endConnection();
    }
    
    private static void startConnection() {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "SciSearcher";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "t4k34ch4nc3";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url+dbName,userName,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void endConnection() {
        try {
	    conn.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }
    
    void addEdge(int i, int j) {
        graph[i][j] = true;
    }
    
    private static ArrayList<Integer> getPapersCitedByPaperID(int paperID) {
	ArrayList<Integer> paperIDs = new ArrayList<Integer>();
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery("SELECT Citations.paperID  FROM Papers INNER JOIN Citations ON Papers.paperID=Citations.citedPaperID WHERE Papers.paperID="+paperID);
            while (res.next()) {
        	int citingPaperID = res.getInt("paperID");
        	paperIDs.add(citingPaperID);
            }
            return paperIDs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static ArrayList<Integer> getAllPaperIDs() {
	ArrayList<Integer> papers = new ArrayList<Integer>();
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery("SELECT paperID FROM Papers");
            while (res.next()) {
        	int citingPaperID = res.getInt("paperID");
        	papers.add(citingPaperID);
            }
            return papers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
