import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.users.web.Koneksi;

/**
 *
 * @author Hakushuu
 */
@ManagedBean
@RequestScoped
public class Mahasiswa {

    /**
     * Creates a new instance of Mahasiswa
     */
    
    private String NIM;
    public void setNIM(String NIM) {
        this.NIM = NIM;
    }
    public String getNIM() {
        return NIM;
    }

    private String NAMA;
    public void setNAMA(String NAMA) {
        this.NAMA = NAMA;
    }
    public String getNAMA() {
        return NAMA;
    }
    
    private String PENJURUSAN;
    public void setPENJURUSAN(String PENJURUSAN) {
        this.PENJURUSAN = PENJURUSAN;
    }
    public String getPENJURUSAN() {
        return PENJURUSAN;
    }
   
    private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap(); 

    public String Edit_Mahasiswa(){
     FacesContext fc = FacesContext.getCurrentInstance();
     Map<String,String > params = fc.getExternalContext().getRequestParameterMap();
     String Field_NIM = params.get("action");
     try {
          Koneksi obj_koneksi = new Koneksi();
          Connection connection = obj_koneksi.get_connection();
         
          Statement st = connection.createStatement();
          ResultSet rs = st.executeQuery("select * from mahasiswa where NIM="+Field_NIM);
          Mahasiswa obj_Mahasiswa = new Mahasiswa();
          rs.next();
          obj_Mahasiswa.setNIM(rs.getString("NIM"));
          obj_Mahasiswa.setNAMA(rs.getString("Nama"));
          obj_Mahasiswa.setPENJURUSAN(rs.getString("Penjurusan"));
          sessionMap.put("EditMahasiswa", obj_Mahasiswa);  

      } catch (Exception e) {
            System.out.println(e);
      }
     return "/Edit.xhtml?faces-redirect=true";   
   }

  public String Delete_Mahasiswa(){
      FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
      String Field_NIM = params.get("action");
      try {
         Koneksi obj_koneksi = new Koneksi();
         Connection connection = obj_koneksi.get_connection();
         PreparedStatement ps = connection.prepareStatement("delete from mahasiswa where NIM=?");
         ps.setString(1, Field_NIM);
         System.out.println(ps);
         ps.executeUpdate();
        } catch (Exception e) {
         System.out.println(e);
        }
       return "/index.xhtml?faces-redirect=true";   
  }

  public String Update_Mahasiswa(){
      FacesContext fc = FacesContext.getCurrentInstance();
      Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
      String Update_NIM= params.get("Update_NIM");
      
      try {
          Koneksi obj_koneksi = new Koneksi();
          Connection connection = obj_koneksi.get_connection();
          
          PreparedStatement sc=connection.prepareStatement("select nama from penjurusan where nama='" + PENJURUSAN + "'");

          ResultSet df = sc.executeQuery();

          if (df.next()){
            PreparedStatement ps = connection.prepareStatement("update mahasiswa set NIM=?, Nama=?, Penjurusan=? where NIM=?");
            ps.setString(1, NIM);
            ps.setString(2, NAMA);
            ps.setString(3, PENJURUSAN);
            ps.setString(4, Update_NIM);
            System.out.println(ps);
            ps.executeUpdate();
            System.out.println("INFO: Data has been successfully modified for " + NAMA + ".");
            return "/Success.xhtml?faces-redirect=true";

          }else {
                System.out.println("ERROR: Invalid data input at 'penjurusan'.");
                return "/Error.xhtml?faces-redirect=true";
          }
          
      } catch (Exception e) {
          System.out.println(e);
      }

      return "/index.xhtml?faces-redirect=true";   
  }
    
    public ArrayList getGet_all_mahasiswa() throws Exception{
        ArrayList list_of_mahasiswa=new ArrayList();
             Connection connection=null;
        try {
            Koneksi obj_koneksi = new Koneksi();
            connection = obj_koneksi.get_connection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("Select * from mahasiswa order by nim asc");
            while(rs.next()){
                Mahasiswa obj_Mahasiswa = new Mahasiswa();
                obj_Mahasiswa.setNIM(rs.getString("NIM"));
                obj_Mahasiswa.setNAMA(rs.getString("Nama"));
                obj_Mahasiswa.setPENJURUSAN(rs.getString("Penjurusan"));
                list_of_mahasiswa.add(obj_Mahasiswa);
            }
        } catch (Exception e) {
            System.out.println(e);
        }finally{
            if(connection!=null){
                connection.close();
            }
        }
        return list_of_mahasiswa;
    }
    
  public String Tambah_Mahasiswa(){
      try {
          Connection connection=null;
          Koneksi obj_koneksi = new Koneksi();
          connection = obj_koneksi.get_connection();
          PreparedStatement sc=connection.prepareStatement("select nama from penjurusan where nama='" + PENJURUSAN + "'");

          ResultSet df = sc.executeQuery();

          if (df.next()){
            PreparedStatement ps=connection.prepareStatement("insert into mahasiswa(NIM, Nama, Penjurusan) value('"+NIM+"','"+NAMA+"','"+PENJURUSAN+"')");
            ps.executeUpdate();
            System.out.println("INFO: Data has been successfully added for " + NAMA + ".");
            return "/Success.xhtml?faces-redirect=true";

          }else {
                System.out.println("ERROR: Invalid data input at 'penjurusan'.");
                return "/Error.xhtml?faces-redirect=true";

          }
          
      } catch (Exception e) {
          System.out.println(e);
      }
        return "/index.xhtml?faces-redirect=true";
  }
    
  public Mahasiswa() {}
    
}