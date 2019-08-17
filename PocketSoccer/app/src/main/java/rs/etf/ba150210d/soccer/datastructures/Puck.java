package rs.etf.ba150210d.soccer.datastructures;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

import rs.etf.ba150210d.soccer.util.EventRegister;

/**
 * Abstraction of a spherical object on the field - either a team member, or the ball.
 * Includes the logic for crashing into lines, points, and each other.
 */

public class Puck {
    private static final float[] SLOWDOWN_COEFFICIENTS = new float[] {
            0.999f, 0.998f, 0.997f, 0.996f, 0.995f,
            0.994f, 0.993f, 0.992f, 0.991f, 0.99f
    };

    private PointF mCenter;
    private float mRadius;
    private PointF mVelocity = new PointF(0, 0);

    private Bitmap mImage;

    private Puck() {
        mCenter = new PointF(0, 0);
        mRadius = 0;
        mImage = null;
    }

    public Puck(PointF center, float radius, Bitmap image) {
        mCenter = center;
        mRadius = radius;
        mImage = image;
    }

    public Puck(Puck puck) {
        mCenter = new PointF(puck.mCenter.x, puck.mCenter.y);
        mRadius = puck.mRadius;
        mVelocity = new PointF(puck.mVelocity.x, puck.mVelocity.y);
        mImage = puck.mImage;
    }

    public PointF getCenter() {
        return mCenter;
    }

    public void setCenter(PointF center) {
        mCenter = center;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public PointF getVelocity() {
        return mVelocity;
    }

    public void setVelocity(PointF speed) {
        mVelocity.set(speed);
    }

    public void setVelocity(float x, float y) {
        mVelocity.set(x, y);
    }

    public void accelerate(PointF dVelocity) {
        mVelocity.offset(dVelocity.x, dVelocity.y);
    }

    public void accelerate(float dVelocityX, float dVelocityY) {
        mVelocity.offset(dVelocityX, dVelocityY);
    }

    public void decelerate(int speed) {
        mVelocity.x *= SLOWDOWN_COEFFICIENTS[speed];
        mVelocity.y *= SLOWDOWN_COEFFICIENTS[speed];
    }

    public void move() {
        mCenter.offset(mVelocity.x, mVelocity.y);
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE);
        canvas.drawCircle(mCenter.x, mCenter.y, mRadius, paint);

        if (mRadius > 0) {
            Bitmap scaledImage = Bitmap.createScaledBitmap(mImage,
                    (int)(2 * mRadius), (int)(2 * mRadius), false);
            canvas.drawBitmap(scaledImage, mCenter.x - mRadius,
                    mCenter.y - mRadius, paint);
        }
    }

    public Puck calculateNewVelocity(List<PointF> deltaVelocities) {
        Puck puck = new Puck(this);

        for (PointF dVelocity: deltaVelocities) {
            puck.mVelocity.offset(dVelocity.x, dVelocity.y);
        }
        return puck;
    }

    public PointF getDeltaVelocityFromCollision(Puck hitP) {
        PointF distance = new PointF(
                this.mCenter.x - hitP.mCenter.x,
                this.mCenter.y - hitP.mCenter.y);
        float distanceModulus = (float)Math.sqrt(distance.x * distance.x + distance.y * distance.y);

        if (distanceModulus > this.mRadius + hitP.mRadius || this == hitP) {
            return null;
        } else {
            PointF relativeVelocity = new PointF(
                    this.mVelocity.x - hitP.mVelocity.x,
                    this.mVelocity.y - hitP.mVelocity.y);

            /*
                Angle between the velocity of the puck and the distance between the point and
                the puck. Identity:

                atan2(y1, x1) - atan2(y2, x2) = atan2(y1x2 - x1y2, x1x2 + y1y2)

                (y1, x1) = (velocity.y, velocity.x)
                (y2, x2) = (-distance.y, -distance.x)
             */
            double angle = Math.atan2(
                    relativeVelocity.y * (-distance.x) - relativeVelocity.x * (-distance.y),
                    relativeVelocity.x * (-distance.x) + relativeVelocity.y * (-distance.y));

            if (-Math.PI / 2 < angle && angle < Math.PI / 2) {
                /* Relative speed has a vector component towards the other puck */

                float dVelocity = 2 / distanceModulus
                        * (distance.x * (hitP.mVelocity.x - this.mVelocity.x)
                        + distance.y * (hitP.mVelocity.y - this.mVelocity.y));

                float massThis = this.mRadius * this.mRadius;
                float massHitP = hitP.mRadius * hitP.mRadius;

                float dVelocityNewP = dVelocity * massHitP / (massThis + massHitP);

                return new PointF(
                        dVelocityNewP * (distance.x / distanceModulus),
                        dVelocityNewP * (distance.y / distanceModulus));
            } else {
                return null;
            }
        }
    }

