package resources;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class LoginFrame extends JFrame {
    JTextField userField = new JTextField(15);
    JPasswordField passField = new JPasswordField(15);
    JButton loginButton = createButton("Login", new Color(66, 133, 244));
    JLabel msgLabel = new JLabel("", SwingConstants.CENTER);
    static List<String> allowedNames = Arrays.asList("devansh", "vaibhav", "chanchal", "sheetal");

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Fullscreen with controls

        ImageIcon imgIcon = new ImageIcon(getClass().getResource("logo.png"));
        BackgroundPanel bgPanel = new BackgroundPanel(imgIcon.getImage());
        bgPanel.setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(16, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Login Page", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 38));
        heading.setForeground(new Color(33, 33, 99));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(heading, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        userField.setFont(new Font("Arial", Font.PLAIN, 22));
        formPanel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        passField.setFont(new Font("Arial", Font.PLAIN, 22));
        formPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(loginButton, gbc);

        gbc.gridy = 4;
        msgLabel.setFont(new Font("Arial", Font.BOLD, 18));
        formPanel.add(msgLabel, gbc);

        bgPanel.add(formPanel, new GridBagConstraints());

        loginButton.addActionListener(e -> {
            String u = userField.getText().toLowerCase().trim();
            String p = new String(passField.getPassword()).trim();
            if (!allowedNames.contains(u)) {
                msgLabel.setText("Allowed users: Devansh, Vaibhav, Chanchal, Sheetal");
                msgLabel.setForeground(Color.RED);
            } else if (!p.equals("1234")) {
                msgLabel.setText("Password must be '1234' (only numbers)!");
                msgLabel.setForeground(Color.RED);
            } else {
                dispose();
                new SearchFrame(capitalize(u));
            }
        });

        setContentPane(bgPanel);
        setVisible(true);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        return btn;
    }
    private static String capitalize(String name) {
        if (name == null || name.isEmpty()) return name;
        return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
