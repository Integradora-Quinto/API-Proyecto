package mx.edu.utez.controllers;

import mx.edu.utez.platillo.model.Platillo;
import mx.edu.utez.platillo.model.PlatilloDao;
import mx.edu.utez.response.MyResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("/daily")
public class ServicePlatillo {

    @GET
    @Path("/platillos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getPlatillos() throws SQLException{
        MyResponse response = new MyResponse();
        List list = (new PlatilloDao().getPlatillo());
        if(list.size() > 0){
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("READ PLATILLOS");
            response.setData(list);
        }else{
            response.setCode(400);
            response.setStatus("error");
            response.setMessage("ERROR READ PLATILLOS");
            response.setData(null);
        }
        return response;
    }

    @GET
    @Path("/platillos/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getPlatillosById(@PathParam("id") int id) throws SQLException{
        MyResponse response = new MyResponse();
        Platillo platillo = (new PlatilloDao().getPlatilloById(id));
        if(platillo.getIdPlatillo() > 0){
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("READ PLATILLO BY ID");
            response.setData(platillo);
        }else{
            response.setCode(400);
            response.setStatus("error");
            response.setMessage("ERROR READ PLATILLO BY ID");
            response.setData(null);
        }
        return response;
    }

    @POST
    @Path("/platillos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse  createPlatillo(Platillo platillo) throws SQLException{
        MyResponse response = new MyResponse();
        Platillo platilloInsert = (new PlatilloDao().createPlatillo(platillo));
        if(platilloInsert.getIdPlatillo() > 0){
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("PLATILLO CREATED");
            response.setData(platilloInsert);
        }else{
            response.setCode(400);
            response.setStatus("error");
            response.setMessage("ERROR PLATILLO CREATED");
            response.setData(null);
        }
        return response;
    }


    @PUT
    @Path("/platillos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse updatePlatillo(Platillo platillo) throws SQLException{
        MyResponse response = new MyResponse();
        boolean flag = (new PlatilloDao().updatePlatillo(platillo));
        if(flag){
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("PLATILLO UPDATE");
            response.setData(platillo);
        }else{
            response.setCode(400);
            response.setStatus("error");
            response.setMessage("ERROR PLATILLO UPDATE");
            response.setData(null);
        }
        return response;
    }


    @DELETE
    @Path("/platillos/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse deletePlatillo(@PathParam("id") int id) throws SQLException{
        MyResponse response = new MyResponse();
        boolean flag = (new PlatilloDao().deletePlatillo(id));
        if(flag){
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("PLATILLO DELETE");
            response.setData(flag);
        }else{
            response.setCode(400);
            response.setStatus("error");
            response.setMessage("ERROR PLATILLO DELETE");
            response.setData(null);
        }
        return response;
    }


}