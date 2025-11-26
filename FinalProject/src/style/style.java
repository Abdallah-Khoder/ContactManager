package style;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Group;

public class style {

    public static void styletxtField(JTextField textField) {
        textField.setPreferredSize(new Dimension(300, 20));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);

    }

    public static void styleJLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.PLAIN, 20));
    }

    public static void styleTable(JTable table, JScrollPane scrollPane, DefaultTableModel model) {
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(24);

        for (int i = 0; i < 30; i++) {
            model.addRow(new Object[]{"", ""});
        }

        int rowHeight = table.getRowHeight();
        int headerHeight = table.getTableHeader().getPreferredSize().height;
        scrollPane.setPreferredSize(new Dimension(500, rowHeight * 6 + headerHeight));
    }

    public static void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(new Font("Arial", Font.BOLD, 18));
        checkBox.setMargin(new Insets(5, 30, 5, 30));
        checkBox.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 3));

    }

    public static void styleJButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setMargin(new Insets(5, 30, 5, 30));
    }

}
