import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class IngredientSubstituteGUI extends JFrame {
    private JTextField ingredientField;
    private JButton searchButton;
    private JTextArea resultArea;

    public IngredientSubstituteGUI() {
        setTitle("Ingredient Substitute Finder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        mainPanel.add(new JLabel("Ingredient Substitute Finder", JLabel.CENTER), BorderLayout.NORTH);
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
        Map<String, String[]> substitutes = Map.of(
                "all-purpose flour", new String[]{
                        "Bread flour", "Cake flour (adjust quantity)", "Whole wheat flour",
                        "Rice flour (gluten-free)", "Nut-based flour (3/4 cup per cup)",
                        "Chickpea flour (besan)"
                },
                "butter", new String[]{
                        "Margarine", "Coconut oil", "Olive oil", "Vegetable oil",
                        "Applesauce (for baking)", "Mashed bananas (1:1)", "Pureed avocados (1:1)"
                },
                "milk", new String[]{
                        "Almond milk", "Soy milk", "Oat milk", "Coconut milk",
                        "Greek yogurt thinned with water (1:1)"
                },
                "eggs", new String[]{
                        "Flax eggs (1 tbsp flax + 3 tbsp water)", "Applesauce (1/4 cup per egg)",
                        "Chia seeds (1 tbsp + 3 tbsp water)", "Commercial egg replacer",
                        "Mashed banana (1/2 per egg)", "Pureed pumpkin or avocado"
                },
                "sugar", new String[]{
                        "Honey (3/4 cup per cup)", "Maple syrup (3/4 cup per cup)",
                        "Coconut sugar", "Stevia (adjust to taste)",
                        "Unsweetened applesauce (1:1, reduce liquid)", "Mashed ripe bananas (1/2 cup per cup)"
                }
        );

        String key = ingredient.toLowerCase().trim();
        String[] subs = substitutes.getOrDefault(key, new String[]{});
        if (subs.length > 0) {
            return String.join("\n- ", subs);
        }
        return "No substitutes found for '" + ingredient + "'.\n\nTry: all-purpose flour, butter, milk, eggs, sugar.";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            new IngredientSubstituteGUI().setVisible(true);
        });
    }
}
