package suza.project.wackyballs.model.figures;

import android.graphics.BitmapFactory;

import java.util.List;

import suza.project.wackyballs.R;
import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.model.components.AbstractAnimation;
import suza.project.wackyballs.model.components.AbstractFigure;
import suza.project.wackyballs.model.containers.BasketBallContainer;
import suza.project.wackyballs.model.properties.Collision;
import suza.project.wackyballs.model.properties.FigureState;
import suza.project.wackyballs.model.properties.FigureType;
import suza.project.wackyballs.model.properties.MySpeed;
import suza.project.wackyballs.util.Util;

/**
 * Figure increases player score when deposited in basket.
 * 
 * Created by lmark on 13/09/2017.
 */

public class BasketBallGoodFigure extends AbstractAnimation {

    private GamePanel panel;
    private BasketBallContainer figureContainer;

    public BasketBallGoodFigure(GamePanel gamePanel, BasketBallContainer figureContainer) {
        super(BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.face_animation),
                Util.randomInteger(0, gamePanel.getScreenWidth()), -50,
                10, 4);
        super.setSpeed(new MySpeed(
                Util.randomInteger(-10, 10),
                Util.randomInteger(2, 5)
        ));

        this.figureContainer = figureContainer;
        this.panel = gamePanel;
        super.getSpeed().setGravity(true);
        setState(FigureState.ALIVE);
        setType(FigureType.BALL);
    }

    @Override
    public void resolveCollision(int screenWidth, int screenHeight, List<AbstractFigure> others) {
        Collision.resolveSideWallCollision(screenWidth, screenHeight, this);

        // Resolve collision
        for (int i = 0, count = others.size(); i < count; i++) {

            if (this.getID() == others.get(i).getID()) {
                continue;
            }

            if (others.get(i).getType() == FigureType.BASKET) {
                // Resolve basket collision
                Collision.resolveBasketBallCollision(others.get(i), this);
            } else {
                // Resolve collision with other balls
                boolean col = Collision.resolveFigureCollision2(this, others.get(i));

                // Re-enable gravity after the collision ( in case it was disabled )
                if (col) {
                    this.getSpeed().setGravity(true);
                    others.get(i).getSpeed().setGravity(true);
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        getSpeed().update();

        // If out of the bottom of the screen set to DEAD
        if (getY() > panel.getHeight() + 2*getRadius()) {
            setState(FigureState.DEAD);

            // Signal that the lives changed
            livesChanged(-1);
        }
    }

}
