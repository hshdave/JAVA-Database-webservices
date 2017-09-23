/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import static project2.GenericResource.JDBC_DRIVER;

/**
 * REST Web Service
 *
 * @author Harsh
 */

@Path("hr")
public class GenericResource {
    
// JDBC driver name and database URL

static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
static final String DB_URL= "jdbc:oracle:thin:@144.217.163.57:1521:XE";

  String sql = "";

//Database credentials

static final String USER = "HR";
static final String PASS = "cegepgim";

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     * Retrieves representation of an instance of project2.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    
     public Connection getCon()
    {
         Connection conn = null;
        try
        {
            //Register JDBC Driver
            Class.forName(JDBC_DRIVER);
            
            //Open Connection
           
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
      return conn;
    }
    
    @GET
    @Path("employeeList")
    @Produces("application/json")
    public String employeeList() {
        
            Statement stmt = null;
        
     
          JSONObject jb = new JSONObject();
          JSONArray ja =  new JSONArray();
        
          Connection conn =  getCon();
            
             try
            {
             sql = "select e.first_name,e.last_name,e.salary,d.department_name,j.job_title from employees e LEFT JOIN departments d on e.DEPARTMENT_ID=d.DEPARTMENT_ID LEFT JOIN jobs j on e.JOB_ID=j.JOB_ID";
             
              stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
          while(rs.next())
            {
                String fname = rs.getString(1);
                String lname = rs.getString(2);
                double salary = rs.getDouble(3);
                String depName = rs.getString(4);
                String jobtit = rs.getString(5);
                
                jb.accumulate("fname", fname);
                jb.accumulate("lname",  lname);
                jb.accumulate("Salary",  salary);
                jb.accumulate("deptName",  depName);
                jb.accumulate("jobTitlle",  jobtit);
                
                ja.add(jb);
                jb.clear();
            }
            
            rs.close();
            stmt.close();
            conn.close();
          
        }catch(SQLException e)
        {
            e.printStackTrace();
        }   
        return  ja.toString();
    }
    

        
    @GET
    @Path("singleEmployee&{id}")
    @Produces("application/json")
    public String singleEmployee(@PathParam("id") int eid) {
        
        Connection conn = getCon();
        PreparedStatement stmt = null;
          JSONObject jsn = new JSONObject();
       
        
        try
        {
             String sql;
             sql = "select e.employee_id, e.first_name,e.last_name,e.salary,d.department_name,j.job_title from employees e,departments d,jobs j where e.DEPARTMENT_ID=d.DEPARTMENT_ID and j.JOB_ID=e.JOB_ID and e.EMPLOYEE_ID=?";
            
             stmt =  conn.prepareStatement(sql);
             stmt.setInt(1, eid);
               ResultSet rs = stmt.executeQuery();
               
               while (rs.next()) {
                
                  int id =  rs.getInt(1);
                 String fname = rs.getString(2);
                 String lname = rs.getString(3);
              double salary = rs.getDouble(4);
                String depName = rs.getString(5);
                String jobtit = rs.getString(6);
                     
                jsn.accumulate("id", id);
                jsn.accumulate("fname", fname);
                jsn.accumulate("lname",  lname);
                jsn.accumulate("Salary",  salary);
                jsn.accumulate("deptName",  depName);
                jsn.accumulate("jobTitlle",  jobtit);
            }
               
             rs.close();
            stmt.close();
            conn.close();
            
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
          
        return jsn.toString();
    }
    
    
    
     
    @GET
    @Path("departmentList")
    @Produces("application/json")
    public String departmentList() {
        
            Statement stmt = null;
        
     
          JSONObject jb = new JSONObject();
          JSONArray ja =  new JSONArray();
        
          Connection conn =  getCon();
            
             try
            {
             sql = "select d.department_id,d.department_name,l.city,c.country_name,r.region_name,e.first_name,e.last_name from locations l,countries c,regions r,departments d LEFT JOIN employees e on d.MANAGER_ID = e.EMPLOYEE_ID where d.LOCATION_ID=l.LOCATION_ID and l.COUNTRY_ID=c.COUNTRY_ID and c.REGION_ID=r.REGION_ID";
             
              stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
          while(rs.next())
            {
                int did = rs.getInt(1);
                String dname = rs.getString(2);
                String city = rs.getString(3);
                String coun = rs.getString(4);
                String region = rs.getString(5);
                String fname = rs.getString(6);
                String lname = rs.getString(7);
  
            
                jb.accumulate("id", did);
                jb.accumulate("deptName",  dname);
                jb.accumulate("City",  city);
                jb.accumulate("country",  coun);
                jb.accumulate("Region",  region);
                jb.accumulate("fname",  fname);
                jb.accumulate("lname",  lname);
                
                ja.add(jb);
                jb.clear();
            }
            
            rs.close();
            stmt.close();
            conn.close();
          
        }catch(SQLException e)
        {
            e.printStackTrace();
        }    
        return  ja.toString();
    }
    
    
    
     @GET
   @Path("singleDepartment&{id}")
    @Produces("application/json")
    public String singleDepartment(@PathParam("id") int id) {
           
          PreparedStatement stmt = null;
           Connection conn =  getCon();
     
          JSONObject jb = new JSONObject();
      
          try {
            
         sql = "select d.DEPARTMENT_ID,d.DEPARTMENT_NAME, l.CITY , c.COUNTRY_NAME, r.REGION_NAME,e.FIRST_NAME,e.LAST_NAME from DEPARTMENTS d left join LOCATIONS l on d.LOCATION_ID = l.LOCATION_ID left join COUNTRIES c on l.COUNTRY_ID = c.COUNTRY_ID left join REGIONS r on c.REGION_ID = r.REGION_ID left join EMPLOYEES e on e.EMPLOYEE_ID = d.MANAGER_ID where d.DEPARTMENT_ID=?";

         stmt = conn.prepareStatement(sql);
           stmt.setInt(1, id);
            
           ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                
                int deprt_id = rs.getInt(1);
                String fname=rs.getString(2);
                String lname=rs.getString(3);
                String DEPARTMENT_NAME=rs.getString(4);
                String REGION_NAME = rs.getString(5);
                String CITY = rs.getString(6);
                String COUNTRY_NAME = rs.getString(7);
                
                 jb.accumulate("id", deprt_id);
                 jb.accumulate("deptname", DEPARTMENT_NAME);
                 jb.accumulate("City", CITY);
                 jb.accumulate("country", COUNTRY_NAME);              
                 jb.accumulate("Region", REGION_NAME);
                 jb.accumulate("fname", fname);
                 jb.accumulate("lname", lname);
              
            }
        
              rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
             String result = "test";
        } catch (Exception e){
            e.printStackTrace();
             String result = "test";
        } 
          
            return jb.toString();
        
       
    }
    
    
    
    
     @GET
   @Path("depNumberList")
    @Produces("application/json")
    public String depNumberList() {
           
           Statement stmt = null;
           Connection conn =  getCon();
     
          JSONObject jb = new JSONObject();
          JSONArray ja = new JSONArray();
      
          try {
       
           
            String sql;
           sql = "select count(e.EMPLOYEE_ID) as total,d.DEPARTMENT_ID,d.DEPARTMENT_NAME from DEPARTMENTS d left join EMPLOYEES e on d.DEPARTMENT_ID=e.DEPARTMENT_ID  group by d.DEPARTMENT_ID,d.DEPARTMENT_NAME";

            stmt=conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
          
            while (rs.next()) {
                int deprt_id = rs.getInt("DEPARTMENT_ID");
                String DEPARTMENT_NAME=rs.getString("DEPARTMENT_NAME");
                int total_employee = rs.getInt("total");
              
                 jb.accumulate("id", deprt_id);
                 jb.accumulate("deptname", DEPARTMENT_NAME);
                 jb.accumulate("numberOfEmployee", total_employee);
                 
                 ja.add(jb);
                   jb.clear();
            }
        
          rs.close();
            stmt.close();
            conn.close();
       
            
        } catch (SQLException se) {
            se.printStackTrace();
             String result = "test";
        } catch (Exception e){
            e.printStackTrace();
             String result = "test";
        } 
     
            return ja.toString();
        
       
    }
  
    
    
    
    
   @GET
   @Path("singleDepNumber&{id}")
    @Produces("application/json")
    public String singleDepNumber(@PathParam("id") int id) {
           
       
         PreparedStatement stmt = null;
         Connection conn =  getCon();
       
          JSONObject jb = new JSONObject();
      
          
          
          try {
         sql = "select count(e.EMPLOYEE_ID) as total,d.DEPARTMENT_ID,d.DEPARTMENT_NAME from DEPARTMENTS d left join EMPLOYEES e on d.DEPARTMENT_ID=e.DEPARTMENT_ID where d.DEPARTMENT_ID=? group by d.DEPARTMENT_ID,d.DEPARTMENT_NAME";

                 stmt = conn.prepareStatement(sql);
                 stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
         
            while (rs.next()) {
                int deprt_id = rs.getInt("DEPARTMENT_ID");
                String DEPARTMENT_NAME=rs.getString("DEPARTMENT_NAME");
                int total_employee = rs.getInt("total");
              
               jb.accumulate("id", deprt_id);
                 jb.accumulate("deptname", DEPARTMENT_NAME);
               jb.accumulate("numberOfEmployee", total_employee);
                 
             rs.close();
             rs.close();
            stmt.close();
            conn.close();
            }
        
        } catch (SQLException se) {
            se.printStackTrace();
             String result = "test";
        } catch (Exception e){
            e.printStackTrace();
             String result = "test";
        } 
          
          
            
     
            return jb.toString();
        
       
    }
   
    
    
     @GET
    @Path("singleRegionNumber&{id}")
    @Produces("application/json")
    public String singleRegion(@PathParam("id") int eid) {
        
        Connection conn = getCon();
        PreparedStatement stmt = null;
          JSONObject jb = new JSONObject();
       
        
        try
        {
             String sql;
             sql = "select r.region_id,r.region_name, count(d.department_id) from departments d,locations l,countries c,regions r where d.LOCATION_ID=l.LOCATION_ID and l.COUNTRY_ID=c.COUNTRY_ID and c.REGION_ID=r.REGION_ID and r.REGION_ID=? group by(r.REGION_ID,r.REGION_NAME)";
            
             stmt =  conn.prepareStatement(sql);
             stmt.setInt(1, eid);
               ResultSet rs = stmt.executeQuery();
               
               while (rs.next()) {
                
                 int rid = rs.getInt(1);
                String rname = rs.getString(2);
                int dnum = rs.getInt(3);
              
 
                jb.accumulate("id", rid);
               jb.accumulate("regionName", rname);
               jb.accumulate("deptNumber", dnum);
                
            }
               
                rs.close();
            stmt.close();
            conn.close();
            
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        
        return jb.toString();
    }
    
    @GET
    @Path("regionNumberList")
    @Produces("application/json")
    public String regionNumberList() {
        
            Statement stmt = null;
        
     
          JSONObject jb = new JSONObject();
          JSONArray ja =  new JSONArray();
        
          Connection conn =  getCon();
            
             try
            {
             sql = "select r.region_id,r.region_name, count(d.department_id) from departments d,locations l,countries c,regions r where d.LOCATION_ID=l.LOCATION_ID and l.COUNTRY_ID=c.COUNTRY_ID and c.REGION_ID=r.REGION_ID group by(r.REGION_ID,r.REGION_NAME)";
             
              stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
          while(rs.next())
            {
                int rid = rs.getInt(1);
                String rname = rs.getString(2);
                int dnum = rs.getInt(3);
              
 
                jb.accumulate("id", rid);
               jb.accumulate("regionName", rname);
               jb.accumulate("deptNumber", dnum);
                
                ja.add(jb);
                jb.clear();
            }
            
            rs.close();
            stmt.close();
            conn.close();
          
          
        }catch(SQLException e)
        {
            e.printStackTrace();
        }    
        return  ja.toString();
    }
    
   
}


