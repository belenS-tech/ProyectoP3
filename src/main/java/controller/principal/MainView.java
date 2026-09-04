package controller.principal;

import controller.categorias.CategoriasController;
import controller.categorias.CategoriasView;
import controller.funcionarios.FuncionariosController;
import controller.funcionarios.FuncionariosView;
import controller.RecursoController;
import controller.ReservaController;
import model.login.Rol;
import model.login.Usuario;
import service.categorias.CategoriaService;
import service.funcionarios.FuncionarioService;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del sistema (No es un punto de entrada, no tiene main()).
 * Contiene las pestañas de los módulos y el menú de sesión.
 * Las pestañas se agregan según el rol del usuario autenticado.
 * Juan.
 */
public class MainView extends JFrame {

    private JTabbedPane pestanas;

    private JMenuItem itemCambiarClave;
    private JMenuItem itemCerrarSesion;
    private JMenuItem itemSalir;

    public MainView(Usuario usuarioActivo,
                    FuncionarioService funcionarioService,
                    CategoriaService categoriaService) {
        super("Sistema de Reserva de Recursos");
        construirVentana(usuarioActivo);
        agregarPestanasSegunRol(usuarioActivo, funcionarioService, categoriaService);
    }

    private void construirVentana(Usuario usuario) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        setTitle("Sistema de Reserva de Recursos - "
                + usuario.getId() + " (" + usuario.getRol() + ")");

        setJMenuBar(construirMenu());

        pestanas = new JTabbedPane();
        setContentPane(pestanas);
    }

    private JMenuBar construirMenu() {
        JMenuBar barra = new JMenuBar();
        JMenu menuSistema = new JMenu("Sistema");

        itemCambiarClave = new JMenuItem("Cambiar clave");
        itemCerrarSesion = new JMenuItem("Cerrar sesión");
        itemSalir = new JMenuItem("Salir");

        menuSistema.add(itemCambiarClave);
        menuSistema.addSeparator();
        menuSistema.add(itemCerrarSesion);
        menuSistema.add(itemSalir);

        barra.add(menuSistema);
        return barra;
    }

    /**
     * Agrega únicamente las pestañas permitidas para el rol.
     * El funcionario nunca ve los módulos administrativos.
     */
    private void agregarPestanasSegunRol(Usuario usuario,
                                         FuncionarioService funcionarioService,
                                         CategoriaService categoriaService) {
        if (usuario.getRol() == Rol.ADMINISTRADOR) {
            FuncionariosView vistaFuncionarios = new FuncionariosView();
            new FuncionariosController(vistaFuncionarios, funcionarioService);
            agregarPestana("Funcionarios", vistaFuncionarios);

            CategoriasView vistaCategorias = new CategoriasView();
            new CategoriasController(vistaCategorias, categoriaService);
            agregarPestana("Categorías", vistaCategorias);

            agregarPestana("Recursos", new RecursoController(categoriaService));
        }

        // Ambos roles: reservas, calendarización, actividades y estadísticas
        agregarPestana("Reservas", new ReservaController());
        agregarPestana("Calendarización", pendiente("Calendarización - Integrante 3"));
        agregarPestana("Actividades", pendiente("Actividades - Integrante 3"));
        agregarPestana("Estadísticas", pendiente("Estadísticas"));
    }

    /**
     * Punto de conexión para el resto del equipo:
     * cada integrante entrega el JPanel de su pantalla y se agrega aquí.
     */
    public void agregarPestana(String titulo, JPanel panel) {
        pestanas.addTab(titulo, panel);
    }

    /** Panel temporal mientras la pantalla real no exista. */
    private JPanel pendiente(String nombre) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(new JLabel(nombre + ": pendiente."));
        return panel;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public boolean confirmar(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(
                this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }

    public JMenuItem getItemCambiarClave() {
        return itemCambiarClave;
    }

    public JMenuItem getItemCerrarSesion() {
        return itemCerrarSesion;
    }

    public JMenuItem getItemSalir() {
        return itemSalir;
    }
}