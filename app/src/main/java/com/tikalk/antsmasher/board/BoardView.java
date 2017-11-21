package com.tikalk.antsmasher.board;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;

import static com.tikalk.graphics.ImageUtils.tintImage;

/**
 * Board view with wood background and ants walking on top.
 */

public class BoardView extends View {

    interface AntListener {
        void onAntTouch(@Nullable String antId);
    }

    int bgWidth = 0;
    int bgHeight = 0;
    int antWidth;
    int antHeight;
    int antDeadWidth;
    int antDeadHeight;
    private Map<String, Bitmap> bitmapsAlive = new HashMap<>();
    private Map<String, Bitmap> bitmapsDead = new HashMap<>();
    private final List<AntRect> ants = new ArrayList<>();
    private final Map<String, AntRect> antsById = new HashMap<>();
    private AntListener antListener;

    public BoardView(Context context) {
        super(context);
        init(context);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        final Resources res = context.getResources();
        antWidth = res.getDimensionPixelSize(R.dimen.ant_width);
        antHeight = res.getDimensionPixelSize(R.dimen.ant_height);
        antDeadWidth = res.getDimensionPixelSize(R.dimen.ant_dead_width);
        antDeadHeight = res.getDimensionPixelSize(R.dimen.ant_ded_height);
    }

    public void clear() {
        ants.clear();
        bitmapsAlive.clear();
        bitmapsDead.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap;
        float angle;
        float px, py;
        for (AntRect ant : ants) {
            bitmap = ant.alive ? bitmapsAlive.get(ant.speciesId) : bitmapsDead.get(ant.speciesId);
            angle = ant.angle;
            px = ant.x();
            py = ant.y();
            canvas.rotate(angle, px, py);
            canvas.drawBitmap(bitmap, ant.left, ant.top, null);
            canvas.rotate(-angle, px, py);
        }
    }

    public void addAnt(Ant ant) {
        final float width = getWidth();
        final float height = getHeight();

        final AntSpecies species = ant.getSpecies();
        final String speciesId = species.getId();

        float x = ant.getLocation().x * width;
        float y = ant.getLocation().y * height;
        float left = x - (antWidth / 2);
        float top = y - (antHeight / 2);
        float right = left + antWidth;
        float bottom = top + antHeight;
        final AntRect rect = new AntRect();
        rect.id = ant.getId();
        rect.speciesId = speciesId;
        rect.set(left, top, right, bottom);
        rect.alive = ant.isAlive();
        antsById.put(rect.id, rect);
        ants.add(rect);

        Bitmap bitmap = bitmapsAlive.get(speciesId);
        if (bitmap == null) {
            final Resources res = getResources();

            Bitmap antNormal = BitmapFactory.decodeResource(res, R.drawable.ant_normal);
            antNormal = Bitmap.createScaledBitmap(antNormal, antWidth, antHeight, false);
            bitmap = tintImage(antNormal, species.getTint());
            bitmapsAlive.put(speciesId, bitmap);

            Bitmap antSmashed = BitmapFactory.decodeResource(res, R.drawable.ant_squashed);
            antSmashed = Bitmap.createScaledBitmap(antSmashed, antDeadWidth, antDeadHeight, false);
            bitmap = tintImage(antSmashed, species.getTint());
            bitmapsDead.put(speciesId, bitmap);
        }
    }

    public void removeAnt(Ant ant) {
        AntRect rect = antsById.remove(ant.getId());
        if (rect != null) {
            ants.remove(rect);
        }
    }

    public void moveTo(Ant ant) {
        AntRect rect = antsById.get(ant.getId());
        if (rect != null) {
            float dxPercent = ant.getLocation().x;
            float dyPercent = ant.getLocation().y;
            rect.moveTo(dxPercent * getWidth(), dyPercent * getHeight());
        }
    }

    public void setAntListener(AntListener antListener) {
        this.antListener = antListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                final AntListener listener = antListener;
                if (listener != null) {
                    final float x = event.getX();
                    final float y = event.getY();
                    boolean hit = false;
                    for (AntRect ant : ants) {
                        if (ant.isHit(x, y)) {
                            hit = true;
                            listener.onAntTouch(ant.id);
                        }
                    }
                    if (!hit) {
                        listener.onAntTouch(null);
                    }
                    return true;
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    public void smashAnt(Ant ant) {
        AntRect rect = antsById.get(ant.getId());
        if (rect != null) {
            rect.alive = false;
            rect.offset((antWidth - antDeadWidth) / 2, (antHeight - antDeadHeight) / 2);
            postInvalidate();
        }
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if ((w != oldw) && (h != oldh)) {
            final Resources res = getResources();
            BitmapFactory.Options options = new BitmapFactory.Options();
            if ((bgWidth == 0) || (bgHeight == 0)) {
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(res, R.drawable.board, options);
                bgWidth = options.outWidth;
                bgHeight = options.outHeight;
                options.inJustDecodeBounds = false;
            }
            options.inSampleSize = Math.max(1, Math.min(bgWidth / w, bgHeight / h));
            Bitmap bg = BitmapFactory.decodeResource(res, R.drawable.board, options);
            setBackground(new BitmapDrawable(res, bg));
        }
    }
}
