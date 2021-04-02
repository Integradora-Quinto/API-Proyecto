package mx.edu.utez.platillo.model;

import mx.edu.utez.imagenplatillo.model.ImagenPlatillo;
import mx.edu.utez.imagenplatillo.model.ImagenPlatilloDAO;
import mx.edu.utez.ingredientePlatillo.model.IngredientePlatilloDao;
import mx.edu.utez.precio.model.PrecioDao;
import mx.edu.utez.preparacion.model.Preparacion;
import mx.edu.utez.preparacion.model.PreparacionDao;
import mx.edu.utez.tipoPlatillo.model.TipoPlatilloDao;
import mx.edu.utez.tools.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlatilloDao {

    Connection con;
    ResultSet rs;
    PreparedStatement ps;

    public List getPlatillo() throws SQLException {
        ArrayList<PlatilloCompleto> platillos = new ArrayList();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT p.idplatillo, i.idimagenplatillo FROM platillo p INNER JOIN imagenplatillo i ON p.idplatillo = i.idplatillo;");
            rs = ps.executeQuery();
            PrecioDao precioDao = new PrecioDao();
            ImagenPlatilloDAO imagenDao = new ImagenPlatilloDAO();
            while(rs.next()){
                System.out.println(rs.getInt(1) + rs.getInt(2));
                PlatilloCompleto platilloCompleto = new PlatilloCompleto();
                Platillo platillo = this.getPlatilloById(rs.getInt(1));
                platilloCompleto.setPrecio(precioDao.getPrecioByPlatillo(platillo.getIdPlatillo()));
                platilloCompleto.getPrecio().setIdPlatillo(null);
                platilloCompleto.setImagen(imagenDao.getImagenPlatilloById(rs.getInt(2)));
                platilloCompleto.getImagen().setIdPlatillo(null);
                platilloCompleto.setPlatillo(platillo);
                platillos.add(platilloCompleto);
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(con!=null) con.close();
            if(ps!=null)ps.close();
            if(rs!=null)rs.close();
        }
        return platillos;
    }

    public PlatilloCompleto getPlatilloCompletoById(int id) throws SQLException{
        PlatilloCompleto platillo = new PlatilloCompleto();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT p.idPlatillo, img.idImagenPlatillo FROM platillo p\n" +
                    "INNER JOIN imagenplatillo img ON p.idPlatillo = img.idPlatillo WHERE p.idPlatillo = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            PlatilloDao dao = new PlatilloDao();
            ImagenPlatilloDAO imagenDao = new ImagenPlatilloDAO();
            PrecioDao precio = new PrecioDao();
            IngredientePlatilloDao ingredientes = new IngredientePlatilloDao();
            PreparacionDao preparacion = new PreparacionDao();
            while(rs.next()){
                platillo.setPlatillo(dao.getPlatilloById(rs.getInt(1)));
                platillo.setImagen(imagenDao.getImagenPlatilloById(rs.getInt(2)));
                platillo.setPrecio(precio.getPrecioByPlatillo(rs.getInt(1)));
                platillo.setPreparacion(preparacion.readPreparacionById(rs.getInt(1)));
                platillo.setIngredientes(ingredientes.getIngredientesByPlatillo(rs.getInt(1)));
                platillo.getImagen().setIdPlatillo(null);
                platillo.getPrecio().setIdPlatillo(null);
                platillo.getPreparacion().setIdPlatillo(null);
            }
        }catch(Exception e){
            System.err.println("ERROR EN OBTENER PLATILLO COMPLETO");
        }finally{
            if(con!=null) con.close();
            if(rs!=null)rs.close();
            if(ps!=null)ps.close();
        }
        return platillo;
    }

    public Platillo getPlatilloById(int id) throws SQLException{
        Platillo platillo = new Platillo();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT * FROM platillo WHERE idPlatillo = ?;");
            ps.setInt(1,id);
            rs = ps.executeQuery();
            while(rs.next()){
                TipoPlatilloDao tipoPlatillo = new TipoPlatilloDao();
                platillo.setIdPlatillo(rs.getInt(1));
                platillo.setNombrePlatillo(rs.getString(2));
                platillo.setTiempoPreparacion(rs.getInt(3));
                platillo.setDescripcion(rs.getString(4));
                platillo.setIdTipoPlatillo(tipoPlatillo.getTipoPlatilloById(rs.getInt(5)));
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(con!=null) con.close();
            if(rs!=null)rs.close();
            if(ps!=null)ps.close();
        }

        return platillo;
    }

    public List getPlatillosForSetPrecio() throws SQLException {
        ArrayList list = new ArrayList();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT P.idplatillo,I.idimagenplatillo ,PC.idprecio FROM platillo P \n" +
                    "\tLEFT JOIN imagenplatillo I ON P.idplatillo = I.idplatillo\n" +
                    "    LEFT JOIN precio PC ON P.idplatillo = PC.idplatillo\n" +
                    "    ORDER BY P.idplatillo;");
            rs = ps.executeQuery();
            PlatilloDao platilloDao = new PlatilloDao();
            ImagenPlatilloDAO imagenPlatilloDAO = new ImagenPlatilloDAO();
            PrecioDao precioDao = new PrecioDao();
            while(rs.next()){
                HashMap map = new HashMap();
                map.put("platillo",platilloDao.getPlatilloById(rs.getInt(1)));
                map.put("imagen",imagenPlatilloDAO.getImagenPlatilloById(rs.getInt(2)));
//                int id = rs.getInt(3) > 0 ?  : 0;
                map.put("precio",precioDao.getPrecioById(rs.getInt(3)));
                list.add(map);
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(con!=null) con.close();
            if(rs!=null)rs.close();
            if(ps!=null)ps.close();
        }
        return list;
    }

    public boolean createPlatillo(PlatilloCompleto platillo) throws SQLException{
        boolean flag = false;
        boolean myFlag = false;
        Platillo platilloInsert = new Platillo();
        con = null;
        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement("INSERT INTO platillo (`nombrePlatillo`,`tiempoPreparacion`, `descripcion`, `idTipoPlatillo`) VALUES (?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,platillo.getPlatillo().getNombrePlatillo());
            ps.setInt(2,platillo.getPlatillo().getTiempoPreparacion());
            ps.setString(3, platillo.getPlatillo().getDescripcion());
            ps.setInt(4,platillo.getPlatillo().getIdTipoPlatillo().getIdTipoPlatillo());

            ImagenPlatilloDAO imgDao = new ImagenPlatilloDAO();
            PreparacionDao preparacion = new PreparacionDao();
            IngredientePlatilloDao ingredientes = new IngredientePlatilloDao();

            flag = (ps.executeUpdate() == 1);
            System.out.println(flag);
            if(flag){
                try(ResultSet generateKeys = ps.getGeneratedKeys()){
                    if(generateKeys.next()){
                        int idRecovery = generateKeys.getInt(1);
                        platilloInsert = platillo.getPlatillo();
                        System.out.println(idRecovery);
                        platilloInsert.setIdPlatillo(idRecovery);
                        ImagenPlatillo imagen = imgDao.createImagenPlatillo(platillo.getImagen().getImgFile(), platilloInsert.getIdPlatillo());
                        platillo.getPreparacion().setIdPlatillo(platilloInsert);
                        Preparacion prep = preparacion.createPreparacion(platillo.getPreparacion());
                        for (int i = 0; i < platillo.getIngredientes().size(); i++){
                            System.out.println("Entre aquí");
                            platillo.getIngredientes().get(i).setIdPlatillo(platilloInsert);
                            System.out.println(platilloInsert.getIdPlatillo());
                            myFlag = ingredientes.createIngredientePlatillo(platillo.getIngredientes().get(i));
                        }
                    }else{
                        throw new SQLException("FAIL PLATILLO NOT CREATED");
                    }
                }
                con.commit();
            }
            if(platilloInsert.getIdPlatillo() > 0 && myFlag){
                flag = true;
            }else{
                flag = false;
            }
        }catch(Exception e){
            System.err.println("ERROR CREATE" + e.getMessage());
            con.rollback();
        }finally{
            if(con!=null) con.close();
            if(rs!=null)rs.close();
            if(ps!=null)ps.close();
        }
        return flag;
    }


    public boolean updatePlatillo(Platillo platillo) throws SQLException{
        boolean flag = false;
        con = null;
        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement("UPDATE platillo SET `nombrePlatillo` = ? ,`tiempoPreparacion` = ?, `descripcion` = ? , `idTipoPlatillo` = ? WHERE idPlatillo = ?;");
            ps.setString(1,platillo.getNombrePlatillo());
            ps.setInt(2,platillo.getTiempoPreparacion());
            ps.setString(3, platillo.getDescripcion());
            ps.setInt(4,platillo.getIdTipoPlatillo().getIdTipoPlatillo());
            ps.setInt(5,platillo.getIdPlatillo());
            flag = (ps.executeUpdate() == 1);

            if(flag){
                con.commit();
            }
        }catch(Exception e){
            System.err.println("ERROR UPDATE" + e.getMessage());
            con.rollback();
        }finally{
            if(con!=null) con.close();
            if(rs!=null)rs.close();
            if(ps!=null)ps.close();
        }

        return flag;
    }

    public boolean deletePlatillo(int id) throws SQLException{
        boolean flag = false;
        con = null;
        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement("DELETE FROM platillo  WHERE idPlatillo = ?;");
            ps.setInt(1,id);

            flag = (ps.executeUpdate() == 1);

            if(flag){
                con.commit();
            }
        }catch(Exception e){
            System.err.println("ERROR DELETE" + e.getMessage());
            con.rollback();
        }finally{
            if(con!=null) con.close();
            if(rs!=null)rs.close();
            if(ps!=null)ps.close();
        }

        return flag;
    }

}
