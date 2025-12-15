package uiutil;

import java.awt.*;

public class WrapLayout extends FlowLayout {
    public WrapLayout() {
        super(LEFT);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension minimum = layoutSize(target, false);
        minimum.width -= (getHgap() + 1);
        return minimum;
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        Container container = target;

        int maxWidth;

        // 스크롤 안에 들어가 있으면, 뷰포트 너비 기준
        Container parent = container.getParent();
        if (parent != null && parent.getWidth() > 0) {
            maxWidth = parent.getWidth();
        } else if (container.getWidth() > 0) {
            // 아니면 자기 자신의 현재 너비
            maxWidth = container.getWidth();
        } else {
            // 아직 아무것도 정해지지 않은 초기 상태면, 일단 줄바꿈 없이 계산
            maxWidth = Integer.MAX_VALUE;
        }

        int hgap = getHgap();
        int vgap = getVgap();
        Insets insets = container.getInsets();
        int availableWidth = maxWidth - insets.left - insets.right;

        Dimension dim = new Dimension(0, 0);
        int rowWidth = 0;
        int rowHeight = 0;

        int nmembers = target.getComponentCount();
        for (int i = 0; i < nmembers; i++) {
            Component comp = target.getComponent(i);
            if (!comp.isVisible()) continue;

            Dimension d = preferred ? comp.getPreferredSize() : comp.getMinimumSize();

            if (rowWidth + d.width > availableWidth) {
                dim.width = Math.max(dim.width, rowWidth);
                dim.height += rowHeight + vgap;
                rowWidth = 0;
                rowHeight = 0;
            }

            rowWidth += d.width + hgap;
            rowHeight = Math.max(rowHeight, d.height);
        }

        dim.width = Math.max(dim.width, rowWidth);
        dim.height += rowHeight;

        dim.width += insets.left + insets.right;
        dim.height += insets.top + insets.bottom;

        return dim;
    }
}