    public Puck handleCollisions(List<Puck> pucks, EventRegister hitRegister) {
        List<PointF> deltaVelocities = new ArrayList<>(pucks.size());

        for (Puck puck: pucks) {
            PointF deltaVelocity = getDeltaVelocityFromCollision(puck);
            if (deltaVelocity != null) {
                deltaVelocities.add(deltaVelocity);
            }
        }

        if (deltaVelocities.size() == 0) {
            /* No collisions */
            return this;
        } else {
            hitRegister.register();
            return calculateNewVelocity(deltaVelocities);
        }
    }

    public void handleWallHits(Point dimensions, EventRegister hitRegister) {

        if (mCenter.x <= mRadius && mVelocity.x < 0) {
            mVelocity.x *= -1;
            hitRegister.register();
        }
        if (mCenter.x >= dimensions.x - mRadius && mVelocity.x > 0) {
            mVelocity.x *= -1;
            hitRegister.register();
        }
        if (mCenter.y <= mRadius && mVelocity.y < 0) {
            mVelocity.y *= -1;
            hitRegister.register();
        }
        if (mCenter.y >= dimensions.y - mRadius && mVelocity.y > 0) {
            mVelocity.y *= -1;
            hitRegister.register();
        }
    }

    public void handleGoalpostHits(RectF[] goals, EventRegister hitRegister) {
        for (RectF goal: goals) {
            /* Upper post hit from above */
            if (mVelocity.y > 0
                    && mCenter.y + mRadius >= goal.top
                    && mCenter.y <= goal.top
                    && mCenter.x >= goal.left
                    && mCenter.x <= goal.right) {
                mVelocity.y *= -1;
                hitRegister.register();

            /* Upper post hit from below */
            } else if (mVelocity.y < 0
                    && mCenter.y - mRadius <= goal.top
                    && mCenter.y >= goal.top
                    && mCenter.x >= goal.left
                    && mCenter.x <= goal.right) {
                mVelocity.y *= -1;
                hitRegister.register();

            /* Lower post hit from above */
            } else if (mVelocity.y > 0
                    && mCenter.y + mRadius >= goal.bottom
                    && mCenter.y <= goal.bottom
                    && mCenter.x >= goal.left
                    && mCenter.x <= goal.right) {
                mVelocity.y *= -1;
                hitRegister.register();

            /* Lower post hit from below */
            } else if (mVelocity.y < 0
                    && mCenter.y - mRadius <= goal.bottom
                    && mCenter.y >= goal.bottom
                    && mCenter.x >= goal.left
                    && mCenter.x <= goal.right) {
                mVelocity.y *= -1;
                hitRegister.register();

            /* Hit in one of the corners */
            } else {
                handlePointHit(new PointF(goal.right, goal.top), hitRegister);
                handlePointHit(new PointF(goal.left, goal.top), hitRegister);
                handlePointHit(new PointF(goal.right, goal.bottom), hitRegister);
                handlePointHit(new PointF(goal.left, goal.bottom), hitRegister);
            }
        }
    }

    private void handlePointHit(PointF point, EventRegister hitRegister) {
        PointF distance = new PointF(mCenter.x - point.x, mCenter.y - point.y);
        if (distance.x * distance.x + distance.y * distance.y <= mRadius * mRadius) {

            /*
                Angle between the velocity of the puck and the distance between the point and
                the puck. Identity:

                atan2(y1, x1) - atan2(y2, x2) = atan2(y1x2 - x1y2, x1x2 + y1y2)

                (y1, x1) = (velocity.y, velocity.x)
                (y2, x2) = (-distance.y, -distance.x)
             */
            double angle = Math.atan2(
                    mVelocity.y * (-distance.x) - mVelocity.x * (-distance.y),
                    mVelocity.x * (-distance.x) + mVelocity.y * (-distance.y));

            if (-Math.PI / 2 < angle && angle < Math.PI / 2) {
                double velocityModulus = Math.sqrt(mVelocity.x * mVelocity.x + mVelocity.y * mVelocity.y);
                double distanceAngle = Math.atan2(-distance.y, -distance.x);

                mVelocity.set(
                        (float) (-velocityModulus * Math.cos(angle - distanceAngle)),
                        (float) (velocityModulus * Math.sin(angle - distanceAngle)));
                hitRegister.register();
            }
        }
    }

    public PointF getDistance(Puck puck) {
        return new PointF(
                puck.getCenter().x - this.getCenter().x,
                puck.getCenter().y - this.getCenter().y);
    }
}
