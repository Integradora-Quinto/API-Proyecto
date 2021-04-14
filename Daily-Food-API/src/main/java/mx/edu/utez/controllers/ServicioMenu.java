package mx.edu.utez.controllers;

import mx.edu.utez.menu.model.Menu;
import mx.edu.utez.menu.model.MenuCompleto;
import mx.edu.utez.menu.model.MenuDao;
import mx.edu.utez.menudia.model.MenuDia;
import mx.edu.utez.menudia.model.MenuDiaDao;
import mx.edu.utez.response.MyResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/daily")
public class ServicioMenu {

    @GET
    @Path("/menus/dia")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getMenuDia(){
        MyResponse response = new MyResponse();
        Menu menuDia = (new MenuDao().getMenuDelDia());
        response.setData(menuDia);
        if(menuDia.getIdMenu() > 0){
            response.setCode(200);
            response.setStatus("SUCCESS");
            response.setMessage("Recuperación de menú del día exitosa");
        }else{
            response.setCode(400);
            response.setStatus("ERROR");
            response.setMessage("Error en la recuperación del día");
        }
        return response;
    }

    @GET
    @Path("/menus")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getMenus(){
        MyResponse response = new MyResponse();
        List<Menu> menu = new ArrayList<Menu>();
        menu = (new MenuDao().getMenuCompleto());
        response.setData(menu);
        if(menu.size() > 0){
            response.setStatus("success");
            response.setCode(200);
            response.setMessage("Consulta exitosa");
        }else{
            response.setCode(400);
            response.setStatus("error");
            response.setMessage("Sin registros o falla");
        }
        return response;
    }

    @GET
    @Path("/menus/proximos")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getMenusProgramados(){
        MyResponse response = new MyResponse();
        List<MenuDia> menusProximos = (new MenuDao().getMenusProximos());
        response.setData(menusProximos);
        if(menusProximos.size() > 0){
            response.setCode(200);
            response.setStatus("SUCCESS");
            response.setMessage("Recuperación de menus próximos exitosa");
        }else{
            response.setCode(400);
            response.setStatus("ERROR");
            response.setMessage("Error en recuperación de menus próximos");
        }
        return response;
    }

    @GET
    @Path("/menus/{idMenu}")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getMenu(@PathParam("idMenu") int idM){
        MyResponse response = new MyResponse();
        Menu menu = (new MenuDao().getMenuById(idM));
        response.setData(menu);
        if(menu.getIdMenu()>0){
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("Consulta exitosa");
        }else {
            response.setCode(400);
            response.setStatus("error");
            response.setMessage("Error en la consulta");
        }
        return response;
    }

    @POST
    @Path("/menus")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MyResponse createMenu(MenuCompleto menuN) throws SQLException {
        MyResponse response = new MyResponse();
        Menu menuR = (new MenuDao().createMenu(menuN));
        response.setData(menuR);
        if(menuR.getIdMenu() != 0){
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("Se ha creado con exito");
        }else {
            response.setCode(400);
            response.setStatus("error");
            response.setMessage("Error en la creación");
        }
        return response;
    }

    @PUT
    @Path("/menus")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MyResponse updateMenu(Menu menuN) throws SQLException{
        MyResponse response = new MyResponse();
        boolean m = (new MenuDao().updateMenu(menuN));
        response.setData(m);
        if(m){
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("La actualización se realizó con éxito");
        }else {
            response.setCode(400);
            response.setStatus("error");
            response.setMessage("Error en la actualización");
        }
        return response;
    }

    @DELETE
    @Path("/menus/{idMenu}")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse deleteMenu(@PathParam("idMenu") int idM) throws SQLException{
        MyResponse response = new MyResponse();
        boolean d = (new MenuDao().deleteMenu(idM));
        response.setData(d);
        if(d){
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("Eliminación exitosa");
        }else {
            response.setCode(400);
            response.setStatus("error");
            response.setMessage("Falla en la eliminación");
        }
        return response;
    }

}
