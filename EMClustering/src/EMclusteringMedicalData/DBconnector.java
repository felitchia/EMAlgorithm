package EMclusteringMedicalData;

/**
 * Class that connects to the data base and extracts the needed sample
 */
import java.sql.*;
import java.util.LinkedList;

public class DBconnector {
	/**
	 *  JDBC driver name and database URL
	 */
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static String dbUrl = "";

	/**
	 *   Database credentials
	 */
	private static final String USER = "amc";
	static final String PASS = "amc2016";

	static String tableName = "";
	private static ResultSet rs;

	/**
	 * Connects and gets the data from the data base.
	 * Each row will be a PatientData, which then is added to a LinkedList,
	 * forming the total sample
	 * 
	 * @return LinkedList<PatientData> containing the sample
	 */
	public static LinkedList<PatientData> getData(){
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(dbUrl ,USER,PASS);

			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			//sql = "SELECT * from  Teste;";
			//sql = "SELECT * from  Teste where PatientID=3;";
			//sql = "SELECT * from  Teste where PatientID<3;";

			rs = stmt.executeQuery("SELECT * from  " + tableName + ";");

			int dimension = DBconnector.dimension();
			//rs.beforeFirst();
			LinkedList<PatientData> sample = new LinkedList<PatientData>();
			while(rs.next()){
				PatientData patient = new PatientData();
				for(int i= 1; i <= dimension; i++){
					patient.getData().add(rs.getDouble(i));
				}

				sample.add(patient);
			}
			
			//If the dimensions is variable we need to change this!!! - it works for the current DB

			//STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
			
			return sample;
			
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		return null;
	}

	public static int dimension(){
		try {
			ResultSetMetaData metadata = rs.getMetaData();
			return metadata.getColumnCount();
		} catch (SQLException e) {e.printStackTrace();
		}
		return (Integer) null;
	}
	
}