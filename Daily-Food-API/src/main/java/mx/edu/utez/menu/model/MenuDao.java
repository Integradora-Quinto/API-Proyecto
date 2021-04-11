package mx.edu.utez.menu.model;

import mx.edu.utez.imagenplatillo.model.ImagenPlatillo;
import mx.edu.utez.platillo.model.Platillo;
import mx.edu.utez.platillo.model.PlatilloCompleto;
import mx.edu.utez.platillo.model.PlatilloDao;
import mx.edu.utez.platilloenmenu.model.PlatilloEnMenu;
import mx.edu.utez.platilloenmenu.model.PlatilloEnMenuDao;
import mx.edu.utez.precio.model.Precio;
import mx.edu.utez.sucursal.model.SucursalDao;
import mx.edu.utez.tipoPlatillo.model.TipoPlatillo;
import mx.edu.utez.tipomenu.model.TipoMenu;
import mx.edu.utez.tipomenu.model.TipoMenuDao;
import mx.edu.utez.tools.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuDao {

    public Menu getMenuDelDia(){
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

    public Menu getMenuDeDia(){
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

    public Menu getMenuDesayuno(){
        Menu desayuno = new Menu();
        List<PlatilloCompleto> platillos = new ArrayList();
        try{
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT m.idMenu, m.nombreMenu, tm.idTipoMenu, tm.nombreTMenu, \n" +
                    "pem.idMenuPlatillo, p.nombrePlatillo, p.tiempoPreparacion, p.descripcion, \n" +
                    "ip.img, pr.precio, tp.nombreTipo FROM menu m \n" +
                    "INNER JOIN tipomenu tm ON m.idTipoMenu = tm.idTipoMenu\n" +
                    "INNER JOIN platilloenmenu pem ON m.idMenu = pem.idMenu\n" +
                    "INNER JOIN platillo p ON pem.idPlatillo = p.idPlatillo\n" +
                    "INNER JOIN imagenplatillo ip ON p.idPlatillo = ip.idPlatillo\n" +
                    "INNER JOIN precio pr ON p.idPlatillo = pr.idPlatillo\n" +
                    "INNER JOIN tipoplatillo tp ON p.idTipoPlatillo = tp.idTipoPlatillo\n" +
                    "WHERE tm.modoMenu = 0 AND pem.status = 1 ORDER BY tp.idTipoPlatillo;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                TipoMenu tipoMenu = new TipoMenu();
                PlatilloCompleto platillo = new PlatilloCompleto();
                Platillo plato = new Platillo();
                ImagenPlatillo imagenPlatillo = new ImagenPlatillo();
                Precio precio = new Precio();
                TipoPlatillo tipoPlatillo = new TipoPlatillo();
                desayuno.setIdMenu(rs.getInt(1));
                desayuno.setNombreMenu(rs.getString(2));
                tipoMenu.setIdTipoMenu(rs.getInt(3));
                tipoMenu.setNombreTipoMenu(rs.getString(4));
                plato.setNombrePlatillo(rs.getString(6));
                plato.setTiempoPreparacion(rs.getInt(7));
                plato.setDescripcion(rs.getString(8));
                platillo.setPlatillo(plato);
                imagenPlatillo.setImg(rs.getString(9));
                precio.setPrecio(rs.getDouble(10));
                tipoPlatillo.setNombreTipo(rs.getString(11));
                platillo.setImagen(imagenPlatillo);
                platillo.setPrecio(precio);
                platillo.setTipoPlatillo(tipoPlatillo);
                platillo.setIdMenuPlatillo(rs.getInt(5));
                desayuno.setIdTipoMenu(tipoMenu);
                platillos.add(platillo);
            }
            desayuno.setPlatillos(platillos);
            rs.close();
            ps.close();
            con.close();
        }catch(Exception e){
            System.err.println("ERROR getMenuDesayuno " + e.getMessage());
        }
        return desayuno;
    }

    public List getMenuCompleto (){
        List<Menu> menuCompleto = new ArrayList<Menu>();
        MenuDao menuDao = new MenuDao();
        menuCompleto.add(menuDao.getMenuDesayuno());
        System.out.println("desayuno: " + menuCompleto.get(0).getIdMenu());
        menuCompleto.add(menuDao.getMenuDelDia());
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

    public Menu createMenu(Map menuNuevo) throws SQLException {
        boolean insert = false;
        Menu menuReturn = new Menu();
        MenuDao menuDao = new MenuDao();
        Connection con = null;
        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("INSERT INTO menu (`nombreMenu`, `idTipoMenu`, `idSucursal`) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ((Menu)menuNuevo.get("menu")).getNombreMenu());
            ps.setInt(2, (((Menu)menuNuevo.get("menu")).getIdTipoMenu().getIdTipoMenu()));
            ps.setInt(3, 1);
            insert = (ps.executeUpdate() == 1);
            if(insert){
                try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        int idRecovery = generatedKeys.getInt(1);
                        menuReturn = menuDao.getMenuById(idRecovery);

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
