package mx.edu.utez.pedido.model;

import mx.edu.utez.direccion.model.DireccionDao;
import mx.edu.utez.pedidotieneplatillo.model.PedidoTienePlatillo;
import mx.edu.utez.pedidotieneplatillo.model.PedidoTienePlatilloDao;
import mx.edu.utez.persona.model.PersonaDAO;
import mx.edu.utez.sucursal.model.SucursalDao;
import mx.edu.utez.tools.ConnectionDB;
import mx.edu.utez.usuario.model.Usuario;
import mx.edu.utez.usuario.model.UsuarioDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDao {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    /*public List  getPedidos() throws SQLException {
        ArrayList<PedidoCompleto> list = new ArrayList();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT * FROM pedido;");
            rs = ps.executeQuery();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            DireccionDao direccionDao = new DireccionDao();
            SucursalDao sucursalDao = new SucursalDao();
            while(rs.next()){
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt(1));
                pedido.setFecha(rs.getString(2));
                pedido.setCostoTotal(rs.getDouble(3));
                pedido.setCantidadPago(rs.getDouble(4));
                pedido.setStatus(rs.getString(5));
                pedido.setNombreUsuario(usuarioDAO.getUsuarioByUser(rs.getString(6)));
                pedido.setIdDireccion(direccionDao.getDireccionById(rs.getInt(7)));
                pedido.setIdSucursal(sucursalDao.getSucursalById(rs.getInt(8)));
                list.add(pedido);
            }
        }catch(Exception e){
            System.err.println("ERROR READ PEDIDO");
        }finally{
            if (ps != null) ps.close();
            if (rs != null) rs.close();
            if (con != null) con.close();
        }
        return list;
    }*/

    public List getAllPedidosPreparacion() throws SQLException{
        ArrayList<PedidoCompleto> pedidosP = new ArrayList();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT p.idPedido, p.fecha, p.costoTotal, p.cantidadPago, p.nombreUsuario, p.comentario FROM pedido p \n" +
                    "INNER JOIN pedidotieneplatillo ptp ON p.idPedido = ptp.idPedido\n" +
                    "INNER JOIN platilloenmenu pem ON ptp.idMenuPlatillo = pem.idMenuPlatillo\n" +
                    "INNER JOIN platillo pl ON pem.idPlatillo = pl.idPlatillo WHERE p.status LIKE 'Preparación' GROUP BY p.idPedido");
            rs = ps.executeQuery();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            PersonaDAO persona = new PersonaDAO();
            PedidoTienePlatilloDao ptp = new PedidoTienePlatilloDao();
            while(rs.next()){
                System.out.println("nombre usuario: " + rs.getString(5));
                PedidoCompleto ped = new PedidoCompleto();
                Pedido p = new Pedido();
                p.setId(rs.getInt(1));
                p.setFecha(rs.getString(2));
                p.setCostoTotal(rs.getDouble(3));
                p.setCantidadPago(rs.getDouble(4));
                ped.setIdPedido(p);
                ped.setPersona(persona.getPersonaById(usuarioDAO.getUsuarioByUser(rs.getString(5)).getIdPersona().getIdPersona()));
                System.out.println(ped.getPersona().getNombre());
                ped.getIdPedido().setComentario(rs.getString(6));
                ped.setTelefono(usuarioDAO.getUsuarioByUser(rs.getString(5)).getTelefono());
                ped.setPedidoplatillos(ptp.getPlatillosByPedido(rs.getInt(1)));
                pedidosP.add(ped);
            }
        }catch (Exception e) {
            System.err.println("ERROR DAO getAllPedidosPreparación=> " + e.getMessage());
        }finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return pedidosP;
    }

    public List getPedidosPreparacionByUser(String user) throws SQLException{
        ArrayList<Pedido> pedidosP = new ArrayList();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT * FROM pedido WHERE status LIKE 'Preparación' AND nombreUsuario LIKE ?");
            ps.setString(1, user);
            rs = ps.executeQuery();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            DireccionDao direccionDao = new DireccionDao();
            SucursalDao sucursalDao = new SucursalDao();
            while(rs.next()){
                Pedido ped = new Pedido();
                ped.setId(rs.getInt(1));
                ped.setFecha(rs.getString(2));
                ped.setCostoTotal(rs.getDouble(3));
                ped.setCantidadPago(rs.getDouble(4));
                ped.setStatus(rs.getString(5));
                ped.setNombreUsuario(usuarioDAO.getUsuarioByUser(rs.getString(6)));
                ped.setComentario((rs.getString(7)));
                ped.setIdDireccion(direccionDao.getDireccionById(rs.getInt(8)));
                ped.setIdSucursal(sucursalDao.getSucursalById(rs.getInt(9)));
                pedidosP.add(ped);
            }
        }catch (Exception e) {
            System.err.println("ERROR => " + e.getMessage());
        }finally {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
            if (con != null) con.close();
        }
        return pedidosP;
    }

    public List getPedidosEnCurso() throws SQLException{
        ArrayList<Pedido> pedidosC = new ArrayList();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT idPedido FROM pedido WHERE status LIKE 'En curso'");
            rs = ps.executeQuery();
            PedidoDao pedidoDao = new PedidoDao();
            while(rs.next()){
                Pedido ped = new Pedido();
                ped = pedidoDao.getPedidoById(rs.getInt(1));
                pedidosC.add(ped);
            }
        }catch (Exception e) {
            System.err.println("ERROR => " + e.getMessage());
        }finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return pedidosC;
    }

    public List getPedidosEnCursoByUser(String user) throws SQLException{
        ArrayList<Pedido> pedidosC = new ArrayList();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT idPedido FROM pedido WHERE status LIKE 'En curso' AND nombreUsuario LIKE ?");
            ps.setString(1, user);
            rs = ps.executeQuery();
            PedidoDao pedidoDao = new PedidoDao();
            while(rs.next()){
                Pedido ped = new Pedido();
                ped = pedidoDao.getPedidoById(rs.getInt(1));
                pedidosC.add(ped);
            }
        }catch (Exception e) {
            System.err.println("ERROR => " + e.getMessage());
        }finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return pedidosC;
    }

    public List getPedidosEntregados() throws SQLException{
        ArrayList<Pedido> pedidosE = new ArrayList();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT * FROM pedido WHERE status LIKE 'Entregado'");
            rs = ps.executeQuery();
            PedidoDao pedidoDao = new PedidoDao();
            while(rs.next()){
                Pedido ped = new Pedido();
                ped = pedidoDao.getPedidoById(rs.getInt(1));
                pedidosE.add(ped);
            }
        }catch (Exception e) {
            System.err.println("ERROR => " + e.getMessage());
        }finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return pedidosE;
    }

    public List getPedidosEntregadosByUser(String user) throws SQLException{
        ArrayList<Pedido> pedidosE = new ArrayList();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT idPedido FROM pedido WHERE status LIKE 'Entregado' AND nombreUsuario LIKE ?");
            ps.setString(1, user);
            rs = ps.executeQuery();
            PedidoDao pedidoDao = new PedidoDao();
            while(rs.next()){
                Pedido ped = new Pedido();
                ped = pedidoDao.getPedidoById(rs.getInt(1));
                pedidosE.add(ped);
            }
        }catch (Exception e) {
            System.err.println("ERROR => " + e.getMessage());
        }finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return pedidosE;
    }

    public List getPedidosFinalizadosByUser(String user) throws SQLException{
        ArrayList<Pedido> pedidosF = new ArrayList();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT idPedido FROM pedido WHERE status LIKE 'Finalizado' AND nombreUsuario LIKE ?");
            ps.setString(1, user);
            rs = ps.executeQuery();
            PedidoDao pedidoDao = new PedidoDao();
            while(rs.next()){
                Pedido ped = new Pedido();
                ped = pedidoDao.getPedidoById(rs.getInt(1));
                pedidosF.add(ped);
            }
        }catch (Exception e) {
            System.err.println("ERROR => " + e.getMessage());
        }finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return pedidosF;
    }

    public Pedido getPedidoById(int id) throws SQLException {
        Pedido pedido = new Pedido();
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT * FROM pedido WHERE `idPedido` = ?;");
            ps.setInt(1,id);
            rs = ps.executeQuery();

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            DireccionDao direccionDao = new DireccionDao();
            SucursalDao sucursalDao = new SucursalDao();

            while(rs.next()){
                pedido.setId(rs.getInt(1));
                pedido.setFecha(rs.getString(2));
                pedido.setCostoTotal(rs.getDouble(3));
                pedido.setCantidadPago(rs.getDouble(4));
                pedido.setStatus(rs.getString(5));
                pedido.setNombreUsuario(usuarioDAO.getUsuarioByUser(rs.getString(6)));
                pedido.setComentario(rs.getString(7));
                pedido.setIdDireccion(direccionDao.getDireccionById(rs.getInt(8)));
                pedido.setIdSucursal(sucursalDao.getSucursalById(rs.getInt(9)));
            }

        }catch(Exception e){
            System.err.println("ERROR READ PEDIDO");
        }finally{
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return pedido;
    }

    public String getStatusByPedido(int idPedido) throws SQLException{
        String status = null;
        try{
            con = ConnectionDB.getConnection();
            ps = con.prepareStatement("SELECT status from pedido WHERE idPedido =  ?");
            ps.setInt(1, idPedido);
            rs = ps.executeQuery();
            while(rs.next()){
                status = rs.getString(1);
            }
        }catch (Exception err){
            System.err.println("Error al traer el status " + err.getMessage());
        }finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return status;
    }

    public PedidoCompleto createPedido(PedidoCompleto pedido) throws SQLException {
        PedidoCompleto pedidoInsert = new PedidoCompleto();
        boolean flag = false;
        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement("INSERT INTO pedido (fecha, costoTotal, cantidadPago, status, comentario, nombreUsuario, idDireccion, idSucursal) VALUES (?,?,?,?,?,?,?,?);" , Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,pedido.getIdPedido().getFecha());
            ps.setDouble(2,pedido.getIdPedido().getCostoTotal());
            ps.setDouble(3,pedido.getIdPedido().getCantidadPago());
            ps.setString(4,"Preparación");
            ps.setString(5, pedido.getIdPedido().getComentario());
            ps.setString(6,pedido.getIdPedido().getNombreUsuario().getNombreUsuario());
            ps.setInt(7,pedido.getIdPedido().getIdDireccion().getId());
            ps.setInt(8, 1);
            flag = ps.executeUpdate() == 1;
            System.out.println(flag);
            if(flag){
                con.commit();
                PedidoTienePlatilloDao platillos = new PedidoTienePlatilloDao();
                try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()){
                        int idRecobery = generatedKeys.getInt(1);
                        pedidoInsert = pedido;
                        pedidoInsert.getIdPedido().setId(idRecobery);
                        System.out.println(pedidoInsert.getIdPedido().getId());
                        if (pedidoInsert.getIdPedido().getId() > 0) { //Validamos que si se haya registrado y asignado un id correctamente
                            int size = pedidoInsert.getPedidoplatillos().size();
                            int platillosRegistrados = 0;
                            for (int i = 0; i < size; i++) {
                                pedidoInsert.getPedidoplatillos().get(i).setIdPedido(pedidoInsert.getIdPedido());
                                flag = platillos.createPedidoTienePlatillo(pedidoInsert.getPedidoplatillos().get(i));
                                if (flag) { //Validamos que se haya registrado este platillo
                                    platillosRegistrados++;
                                }
                            }
                            System.out.println("size ->> " + size);
                            System.out.println("platillosRegistrados --> "+ platillosRegistrados);
                            if (size == platillosRegistrados) {
                                 //Confirmamos cambios hasta que todos los platillos del pedido hayan sido registrados
                            }
                        }
                    } else {
                        throw new SQLException("ERROR CREATE PEDIDO");
                    }
                }
            }
        }catch(Exception e){
            System.err.println("ERROR CREATE PEDIDO" + e.getMessage());
            con.rollback();
        }finally{
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return pedidoInsert;
    }

    public boolean updatePedido(Pedido pedido) throws SQLException{
        boolean flag = false;

        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement("UPDATE pedido SET `fecha` = ? , `costoTotal` = ?,`cantidadPago` = ?, `status` = ?, `nombreUsuario` = ?, `comentario` = ?, `idDireccion` = ?, `idSucursal` = ? WHERE  `idPedido` = ?;");
            ps.setString(1,pedido.getFecha());
            ps.setDouble(2,pedido.getCostoTotal());
            ps.setDouble(3,pedido.getCantidadPago());
            ps.setString(4,pedido.getStatus());
            ps.setString(5,pedido.getNombreUsuario().getNombreUsuario());
            ps.setString(6,pedido.getComentario());
            ps.setInt(7,pedido.getIdDireccion().getId());
            ps.setInt(8,pedido.getIdSucursal().getIdSucursal());
            ps.setInt(9,pedido.getId());

            flag = ps.executeUpdate() == 1;
            if(flag) con.commit();
        }catch(Exception e){
            System.err.println("ERROR UPDATE PEDIDO" + e.getMessage());
            con.rollback();
        }finally{
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return flag;
    }

    public boolean changeStatus(Pedido pedido)throws SQLException{
        boolean changed = false;
        System.out.println("Status: " + pedido.getStatus());
        System.out.println("ID: " + pedido.getId());
        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement("UPDATE pedido SET `status` = ? WHERE  `idPedido` = ?;");
            ps.setString(1, pedido.getStatus());
            ps.setDouble(2,pedido.getId());
            changed = ps.executeUpdate() == 1;
            System.out.println("Chnaged : "+changed);
            if(changed){
                con.commit();
            }
        }catch(Exception e){
            System.err.println("ERROR EN ACTUALIZAR STATUS AL PEDIDO -> " + e.getMessage());
            con.rollback();
        }finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return changed;
    }

    public boolean cancelarPedido(int id) throws SQLException {
        boolean flag = false;

        try{
            con = ConnectionDB.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement("UPDATE pedido SET `status` = ? WHERE  `idPedido` = ?;");
            ps.setString(1,"Cancelado");
            ps.setInt(2,id);
            flag = ps.executeUpdate() == 1;

            if(flag) con.commit();

        }catch(Exception e){
            System.err.println("ERROR CANCELAR PEDIDO"+ e.getMessage());
            con.rollback();
        }finally{
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return flag;
    }

}
