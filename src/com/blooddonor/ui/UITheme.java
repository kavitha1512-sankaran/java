package com.blooddonor.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class UITheme {
    public static final Color COLOR_PRIMARY = new Color(183, 28, 28);       // Deep Crimson
    public static final Color COLOR_PRIMARY_HOVER = new Color(211, 47, 47); // Lighter Crimson
    public static final Color COLOR_SECONDARY = new Color(38, 50, 56);      // Dark Slate
    public static final Color COLOR_BG = new Color(245, 247, 250);          // Soft background
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(224, 224, 224);
    public static final Color COLOR_TEXT_MAIN = new Color(33, 33, 33);
    public static final Color COLOR_TEXT_MUTED = new Color(117, 117, 117);
    public static final Color COLOR_SUCCESS = new Color(46, 125, 50);       // Green
    public static final Color COLOR_CRITICAL = new Color(198, 40, 40);      // Warning Red
    public static final Color COLOR_URGENT = new Color(239, 108, 0);        // Orange

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    public static JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }

    public static JButton createPrimaryButton(String text) {
        return createButton(text, COLOR_PRIMARY, Color.WHITE);
    }

    public static JButton createSecondaryButton(String text) {
        return createButton(text, COLOR_SECONDARY, Color.WHITE);
    }

    public static JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_REGULAR);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    public static JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(FONT_REGULAR);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        return card;
    }

    public static JPanel createHeaderBanner(String title, String subtitle, String tag) {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(COLOR_PRIMARY);
        banner.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(FONT_SUBTITLE);
        lblSub.setForeground(new Color(255, 235, 238));

        textPanel.add(lblTitle);
        textPanel.add(lblSub);
        banner.add(textPanel, BorderLayout.WEST);

        if (tag != null && !tag.isEmpty()) {
            JLabel lblTag = new JLabel("  " + tag + "  ");
            lblTag.setFont(FONT_BOLD);
            lblTag.setForeground(COLOR_PRIMARY);
            lblTag.setBackground(Color.WHITE);
            lblTag.setOpaque(true);
            lblTag.setBorder(new EmptyBorder(6, 12, 6, 12));
            JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            tagPanel.setOpaque(false);
            tagPanel.add(lblTag);
            banner.add(tagPanel, BorderLayout.EAST);
        }

        return banner;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(32);
        table.setGridColor(new Color(238, 238, 238));
        table.setSelectionBackground(new Color(255, 235, 238));
        table.setSelectionForeground(COLOR_PRIMARY);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(COLOR_SECONDARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFoc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSel, hasFoc, row, col);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (!isSel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));
                }
                return c;
            }
        });
    }
}
