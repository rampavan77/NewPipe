package org.schabi.newpipe.util;

import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class CommentTextOnTouchListener implements View.OnTouchListener {
    public static final CommentTextOnTouchListener INSTANCE = new CommentTextOnTouchListener();

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (!(v instanceof TextView)) {
            return false;
        }
        final TextView widget = (TextView) v;
        final CharSequence text = widget.getText();
        if (text instanceof Spanned) {
            final Spanned buffer = (Spanned) text;
            final int action = event.getAction();

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                final Layout layout = widget.getLayout();
                final int line = layout.getLineForVertical(y);
                final int off = layout.getOffsetForHorizontal(line, x);

                final ClickableSpan[] links = buffer.getSpans(off, off, ClickableSpan.class);
                if (links.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        links[0].onClick(widget);
                    }
                    // we handle events that intersect links, so return true
                    return true;
                }
            }
        }
        return false;
    }
}
