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

/*
    Contains all objects on the screen, along with methods for initializing and updating the states
    of the objects.
 */

public class PlayData {
    public static final int TEAM_SIZE = 3;

    private static final PointF[] PUCK_LOCATIONS = {
            new PointF(0.35f, 0.5f),
            new PointF(0.2f, 0.3f),
            new PointF(0.2f, 0.7f)
    };

    private static final float[] GOALPOST_VERTICAL_POSITIONS = { 0.3f, 0.7f };
    private static final float GOALPOST_LENGTH = 0.1f;

    private static final float TEAM_SCREEN_RATIO = 0.04f;
    private static final float BALL_SCREEN_RATIO = 0.02f;

    private Point mViewDims;

    private List<Puck> mLeftTeam = new ArrayList<>();
    private List<Puck> mRightTeam = new ArrayList<>();
    private Puck mBall;

    private List<Puck> mAllPucks = new ArrayList<>();

    private RectF mLeftGoal;
    private RectF mRightGoal;

    private int mSpeed;

    public PlayData(int viewWidth, int viewHeight, Bitmap leftFlag, Bitmap rightFlag, Bitmap ballIcon) {

        mViewDims = new Point(viewWidth, viewHeight);

        initPucks(leftFlag, rightFlag, ballIcon);
        initGoals();
    }

    private void initPucks(Bitmap leftFlag, Bitmap rightFlag, Bitmap ballIcon) {
        float radius = mViewDims.x * TEAM_SCREEN_RATIO;

        /* Left team initialization */
        for (int i = 0; i < TEAM_SIZE; ++i) {
            PointF center = new PointF(
                    mViewDims.x * PUCK_LOCATIONS[i].x,
                    mViewDims.y * (PUCK_LOCATIONS[i].y));

            Puck teamPuck = new Puck(center, radius, leftFlag);
            mLeftTeam.add(i, teamPuck);
        }

        /* Right team initialization */
        for (int i = 0; i < TEAM_SIZE; ++i) {
            PointF center = new PointF(
                    mViewDims.x * (1 - PUCK_LOCATIONS[i].x),
                    mViewDims.y * PUCK_LOCATIONS[i].y);

            Puck teamPuck = new Puck(center, radius, rightFlag);
            mRightTeam.add(i, teamPuck);
        }

        /* Ball initialization */
        PointF ballCenter = new PointF(mViewDims.x * 0.5f, mViewDims.y * 0.5f);
        float ballRadius = mViewDims.x * BALL_SCREEN_RATIO;
        mBall = new Puck(ballCenter, ballRadius, ballIcon);

        mAllPucks.addAll(mLeftTeam);
        mAllPucks.addAll(mRightTeam);
        mAllPucks.add(mBall);
    }

    private void initGoals() {
        mLeftGoal = new RectF(
                0, mViewDims.y * GOALPOST_VERTICAL_POSITIONS[0],
                mViewDims.x * GOALPOST_LENGTH, mViewDims.y * GOALPOST_VERTICAL_POSITIONS[1]);
        mRightGoal = new RectF(
                mViewDims.x * (1 - GOALPOST_LENGTH), mViewDims.y * GOALPOST_VERTICAL_POSITIONS[0],
                mViewDims.x, mViewDims.y * GOALPOST_VERTICAL_POSITIONS[1]);
    }

    public Puck getBall() {
        return mBall;
    }

