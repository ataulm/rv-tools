package com.ataulm.rv;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public final class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int horizontalSpacing;
    private final int horizontalSpacingMajorPart;
    private final int horizontalSpacingMinorPart;
    private final int verticalSpacing;

    public static SpacesItemDecoration newInstance(int horizontalSpacing, int verticalSpacing, SpanSizeLookup spanSizeLookup) {
        int maxSpansInRow = spanSizeLookup.getSpanCount();
        int numberOfGaps = maxSpansInRow - 1;
        int horizontalSpacingMajorPart = (int) (1f * numberOfGaps * horizontalSpacing / maxSpansInRow);
        int horizontalSpacingMinorPart = horizontalSpacing - horizontalSpacingMajorPart;

        return new SpacesItemDecoration(horizontalSpacing, horizontalSpacingMajorPart, horizontalSpacingMinorPart, verticalSpacing);
    }

    private SpacesItemDecoration(int horizontalSpacing, int horizontalSpacingMajorPart, int horizontalSpacingMinorPart, int verticalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
        this.horizontalSpacingMajorPart = horizontalSpacingMajorPart;
        this.horizontalSpacingMinorPart = horizontalSpacingMinorPart;
        this.verticalSpacing = verticalSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewPosition();
        int childCount = parent.getAdapter().getItemCount();

        if (layoutParams instanceof GridLayoutManager.LayoutParams) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            int spanCount = layoutManager.getSpanCount();

            SpanLookup spanLookup = new SpanLookup(layoutManager.getSpanSizeLookup(), spanCount);
            applyItemHorizontalOffsets(spanLookup, itemPosition, outRect);
            outRect.top = getItemTopSpacing(spanLookup, verticalSpacing, itemPosition, spanCount, childCount);
            outRect.bottom = getItemBottomSpacing(spanLookup, verticalSpacing, itemPosition, childCount);
        } else {
            // TODO
            throw new IllegalStateException("support more things");
        }
    }

    private void applyItemHorizontalOffsets(SpanLookup spanLookup, int itemPosition, Rect offsets) {
        if (itemIsFullSpan(spanLookup, itemPosition)) {
            offsets.left = 0;
            offsets.right = 0;
            return;
        }

        if (itemStartsAtTheLeftEdge(spanLookup, itemPosition)) {
            offsets.left = 0;
            offsets.right = horizontalSpacingMajorPart;
            return;
        }

        if (itemEndsAtTheRightEdge(spanLookup, itemPosition)) {
            offsets.left = horizontalSpacingMajorPart;
            offsets.right = 0;
            return;
        }

        if (itemIsNextToAnItemThatStartsOnTheLeftEdge(spanLookup, itemPosition)) {
            offsets.left = horizontalSpacingMinorPart;
        } else {
            offsets.left = (int) (.5f * horizontalSpacing);
        }

        if (itemIsNextToAnItemThatEndsOnTheRightEdge(spanLookup, itemPosition)) {
            offsets.right = horizontalSpacingMinorPart;
        } else {
            offsets.right = (int) (.5f * horizontalSpacing);
        }
    }

    private static boolean itemIsNextToAnItemThatStartsOnTheLeftEdge(SpanLookup spanLookup, int itemPosition) {
        return !itemStartsAtTheLeftEdge(spanLookup, itemPosition) && itemStartsAtTheLeftEdge(spanLookup, itemPosition - 1);
    }

    private static boolean itemIsNextToAnItemThatEndsOnTheRightEdge(SpanLookup spanLookup, int itemPosition) {
        return !itemEndsAtTheRightEdge(spanLookup, itemPosition) && itemEndsAtTheRightEdge(spanLookup, itemPosition + 1);
    }

    private static boolean itemIsFullSpan(SpanLookup spanLookup, int itemPosition) {
        return itemStartsAtTheLeftEdge(spanLookup, itemPosition) && itemEndsAtTheRightEdge(spanLookup, itemPosition);
    }

    private static boolean itemStartsAtTheLeftEdge(SpanLookup spanLookup, int itemPosition) {
        return spanLookup.getSpanIndex(itemPosition) == 0;
    }

    private static boolean itemEndsAtTheRightEdge(SpanLookup spanLookup, int itemPosition) {
        return spanLookup.getSpanIndex(itemPosition) + spanLookup.getSpanSize(itemPosition) == spanLookup.getSpanCount();
    }

    private static int getItemTopSpacing(SpanLookup spanLookup, int verticalSpacing, int itemPosition, int spanCount, int childCount) {
        if (itemIsOnTheTopRow(spanLookup, itemPosition, spanCount, childCount)) {
            return 0;
        } else {
            return (int) (.5f * verticalSpacing);
        }
    }

    private static boolean itemIsOnTheTopRow(SpanLookup spanLookup, int itemPosition, int spanCount, int childCount) {
        int latestCheckedPosition = 0;
        for (int i = 0; i < childCount; i++) {
            latestCheckedPosition = i;
            int spanIndex = spanLookup.getSpanIndex(i);
            if (spanIndex == spanCount - 1) {
                break;
            }
        }
        return itemPosition <= latestCheckedPosition;
    }

    private static int getItemBottomSpacing(SpanLookup spanLookup, int verticalSpacing, int itemPosition, int childCount) {
        if (itemIsOnTheBottomRow(spanLookup, itemPosition, childCount)) {
            return 0;
        } else {
            return (int) (.5f * verticalSpacing);
        }
    }

    private static boolean itemIsOnTheBottomRow(SpanLookup spanLookup, int itemPosition, int childCount) {
        int latestCheckedPosition = 0;
        for (int i = childCount - 1; i >= 0; i--) {
            latestCheckedPosition = i;
            int spanIndex = spanLookup.getSpanIndex(i);
            if (spanIndex == 0) {
                break;
            }
        }
        return itemPosition >= latestCheckedPosition;
    }

    private static class SpanLookup {

        private final GridLayoutManager.SpanSizeLookup spanSizeLookup;
        private final int spanCount;

        SpanLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup, int spanCount) {
            this.spanSizeLookup = spanSizeLookup;
            this.spanCount = spanCount;
        }

        int getSpanCount() {
            return spanCount;
        }

        int getSpanIndex(int itemPosition) {
            return spanSizeLookup.getSpanIndex(itemPosition, getSpanCount());
        }

        int getSpanSize(int itemPosition) {
            return spanSizeLookup.getSpanSize(itemPosition);
        }

    }

}
