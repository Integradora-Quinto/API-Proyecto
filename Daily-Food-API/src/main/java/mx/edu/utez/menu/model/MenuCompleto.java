package mx.edu.utez.menu.model;

import mx.edu.utez.menudia.model.MenuDia;
import mx.edu.utez.platillo.model.PlatilloCompleto;
import mx.edu.utez.platilloenmenu.model.PlatilloEnMenu;
import mx.edu.utez.sucursal.model.Sucursal;
import mx.edu.utez.tipomenu.model.TipoMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuCompleto {

    private int idMenu;
    private String nombreMenu;
    private TipoMenu idTipoMenu;
    private Sucursal idSucursal;
    private MenuDia menuDia;
    private List<PlatilloEnMenu> platilloEnMenu = new ArrayList<>();
    private List<PlatilloCompleto> platillos;

    public List<PlatilloEnMenu> getPlatilloEnMenu() {
        return platilloEnMenu;
    }

    public void setPlatilloEnMenu(List<PlatilloEnMenu> platilloEnMenu) {
        this.platilloEnMenu = platilloEnMenu;
    }
    public MenuDia getMenuDia() {
        return menuDia;
    }

    public void setMenuDia(MenuDia menuDia) {
        this.menuDia = menuDia;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public TipoMenu getIdTipoMenu() {
        return idTipoMenu;
    }

    public void setIdTipoMenu(TipoMenu idTipoMenu) {
        this.idTipoMenu = idTipoMenu;
    }

    public Sucursal getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Sucursal idSucursal) {
        this.idSucursal = idSucursal;
    }


}