    public void updateDims(int viewWidth, int viewHeight) {
        mViewDims.set(viewWidth, viewHeight);
        float radius = viewWidth * TEAM_SCREEN_RATIO;

        for (int i = 0; i < TEAM_SIZE; ++i) {
            Puck puck = mLeftTeam.get(i);
            puck.getCenter().set(
                    viewWidth * PUCK_LOCATIONS[i].x,
                    viewHeight * (PUCK_LOCATIONS[i].y));
            puck.setRadius(radius);
            puck.setVelocity(0, 0);
        }

        for (int i = 0; i < TEAM_SIZE; ++i) {
            Puck puck = mRightTeam.get(i);
            puck.getCenter().set(
                    viewWidth * (1 - PUCK_LOCATIONS[i].x),
                    viewHeight * PUCK_LOCATIONS[i].y);
            puck.setRadius(radius);
            puck.setVelocity(0, 0);
        }

        mBall.getCenter().set(
                viewWidth * 0.5f,
                viewHeight * 0.5f);
        mBall.setRadius(viewWidth * BALL_SCREEN_RATIO);
        mBall.setVelocity(0, 0);

        initGoals();
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    public boolean updateData() {
        mLeftTeam.clear();
        mRightTeam.clear();
        EventRegister hitRegister = new EventRegister();
        EventRegister globalRegister = new EventRegister();

        do {
            List<Puck> newState = new ArrayList<>(TEAM_SIZE * 2 + 1);
            hitRegister.reset();

            for (Puck puck: mAllPucks) {
                Puck newPuck = puck.handleCollisions(mAllPucks, hitRegister);
                newPuck.handleWallHits(mViewDims, hitRegister);
                newPuck.handleGoalpostHits(new RectF[] { mLeftGoal, mRightGoal }, hitRegister);

                newState.add(newPuck);
            }
            mAllPucks = newState;

            if (hitRegister.check()) {
                globalRegister.register();
            }

        } while (hitRegister.check());

        for (int i = 0; i < TEAM_SIZE; ++i) {
            mLeftTeam.add(mAllPucks.get(i));
            mRightTeam.add(mAllPucks.get(i + TEAM_SIZE));
        }
        mBall = mAllPucks.get(mAllPucks.size() - 1);

        for (Puck puck: mAllPucks) {
            puck.move();
            puck.decelerate(mSpeed);
        }
        return globalRegister.check();
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.MAGENTA);
        canvas.drawLine(mLeftGoal.left, mLeftGoal.top, mLeftGoal.right, mLeftGoal.top, paint);
        canvas.drawLine(mLeftGoal.left, mLeftGoal.bottom, mLeftGoal.right, mLeftGoal.bottom, paint);
        canvas.drawLine(mRightGoal.left, mRightGoal.top, mRightGoal.right, mRightGoal.top, paint);
        canvas.drawLine(mRightGoal.left, mRightGoal.bottom, mRightGoal.right, mRightGoal.bottom, paint);

        for (Puck puck: mAllPucks) {
            puck.draw(canvas, paint);
        }
    }

    public Puck getLeftTeamPressedPuck(float x, float y) {
        return getPressedPuck(x, y, mLeftTeam);
    }

    public Puck getRightTeamPressedPuck(float x, float y) {
        return getPressedPuck(x, y, mRightTeam);
    }

    private Puck getPressedPuck(float x, float y, List<Puck> pucks) {
        for (Puck puck: pucks) {
            float distanceSqr = (x - puck.getCenter().x) * (x - puck.getCenter().x)
                    + (y - puck.getCenter().y) * (y - puck.getCenter().y);
            if (distanceSqr <= puck.getRadius() * puck.getRadius()) {
                return puck;
            }
        }
        return null;
    }

    public Point getViewDims() {
        return mViewDims;
    }

    public int checkScoring() {
        PointF ballCenter = mBall.getCenter();
        if (mLeftGoal.contains(ballCenter.x, ballCenter.y)) {
            return GameMetadata.RIGHT_PLAYER;
        } else if (mRightGoal.contains(ballCenter.x, ballCenter.y)) {
            return GameMetadata.LEFT_PLAYER;
        } else {
            return GameMetadata.NO_PLAYER;
        }
    }

    public void resetState() {
        updateDims(mViewDims.x, mViewDims.y);
    }

    public Puck getClosestPuck(int side) {
        List<Puck> team = (side == GameMetadata.LEFT_PLAYER) ? mLeftTeam : mRightTeam;

        Puck minPuck = team.get(0);
        float minDistance = mViewDims.x * mViewDims.y;

        for (Puck puck: team) {
            float distanceX = mBall.getCenter().x - puck.getCenter().x;
            float distanceY = mBall.getCenter().y - puck.getCenter().y;

            float distance = distanceX * distanceX + distanceY * distanceY;
            if (distance < minDistance) {
                minDistance = distance;
                minPuck = puck;
            }
        }
        return minPuck;
    }
}
