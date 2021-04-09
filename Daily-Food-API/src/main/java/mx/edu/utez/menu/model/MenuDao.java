package mx.edu.utez.menu.model;

import mx.edu.utez.platillo.model.PlatilloCompleto;
import mx.edu.utez.platillo.model.PlatilloDao;
import mx.edu.utez.platilloenmenu.model.PlatilloEnMenuDao;
import mx.edu.utez.sucursal.model.SucursalDao;
import mx.edu.utez.tipomenu.model.TipoMenuDao;
import mx.edu.utez.tools.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDao {

    public Menu getMenus(){
        Menu menu = new Menu();
        List<PlatilloCompleto> platillos = new ArrayList();
        try{
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT pem.idplatillo, pem.idMenuPlatillo, m.idMenu, md.idMenuDia FROM platilloenmenu pem \n" +
                    "INNER JOIN menu m ON pem.idMenu = m.idMenu\n" +
                    "INNER JOIN menudia md on m.idMenu = md.idMenu\n" +
                    "WHERE md.fecha = curdate()\n" +
                    "AND pem.status = 1\n" +
                    "AND md.status = 1;");
            ResultSet rs = ps.executeQuery();
            PlatilloDao platDao = new PlatilloDao();
            MenuDao menuDao = new MenuDao();
            while(rs.next()){
                System.out.println("IDMENU DIA :> " + rs.getInt(4));
                System.out.println("ID MENU :> " + rs.getInt(3));
                menu = menuDao.getMenuById(rs.getInt(3));
                PlatilloCompleto platillo = platDao.getPlatilloCompletoById(rs.getInt(1));
                platillo.setIdMenuPlatillo(rs.getInt(2));
                platillo.setIngredientes(null);
                platillo.setPreparacion(null);
                platillos.add(platillo);
            }
            menu.setPlatillos(platillos);
            rs.close();
            ps.close();
            con.close();
        }catch(Exception e){
            System.err.println("LIST "+e.getMessage());
        }
        return menu;
    }

    public Menu getDesayuno(){
        Menu menu = new Menu();
        try{
            Connection con  = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT idMenu FROM menu  WHERE idTipoMenu = 1;");
            ResultSet rs = ps.executeQuery();
            PlatilloEnMenuDao platilloEnMenuDao = new PlatilloEnMenuDao();
            MenuDao menuDao = new MenuDao();
            while(rs.next()){
                menu = menuDao.getMenuById(rs.getInt(1));
                menu.setPlatillos(platilloEnMenuDao.getPlatillosEnMenuByMenu(menu.getIdMenu()));
            }
            rs.close();
            ps.close();
            con.close();
        }catch(Exception err){
            System.out.println("ERROR EN getDesayuno");
        }
        return menu;
    }

    public List getMenuCompleto (){
        List<Menu> menuCompleto = new ArrayList<Menu>();
        MenuDao menuDao = new MenuDao();
        menuCompleto.add(menuDao.getDesayuno());
        System.out.println("desayuno: " + menuCompleto.get(0).getIdMenu());
        menuCompleto.add(menuDao.getMenus());
        System.out.println("menus: " + menuCompleto.get(1).getIdMenu());
        if(menuCompleto.size() > 0){
            System.out.println("SUCCESS");
        }else{
            System.out.println("ERROR getMenuCompleto");
        }
        return menuCompleto;
    }

    public Menu getMenuById(int idMenu){
        Menu obj = new Menu();
        try{
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM menu WHERE idMenu = ?");
            ps.setInt(1, idMenu);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                SucursalDao sucursal = new SucursalDao();
                TipoMenuDao tipoMenu = new TipoMenuDao();
                obj.setIdMenu(rs.getInt(1));
                obj.setNombreMenu(rs.getString(2));
                obj.setIdTipoMenu(tipoMenu.getTipoMenuById(rs.getInt(3)));
                obj.setIdSucursal(sucursal.getSucursalById(rs.getInt(4)));
            }
            rs.close();
            ps.close();
            con.close();
        }catch(Exception e){
            System.err.println("Error de conexión");
        }
        return obj;
    }

    public Menu createMenu(Menu menuNuevo) throws SQLException {
        boolean insert = false;
        Menu menuReturn = new Menu();
        Connection con = null;
        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("INSERT INTO menu (`nombreMenu`, `idTipoMenu`, `idSucursal`) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, menuNuevo.getNombreMenu());
            ps.setInt(2, menuNuevo.getIdTipoMenu().getIdTipoMenu());
            ps.setInt(3, menuNuevo.getIdSucursal().getIdSucursal());
            insert = (ps.executeUpdate() == 1);
            if(insert){
                try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        int idRecovery = generatedKeys.getInt(1);
                        menuReturn = menuNuevo;
                        menuReturn.setIdMenu(idRecovery);
                    }else{
                        throw new SQLException("FAIL NOT CREATED");
                    }
                }
            }
            con.commit();
        }catch(Exception e){
            con.rollback();
            System.err.println("Error "+e.getMessage());
        }finally {
            con.close();
        }
        return menuReturn;
    }

    public boolean updateMenu(Menu menu) throws  SQLException{
        boolean update = false;
        Connection con = null;
        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("UPDATE menu " +
                    "SET nombreMenu=?, idTipoMenu=?, idSucursal=?" +
                    " WHERE idMenu=?");
            ps.setString(1, menu.getNombreMenu());
            ps.setInt(2, menu.getIdTipoMenu().getIdTipoMenu());
            ps.setInt(3, menu.getIdSucursal().getIdSucursal());
            ps.setInt(4, menu.getIdMenu());
            update = (ps.executeUpdate() == 1);
            if(update){
                con.commit();
            }
            ps.close();
        }catch(Exception e){
            con.rollback();
            System.err.println("Error "+e.getMessage());
        }finally {
            con.close();
        }
        return update;
    }

    public boolean deleteMenu(int idMenu) throws SQLException{
        boolean delete = false;
        Connection con = null;
        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("DELETE FROM menu WHERE idMenu = ?");
            ps.setInt(1, idMenu);
            delete= (ps.executeUpdate() == 1);
            if(delete){
                con.commit();
            }
            ps.close();
        }catch (Exception e){
            System.err.println("Error "+e.getMessage());
            con.rollback();
        }finally {
            con.close();
        }
        return delete;
    }

}
