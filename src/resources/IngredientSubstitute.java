package resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientSubstitute extends JFrame {
    private JTextField ingredientField;
    private JButton searchButton;
    private JTextArea resultArea;
    private SQLInteractor db;

    public IngredientSubstitute(SQLInteractor db) {
        this.db=db;
        setTitle("Ingredient Substitute Finder");
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);

        // Content panel with proper proportions
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Input panel at top
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        inputPanel.add(new JLabel("Ingredient name: "));
        ingredientField = new JTextField(30);
        inputPanel.add(ingredientField);
        searchButton = new JButton("Search");
        inputPanel.add(searchButton);

        // Results area with preferred sizing
        resultArea = new JTextArea(18, 70);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(700, 400));

        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Main panel with title and padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        //mainPanel.add(new JLabel("Ingredient Substitute Finder", JLabel.CENTER), BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        pack();

        // Resize handling
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
            }
        });

        // Search button action
        searchButton.addActionListener(e -> {
            String ingredient = ingredientField.getText().trim();
            if (!ingredient.isEmpty()) {
                String substitutes = getSubstitutes(ingredient);
                resultArea.setText("Substitutes for '" + ingredient + "':\n\n" + substitutes);
            } else {
                resultArea.setText("Please enter an ingredient name.");
            }
        });

        // Enter key support
        ingredientField.addActionListener(e -> searchButton.doClick());
    }

    private String getSubstitutes(String ingredient) {
        ResultSet temp_rs;
        String substitutes="";
        try{
            temp_rs= db.getAlternative(ingredient);
            substitutes=temp_rs.getString("Substitute");
        }
        catch(SQLException e){
            System.out.println("SQLException occured:"+e.getMessage());
        }
        catch(Exception e){
            System.out.println("A Random Error occured:"+e.getMessage());
        }
        return substitutes;
    }
}