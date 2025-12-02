package uitest;

import core.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class MedicineRecordListPanel extends Base {
    private final MainFrame mainFrame;
    private ArrayList<MedicineRecord> records;
    private User user;
    private Pet pet;

    public MedicineRecordListPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();
        this.records = medicineRecordMgr.getAllByOwner(user);

        // TODO: 아래 테스트용 코드 추후 삭제 (2줄)
        System.out.println("복용기록패널 ID: " + user.getId());
        System.out.println("복용기록패널 펫: " + pet.getName());

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new MedicalHomePanel(mainFrame,
                        MedicalMgr.getInstance().mList,
                        VaccineMgr.getInstance().mList,
                        MedicineRecordMgr.getInstance().mList))), BorderLayout.NORTH);
        contentWrapper.add(createList(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    private JComponent createList() {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 16, 16, 16));

        // 제목
        JLabel title = new JLabel("복용 기록 목록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(UIComponents.createSearchButton(mainFrame, this));

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(20));

        /* === 카드 리스트 === */
        for (MedicineRecord r : records) {

            MedicineCard card = new MedicineCard(r, mainFrame);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(12));
        }

        // 스크롤 래퍼
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        // 스크롤 속도 빠르게
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        return scroll;
    }

    private static class MedicineCard extends BaseCard {

        public MedicineCard(MedicineRecord r, MainFrame mainFrame) {

            String title = r.getUITexts()[1];
            addTitle(title);

            addLine("날짜: " + r.getUITexts()[2]);
            addLine("복용량: " + r.getUITexts()[4] +"ml");

            // 클릭하면 상세 페이지
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    System.out.println("카드 클릭됨, record = " + r);
                    mainFrame.switchPanel(new MedicineDetailPanel(mainFrame, r));
                }
            });
        }
    }

}
