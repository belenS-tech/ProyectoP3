package controller.login;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField campoId;
    private JPasswordField campoClave;
    private JButton botonIngresar;
    private JButton botonCancelar;

    public LoginView() {
        super("Sistema de Reservas");
        construirVentana();
    }

    private void construirVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("ID:"), gbc);

        campoId = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0;
        add(campoId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Clave:"), gbc);

        campoClave = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        add(campoClave, gbc);

        botonIngresar = new JButton("Ingresar");
        gbc.gridx = 0; gbc.gridy = 2;
        add(botonIngresar, gbc);

        botonCancelar = new JButton("Cancelar");
        gbc.gridx = 1; gbc.gridy = 2;
        add(botonCancelar, gbc);
    }

    public String getIdIngresado() {
        return campoId.getText();
    }

    public String getClaveIngresada() {
        return new String(campoClave.getPassword());
    }

    public void limpiarCampos() {
        campoId.setText("");
        campoClave.setText("");
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JButton getBotonIngresar() {
        return botonIngresar;
    }

    public JButton getBotonCancelar() {
        return botonCancelar;
    }
}