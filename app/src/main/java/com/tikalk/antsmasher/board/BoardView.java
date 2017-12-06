package com.tikalk.antsmasher.board;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;

import static com.tikalk.graphics.ImageUtils.tintImage;

/**
 * Board view with wood background and ants walking on top.
 */

public class BoardView extends View {

    private static final String TAG = "BoardView";

    interface AntListener {
        void onAntTouch(@Nullable String antId);
    }

    int antWidth;
    int antHeight;
    int antDeadWidth;
    int antDeadHeight;
    private final Map<Long, Bitmap> bitmapsAlive = new HashMap<>();
    private final Map<Long, Bitmap> bitmapsDead = new HashMap<>();
    private final List<AntRect> ants = new CopyOnWriteArrayList<>();
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
            if ((bitmap == null) || bitmap.isRecycled()) {
                Log.w(TAG, "bitmap invalid for species: " + ant.speciesId + " " + (ant.alive ? "alive" : "dead"));
                return;
            }
            angle = ant.angle;
            px = ant.x();
            py = ant.y();
            canvas.rotate(angle, px, py);
            canvas.drawBitmap(bitmap, ant.left, ant.top, null);
            canvas.rotate(-angle, px, py);
        }
    }

    public AntRect addAnt(Ant ant) {
        final float width = getWidth();
        final float height = getHeight();

        final AntSpecies species = ant.getSpecies();
        final long speciesId = species.getId();

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

        getAntAlive(species);
        getAntDead(species);

        return rect;
    }

    public Bitmap getAntAlive(AntSpecies species) {
        final long speciesId = species.getId();
        Bitmap bitmap = bitmapsAlive.get(speciesId);
        if (bitmap == null) {
            final Resources res = getResources();
            final float speciesSize = species.getSize();

            bitmap = BitmapFactory.decodeResource(res, R.drawable.ant_normal);
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (antWidth * speciesSize), (int) (antHeight * speciesSize), false);
            bitmap = tintImage(bitmap, species.getTint());
            bitmapsAlive.put(speciesId, bitmap);
        }
        return bitmap;
    }

    public Bitmap getAntDead(AntSpecies species) {
        final long speciesId = species.getId();
        Bitmap bitmap = bitmapsDead.get(speciesId);
        if (bitmap == null) {
            final Resources res = getResources();
            final float speciesSize = species.getSize();

            Bitmap antSmashed = BitmapFactory.decodeResource(res, R.drawable.ant_squashed);
            antSmashed = Bitmap.createScaledBitmap(antSmashed, (int) (antDeadWidth * speciesSize), (int) (antDeadHeight * speciesSize), false);
            antSmashed = tintImage(antSmashed, species.getTint());
            Bitmap antBlood = BitmapFactory.decodeResource(res, R.drawable.ant_blood);
            bitmap = Bitmap.createScaledBitmap(antBlood, (int) (antDeadWidth * speciesSize), (int) (antDeadHeight * speciesSize), false);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(antSmashed, 0, 0, null);
            bitmapsDead.put(speciesId, bitmap);
        }
        return bitmap;
    }

    public void removeAnt(Ant ant) {
        AntRect rect = antsById.remove(ant.getId());
        if (rect != null) {
            ants.remove(rect);
        }
    }

    public void moveTo(Ant ant) {
        AntRect rect = antsById.get(ant.getId());
        if (rect == null) {
            rect = addAnt(ant);
        }
        if ((rect != null) && rect.alive) {
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
                    final float radius = event.getTouchMinor();
                    final float left = x - radius;
                    final float top = y - radius;
                    final float right = x + radius;
                    final float bottom = y + radius;
                    boolean hit = false;
                    for (AntRect ant : ants) {
                        if (ant.isHit(left, top, right, bottom)) {
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

    public void smashAnt(@Nullable Ant ant) {
        if (ant != null) {
            AntRect rect = antsById.get(ant.getId());
            if (rect != null) {
                rect.alive = false;
                rect.offset((antWidth - antDeadWidth) / 2, (antHeight - antDeadHeight) / 2);
                postInvalidate();
            }
        }
    }
}
